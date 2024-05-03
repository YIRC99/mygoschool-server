package yirc.mygoschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Affiche;
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

    //修改
    @PostMapping("/update")
    public Result updateAffiche(Affiche affiche) {
        log.info("AfficheController updateAffiche 修改公告");
        if(Objects.isNull(affiche.getId())) return Result.error("参数错误 修改失败");
        afficheService.updateById(affiche);
        return Result.success("修改成功");
    }

    @GetMapping
    public Result getNewAffiche() {
        log.info("AfficheController getNewAffiche 获取最新的公告");
        LambdaQueryWrapper<Affiche> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Affiche::getCreateat).last("limit 1");
        Affiche latestAffiche = afficheService.getOne(wrapper);
        return Result.success(latestAffiche);
    }



}
