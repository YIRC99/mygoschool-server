package yirc.mygoschool.Utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

@Component //自动检测并注入
@Slf4j
public class MyUtil {
    @Value("${yirc99.filePath}")
    private String filePath;

    @Value("${yirc99.AESKey}") // 从配置文件中读取密钥
    private String aesKey;

    @Value("${yirc99.JWTKey}")
    private String JWTKey;

    @Value("${yirc99.Expire}")
    private Long Expire;

    @Value("${yirc99.SEED}") // 从配置文件中读取固定随机数种子
    private String SEED;

    /**
     * 生成jwt令牌
     */
    public String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, JWTKey)
                .setExpiration(new Date(System.currentTimeMillis() + Expire))
                .compact();
    }

    /**
     * 解析jwt令牌
     */
    public Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(JWTKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


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
    /**
     * 加密数据并返回其Base64编码的字符串形式。
     * @param data 需要加密的数据，应为Java对象，将被转换为JSON字符串进行加密。
     */
    public String encrypt(Object data) throws Exception {
        // 创建ObjectMapper实例，用于将数据转换为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        // 将数据转换为JSON字符串
        String jsonData = objectMapper.writeValueAsString(data);
        // 生成一个安全随机的iv
        String iv = getIv();
        //用于初始化向量参数
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());

        // 初始化加密算法
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 初始化加密密钥和向量
        SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        // 拿到加密后的数据
        byte[] encryptedBytes = cipher.doFinal(jsonData.getBytes());
        // 将IV和加密后的数据一起返回

        String encodedIV = Base64.getEncoder().encodeToString(iv.getBytes());
        String encodedData = Base64.getEncoder().encodeToString(encryptedBytes);

        return encodedIV + "|" + encodedData;
    }

    //AES解密  这个方法没有验证 暂时不可用
    public Object decrypt(String encryptedData, Class<?> valueType) throws Exception {
        // 分离 IV 和加密数据
        String[] parts = encryptedData.split("\\|");
        String ivString = parts[0];
        String encryptedDataString = parts[1];

        // 解码 IV 和加密数据
        byte[] ivBytes = Base64.getDecoder().decode(ivString);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedDataString);

        // 使用 IV 初始化 IvParameterSpec
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // 初始化解密算法
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // 初始化解密密钥和向量
        SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        // 解密数据
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // 将解密后的数据转换为字符串
        String decryptedDataString = new String(decryptedBytes);

        // 将字符串转换为指定类型的对象并返回
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(decryptedDataString, valueType);
    }


    // 获取一个随机的16位16进制的iv向量
    public String getIv(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8]; //8字节就是16个字符
        random.nextBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }



}
