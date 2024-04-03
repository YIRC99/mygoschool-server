package yirc.mygoschool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import yirc.mygoschool.Dto.PageInfoShop;
import yirc.mygoschool.domain.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 一见如初
* @description 针对表【shop】的数据库操作Service
* @createDate 2024-04-01 23:59:29
*/
public interface ShopService extends IService<Shop> {

    boolean SaveWeChatImg(Shop shop);

    Page<Shop> listByPage(PageInfoShop pageInfo);
}
