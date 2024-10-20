package yirc.mygoschool.Dto;

import lombok.Data;
import yirc.mygoschool.domain.PageInfo;

import java.io.Serializable;

/**
 * @Version v1.0
 * @DateTime 2024/4/26 23:02
 * @Description 分户分页条件类
 * @Author 一见如初
 */
@Data
public class PageInfoUser extends PageInfo  implements Serializable {
    private Integer isBlack;
}
