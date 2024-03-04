package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}