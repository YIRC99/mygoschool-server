package yirc.mygoschool.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        log.info("成功加载分页插件");
        MybatisPlusInterceptor mp = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(1000L); //设置分页上限
//        paginationInnerInterceptor.setDbType(DbType.MYSQL); //设置数据库类型 也可以在new的时候指定
//        paginationInnerInterceptor.setOverflow(true);
        mp.addInnerInterceptor(paginationInnerInterceptor);
        return mp;
    }

}




