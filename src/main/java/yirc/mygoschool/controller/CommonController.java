package yirc.mygoschool.controller;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yirc.mygoschool.Utils.BaseContext;
import yirc.mygoschool.Utils.MyNSFW;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.common.ResultCode;
import yirc.mygoschool.config.SensitiveWordConfig;
import yirc.mygoschool.domain.Mysensitive;
import yirc.mygoschool.exception.CustomException;
import yirc.mygoschool.sensitiveWord.SensitiveWordService;
import yirc.mygoschool.service.MyimgService;
import yirc.mygoschool.service.MysensitiveService;

import javax.crypto.spec.PSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${yirc99.filePath}")
    private String filePath;

    @Autowired
    private MyimgService myimgService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MyNSFW myNSFW;

    @Value("${yirc99.AESKey}") // 从配置文件中读取密钥
    private String aesKey;

    // 返回AESKey
    @PostMapping("/AESKey")
    public Result getAESKey() {
        return Result.success(aesKey,false);
    }

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
        // 检查默认文件夹路径
        String defaultDirPath =  projectRootPath + filePath + "default"+ File.separator;
        // 在项目根目录下构建相对路径
        String relativePath = filePath + path + File.separator; // 设置相对路径
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
            response.setContentType("image"+ File.separator+"jpeg");
            response.setContentType("image"+ File.separator+"jpg");
            response.setContentType("image"+ File.separator+"png");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + name);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1){
                ops.write(buffer,0,len);
                ops.flush();
            }


        } catch (IOException e){
            log.error("文件下载失败: {}",e.getMessage());
            fis = new FileInputStream(new File(defaultDirPath + "nullFindFile.jpg"));
            ops = response.getOutputStream();
            response.setContentType("image"+File.separator+"jpeg");
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
        String saveFilePath = filePath + path + File.separator;
        log.info("path: {}",path);



        log.info("上传文件: {}",file.getOriginalFilename());
        String filename = file.getOriginalFilename();
        assert filename != null;
        if (!filename.contains(".jpg") && !filename.contains(".png") && !filename.contains(".jpeg")){
            return Result.error("文件格式不正确");
        }


        // 在项目根目录下构建相对路径
        String projectRootPath = new File("").getAbsolutePath();
        filename = "." + filename.split("\\.")[1];
        String uuid = UUID.randomUUID().toString();
        String ResultFilename = uuid + filename;
        String relativePath = projectRootPath + saveFilePath; // 设置相对路径


        //创建一个目录对象
        File dir1 = new File(relativePath);
        //判断目录是否存在
        if (!dir1.exists()) {
            //目录不存在 需要创建
            dir1.mkdirs();
        }
        String resultPath = relativePath + ResultFilename;
        try{
            log.info("文件写入的路径为: {}",resultPath);
            file.transferTo(new File(resultPath));
        } catch (IOException e){
            throw new CustomException("文件上传失败"+e.getMessage());
        }

        // 必须保证图片上传记录是正常保存到数据库中的 不然就不需要上传数据了 因为后期操作需要保证图片一定在数据库中
        boolean isSave = myimgService.MySave(resultPath);
        if (!isSave){
            return Result.error("图片上传失败", ResultCode.INSET_IMAGE_TO_DATABASE_ERROR);
        }

        // 创建线程处理图片
//        VirtualIsNSFW(resultPath);

        return Result.success(ResultFilename);
    }

    /**
     * 创建一个虚拟线程来处理图片的NSFW判断
     * 但是为了不影响主线程的运行 所以判断的方法放到虚拟线程中
     */
    private void VirtualIsNSFW(String resultPath) {
        // 提前取出来因为ThreadLocal切换线程之后报出的值会丢失
        String currentUserId = BaseContext.getCurrentUserId();
//        Thread.ofVirtual().start(() -> {
//            try {
//                myNSFW.isNSFW(resultPath,currentUserId);
//            } catch (IOException e) {
//                throw new CustomException("处理"+resultPath+"图片时出错"+e.getMessage());
//            }
//        });
    }

    @PostMapping("/redis")
    private Result redisTest(){
        for (int i = 0; i < 100000; i++) {
//            redisTemplate.opsForValue().set(String.valueOf(i),"192.162.58,110,192.162.58,110");
            redisTemplate.delete(String.valueOf(i));
        }
        return Result.success("redis测试成功");
    }
}
