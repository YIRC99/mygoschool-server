package yirc.mygoschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import yirc.mygoschool.Dto.PageInfoShop;
import yirc.mygoschool.domain.Affiche;
import yirc.mygoschool.domain.Shop;
import yirc.mygoschool.service.AfficheService;
import yirc.mygoschool.mapper.AfficheMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author 一见如初
* @description 针对表【affiche(公告表)】的数据库操作Service实现
* @createDate 2024-03-12 11:25:38
*/
@Service
public class AfficheServiceImpl extends ServiceImpl<AfficheMapper, Affiche>
    implements AfficheService{


    @Override
    public Page<Affiche> listByPage(PageInfoShop pageInfo) {
        Page<Affiche> page = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        return this.page(page);
    }
}




