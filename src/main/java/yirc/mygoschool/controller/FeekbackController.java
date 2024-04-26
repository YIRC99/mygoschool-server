package yirc.mygoschool.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yirc.mygoschool.Dto.PageInfoFeedback;
import yirc.mygoschool.Dto.PageInfoUser;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Feekback;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.service.FeekbackService;

import java.util.Objects;

/**
 * @Version v1.0
 * @DateTime 2024/3/3 22:41
 * @Description 反馈控制器
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

    @PostMapping("/page")
    public Result list(@RequestBody PageInfoFeedback pageInfo) {
        log.info("/feedback/list 分页查询的参数为: {} {} {} {}",
                pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getReportType(),pageInfo.getStatus());

        if (Objects.isNull(pageInfo.getPageNum()) ||
                Objects.isNull(pageInfo.getPageSize())) {
            return Result.error("分页参数错误");
        }
        Page<Feekback> page = feekbackService.listByPage(pageInfo);
        return Result.success(page);
    }






}
