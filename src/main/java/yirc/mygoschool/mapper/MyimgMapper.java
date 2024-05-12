package yirc.mygoschool.mapper;

import org.apache.ibatis.annotations.Mapper;
import yirc.mygoschool.domain.Myimg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 一见如初
* @description 针对表【myimg(用来记录上传的图片方便后期查询无用图片使用)】的数据库操作Mapper
* @createDate 2024-05-12 19:55:05
* @Entity yirc.mygoschool.domain.Myimg
*/
@Mapper
public interface MyimgMapper extends BaseMapper<Myimg> {

}




