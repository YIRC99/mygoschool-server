package yirc.mygoschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.Dto.PageInfoShop;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Affiche;
import yirc.mygoschool.domain.Shop;
import yirc.mygoschool.service.AfficheService;

import java.util.List;
import java.util.Objects;

/**
 * @Version v1.0
 * @DateTime 2024/3/12 11:26
 * @Description 公告控制器
 * @Author 一见如初
 */
@RestController
@Slf4j
@RequestMapping("/affiche")
public class AfficheController {

    @Autowired
    private AfficheService afficheService;

    // 添加
    @PostMapping("/add")
    public Result addAffiche(Affiche affiche) {
        log.info("AfficheController addAffiche 添加公告");
        if(Objects.isNull(affiche.getTitle()) || Objects.isNull(affiche.getText())){
            return Result.error("参数错误 添加失败");
        }
        afficheService.save(affiche);
        return Result.success("添加成功");
    }

    //分页查询
    @PostMapping("/page")
    public Result list(@RequestBody PageInfoShop pageInfo) {
        log.info("/page/list 分页查询的参数为: {} {} ",
                pageInfo.getPageNum(), pageInfo.getPageSize());

        if (Objects.isNull(pageInfo.getPageNum()) ||
                Objects.isNull(pageInfo.getPageSize()) ||
                (pageInfo.getAddressCodeArr().length == 0)) {
            return Result.error("分页参数错误");
        }
        Page<Affiche> page = afficheService.listByPage(pageInfo);
        return Result.success(page);
    }

    //修改
    @PostMapping("/update")
    public Result updateAffiche(Affiche affiche) {
        log.info("AfficheController updateAffiche 修改公告");
        if(Objects.isNull(affiche.getId())) return Result.error("参数错误 修改失败");
        afficheService.updateById(affiche);
        return Result.success("修改成功");
    }

    //获取最新
    @GetMapping
    public Result getNewAffiche() {
        log.info("AfficheController getNewAffiche 获取最新的公告");
        LambdaQueryWrapper<Affiche> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Affiche::getCreateat).last("limit 1");
        Affiche latestAffiche = afficheService.getOne(wrapper);
        return Result.success(latestAffiche);
    }



}
