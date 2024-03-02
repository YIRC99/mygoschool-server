package yirc.mygoschool.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.Dto.CarshareorderDto;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Carshareorder;
import yirc.mygoschool.domain.PageInfo;
import yirc.mygoschool.exception.CustomException;
import yirc.mygoschool.service.CarshareorderService;

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

    @PostMapping("/page")
    public Result list(@RequestBody PageInfo pageInfo) {
        log.error("分页查询的参数为: {} {} {} {}",pageInfo.getPageNum(),pageInfo.getPageSize(),pageInfo.getPageDate(),pageInfo.getStartAddName());
        if(pageInfo.getPageNum() == null || pageInfo.getPageNum() < 1
                || pageInfo.getPageSize() == null || pageInfo.getPageSize() < 1){
            throw new CustomException("分页参数错误");
        }
        //分页查询全部拼车 附带筛选
        Page<CarshareorderDto> orderDtos = carshareorderService.listByPage(pageInfo);
        return Result.success(orderDtos);
    }



}
