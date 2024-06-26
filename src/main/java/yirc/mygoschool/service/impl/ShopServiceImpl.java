package yirc.mygoschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import org.springframework.beans.factory.annotation.Autowired;
import yirc.mygoschool.Dto.PageInfoShop;
import yirc.mygoschool.domain.Carshareorder;
import yirc.mygoschool.domain.Shop;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.service.MyimgService;
import yirc.mygoschool.service.ShopService;
import yirc.mygoschool.mapper.ShopMapper;
import org.springframework.stereotype.Service;
import yirc.mygoschool.service.UserinfoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author 一见如初
 * @description 针对表【shop】的数据库操作Service实现
 * @createDate 2024-04-01 23:59:29
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop>
        implements ShopService {

    @Autowired
    private UserinfoService userinfoService;

    @Autowired
    private SensitiveWordBs sensitiveWordBs;

    @Autowired
    private MyimgService myimgService;

    @Override
    public boolean SaveWeChatImg(Shop shop) {
        if (Objects.isNull(shop.getWechatimg())) return false;
        LambdaUpdateWrapper<Userinfo> query = new LambdaUpdateWrapper<>();
        query.eq(Userinfo::getOpenid, shop.getCreateuserid())
                .set(Userinfo::getUserWxImg, shop.getWechatimg());
        return userinfoService.update(query);
    }

    @Override
    public Page<Shop> listByPage(PageInfoShop pageInfo) {
        Page<Shop> page = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        LambdaUpdateWrapper<Shop> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Shop::getIsdelete, 0)
                .eq(Shop::getStatus, 0); //这个状态0在售 1下架
        wrapper.gt(Shop::getCancelTime, LocalDateTime.now());
        wrapper.orderByDesc(Shop::getCreateAt);
        wrapper.in(Shop::getAddress, pageInfo.getAddressCodeArr());


        Page<Shop> pageinfo = this.page(page, wrapper);
        pageinfo.getRecords().forEach(shop -> {
            shop.setDetail(sensitiveWordBs.replace(shop.getDetail()));
        });

        return pageinfo;
    }

    @Override
    public Page<Shop> Search(PageInfoShop pageInfo) {
        Page<Shop> page = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());

        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop::getIsdelete, 0)
                .eq(Shop::getStatus, 0);
        wrapper.and(w -> {
            for (String t : pageInfo.getTarget()) {
                w.or().like(Shop::getDetail, t);
            }
        });
        Page<Shop> pageinfo = this.page(page, wrapper);
        pageinfo.getRecords().forEach(shop -> {
            shop.setDetail(sensitiveWordBs.replace(shop.getDetail()));
        });
        return pageinfo;
    }

    @Override
    public List<Shop> byUserId(String openid) {
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop::getCreateuserid, openid)
                .orderByDesc(Shop::getCreateAt)
                .eq(Shop::getIsdelete,0);
        List<Shop> list = this.list(wrapper);
        list.forEach(shop -> {
            shop.setDetail(sensitiveWordBs.replace(shop.getDetail()));
        });
        return list;
    }

    @Override
    public boolean MyUpdateById(Shop shop) {
        shop.setBrowse(0);
        Shop byId = this.getById(shop.getId());
        // 将旧图片标记为未使用
        myimgService.MydeleteImgUseList(byId.getImgs());
        //新图片标记为使用
        myimgService.MyAddImgUseList(shop.getImgs());
        return this.updateById(shop);
    }
}




