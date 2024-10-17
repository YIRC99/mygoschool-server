package yirc.mygoschool.aop;

import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author 一见如初
 */
@Setter
@Component
public class DefaultLoggingConfig implements LoggingConfig {

    private boolean enabled = true;

    public DefaultLoggingConfig() {
        // 可以通过配置文件或其他方式初始化
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}