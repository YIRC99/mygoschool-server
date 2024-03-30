package yirc.mygoschool.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import yirc.mygoschool.domain.Apprise;

/**
 * @Version v1.0
 * @DateTime 2024/3/11 15:04
 * @Description
 * @Author 一见如初
 */
@Data
public class AppriseDto extends Apprise {
//    在实体类上增加 @JsonProperty 注解，指定参数名。
    @JsonProperty(value = "OrderCreateUserId")
    private String OrderCreateUserId;
}
