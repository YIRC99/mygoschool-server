package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 
 * @TableName feekback
 */
@TableName(value ="feekback")
@Data
public class Feekback implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using= ToStringSerializer.class) //使用jackson-datatype 把ID传承字符串传递
    private Long id;

    /**
     * 
     */
    private String userid;

    /**
     * 
     */
    private String remark;

    /**
     * 
     */
    private String imglist;

    /**
     * 反馈的状态 未解决: 0  已解决: 1
     */
    private Integer status;

    /**
     * 
     */
    private Date createat;

    @TableField(value = "reportId")
    private long reportId;

    @TableField(value = "isDelete")
    private Integer isDelete;

    /**
     * 0就是意见反馈 1就是拼车投诉 2就是闲置投诉
     */
    @TableField(value = "reportType")
    private Integer reportType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}