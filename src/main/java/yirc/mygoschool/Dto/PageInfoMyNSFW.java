package yirc.mygoschool.Dto;

import lombok.Data;
import yirc.mygoschool.domain.PageInfo;

import java.io.Serializable;

/**
 * @Version v1.0
 * @DateTime 2024/4/27 0:42
 * @Description
 * @Author 一见如初
 */
@Data
public class PageInfoMyNSFW extends PageInfo implements Serializable {
    /**
     * 0待审核可能是违规  1不是违规图片 2违规图片
     */
    private Integer status;
}
