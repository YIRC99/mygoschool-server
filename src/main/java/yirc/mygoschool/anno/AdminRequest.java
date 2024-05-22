package yirc.mygoschool.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Version v1.0
 * @DateTime 2024/5/22 9:44
 * @Description 设置方法只能Admin用户使用
 * @Author 一见如初
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminRequest {
}
