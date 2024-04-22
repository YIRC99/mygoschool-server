package yirc.mygoschool.controller;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.sensitiveWord.SensitiveWordService;

import java.util.Map;

/**
 * @Version v1.0
 * @DateTime 2024/3/26 0:07
 * @Description TODO
 * @Author 一见如初
 */
@Slf4j
@RestController
@RequestMapping("/sensitive")
public class SensitiveController {

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private SensitiveWordBs sensitiveWordBs;


    @PostMapping("/test")
    public Result test(@RequestBody Map<String,String> obj){
        String result = sensitiveWordBs.replace(obj.get("text"));
        log.info("result:{}", result);
        return Result.success(result);
    }

    @PostMapping
    public Result myRefresh(){
        // TODO 这个接口使用来刷新敏感词的 但是必须只有我才能 不然就可以一直耍我的接口
        // 后期必须加上唯一密码
        sensitiveWordService.refresh();
        return Result.success("重置成功");
    }

}
