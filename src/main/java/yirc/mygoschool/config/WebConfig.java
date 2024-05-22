package yirc.mygoschool.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import yirc.mygoschool.interceptor.AdminInterceptor;

/**
 * @Version v1.0
 * @DateTime 2024/5/22 9:57
 * @Description 拦截器配置类
 * @Author 一见如初
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //正常拦截所有接口就好了
        //因为如果我设置了匹配路径了 那我注解方法还有什么意义
        //直接写匹配路径然后拦截就好了
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/**");
    }
}
