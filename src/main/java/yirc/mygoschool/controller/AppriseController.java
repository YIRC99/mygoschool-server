package yirc.mygoschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.Dto.AppriseDto;
import yirc.mygoschool.common.OrderStatus;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Apprise;
import yirc.mygoschool.domain.Carshareorder;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.service.AppriseService;
import yirc.mygoschool.service.CarshareorderService;

import java.util.List;

/**
 * @Version v1.0
 * @DateTime 2024/3/8 15:50
 * @Description 评论控制器
 * @Author 一见如初
 */
@RestController
@Slf4j
@RequestMapping("apprise")
public class AppriseController {

    @Autowired
    private AppriseService appriseService;

    @Autowired
    private CarshareorderService carshareorderService;

    @Autowired
    private SensitiveWordBs sensitiveWordBs;

    @PostMapping("/add")
    @Transactional
    public Result add(@RequestBody AppriseDto apprise){
        log.info("要添加的评论为: {}",apprise);
        log.info("OrderCreateUserId: {}",apprise.getOrderCreateUserId());

        // 如果拼车的发布人是我自己 就是发布页面的评价 否则就是接受页面的评价
        Apprise app = new Apprise();
        BeanUtils.copyProperties(apprise,app);
        boolean isapp = appriseService.save(app);
        Carshareorder order = new Carshareorder();
        order.setOrderid(apprise.getPostId());
        order.setStatus(OrderStatus.SUCCESS);
        // 拼车发布者 等于 评价的发布者 那就一定给别人评价
        if (apprise.getCreateuserid().equals(apprise.getOrderCreateUserId())){
            order.setCreateUserAppriseId(app.getId()); // 发布人评价
        }else{
            order.setReceiveUserAppriseId(app.getId()); // 接受人评价
        }

        boolean car = carshareorderService.updateById(order);
        if(!car || !isapp){
            return Result.error("添加失败");
        }
        return Result.success("添加成功");
    }

    @PostMapping("/byuserid")
    public Result getByUserId(@RequestBody Userinfo user){
        LambdaQueryWrapper<Apprise> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Apprise::getIsdelate,0)
                .eq(Apprise::getReceiveuserid,user.getOpenid());
        List<Apprise> list = appriseService.list(wrapper);
        list.forEach(i -> {
            i.setApprisedata(sensitiveWordBs.replace(i.getApprisedata()));
        });
        return Result.success(list);
    }



}
