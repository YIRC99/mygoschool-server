package yirc.mygoschool.controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yirc.mygoschool.common.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class commonController {

    @Value("${yirc99.filePath}")
    private String filePath;

    @Value("${yirc99.feedbackImgPath}")
    private String feedbackImgPath;

    // 下载图片
    @GetMapping("/download")
    public void download(@RequestParam  String path,@RequestParam  String name ,HttpServletResponse response) throws IOException {
        log.info("文件下载: {}",name);
        log.info("文件路径: {}",path);
        if("undefined".equals(name)) return;
        FileInputStream fis = null;
        ServletOutputStream ops = null;
        //获取项目根目录
        String projectRootPath = new File("").getAbsolutePath();
        // 在项目根目录下构建相对路径
        String relativePath = filePath + path + "/"; // 设置相对路径
        String resultPath = projectRootPath + File.separator + relativePath;
        log.info("文件路径: {}",resultPath);
        //创建一个目录对象
        File dir1 = new File(resultPath);
        //判断目录是否存在
        if (!dir1.exists()) {
            //目录不存在 需要创建
            dir1.mkdirs();
        }
        try{
            fis = new FileInputStream(new File(resultPath + name));
            ops = response.getOutputStream();
            response.setContentType("image/jpeg");
            response.setContentType("image/jpg");
            response.setContentType("image/png");
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1){
                ops.write(buffer,0,len);
                ops.flush();
            }


        } catch (IOException e){
            log.error("文件下载失败: {}",e.getMessage());
            fis = new FileInputStream(new File(resultPath + "nullFindFile.jpg"));
            ops = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1){
                ops.write(buffer,0,len);
                ops.flush();
            }
        } finally {
            if (fis!= null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ops!= null){
                try {
                    ops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 图片上传
    @PostMapping("/upload")
    public Result upload(MultipartFile file,@RequestParam String path) throws IOException{
        // 根据参数动态的将图片保存到不同的地方
        String saveFilePath = filePath + path + "/";
        log.info("path: {}",path);


        log.info("上传文件: {}",file.getOriginalFilename());
        String filename = file.getOriginalFilename();
        assert filename != null;
        if (!filename.contains(".jpg") && !filename.contains(".png") && !filename.contains(".jpeg")){
            return Result.error("文件格式不正确");
        }
        String uuid = UUID.randomUUID().toString();
        String projectRootPath = new File("").getAbsolutePath();
        // 在项目根目录下构建相对路径
        filename = "." + filename.split("\\.")[1];
        String relativePath = saveFilePath + uuid + filename; // 设置相对路径
        String resultPath = projectRootPath + relativePath;
        //创建一个目录对象
        File dir1 = new File(resultPath);
        //判断目录是否存在
        if (!dir1.exists()) {
            //目录不存在 需要创建
            dir1.mkdirs();
        }

        try{
            log.info("文件写入的路径为: {}",resultPath);
            file.transferTo(new File(resultPath));
        } catch (IOException e){
            e.printStackTrace();
        }

        return Result.success(uuid + filename);
    }



}
