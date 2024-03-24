package yirc.mygoschool.sensitiveWord;

import com.github.houbb.sensitive.word.api.IWordAllow;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Version v1.0
 * @DateTime 2024/3/25 0:06
 * @Description TODO
 * @Author 一见如初
 */
@Component
public class MyWordAllow implements IWordAllow {
    @Override
    public List<String> allow() {
        // 自定义白名单
        List<String> list = new ArrayList<>();
        list.add("五星红旗");
        list.add("天安门");
        return list;
    }
}
