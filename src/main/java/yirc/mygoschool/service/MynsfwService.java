package yirc.mygoschool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import yirc.mygoschool.Dto.PageInfoMyNSFW;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Mynsfw;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 一见如初
* @description 针对表【mynsfw】的数据库操作Service
* @createDate 2024-04-26 23:58:56
*/
public interface MynsfwService extends IService<Mynsfw> {

    Page<Mynsfw> listByPage(PageInfoMyNSFW pageInfo);

    Result sign(Mynsfw mynsfw);

    Result recover(Mynsfw mynsfw);
}
