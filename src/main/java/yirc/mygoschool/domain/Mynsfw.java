package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

/**
 * 
 * @TableName mynsfw
 */
@TableName(value ="mynsfw")
@Data
public class Mynsfw implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 
     */
    private String postuseropenid;

    /**
     * 
     */
    private String imgpath;

    /**
     * 0待审核可能是违规  1不是违规图片 2违规图片
     */
    private Integer status;

    /**
     * nsfw分数
     */
    private Double score;

    @TableField(value = "createAt")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createAt;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}