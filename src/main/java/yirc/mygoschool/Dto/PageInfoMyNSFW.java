package yirc.mygoschool.Dto;

import lombok.Data;
import yirc.mygoschool.domain.PageInfo;

/**
 * @Version v1.0
 * @DateTime 2024/4/27 0:42
 * @Description
 * @Author 一见如初
 */
@Data
public class PageInfoMyNSFW extends PageInfo {
    /**
     * 0待审核可能是违规  1不是违规图片 2违规图片
     */
    private Integer status;
}
