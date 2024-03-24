package yirc.mygoschool;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.core.SensitiveWord;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.houbb.sensitive.word.support.allow.WordAllows;

/**
 * @Version v1.0
 * @DateTime 2024/3/24 19:54
 * @Description TODO
 * @Author 一见如初
 */
public class WordTest {
    public static void main(String[] args) {
        SensitiveWordBs init = SensitiveWordBs.newInstance().init();
        boolean contains = init.contains("f五星红旗迎风飘扬，毛主席的画像屹立在天安门前。");
        System.out.println(contains);
    }
}
