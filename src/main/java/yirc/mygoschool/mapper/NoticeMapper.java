package yirc.mygoschool.mapper;

import org.apache.ibatis.annotations.Mapper;
import yirc.mygoschool.domain.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 一见如初
* @description 针对表【notice】的数据库操作Mapper
* @createDate 2024-05-08 00:59:04
* @Entity yirc.mygoschool.domain.Notice
*/
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

}




