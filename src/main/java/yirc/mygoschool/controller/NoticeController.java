package yirc.mygoschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Notice;
import yirc.mygoschool.service.NoticeService;

import java.util.Objects;

/**
 * @Version v1.0
 * @DateTime 2024/5/8 1:00
 * @Description 后台管理端管理软件介绍的控制类
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
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getIsdelete,0)
                .orderByDesc(Notice::getSort)
                .orderByDesc(Notice::getId);
        return Result.success(noticeService.list(wrapper));
    }

    @PostMapping("/update")
    public Result update(@RequestBody Notice notice) {
        log.info("修改软件介绍");
        if(Objects.isNull(notice.getId())) return Result.error("参数错误 修改失败");
        noticeService.updateById(notice);
        return Result.success("修改成功");
    }

    @PostMapping("/add")
    public Result add(@RequestBody Notice notice){
        if(Objects.isNull(notice.getTitle()) || Objects.isNull(notice.getText()))
            return Result.error("标题或内容为空");
        boolean save = noticeService.save(notice);
        if (save)
            return Result.success("success");
        else
            return Result.error("添加失败");
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Notice notice){
        if(Objects.isNull(notice.getId())) return Result.error("参数错误 删除失败");
        notice.setIsdelete(1);
        noticeService.updateById(notice);
        return Result.success("删除成功");
    }




}
