package yirc.mygoschool.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @Version v1.0
 * @DateTime 2024/4/5 0:50
 * @Description jackson配置类
 * @Author 一见如初
 */
@Configuration
@Slf4j
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // 配置 Jackson 将 Long 类型转换为字符串类型
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class,ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        log.info("Jackson配置成功");
        return objectMapper;
    }
}