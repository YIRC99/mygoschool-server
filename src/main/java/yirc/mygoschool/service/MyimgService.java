package yirc.mygoschool.service;

import yirc.mygoschool.domain.Myimg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 一见如初
* @description 针对表【myimg(用来记录上传的图片方便后期查询无用图片使用)】的数据库操作Service
* @createDate 2024-05-12 19:55:05
*/
public interface MyimgService extends IService<Myimg> {

    boolean MySave(String resultPath);

    void MyAddImgUseList(String imglist);

    void MydeleteImgUseList(String imgs);
}
