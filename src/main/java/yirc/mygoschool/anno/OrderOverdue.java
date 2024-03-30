package yirc.mygoschool.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Version v1.0
 * @DateTime 2024/3/8 14:34
 * @Description 订单超时AOP
 * @Author 一见如初
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OrderOverdue {
}
