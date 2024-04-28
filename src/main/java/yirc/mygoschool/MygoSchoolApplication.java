package yirc.mygoschool;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import yirc.mygoschool.common.Result;

@Slf4j
@SpringBootApplication
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
@ServletComponentScan(basePackages = {"yirc.mygoschool.filter"})
@EnableEncryptableProperties // jasypt 配置 启动时解密
@EnableTransactionManagement
public class MygoSchoolApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(MygoSchoolApplication.class, args);
        Result.setApplicationContext(ctx);
        System.out.println(" __   __   ___    ____      ____     ___    ___      ");
        System.out.println(" \\ \\ / /  |_ _|  |  _ \\   / ___|   / _ \\   / _ \\   ");
        System.out.println("  \\ V /    | |   | |_) |  | |      | (_)|  | (_)|   ");
        System.out.println("   | |     | |   | _< |   | ___    \\__, |  \\__, |   ");
        System.out.println("   |_|    |___|  |_|\\_\\   \\____|    /_/      /_/      ");
        log.info("项目启动成功");
    }
}
