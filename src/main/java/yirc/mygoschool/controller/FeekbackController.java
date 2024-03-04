package yirc.mygoschool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Feekback;
import yirc.mygoschool.service.FeekbackService;

/**
 * @Version v1.0
 * @DateTime 2024/3/3 22:41
 * @Description TODO
 * @Author 一见如初
 */
@RestController
@RequestMapping("feekback/")
@Slf4j
public class FeekbackController {

    @Autowired
    private FeekbackService feekbackService;

    @PostMapping("add")
    public Result add(@RequestBody Feekback feekback){
        log.info("添加反馈: {}",feekback);
        feekbackService.save(feekback);
        return Result.success("反馈成功");
    }



}
