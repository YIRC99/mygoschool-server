package yirc.mygoschool.controller;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Mysensitive;
import yirc.mygoschool.sensitiveWord.SensitiveWordService;
import yirc.mygoschool.service.MysensitiveService;

import java.util.Map;

/**
 * @Version v1.0
 * @DateTime 2024/3/26 0:07
 * @Description 违规词控制器
 * @Author 一见如初
 */
@Slf4j
@RestController
@RequestMapping("/sensitive")
public class SensitiveController {

    @Autowired
    private MysensitiveService mysensitiveService;

    @Autowired
    private SensitiveWordService sensitiveWordService;

    @Autowired
    private SensitiveWordBs sensitiveWordBs;

    // 查询所有的违规词
    @PostMapping("/list")
    public Result list(){
        return Result.success(mysensitiveService.list());
    }

    // 修改所有违规词
    @PostMapping
    public Result update(@RequestBody Mysensitive mysensitive){
        boolean result = mysensitiveService.updateById(mysensitive);
        if (!result)return Result.error("修改失败");
        myRefresh(); // 动态刷新违规词
        return Result.success("修改成功 请勿频繁修改违规词");
    }

    // 用来测试的违规词的接口  没啥用
    @PostMapping("/test")
    public Result test(@RequestBody Map<String,String> obj){
        String result = sensitiveWordBs.replace(obj.get("text"));
        log.info("result:{}", result);
        return Result.success(result);
    }

    @PostMapping
    public Result myRefresh(){
        sensitiveWordService.refresh();
        return Result.success("重置成功");
    }

}
