package yirc.mygoschool.Dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import yirc.mygoschool.domain.PageInfo;

import java.time.LocalDate;

/**
 * @Version v1.0
 * @DateTime 2024/4/3 21:32
 * @Description car的分页请求的扩展类
 * @Author 一见如初
 */
@Data
public class PageInfoCar extends PageInfo {
    private String startAddName;
    private LocalDate pageDate;

}
