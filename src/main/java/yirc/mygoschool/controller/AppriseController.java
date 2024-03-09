package yirc.mygoschool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.common.OrderStatus;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Apprise;
import yirc.mygoschool.domain.Carshareorder;
import yirc.mygoschool.service.AppriseService;
import yirc.mygoschool.service.CarshareorderService;

/**
 * @Version v1.0
 * @DateTime 2024/3/8 15:50
 * @Description TODO
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

    @PostMapping("/add")
    @Transactional
    public Result add(@RequestBody Apprise apprise){
        log.info("要添加的评论为: {}",apprise);
        Carshareorder order = new Carshareorder();
        order.setOrderid(apprise.getPostId());
        order.setStatus(OrderStatus.SUCCESS);
        boolean car = carshareorderService.updateById(order);
        boolean app = appriseService.save(apprise);
        if(!car || !app){
            return Result.error("添加失败");
        }
        return Result.success("添加成功");
    }





}
