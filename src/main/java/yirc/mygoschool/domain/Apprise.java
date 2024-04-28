package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

/**
 * 
 * @TableName apprise
 */
@TableName(value ="apprise")
@Data
public class Apprise implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 评论人
     */
    private String createuserid;

    /**
     * 被评价人id
     */
    private String receiveuserid;

    /**
     * 拼接内容
     */
    private String apprisedata;

    /**
     * 评论内容的类型  拼车 1 二手 2  
     */
    private Integer type;

    @TableField(value = "postId")
    private Long postId;

    /**
     * 
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createat;

    /**
     * 
     */
    private Integer isdelate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}