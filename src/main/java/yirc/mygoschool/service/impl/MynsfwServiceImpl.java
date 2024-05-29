package yirc.mygoschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import yirc.mygoschool.Dto.PageInfoMyNSFW;
import yirc.mygoschool.Utils.MyUtil;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Mynsfw;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.service.MynsfwService;
import yirc.mygoschool.mapper.MynsfwMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author 一见如初
* @description 针对表【mynsfw】的数据库操作Service实现
* @createDate 2024-04-26 23:58:56
*/
@Service
public class MynsfwServiceImpl extends ServiceImpl<MynsfwMapper, Mynsfw>
    implements MynsfwService{

    @Autowired
    private MyUtil myUtil;

    @Override
    public Page<Mynsfw> listByPage(PageInfoMyNSFW pageInfo) {
        Page<Mynsfw> page = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        LambdaQueryWrapper<Mynsfw> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(pageInfo.getStatus() != null, Mynsfw::getStatus, pageInfo.getStatus())
                .orderByDesc(Mynsfw::getCreateAt);
        return this.page(page, wrapper);
    }

    @Override
    public Result sign(Mynsfw mynsfw) {
        Mynsfw byId = this.getById(mynsfw);
        if(Objects.isNull(byId)){
            return Result.error("图片不存在");
        }
        if(byId.getStatus() == 2 || byId.getImgpath() == null){
            return Result.error("图片状态错误,已处理");
        }
        // 防止io阻塞 使用虚拟线程 将违规图片备份并且替换掉
        VirtualSignNSFW(byId.getImgpath(), byId.getId());
        // TODO 这里还是有问题 因为是虚拟线程处理问题 如果虚拟线程失败了
        // 我这里一样会返回成功
        return Result.success("标记成功");
    }

    @Override
    public Result recover(Mynsfw mynsfw) {
        Mynsfw byId = this.getById(mynsfw);
        if(byId == null){
            return Result.error("图片不存在");
        }
        if(byId.getStatus() != 2 || byId.getImgpath() == null){
            return Result.error("图片状态错误,无法恢复");
        }
        VirtualRecoverNSFW(byId.getImgpath(), byId);
        byId.setStatus(1);
        this.updateById(byId);
        return Result.success("恢复成功");
    }

    // 根据图片路径恢复图片
    private void VirtualRecoverNSFW(String imgPath,Mynsfw byId) {
        Thread.ofVirtual().start(() -> {
            // 这里恢复之前先判断一下 如果纯在备份 就直接从备份里面拿数据 就不用recover了
//            if(!Objects.isNull(byId.getBackupImgPath())){
//                byId.setImgpath(byId.getBackupImgPath());
//                byId.setStatus(1);
//                this.updateById(byId);
//            }else{
//                // 处理没有备份 就是上来就是0然后直接标记为通过的图片类型
//                myUtil.recoverImg(imgPath);
//                byId.setStatus(1);
//                this.updateById(byId);
//            }
            // 这里就这样操作 因为我们替换图片都是直接替换本地图片不是io 所以就可以这样操作
            myUtil.recoverImg(imgPath);
        });
    }

    //替换NSFW图片
    private void VirtualSignNSFW(String imgPath, Long id){
        Thread.ofVirtual().start(() -> {
            // 这里标记图片的时候 .....
            String replacedImg = myUtil.replaceImg(imgPath);
            Mynsfw mynsfw = new Mynsfw();
            mynsfw.setId(id);
            mynsfw.setStatus(2);
            mynsfw.setBackupImgPath(replacedImg);
            this.updateById(mynsfw);
        });
    }

}




