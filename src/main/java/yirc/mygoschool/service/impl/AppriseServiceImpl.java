package yirc.mygoschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import yirc.mygoschool.Dto.PageInfoApprise;
import yirc.mygoschool.domain.Apprise;
import yirc.mygoschool.service.AppriseService;
import yirc.mygoschool.mapper.AppriseMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
* @author 一见如初
* @description 针对表【apprise】的数据库操作Service实现
* @createDate 2024-03-08 15:49:19
*/
@Service
public class AppriseServiceImpl extends ServiceImpl<AppriseMapper, Apprise>
    implements AppriseService{

    @Override
    public Page<Apprise> MyPage(PageInfoApprise pageinfo) {
        Page<Apprise> page = new Page<>(pageinfo.getPageNum(), pageinfo.getPageSize());
        LambdaQueryWrapper<Apprise> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Apprise::getIsdelate, 0)
                .like(!Objects.isNull(pageinfo.getCreateuserid()),Apprise::getCreateuserid,pageinfo.getCreateuserid())
                .like(!Objects.isNull(pageinfo.getReceiveuserid()),Apprise::getReceiveuserid,pageinfo.getReceiveuserid())
                .orderByDesc(Apprise::getCreateuserid);
        return this.page(page, wrapper);
    }
}




