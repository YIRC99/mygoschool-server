package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @Version v1.0
 * @DateTime 2024/3/1 21:29
 * @Description 分页查询对象
 * @Author 一见如初
 */
@Data
public class PageInfo implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    private Integer pageNum;
    private Integer pageSize;
}
