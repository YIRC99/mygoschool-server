package yirc.mygoschool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import yirc.mygoschool.Dto.PageInfoUser;
import yirc.mygoschool.common.WxResult;
import yirc.mygoschool.domain.Shop;
import yirc.mygoschool.domain.Userinfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 一见如初
* @description 针对表【userinfo】的数据库操作Service
* @createDate 2024-02-26 21:49:02
*/
public interface UserinfoService extends IService<Userinfo> {

    Userinfo getByOpenId(WxResult wxResult);

    Page<Userinfo> listByPage(PageInfoUser pageInfo);

}
