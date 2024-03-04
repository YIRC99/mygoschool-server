package yirc.mygoschool.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import yirc.mygoschool.Dto.CarshareorderDto;
import yirc.mygoschool.domain.Carshareorder;
import com.baomidou.mybatisplus.extension.service.IService;
import yirc.mygoschool.domain.PageInfo;

import java.util.List;

/**
* @author 一见如初
* @description 针对表【carshareorder(拼车订单表)】的数据库操作Service
* @createDate 2024-03-01 15:19:17
*/
public interface CarshareorderService extends IService<Carshareorder> {

    boolean isSavePhoneOrWeChat(Carshareorder carshareorder);

    Page<CarshareorderDto> listByPage(PageInfo pageInfo);


    Carshareorder selectForUpdate(Long orderid);
}
