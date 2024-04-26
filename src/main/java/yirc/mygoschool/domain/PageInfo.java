package yirc.mygoschool.domain;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Version v1.0
 * @DateTime 2024/3/1 21:29
 * @Description 分页查询对象
 * @Author 一见如初
 */
@Data
public class PageInfo {
    private Integer pageNum;
    private Integer pageSize;
}
