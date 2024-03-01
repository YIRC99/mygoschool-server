package yirc.mygoschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.UpdatableSqlQuery;
import yirc.mygoschool.domain.Carshareorder;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.service.CarshareorderService;
import yirc.mygoschool.mapper.CarshareorderMapper;
import org.springframework.stereotype.Service;
import yirc.mygoschool.service.UserinfoService;

/**
* @author 一见如初
* @description 针对表【carshareorder(拼车订单表)】的数据库操作Service实现
* @createDate 2024-03-01 15:19:17
*/
@Service
public class CarshareorderServiceImpl extends ServiceImpl<CarshareorderMapper, Carshareorder>
    implements CarshareorderService{

    @Autowired
    private UserinfoService userinfoService;

    @Override
    public boolean isSavePhoneOrWeChat(Carshareorder order) {
        if (order.getPhonenumber() == null && order.getWechataccount() == null)
            return false;
        if (order.getPhonenumber() != null){
            LambdaUpdateWrapper<Userinfo> query = new LambdaUpdateWrapper<>();
            query.eq(Userinfo::getOpenid,order.getCreateuserid())
                    .set(Userinfo::getUserphone,order.getPhonenumber())
                    .isNull(Userinfo::getUserphone);
            userinfoService.update(query);
        }
        // 同样的对微信号也做同样的处理
        if (order.getWechataccount() != null){
            LambdaUpdateWrapper<Userinfo> query = new LambdaUpdateWrapper<>();
            query.eq(Userinfo::getOpenid,order.getCreateuserid())
                    .set(Userinfo::getUserwx,order.getWechataccount())
                    .isNull(Userinfo::getUserwx);
            userinfoService.update(query);
        }
        return true;
    }
}




