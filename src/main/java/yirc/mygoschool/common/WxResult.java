package yirc.mygoschool.common;

import lombok.Data;
import lombok.ToString;

/**
 * @Version v1.0
 * @DateTime 2024/2/26 22:30
 * @Description 微信一键登录的结果实体类
 * @Author 一见如初
 */
@Data
@ToString
public class WxResult {
    private String openid;
    private String session_key;
    private String unionid;
    private int errcode;
    private String errmsg;
}
