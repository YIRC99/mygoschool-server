package yirc.mygoschool.mapper;

import org.apache.ibatis.annotations.Mapper;
import yirc.mygoschool.domain.Carshareorder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 一见如初
* @description 针对表【carshareorder(拼车订单表)】的数据库操作Mapper
* @createDate 2024-03-01 15:19:17
* @Entity yirc.mygoschool.domain.Carshareorder
*/
@Mapper
public interface CarshareorderMapper extends BaseMapper<Carshareorder> {

}




