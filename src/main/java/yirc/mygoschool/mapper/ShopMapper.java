package yirc.mygoschool.mapper;

import org.apache.ibatis.annotations.Mapper;
import yirc.mygoschool.domain.Shop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 一见如初
* @description 针对表【shop】的数据库操作Mapper
* @createDate 2024-04-01 23:59:29
* @Entity yirc.mygoschool.domain.Shop
*/
@Mapper
public interface ShopMapper extends BaseMapper<Shop> {

}




