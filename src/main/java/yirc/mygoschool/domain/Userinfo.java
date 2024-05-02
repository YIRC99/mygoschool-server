package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

/**
 * 
 * @TableName userinfo
 */
@TableName(value ="userinfo")
@Data
public class Userinfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String userid;

    @TableField(value = "openId")
    private String openid;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String userwx;

    @TableField(value = "userWxImg")
    private String userWxImg;

    /**
     * 
     */
    private String userphone;

    /**
     * 
     */
    private String avatar;

    /**
     * 1为男 0为女
     */
    private Integer sex;

    /**
     * 举报次数
     */
    private Integer reportnum;

    /**
     * 是不是学生
     */
    private Integer isstudent;

    /**
     * 是不是被封号了
     */
    private Integer isblack;

    @TableField(value = "createAt")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createAt;

    @TableField(value = "token")
    private String token;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}