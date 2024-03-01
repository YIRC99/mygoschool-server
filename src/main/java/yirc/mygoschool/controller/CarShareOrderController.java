package yirc.mygoschool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Carshareorder;
import yirc.mygoschool.service.CarshareorderService;

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

    @Autowired
    private CarshareorderService carshareorderService;

    @PostMapping("/add")
    public Result addCarShareOrder(@RequestBody Carshareorder carshareorder) {
        log.info("addCarShareOrder: {}",carshareorder);
        // 判断一下订单中是否有用户的联系信息保存一下 因为优先级并不高 所以开个虚拟线程就好
        Thread.ofVirtual().start(()->{carshareorderService.isSavePhoneOrWeChat(carshareorder);});
        var issuccess = carshareorderService.save(carshareorder);
        if(issuccess)
            return Result.success(carshareorder);
        return Result.error("添加失败");
    }


}
