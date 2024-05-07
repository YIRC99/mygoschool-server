package yirc.mygoschool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Notice;
import yirc.mygoschool.service.NoticeService;

import java.util.Objects;

/**
 * @Version v1.0
 * @DateTime 2024/5/8 1:00
 * @Description TODO
 * @Author 一见如初
 */
@Slf4j
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    // 这个不会多的 基本都是静态 所以直接全查
    @PostMapping("/list")
    public Result list() {
        return Result.success(noticeService.list());
    }

    @PostMapping("/update")
    public Result update(Notice notice) {
        log.info("修改软件介绍");
        if(Objects.isNull(notice.getId())) return Result.error("参数错误 修改失败");
        noticeService.updateById(notice);
        return Result.success("修改成功");
    }


}
