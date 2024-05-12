package yirc.mygoschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import yirc.mygoschool.domain.Myimg;
import yirc.mygoschool.service.MyimgService;
import yirc.mygoschool.mapper.MyimgMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author 一见如初
* @description 针对表【myimg(用来记录上传的图片方便后期查询无用图片使用)】的数据库操作Service实现
 *              这里的方法基本除了save都是异步的因为不影响主业务的执行
* @createDate 2024-05-12 19:55:05
*/
@Service
public class MyimgServiceImpl extends ServiceImpl<MyimgMapper, Myimg>
    implements MyimgService{

    @Override
    public boolean MySave(String resultPath) {
        Myimg myimg = new Myimg();
        myimg.setImgpath(resultPath);
        myimg.setIsuse(0);
        return this.save(myimg);
    }

    @Override
    public void MyAddImgUseList(String imglist) {
        // 创建一个线程修改数据
        Thread.ofVirtual().start(() ->{
            if(Objects.isNull(imglist)) return;
            String[] imgs = imglist.split(",");
            LambdaUpdateWrapper<Myimg> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(Myimg::getIsuse,1);
            wrapper.and(w -> {
                for (String img : imgs) {
                    w.or().like(Myimg::getImgpath,img);
                }
            });
            this.update(wrapper);
        });
    }

    @Override
    public void MydeleteImgUseList(String imglist) {
        // 创建一个线程修改数据
        Thread.ofVirtual().start(() ->{
            if(Objects.isNull(imglist)) return;
            String[] imgs = imglist.split(",");
            LambdaUpdateWrapper<Myimg> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(Myimg::getIsuse,0);
            wrapper.and(w -> {
                for (String img : imgs) {
                    w.or().like(Myimg::getImgpath,img);
                }
            });
            this.update(wrapper);
        });
    }
}




