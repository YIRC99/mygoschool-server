package yirc.mygoschool.Dto;

import lombok.Data;
import yirc.mygoschool.domain.PageInfo;

/**
 * @Version v1.0
 * @DateTime 2024/4/26 23:43
 * @Description 意见查询条件分页类
 * @Author 一见如初
 */
@Data
public class PageInfoFeedback extends PageInfo {
    private Integer reportType;
    private Integer status;
}
