package yirc.mygoschool.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// mybatis plus 自动填充 配置类
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        // mybatis plus 自动填充配置类 但是不需要使用
//        log.info("公共字段插入自动填充");
//        metaObject.setValue("createdAt", LocalDateTime.now());
//        metaObject.setValue("createdAt", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
