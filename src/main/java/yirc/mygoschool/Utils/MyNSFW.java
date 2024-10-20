package yirc.mygoschool.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import yirc.mygoschool.domain.Myimg;
import yirc.mygoschool.domain.Mynsfw;
import yirc.mygoschool.exception.CustomException;
import yirc.mygoschool.service.MyimgService;
import yirc.mygoschool.service.MynsfwService;

import java.io.*;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @Version v1.0
 * @DateTime 2024/3/28 21:36
 * @Description NSFW检测的工具
 * @Author 一见如初
 */
@Component
@Slf4j
public class MyNSFW implements CommandLineRunner {

    @Value("${yirc99.pythonExePath}")
    private String pythonExePath;

    @Value("${yirc99.pythonPath}")
    private String pythonPath;

    @Value("${yirc99.NSFWScore}")
    private double NSFWScore;

    @Value("${yirc99.NSFWMaxScore}")
    private double NSFWMaxScore;

    @Autowired
    private MyUtil myUtil;

    @Autowired
    private MyimgService myimgService;

    @Autowired
    private MynsfwService mynsfwService;

    private Process process;
    private PrintWriter out;
    private BufferedReader reader;


//    @Override
//    public void run(String... args)  {
//        try {
//            String pythonExecutable = pythonExePath;
//            String scriptPath = pythonPath;
//
//            // 调用Python脚本来处理图片
//            ProcessBuilder python = new ProcessBuilder(pythonExecutable, scriptPath);
//            process = python.start();
//            // 获取进程的输出流，用于向 Python 发送数据
//            out = new PrintWriter(process.getOutputStream(), true);
//            // 获取结果 的类
//            reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
//            log.info("python 启动成功");
//            log.info("为了节约内存 不再启动python了");
//        } catch (Exception e) {
//            closeResources();
//            log.error("启动python时出现错误: {}" , e.getMessage());
//            throw new CustomException("python 启动失败 " + e.getMessage());
//        }
//    }
//
//    // 关闭资源
//    private void closeResources() {
//        try {
//            if (reader != null) {
//                reader.close();
//            }
//            if (out != null) {
//                out.close();
//            }
//        } catch (IOException ex) {
//            log.error("关闭python流时报错了: {}", ex.getMessage());
//            throw new CustomException("启动python时出现错误 关闭python流时报错了" + ex.getMessage());
//        }
//    }
//
//    public void isNSFW(String imgPath, String userUUID) throws IOException {
//        // 发送图片路径给Python脚本
//        out.println(imgPath);
//        String line = reader.readLine();
//        double ImgScore = Double.parseDouble(line);
////        log.info("图片NSFW评分为：" + ImgScore);
////        // 判断图片是不是为NSFW图片 大于NSFWScore 观察一下   大于NSFWMaxScore直接替换图片
////        if (ImgScore > NSFWScore && ImgScore < NSFWMaxScore) {
////            // 这张图片可能是NSFW图片
////            Mynsfw nsfwObj = new Mynsfw();
////            nsfwObj.setImgpath(imgPath);
////            nsfwObj.setStatus(0);
////            nsfwObj.setPostuseropenid(userUUID);
////            nsfwObj.setScore(ImgScore);
////            mynsfwService.save(nsfwObj);
////        } else if (ImgScore > NSFWMaxScore) {
////            //这张图片一定是NSFW图片
////            Mynsfw nsfwObj = new Mynsfw();
////            nsfwObj.setStatus(2);
////            nsfwObj.setPostuseropenid(userUUID);
////            nsfwObj.setScore(ImgScore);
////
////            String[] split = imgPath.split(Pattern.quote(File.separator));
////            myimgService.MyAddImgUseList(split[split.length - 1]);
////            String replacedImg = myUtil.replaceImg(imgPath);
////            nsfwObj.setImgpath(imgPath); // 将违规的图片路径保存到数据库
////            nsfwObj.setBackupImgPath(replacedImg);  // 将备份的图片路径保存到数据库 目前测试通过 暂时没有发现问题 但是不敢确定 还需要大面积的测
////
////            mynsfwService.save(nsfwObj);
////        }
//    }
//
//
    @Override
    public void run(String... args)  {
        try {
//            String pythonExecutable = "D:\\Pyenv\\pyenv-win\\pyenv-win\\versions\\3.10.11\\python3.10.exe";
//            String scriptPath = "E:\\CodeFile\\Python\\opencvPy\\luFace4.py ";
//
//            // 调用Python脚本来处理图片
//            ProcessBuilder python = new ProcessBuilder(pythonExecutable, scriptPath);
//            process = python.start();
//            // 获取进程的输出流，用于向 Python 发送数据
//            out = new PrintWriter(process.getOutputStream(), true);
//            // 获取结果 的类
//            reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
//            log.info("python 启动成功");
            log.info("为了节约内存 不再启动python了");
        } catch (Exception e) {
            log.error("启动python时出现错误: {}" , e.getMessage());
            throw new CustomException("python 启动失败 " + e.getMessage());
        }
    }

    public void isNSFW(String imgPath, String userUUID) throws IOException {
        // 发送图片路径给Python脚本
        out.println("test E:\\CodeFile\\Python\\opencvPy\\imgs\\zhujiayi.jpg");
        String line = reader.readLine();
        System.out.println(line);
//        log.info("图片NSFW评分为：" + ImgScore);
//        // 判断图片是不是为NSFW图片 大于NSFWScore 观察一下   大于NSFWMaxScore直接替换图片
//        if (ImgScore > NSFWScore && ImgScore < NSFWMaxScore) {
//            // 这张图片可能是NSFW图片
//            Mynsfw nsfwObj = new Mynsfw();
//            nsfwObj.setImgpath(imgPath);
//            nsfwObj.setStatus(0);
//            nsfwObj.setPostuseropenid(userUUID);
//            nsfwObj.setScore(ImgScore);
//            mynsfwService.save(nsfwObj);
//        } else if (ImgScore > NSFWMaxScore) {
//            //这张图片一定是NSFW图片
//            Mynsfw nsfwObj = new Mynsfw();
//            nsfwObj.setStatus(2);
//            nsfwObj.setPostuseropenid(userUUID);
//            nsfwObj.setScore(ImgScore);
//
//            String[] split = imgPath.split(Pattern.quote(File.separator));
//            myimgService.MyAddImgUseList(split[split.length - 1]);
//            String replacedImg = myUtil.replaceImg(imgPath);
//            nsfwObj.setImgpath(imgPath); // 将违规的图片路径保存到数据库
//            nsfwObj.setBackupImgPath(replacedImg);  // 将备份的图片路径保存到数据库 目前测试通过 暂时没有发现问题 但是不敢确定 还需要大面积的测
//
//            mynsfwService.save(nsfwObj);
//        }
    }

}
