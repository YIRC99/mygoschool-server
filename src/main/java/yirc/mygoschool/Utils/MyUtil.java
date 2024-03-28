package yirc.mygoschool.Utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component //自动检测并注入
@Slf4j
public class MyUtil {
    @Value("${yirc99.filePath}")
    private String filePath;

    //给你一个图片路径 将这个图片替换为空白图片但是不替换名称 覆盖掉
    public void replaceImg(String nsfwImgPath){
        String projectRootPath = new File("").getAbsolutePath();
        String dir =  projectRootPath + filePath + "default/";
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
