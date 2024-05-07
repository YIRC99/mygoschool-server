package yirc.mygoschool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import yirc.mygoschool.Dto.PageInfoShop;
import yirc.mygoschool.domain.Affiche;
import com.baomidou.mybatisplus.extension.service.IService;
import yirc.mygoschool.domain.Shop;

/**
* @author 一见如初
* @description 针对表【affiche(公告表)】的数据库操作Service
* @createDate 2024-03-12 11:25:38
*/
public interface AfficheService extends IService<Affiche> {

    Page<Affiche> listByPage(PageInfoShop pageInfo);
}
