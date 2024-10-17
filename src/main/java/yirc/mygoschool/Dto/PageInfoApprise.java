package yirc.mygoschool.Dto;

import lombok.Data;
import yirc.mygoschool.domain.PageInfo;

import java.io.Serializable;

/**
 * 评价分页类
 * @Version v1.0
 * @DateTime 2024/5/22 10:54
 * @Description 评价分页类
 * @Author 一见如初
 */
@Data
public class PageInfoApprise  extends PageInfo  implements Serializable {
    /**
     * 评论人
     */
    private String createuserid;
    /**
     * 被评价人id
     */
    private String receiveuserid;
}
