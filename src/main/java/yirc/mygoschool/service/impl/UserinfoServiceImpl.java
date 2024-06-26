package yirc.mygoschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import yirc.mygoschool.Dto.PageInfoUser;
import yirc.mygoschool.common.WxResult;
import yirc.mygoschool.domain.Shop;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.service.UserinfoService;
import yirc.mygoschool.mapper.UserinfoMapper;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

/**
* @author 一见如初
* @description 针对表【userinfo】的数据库操作Service实现
* @createDate 2024-02-26 21:49:02
*/
@Service
public class UserinfoServiceImpl extends ServiceImpl<UserinfoMapper, Userinfo>
    implements UserinfoService{

    @Value("${yirc99.filePath}")
    private String filePath;

    @Override
    public Userinfo getByOpenId(WxResult wxResult) {
        LambdaQueryWrapper<Userinfo> query = new LambdaQueryWrapper<>();
        query.eq(Userinfo::getOpenid, wxResult.getOpenid());
        query.eq(Userinfo::getIsblack,0);
        Userinfo userinfo = this.getOne(query);
        if (userinfo == null){
            //第一次登录的话 随机生成一个头像和名称
            Userinfo newUser = new Userinfo();
            newUser.setOpenid(wxResult.getOpenid());
            newUser.setUsername("微信用户"+ UUID.randomUUID().toString().substring(0,3));
            // 随机生成一个[1,33]闭区间的数字 代表随机图片
            Integer avatarIndex = new Random().nextInt(1, 34);
            newUser.setAvatar(avatarIndex + ".jpg");
            newUser.setSex(1);
            newUser.setIsblack(0);
            newUser.setReportnum(0);
            this.saveOrUpdate(newUser);
            return newUser;
        }
        return userinfo;
    }

    @Override
    public Page<Userinfo> listByPage(PageInfoUser pageInfo) {
        Page<Userinfo> page = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        LambdaQueryWrapper<Userinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(pageInfo.getIsBlack() != null, Userinfo::getIsblack, pageInfo.getIsBlack())
                .orderByDesc(Userinfo::getCreateAt);
        Page<Userinfo> userinfoPage = this.page(page, wrapper);
        return userinfoPage;
    }
}




