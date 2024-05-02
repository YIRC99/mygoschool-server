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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
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
    @JsonSerialize(using= ToStringSerializer.class) //使用jackson-datatype 把ID传承字符串传递
    private Long orderid;

    /**
     * 创建用户id
     */
    private String createuserid;

    /**
     * 接受用户id
     */
    private String receiveuserid;

    @TableField(value = "receiveUserWechatImg")
    private String receiveUserWechatImg;

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
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
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

    @TableField(value = "wechatImg")
    private String wechatImg;

    /**
     * 订单接受时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime receivedatetime;

    /**
     * 是否提前
     */
    private Integer isbefore;

    /**
     * 提前时间
     */
    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime beforetime;

    /**
     * 是否延后
     */
    private Integer isafter;

    /**
     * 延后时间
     */
    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    private LocalTime aftertime;

    /**
     * 备注
     */
    private String remark;

    //创建订单的用户对订单的评价id
    @TableField(value = "createUserAppriseId")
    private Long createUserAppriseId;

    //接受订单用户对订单的评价Id
    @TableField(value = "receiveUserAppriseId")
    private Long receiveUserAppriseId;

    /**
     * 
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createat;

    @TableField(value = "updateAt")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateAt;

    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}