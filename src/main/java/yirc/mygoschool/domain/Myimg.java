package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用来记录上传的图片方便后期查询无用图片使用
 * @TableName myimg
 */
@TableName(value ="myimg")
@Data
public class Myimg implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 图片的全路径
     */
    private String imgpath;

    /**
     * 是否使用默认为没有使用 定时删除没有使用的图片
     */
    private Integer isuse;

    /**
     * 
     */
    private Date createat;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}