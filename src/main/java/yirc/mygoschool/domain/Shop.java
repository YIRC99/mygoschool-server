package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.Value;

/**
 * 
 * @TableName shop
 */
@TableName(value ="shop")
@Data
public class Shop implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using= ToStringSerializer.class) //使用jackson-datatype 把ID传承字符串传递
    private Long id;

    /**
     * 
     */
    private String detail;

    /**
     * 
     */
    private Double price;

    /**
     * 0 濂溪校区 1鹤问湖校区 2其他
     */
    private Integer address;

    /**
     * 图片列表 英文逗号分割
     */
    private String imgs;

    /**
     * 0在售 1下架 
     */
    private Integer status;

    /**
     * 
     */
    private String createuserid;

    /**
     * 
     */
    private String wechatimg;

    /**
     * 
     */
    private Integer isdelete;

    /**
     * 商品浏览量
     */
    private Integer browse;

    @TableField(value = "cancelTime")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime cancelTime;

    @TableField(value = "createAt")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createAt;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}