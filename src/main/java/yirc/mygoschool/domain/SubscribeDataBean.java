package yirc.mygoschool.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Version v1.0
 * @DateTime 2024/3/4 18:49
 * @Description 微信通知备忘录模板
 * @Author 一见如初
 */
@Data
public class SubscribeDataBean implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    //去查看具体地订阅模板 根据模板设置变量名称
    //此模板名称: 备忘录通知
    private Thing1 thing1;
    private Thing2 thing2;
    private Time3 time3;
    @Data
    @AllArgsConstructor
    public static class Thing1{
        private String value;
    }

    @Data
    @AllArgsConstructor
    public static class Thing2{
        private String value;
    }



    @Data
    @AllArgsConstructor
    public static class Time3{
        private String value;
    }
}