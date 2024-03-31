package yirc.mygoschool.Utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Pattern;

@Component //自动检测并注入
@Slf4j
public class MyUtil {
    @Value("${yirc99.filePath}")
    private String filePath;

    //给你一个图片路径 将这个图片替换为空白图片但是不替换名称 覆盖掉
    public void replaceImg(String nsfwImgPath){
        String projectRootPath = new File("").getAbsolutePath();

        //覆盖图片之前 先提前保存一份
        String template =  projectRootPath + filePath + "nsfw" + File.separator;
        File file2 = new File(template);
        if(!file2.exists()){
            file2.mkdirs();
        }
        // 拿到图片名称
        String[] arr = nsfwImgPath.split(Pattern.quote(File.separator));
        String nsfwImgName = arr[arr.length-1];
        try {
            Files.copy(Paths.get(nsfwImgPath),Paths.get(template+nsfwImgName));
        } catch (IOException e) {
            throw new RuntimeException("保存违规图片出错"+ e.getMessage());
        }


        // 检查默认文件夹 并且覆盖nsfw图片
        String dir =  projectRootPath + filePath + "default"+ File.separator;
        File file = new File(dir);
        if(!file.exists()){
            file.mkdirs();
        }
        String blankImg = dir + "NSFWImg.jpg";
        try {
            Files.copy(Paths.get(blankImg),Paths.get(nsfwImgPath), StandardCopyOption.REPLACE_EXISTING);
            log.info("成功替换掉违规图片 {}",nsfwImgPath);
        } catch (IOException e) {
            throw new RuntimeException("替换违规图片出错"+ e.getMessage());
        }

    }

}
