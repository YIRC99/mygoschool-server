package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 拼车订单表
 * @TableName carshareorder
 */
@TableName(value ="carshareorder")
@Data
public class Carshareorder implements Serializable {
    /**
     * 拼车订单id
     */
    @TableId(type = IdType.AUTO)
    private Long orderid;

    /**
     * 创建用户id
     */
    private String createuserid;

    /**
     * 接受用户id
     */
    private String receiveuserid;

    /**
     * 出发地点全称
     */
    private String startaddressall;

    /**
     * 出发地点简称
     */
    private String startaddress;

    /**
     * 目的地全称
     */
    private String endaddressall;

    /**
     * 目的地简称
     */
    private String endaddress;

    /**
     * 出发日期时间
     */
    private LocalDateTime startdatetime;

    /**
     * 当前人数
     */
    private Integer currentperson;

    /**
     * 缺少人数
     */
    private Integer lackperson;

    /**
     * 
     */
    private String phonenumber;

    /**
     * 微信账户
     */
    private String wechataccount;

    /**
     * 订单接受时间
     */
    private LocalDateTime receivedatetime;

    /**
     * 是否提前
     */
    private Integer isbefore;

    /**
     * 提前时间
     */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime beforetime;

    /**
     * 是否延后
     */
    private Integer isafter;

    /**
     * 延后时间
     */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime aftertime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 
     */
    private LocalDateTime createat;

    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}