package yirc.mygoschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import yirc.mygoschool.Dto.PageInfoFeedback;
import yirc.mygoschool.domain.Feekback;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.service.FeekbackService;
import yirc.mygoschool.mapper.FeekbackMapper;
import org.springframework.stereotype.Service;

/**
* @author 一见如初
* @description 针对表【feekback】的数据库操作Service实现
* @createDate 2024-03-03 22:40:07
*/
@Service
public class FeekbackServiceImpl extends ServiceImpl<FeekbackMapper, Feekback>
    implements FeekbackService{

    @Override
    public Page<Feekback> listByPage(PageInfoFeedback pageInfo) {
        Page<Feekback> page = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        LambdaQueryWrapper<Feekback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(pageInfo.getReportType() != null, Feekback::getReportType, pageInfo.getReportType())
                .eq(pageInfo.getStatus() != null,Feekback::getStatus, pageInfo.getStatus())
                .orderByDesc(Feekback::getCreateat);
        return this.page(page, wrapper);
    }
}




