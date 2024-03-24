package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 公告表
 * @TableName affiche
 */
@TableName(value ="affiche")
@Data
public class Affiche implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String text;

    /**
     * 
     */
    private Date createat;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}