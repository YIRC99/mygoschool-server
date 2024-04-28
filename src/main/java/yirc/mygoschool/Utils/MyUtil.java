package yirc.mygoschool.Utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.regex.Pattern;

@Component //自动检测并注入
@Slf4j
public class MyUtil {
    @Value("${yirc99.filePath}")
    private String filePath;

    @Value("${yirc99.AESKey}") // 从配置文件中读取密钥
    private String aesKey;

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
        Path nsfwPath = Paths.get(nsfwImgPath);
        try {
            Files.copy(nsfwPath,Paths.get(template+nsfwImgName));
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
            Files.copy(Paths.get(blankImg),nsfwPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("成功替换掉违规图片 {}",nsfwImgPath);
        } catch (IOException e) {
            throw new RuntimeException("替换违规图片出错"+ e.getMessage());
        }

    }

    //传入一个已经违规的图片 然后去nsfw文件夹中恢复
    public void recoverImg(String imgPath) {
        //拿到图片名称
        // 拿到图片名称
        String[] arr = imgPath.split(Pattern.quote(File.separator));
        String nsfwImgName = arr[arr.length-1];
        //去nsfw文件夹中找到图片
        String projectRootPath = new File("").getAbsolutePath();
        String template =  projectRootPath + filePath + "nsfw" + File.separator;
        File nsfwImgFile = new File(template + nsfwImgName);
        if(!nsfwImgFile.exists()) throw new RuntimeException("恢复图片出错，nsfw中没有找到图片");
        //然后替换掉我传入的图片路径
        try {
            Files.copy(Paths.get(nsfwImgFile.toURI()),Paths.get(imgPath),StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.info("成功恢复违规图片 {}",imgPath);
            throw new RuntimeException("恢复图片出错，替换时出现错误");
        }

    }

    //AES加密
    public String encrypt(Object data) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(data);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(jsonData.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }



}
