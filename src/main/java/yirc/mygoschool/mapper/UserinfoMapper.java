package yirc.mygoschool.mapper;

import org.apache.ibatis.annotations.Mapper;
import yirc.mygoschool.domain.Userinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 一见如初
* @description 针对表【userinfo】的数据库操作Mapper
* @createDate 2024-02-26 21:49:02
* @Entity yirc.mygoschool.domain.Userinfo
*/
@Mapper
public interface UserinfoMapper extends BaseMapper<Userinfo> {

}




