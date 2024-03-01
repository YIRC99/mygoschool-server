package yirc.mygoschool.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
@Slf4j
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        log.info("跨域请求配置成功");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); //设置所有的域通过访问
        corsConfiguration.addAllowedHeader("*"); //设置所有的头字段通过访问
        corsConfiguration.addAllowedMethod("*"); //设置所有的访问方式通过访问

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);//添加映射路径 和配置类
        return new CorsFilter(source);
    }
}













