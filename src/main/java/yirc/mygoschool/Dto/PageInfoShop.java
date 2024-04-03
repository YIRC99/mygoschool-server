package yirc.mygoschool.Dto;

import lombok.Data;
import yirc.mygoschool.domain.PageInfo;

import java.time.LocalDate;

/**
 * @Version v1.0
 * @DateTime 2024/4/3 21:32
 * @Description shop的分页请求的扩展类
 * @Author 一见如初
 */
@Data
public class PageInfoShop extends PageInfo {
    private Integer addressCode;

}
