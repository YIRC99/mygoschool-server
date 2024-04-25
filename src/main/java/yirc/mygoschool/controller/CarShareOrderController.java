package yirc.mygoschool.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.Dto.CarshareorderDto;
import yirc.mygoschool.Dto.PageInfoCar;
import yirc.mygoschool.anno.OrderOverdue;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.config.AccessTokenService;
import yirc.mygoschool.domain.*;
import yirc.mygoschool.exception.CustomException;
import yirc.mygoschool.service.CarshareorderService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Version v1.0
 * @DateTime 2024/3/1 15:30
 * @Description TODO
 * @Author 一见如初
 */
@Slf4j
@RestController
@RequestMapping("/carshareorder")
public class CarShareOrderController {


    @Value("${yirc99.Template.id}")
    private String TemplateId;

    @Autowired
    private CarshareorderService carshareorderService;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private SensitiveWordBs sensitiveWordBs;

    @PostMapping("/add")
    public Result addCarShareOrder(@RequestBody Carshareorder carshareorder) {
        log.info("addCarShareOrder: {}", carshareorder);
        // 保存用户的添加的微信二维码 方便下次直接选择不上传 因为优先级并不高 所以开个虚拟线程就好
        Thread.ofVirtual().start(() -> {
            // 保存电话和微信号 但是因为输入麻烦 选择废弃 这里可以抽象出来 因为 拼车和闲置都有微信图片属性 但是已经是后期了 懒得改 就这样吧
//            carshareorderService.isSavePhoneOrWeChat(carshareorder);
            carshareorderService.SaveWeChatImg(carshareorder);
        });
        var issuccess = carshareorderService.save(carshareorder);
        if (issuccess)
            return Result.success(carshareorder);
        return Result.error("添加失败");
    }

    @PostMapping("/page")
    public Result list(@RequestBody PageInfoCar pageInfo) {
        log.info("分页查询的参数为: {} {} {} {}", pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getPageDate(), pageInfo.getStartAddName());
        if (pageInfo.getPageNum() == null || pageInfo.getPageNum() < 1
                || pageInfo.getPageSize() == null || pageInfo.getPageSize() < 1) {
            throw new CustomException("分页参数错误");
        }
        //分页查询全部拼车 附带筛选
        Page<CarshareorderDto> orderDtos = carshareorderService.listByPage(pageInfo);
        return Result.success(orderDtos);
    }

    @PostMapping("/receive")
    @Transactional
    public Result receiveOrder(@RequestBody Carshareorder order) throws IOException {
        log.info("接受的订单数据为: {}", order);

        // 锁定订单行
        Carshareorder lockedOrder = carshareorderService.selectForUpdate(order.getOrderid());

        // 检查订单是否已经被接受
        if (lockedOrder.getReceiveuserid() != null) {
            return Result.error("订单已经被接受或删除");
        }

        // 更新订单
        lockedOrder.setReceiveuserid(order.getReceiveuserid());
        lockedOrder.setReceiveUserWechatImg(order.getReceiveUserWechatImg());
        lockedOrder.setStatus(1);
        boolean b = carshareorderService.updateById(lockedOrder);
        // 跟新成功了 才发送消息
        if(b){
            sendUserMeg(order.getCreateuserid());
        }
        return Result.success("订单接受成功。");
    }

    @PostMapping("/getreceivebyuserid")
    public Result getReceiveByUserId(@RequestBody Userinfo user){
        log.info("根据用户id获取用户接受的拼车订单: {}", user);
        // 查询自己接受的订单 顺便带上创建用户的信息
        List<CarshareorderDto> orders = carshareorderService.getReceiveByUserId(user);
        replaceSensitive(orders);
        return Result.success(orders);
    }

    @PostMapping("/getbyuserid")
    @OrderOverdue
    public Result getByUserId(@RequestBody Userinfo user){
        log.info("根据id获取用户拼车订单: {}", user);
        // 获取用户发布的拼车 如果有人接受了 就附带上接受用户的信息
        List<CarshareorderDto> results = carshareorderService.getUpOrderByUserId(user);
        replaceSensitive(results);
        return Result.success(results);
    }

