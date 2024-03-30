package yirc.mygoschool.sensitiveWord;

import com.github.houbb.sensitive.word.api.IWordDeny;
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
public class MyWordDeny implements IWordDeny {

    //如果有直接注入，没有跳过，不要报错。
    @Autowired(required = false)
    private MysensitiveService mysensitiveService;
    @Override
    public List<String> deny() {
        //自定义黑名单
        // 后面在这里读取数据库 加载敏感词
        List<String> Deny = new ArrayList<>();
        try{
            if(mysensitiveService != null){
                mysensitiveService.list().forEach(i -> {
                    Deny.addAll(Arrays.asList(i.getBlack().split(",")));
                });
                log.info("自定义黑名单 加载成功");
            }else {
                log.warn("MysensitiveService未成功注入，黑名单加载失败");
            }
        }catch (Exception e) {
            log.error("加载黑名单时出现异常: " + e.getMessage(), e);
        }
        return Deny;
    }
}
