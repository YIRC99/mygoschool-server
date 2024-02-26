package yirc.mygoschool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.service.UserinfoService;

/**
 * @Version v1.0
 * @DateTime 2024/2/26 21:53
 * @Description TODO
 * @Author 一见如初
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserinfoController {

    @Autowired
    private UserinfoService userinfoService;

    @PostMapping
    public Result login(@RequestParam String code){
        log.info("code:{}", code);
        Userinfo userinfo = userinfoService.getById(1);
        return Result.success(userinfo);
    }



}
