package yirc.mygoschool.sensitiveWord;

import com.github.houbb.sensitive.word.api.IWordAllow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yirc.mygoschool.service.MysensitiveService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Version v1.0
 * @DateTime 2024/3/25 0:06
 * @Description
 * @Author 一见如初
 */
@Component
@Slf4j
public class MyWordAllow implements IWordAllow {

    //如果有直接注入，没有跳过，不要报错。
    @Autowired(required = false)
    private MysensitiveService mysensitiveService;
    @Override
    public List<String> allow() {
        List<String> allow = new ArrayList<>();
        try {
            if (mysensitiveService != null) {
                mysensitiveService.list().forEach(i -> {
                    allow.addAll(Arrays.asList(i.getWhite().split(",")));
                });
                log.info("白名单加载完成");
            } else {
                log.warn("MysensitiveService未成功注入，白名单加载失败");
            }
        } catch (Exception e) {
            log.error("加载白名单时出现异常: " + e.getMessage(), e);
        }
        return allow;
    }
}
