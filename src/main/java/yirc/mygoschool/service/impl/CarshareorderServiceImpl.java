package yirc.mygoschool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import yirc.mygoschool.Dto.CarshareorderDto;
import yirc.mygoschool.Dto.PageInfoCar;
import yirc.mygoschool.domain.Carshareorder;
import yirc.mygoschool.domain.PageInfo;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.service.CarshareorderService;
import yirc.mygoschool.mapper.CarshareorderMapper;
import org.springframework.stereotype.Service;
import yirc.mygoschool.service.UserinfoService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 一见如初
 * @description 针对表【carshareorder(拼车订单表)】的数据库操作Service实现
 * @createDate 2024-03-01 15:19:17
 */
@Service
public class CarshareorderServiceImpl extends ServiceImpl<CarshareorderMapper, Carshareorder>
        implements CarshareorderService {

    @Autowired
    private UserinfoService userinfoService;

    @Autowired
    private SensitiveWordBs sensitiveWordBs;

    @Override
    public boolean SaveWeChatImg(Carshareorder order) {
        if(Objects.isNull(order.getWechatImg())) return false;
        LambdaUpdateWrapper<Userinfo> query = new LambdaUpdateWrapper<>();
        query.eq(Userinfo::getOpenid, order.getCreateuserid())
                .set(Userinfo::getUserWxImg, order.getWechatImg());
        return userinfoService.update(query);
    }

    @Override
    public boolean isSavePhoneOrWeChat(Carshareorder order) {
        if (order.getPhonenumber() == null && order.getWechataccount() == null)
            return false;
        if (order.getPhonenumber() != null) {
            LambdaUpdateWrapper<Userinfo> query = new LambdaUpdateWrapper<>();
            query.eq(Userinfo::getOpenid, order.getCreateuserid())
                    .set(Userinfo::getUserphone, order.getPhonenumber())
                    .isNull(Userinfo::getUserphone);
            userinfoService.update(query);
        }
        // 同样的对微信号也做同样的处理
        if (order.getWechataccount() != null) {
            LambdaUpdateWrapper<Userinfo> query = new LambdaUpdateWrapper<>();
            query.eq(Userinfo::getOpenid, order.getCreateuserid())
                    .set(Userinfo::getUserwx, order.getWechataccount())
                    .and(i -> i.isNull(Userinfo::getUserwx).or().eq(Userinfo::getUserwx, ""));
            userinfoService.update(query);
        }
        return true;
    }

    @Override
    public Page<CarshareorderDto> listByPage(PageInfoCar pageInfo) {
        Page<CarshareorderDto> pageDto = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());

        Page<Carshareorder> page = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        LambdaQueryWrapper<Carshareorder> wrapper = new LambdaQueryWrapper<>();
        // 公共条件 没有删除 没有人接受 没有超时 开始时间降序
        wrapper.eq(Carshareorder::getIsDelete, 0);
        wrapper.isNull(Carshareorder::getReceiveuserid);
        wrapper.gt(Carshareorder::getStartdatetime, LocalDateTime.now());
        wrapper.orderByDesc(Carshareorder::getStartdatetime);

        // 根据传入的地址 获取不同的地址拼车信息
        if (pageInfo.getStartAddName() != null) {
            if (!"其他".equals(pageInfo.getStartAddName())) {
                // 新校区不仅濂溪校区一个关键词 可能出现 还有  九江职业大学新校区
                if ("濂溪校区".equals(pageInfo.getStartAddName())) {
                    // TODO 这里加一个需求  如果目的地是九职大也算
                    wrapper.and(i -> i.like(Carshareorder::getStartaddressall, pageInfo.getStartAddName())
                            .or().like(Carshareorder::getStartaddressall, "九江职业大学-北门")
                            .or().like(Carshareorder::getStartaddressall, "九江职业大学新校区"));
                } else if ("鹤问湖校区".equals(pageInfo.getStartAddName())) {
                    // TODO 这里加一个需求  如果目的地是九职大也算
                    wrapper.like(Carshareorder::getStartaddressall, pageInfo.getStartAddName());
                }
            } else {
                wrapper.notLike(Carshareorder::getStartaddressall, "濂溪校区");
                wrapper.notLike(Carshareorder::getStartaddressall, "九江职业大学-北门");
                wrapper.notLike(Carshareorder::getStartaddressall, "九江职业大学新校区");
                wrapper.notLike(Carshareorder::getStartaddressall, "鹤问湖校区");
            }
        }

        //如果有传入日期 那就构建边界 进行日期范围查询
        if (pageInfo.getPageDate() != null) {
            LocalDate targetDate = pageInfo.getPageDate();
            // 创建当天的开始时间
            LocalDateTime startOfDay = targetDate.atStartOfDay();
            // 创建当天的结束时间
            LocalDateTime endOfDay = targetDate.plusDays(1).atStartOfDay().minusSeconds(1);
            // 将 LocalDatetime 转换为数据库可识别的格式（假设数据库字段类型是 datetime）
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startDateStr = startOfDay.format(formatter);
            String endDateStr = endOfDay.format(formatter);
            // 在查询条件中添加时间范围查询
            wrapper.between(Carshareorder::getStartdatetime, startDateStr, endDateStr);
        }


        Page<Carshareorder> pageinfo = this.page(page, wrapper);
        BeanUtils.copyProperties(pageinfo, pageDto);
        List<CarshareorderDto> ResultList = new ArrayList<>();

        pageinfo.getRecords().forEach(item -> {
            CarshareorderDto Dto = new CarshareorderDto();
            LambdaQueryWrapper<Userinfo> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.eq(Userinfo::getOpenid, item.getCreateuserid());
            Userinfo userinfo = userinfoService.getOne(userWrapper);
            BeanUtils.copyProperties(item, Dto);
            Dto.setCreateUserInfo(userinfo);
            // 过滤敏感词
            Dto.setRemark(sensitiveWordBs.replace(Dto.getRemark()));
            Userinfo userInfo = Dto.getCreateUserInfo();
            userInfo.setUsername(sensitiveWordBs.replace(userInfo.getUsername()));
            Dto.setCreateUserInfo(userInfo);

            ResultList.add(Dto);
        });
        pageDto.setRecords(ResultList);
        return pageDto;
    }

    @Override
    public Carshareorder selectForUpdate(Long orderId) {
        LambdaQueryWrapper<Carshareorder> w = new LambdaQueryWrapper<>();
        w.eq(Carshareorder::getIsDelete, 0);
        w.eq(Carshareorder::getOrderid, orderId).last("for update");
        return this.getOne(w);
    }

    @Override
    public List<CarshareorderDto> getReceiveByUserId(Userinfo user) {
        LambdaQueryWrapper<Carshareorder> wrapper = new LambdaQueryWrapper<>();
        List<CarshareorderDto> ResultList = new ArrayList<>();
        wrapper.eq(Carshareorder::getIsDelete, 0)
                .eq(Carshareorder::getReceiveuserid, user.getOpenid())
                .orderByDesc(Carshareorder::getCreateat);
        List<Carshareorder> orders = this.list(wrapper);
        orders.forEach(item -> {
            CarshareorderDto Dto = new CarshareorderDto();
            BeanUtils.copyProperties(item, Dto);
            LambdaQueryWrapper<Userinfo> w = new LambdaQueryWrapper<>();
            w.eq(Userinfo::getOpenid, item.getCreateuserid());
            Userinfo one = userinfoService.getOne(w);
            Dto.setCreateUserInfo(one);
            ResultList.add(Dto);
        });
        return ResultList;
    }

    @Override
    public List<CarshareorderDto> getUpOrderByUserId(Userinfo user) {
        LambdaQueryWrapper<Carshareorder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Carshareorder::getIsDelete,0)
                .eq(Carshareorder::getCreateuserid,user.getOpenid())
                .orderByDesc(Carshareorder::getCreateat);
        List<Carshareorder> orders = this.list(wrapper);
        List<CarshareorderDto> orderDto = new ArrayList<>();

        for (Carshareorder order : orders) {
            CarshareorderDto dto = new CarshareorderDto();
            BeanUtils.copyProperties(order, dto); // 拷贝基本属性
            if (order.getStatus() != 0) {
                LambdaQueryWrapper<Userinfo> query = new LambdaQueryWrapper<>();
                query.eq(Userinfo::getOpenid, order.getReceiveuserid());
                Userinfo receiveUser = userinfoService.getOne(query);
                // 拷贝其他属性
                dto.setReceiveUserInfo(receiveUser); // 假设有一个方法将接收用户设置到 DTO 中
            }
            orderDto.add(dto); // 将 DTO 添加到列表中
        }
        return orderDto;
    }


}




