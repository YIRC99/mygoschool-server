package yirc.mygoschool.mapper;

import org.apache.ibatis.annotations.Mapper;
import yirc.mygoschool.domain.Affiche;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 一见如初
* @description 针对表【affiche(公告表)】的数据库操作Mapper
* @createDate 2024-03-12 11:25:38
* @Entity yirc.mygoschool.domain.Affiche
*/
@Mapper
public interface AfficheMapper extends BaseMapper<Affiche> {

}




