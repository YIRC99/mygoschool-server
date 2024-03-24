package yirc.mygoschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Affiche;
import yirc.mygoschool.service.AfficheService;

import java.util.List;

/**
 * @Version v1.0
 * @DateTime 2024/3/12 11:26
 * @Description TODO
 * @Author 一见如初
 */
@RestController
@Slf4j
@RequestMapping("affiche")
public class AfficheController {

    @Autowired
    private AfficheService afficheService;

    @GetMapping
    public Result getNewAffiche() {
        log.info("AfficheController getNewAffiche 获取最新的公告");
        LambdaQueryWrapper<Affiche> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Affiche::getCreateat).last("limit 1");
        Affiche latestAffiche = afficheService.getOne(wrapper);
        return Result.success(latestAffiche);
    }



}
