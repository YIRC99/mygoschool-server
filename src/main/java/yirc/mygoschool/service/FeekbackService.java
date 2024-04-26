package yirc.mygoschool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import yirc.mygoschool.Dto.PageInfoFeedback;
import yirc.mygoschool.domain.Feekback;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 一见如初
* @description 针对表【feekback】的数据库操作Service
* @createDate 2024-03-03 22:40:07
*/
public interface FeekbackService extends IService<Feekback> {

    Page<Feekback> listByPage(PageInfoFeedback pageInfo);
}
