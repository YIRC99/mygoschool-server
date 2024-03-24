package yirc.mygoschool.sensitiveWord;

import com.github.houbb.sensitive.word.api.IWordDeny;
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
public class MyWordDeny implements IWordDeny {
    @Override
    public List<String> deny() {
        //自定义黑名单
        List<String> list = new ArrayList<>();
        list.add("你好");
        list.add("YIRC99");
        return list;
    }
}
