package yirc.mygoschool.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yirc.mygoschool.Dto.PageInfoShop;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.PageInfo;
import yirc.mygoschool.domain.Shop;
import yirc.mygoschool.exception.CustomException;
import yirc.mygoschool.service.ShopService;

import java.util.Objects;

/**
 * @Version v1.0
 * @DateTime 2024/4/2 0:00
 * @Description TODO
 * @Author 一见如初
 */
@RestController
@Slf4j
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @PostMapping("/add")
    public Result addShop(@RequestBody Shop shop){
        log.info("shop:{}",shop);
        Thread.ofVirtual().start(() -> {
            shopService.SaveWeChatImg(shop);
        });
        boolean save = shopService.save(shop);
        if(save)
            return Result.success("添加成功");
        return Result.error("添加失败");
    }

    @PostMapping("/page")
    public Result list(@RequestBody PageInfoShop pageInfo){
        log.info("/page/list 分页查询的参数为: {} {} {}",
                pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getAddressCodeArr());

        if (Objects.isNull(pageInfo.getPageNum()) ||
            Objects.isNull(pageInfo.getPageSize()) ||
            (pageInfo.getAddressCodeArr().length == 0)){
            return Result.error("分页参数错误");
        }
        Page<Shop> page =  shopService.listByPage(pageInfo);
        return Result.success(page);
    }


}
