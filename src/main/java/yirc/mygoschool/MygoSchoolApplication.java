package yirc.mygoschool;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import jakarta.annotation.Resource;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import yirc.mygoschool.Utils.MyNSFW;
import yirc.mygoschool.common.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@Slf4j
@SpringBootApplication
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
@ServletComponentScan(basePackages = {"yirc.mygoschool.filter"})
@EnableEncryptableProperties // jasypt 配置 启动时解密
@EnableTransactionManagement
public class MygoSchoolApplication {

    private static Process process;
    private static PrintWriter out;
    private static BufferedReader reader;

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = SpringApplication.run(MygoSchoolApplication.class, args);
        Result.setApplicationContext(ctx);
        System.out.println(" __   __   ___    ____      ____     ___    ___      ");
        System.out.println(" \\ \\ / /  |_ _|  |  _ \\   / ___|   / _ \\   / _ \\   ");
        System.out.println("  \\ V /    | |   | |_) |  | |      | (_)|  | (_)|   ");
        System.out.println("   | |     | |   | _< |   | ___    \\__, |  \\__, |   ");
        System.out.println("   |_|    |___|  |_|\\_\\   \\____|    /_/      /_/      ");

        isNSFW();
        log.info("项目启动成功");
    }


    public static void isNSFW() throws IOException {
        String pythonExecutable = "D:/Pyenv/pyenv-win/pyenv-win/versions/3.10.11/python3.10.exe";
        String scriptPath = "E:/CodeFile/Python/opencvPy/luFace4.py";

        // 调用Python脚本来处理图片
        ProcessBuilder python = new ProcessBuilder(pythonExecutable, scriptPath);
        process = python.start();
        // 获取进程的输出流，用于向 Python 发送数据
        out = new PrintWriter(process.getOutputStream(), true);
        // 获取结果 的类
        reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        log.info("python 启动成功");

        // 发送图片路径给Python脚本
        out.println("test E:/CodeFile/Python/opencvPy/imgs/zhujiayi.jpg");
        String line = reader.readLine();
        System.out.println(line);
    }
}
