package yirc.mygoschool.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yirc.mygoschool.Dto.PageInfoMyNSFW;
import yirc.mygoschool.anno.AdminRequest;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Mynsfw;
import yirc.mygoschool.service.MynsfwService;

import java.util.Objects;

/**
 * @Version v1.0
 * @DateTime 2024/4/26 23:59
 * @Description 后台管理端管理NSFW图片的控制类
 * @Author 一见如初
 */
@Slf4j
@RequestMapping("/nsfw")
@RestController
public class NSFWController {

    @Autowired
    private MynsfwService mynsfwService;

    /**
     * 分页查询NSFW图片
     */
    @PostMapping("/page")
    @AdminRequest
    public Result list(@RequestBody PageInfoMyNSFW pageInfo) {
        log.info("/nsfw/list 分页查询的参数为: {} {} {}",
                pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getStatus());

        if (Objects.isNull(pageInfo.getPageNum()) ||
                Objects.isNull(pageInfo.getPageSize())) {
            return Result.error("分页参数错误");
        }
        Page<Mynsfw> page = mynsfwService.listByPage(pageInfo);
        return Result.success(page);
    }

    /**
     * 标记图片为NSFW
     */
    @PostMapping("/sign")
    @AdminRequest
    public Result sign(@RequestBody Mynsfw mynsfw){
        if(Objects.isNull(mynsfw.getId())){
            return Result.error("参数错误");
        }
        return mynsfwService.sign(mynsfw);
    }

    /**
     * 恢复NSFW图片标记
     */
    @PostMapping("/recover")
    @AdminRequest
    public Result cancel(@RequestBody Mynsfw mynsfw){
        if(Objects.isNull(mynsfw.getId())){
            return Result.error("参数错误");
        }
        return mynsfwService.recover(mynsfw);
    }

    /**
     * 标记图片为正常图片
     */
    @PostMapping("/fine")
    @AdminRequest
    public Result fine(@RequestBody Mynsfw mynsfw){
        if(Objects.isNull(mynsfw.getId())){
            return Result.error("参数错误");
        }
        // TODO 这里直接标记图片为正常图片
        // 如果后期我觉得那个是nsfw图片 我需要改变图片的状态 这个还没有测试
        // 等后期 标记图片之后 然后在恢复图片  循环暴力测试
        Mynsfw byId = mynsfwService.getById(mynsfw);
        if(byId.getStatus() != 0)
            return Result.error("图片状态错误,无法标记");
        byId.setStatus(1);
        mynsfwService.updateById(byId);
        return Result.success("标记成功");
    }

}
