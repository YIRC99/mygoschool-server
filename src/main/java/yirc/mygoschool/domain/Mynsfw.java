package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 
     */
    @TableField(value = "postUserOpenId")
    private String postuseropenid;

    /**
     *
     * 
     */
    @TableField(value = "imgPath")
    private String imgpath;

    /**
     * 0待审核可能是违规  1不是违规图片 2违规图片
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}