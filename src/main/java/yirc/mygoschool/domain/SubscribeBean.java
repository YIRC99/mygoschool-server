package yirc.mygoschool.domain;

import lombok.Data;

/**
 * @Version v1.0
 * @DateTime 2024/3/4 18:52
 * @Description TODO
 * @Author 一见如初
 */
@Data
public class SubscribeBean {
    /** 接收者（用户）的 openid */
    private String touser;
    /** 所需下发的订阅模板id */
    private String template_id;
    /** 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数 该字段不填则模板无跳转。*/
    private String page;
    /** 模板|订阅内容 */
    private SubscribeDataBean data;
    /** 进入小程序查看”的语言类型 支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN返回值*/
    private String lang = "zh_CN";
    /** 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版 */
    private String miniprogram_state;
}
