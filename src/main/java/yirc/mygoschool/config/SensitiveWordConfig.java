package yirc.mygoschool.config;

import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import com.github.houbb.sensitive.word.support.ignore.SensitiveWordCharIgnores;
import com.github.houbb.sensitive.word.support.resultcondition.WordResultConditions;
import com.github.houbb.sensitive.word.support.tag.WordTags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import yirc.mygoschool.sensitiveWord.MyWordAllow;
import yirc.mygoschool.sensitiveWord.MyWordDeny;
import yirc.mygoschool.sensitiveWord.SensitiveWordService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Version v1.0
 * @DateTime 2024/3/24 20:08
 * @Description 违规词配置
 * @Author 一见如初
 */
@Configuration
@Slf4j
public class SensitiveWordConfig {

    @Autowired
    private MyWordDeny myWordDeny;

    @Autowired
    private MyWordAllow myWordAllow;

    @Bean
    public SensitiveWordBs MySensitiveWordBs(){

        //合并白名单和黑名单 不仅有自己的还要有官方的
        IWordDeny wordDeny = WordDenys.chains(WordDenys.defaults(), myWordDeny);
        IWordAllow wordAllow = WordAllows.chains(WordAllows.defaults(), myWordAllow);

        SensitiveWordBs wordBs = SensitiveWordBs.newInstance()
                .ignoreCase(true) //ignoreCase	忽略大小写
                .ignoreWidth(true)//ignoreWidth	忽略半角圆角
                .ignoreNumStyle(true)//ignoreNumStyle	忽略数字的写法
                .ignoreChineseStyle(true)//ignoreChineseStyle	忽略中文的书写格式
                .ignoreEnglishStyle(true)//ignoreEnglishStyle	忽略英文的书写格式
                .ignoreRepeat(false)//ignoreRepeat	忽略重复词
                .enableNumCheck(true)//enableNumCheck	是否启用数字检测。
                .enableEmailCheck(true)//enableEmailCheck	是有启用邮箱检测
                .enableUrlCheck(true)//enableUrlCheck	是否启用链接检测
                .enableWordCheck(true)//enableWordCheck	是否启用敏感单词检测
                .numCheckLen(15)//numCheckLen	数字检测，自定义指定长度
                .wordTag(WordTags.none())//wordTag	词对应的标签
                .charIgnore(SensitiveWordCharIgnores.defaults())//charIgnore	忽略的字符
                .wordResultCondition(WordResultConditions.alwaysTrue())//wordResultCondition 针对匹配的敏感词额外加工，比如可以限制英文单词必须全匹配
                .wordDeny(wordDeny)
                .wordAllow(wordAllow)
                .charIgnore(SensitiveWordCharIgnores.specialChars())
                .init();
        log.info("SensitiveWordBs 初始化成功");
        return wordBs;
    }

}
