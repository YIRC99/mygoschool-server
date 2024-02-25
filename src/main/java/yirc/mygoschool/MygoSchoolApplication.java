package yirc.mygoschool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class MygoSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(MygoSchoolApplication.class, args);
        System.out.println(" __   __   ___    ____      ____     ___    ___      ");
        System.out.println(" \\ \\ / /  |_ _|  |  _ \\   / ___|   / _ \\   / _ \\   ");
        System.out.println("  \\ V /    | |   | |_) |  | |      | (_)|  | (_)|   ");
        System.out.println("   | |     | |   | _< |   | ___    \\__, |  \\__, |   ");
        System.out.println("   |_|    |___|  |_|\\_\\   \\____|    /_/      /_/      ");
        log.info("项目启动成功");
    }

}