    @PostMapping("/getbyorderid")
    public Result getByOrderIdd(@RequestBody Carshareorder order){
        Carshareorder byId = carshareorderService.getById(order);
        byId.setRemark(sensitiveWordBs.replace(byId.getRemark()));
        return Result.success(byId);
    }


    @PostMapping("/update")
    public Result update(@RequestBody Carshareorder order) {
        //  TODO 修改的时候要加上for update锁
        order.setUpdateAt(LocalDateTime.now());
        carshareorderService.updateById(order);
        return Result.success("修改成功");
    }

    @PostMapping("/delete")
    public Result deleteById(@RequestBody Carshareorder order){
        //  TODO 删除的时候要加上for update锁

        LambdaUpdateWrapper<Carshareorder> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Carshareorder::getIsDelete,0)
                .eq(Carshareorder::getOrderid,order.getOrderid())
                .set(Carshareorder::getIsDelete,1);
        boolean update = carshareorderService.update(wrapper);
        if (!update){
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }

    //获取用户发布的订单 第三者的视角 过滤掉超时的
    @PostMapping("/getbyuserid/up")
    public Result getUpOrderByUserId(@RequestBody Userinfo user){
        log.info("根据用户id获取用户发布的拼车订单: {}", user);
        // 获取用户发布的拼车 如果有人接受了 就附带上接受用户的信息
        LambdaQueryWrapper<Carshareorder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Carshareorder::getCreateuserid,user.getOpenid())
                .eq(Carshareorder::getIsDelete,0)
                .eq(Carshareorder::getStatus,0)
                .orderByDesc(Carshareorder::getStartdatetime)
                .ge(Carshareorder::getStartdatetime,LocalDateTime.now());
        List<Carshareorder> results = carshareorderService.list(wrapper);
        // 转一下格式 然后脱敏
        List<CarshareorderDto> dtoResults = results.stream()
                .map(i -> {
                    CarshareorderDto dto = new CarshareorderDto();
                    BeanUtils.copyProperties(i, dto);
                    return dto;
                })
                .collect(Collectors.toList());
        replaceSensitive(dtoResults);
        return Result.success(dtoResults);
    }

    // 对order的用户名和备注进行脱敏
    private void replaceSensitive(List<CarshareorderDto> results){
        results.forEach(i -> {
            if(i.getCreateUserInfo() != null){
                Userinfo userInfo = i.getCreateUserInfo();
                userInfo.setUsername(sensitiveWordBs.replace(userInfo.getUsername()));
                i.setCreateUserInfo(userInfo);
            }
            i.setRemark(sensitiveWordBs.replace(i.getRemark()));
        });
    }

    // order被接受后 发送微信消息
    private void sendUserMeg(String openid) throws IOException {
        // 创建HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String apiUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessTokenService.getAccessToken();
        // 1. 创建一个代表请求的HttpPost对象，并设置URL
        HttpPost httpPost = new HttpPost(apiUrl);

        //模板ID
        String template_id = TemplateId;
        //订阅消息内容组装
        SubscribeDataBean bean = new SubscribeDataBean();
        bean.setThing1(new SubscribeDataBean.Thing1("mygoSchool"));
        bean.setThing2(new SubscribeDataBean.Thing2("您发布的拼车已被接受"));
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(DateTimeFormatter.ofPattern("yyyy年M月d日 HH:mm"));
        bean.setTime3(new SubscribeDataBean.Time3(format));

        //组装接口所需对象
        SubscribeBean sendBean = new SubscribeBean();
        sendBean.setData(bean);//这里的订阅消息对象 不需要额外转json
        sendBean.setTouser(openid);
        sendBean.setTemplate_id(template_id);

        String jsonString = JSON.toJSONString(sendBean);
        // 3. 将JSON字符串封装成StringEntity，并设置内容类型为"application/json"
        HttpEntity entity = new StringEntity(jsonString, "UTF-8");

        // 4. 设置HttpPost请求的实体
        httpPost.setEntity(entity);

        // 5. 发送请求并获取响应
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseBody = EntityUtils.toString(responseEntity, "UTF-8");
                JSONObject object = JSON.parseObject(responseBody);
                String errmsg = object.getString("errmsg");
                if(!"ok".equals(errmsg)) {
                    log.error("发送消息模板失败 失败代码为: "+ object.getString("errcode"));
                } else log.info("发送消息模板成功");

            }
        }
    }


}
