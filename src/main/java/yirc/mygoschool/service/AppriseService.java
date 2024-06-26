package yirc.mygoschool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import yirc.mygoschool.Dto.PageInfoApprise;
import yirc.mygoschool.domain.Apprise;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 一见如初
* @description 针对表【apprise】的数据库操作Service
* @createDate 2024-03-08 15:49:19
*/
public interface AppriseService extends IService<Apprise> {

    Page<Apprise> MyPage(PageInfoApprise pageinfo);
}
