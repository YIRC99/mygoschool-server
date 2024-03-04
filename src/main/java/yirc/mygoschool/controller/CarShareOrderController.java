package yirc.mygoschool.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.Dto.CarshareorderDto;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.config.AccessTokenService;
import yirc.mygoschool.domain.*;
import yirc.mygoschool.exception.CustomException;
import yirc.mygoschool.service.CarshareorderService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    @Value("${yirc99.WxSecret}")
    private String AppSecret;

    @Value("${yirc99.Template.id}")
    private String TemplateId;

    @Autowired
    private CarshareorderService carshareorderService;

    @Autowired
    private AccessTokenService accessTokenService;

    @PostMapping("/add")
    public Result addCarShareOrder(@RequestBody Carshareorder carshareorder) {
        log.info("addCarShareOrder: {}", carshareorder);
        // 判断一下订单中是否有用户的联系信息保存一下 因为优先级并不高 所以开个虚拟线程就好
        Thread.ofVirtual().start(() -> {
            carshareorderService.isSavePhoneOrWeChat(carshareorder);
        });
        var issuccess = carshareorderService.save(carshareorder);
        if (issuccess)
            return Result.success(carshareorder);
        return Result.error("添加失败");
    }

    @PostMapping("/page")
    public Result list(@RequestBody PageInfo pageInfo) {
        log.error("分页查询的参数为: {} {} {} {}", pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getPageDate(), pageInfo.getStartAddName());
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
            return Result.error("订单已经被接受。");
        }

        // 更新订单
        lockedOrder.setReceiveuserid(order.getReceiveuserid());
        boolean b = carshareorderService.updateById(lockedOrder);
        // 跟新成功了 才发送消息
        if(b){
            sendUserMeg(order.getCreateuserid());
        }
        return Result.success("订单接受成功。");
    }

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
        bean.setThing1(new SubscribeDataBean.Thing1("拼车订单已被接受"));
        bean.setThing2(new SubscribeDataBean.Thing2("对方微信:YIRC99"));
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
                if(!"ok".equals(errmsg)) throw new CustomException("发送消息模板失败 失败代码为: "+ object.getString("errcode"));
                else log.info("发送消息模板成功");
            }
        }
    }


}
