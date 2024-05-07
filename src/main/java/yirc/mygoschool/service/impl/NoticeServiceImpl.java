package yirc.mygoschool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import yirc.mygoschool.domain.Notice;
import yirc.mygoschool.service.NoticeService;
import yirc.mygoschool.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

/**
* @author 一见如初
* @description 针对表【notice】的数据库操作Service实现
* @createDate 2024-05-08 00:59:04
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{

}




