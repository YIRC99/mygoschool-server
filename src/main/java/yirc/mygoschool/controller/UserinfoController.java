package yirc.mygoschool.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.common.WxResult;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.exception.CustomException;
import yirc.mygoschool.service.UserinfoService;

import java.io.IOException;

/**
 * @Version v1.0
 * @DateTime 2024/2/26 21:53
 * @Description TODO
 * @Author 一见如初
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserinfoController {

    @Autowired
    private UserinfoService userinfoService;

    @Value("${yirc99.WxSecret}")
    private String AppSecret;

    @PostMapping("/login")
    public Result login(@RequestParam String code){
        log.info("code:{}", code);
        // 发起get请求 获取微信一键登录需要的值
        var wxResult = WxLogin(code);
        if (wxResult == null)
            return Result.error("一键登录失败");
        Userinfo user = userinfoService.getByOpenId(wxResult);
        log.warn("wxResult:{}", wxResult);
        return Result.success(user);
    }

    private WxResult WxLogin(String code){
        // 创建HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String apiUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=wxbc78397b3ae22e5b&" +
                "secret="+AppSecret+"&js_code="+code+"&grant_type=authorization_code";
        HttpGet httpGet = new HttpGet(apiUrl);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String entityString = EntityUtils.toString(response.getEntity(), "UTF-8"); // 获取并读取响应内容
                return JSON.parseObject(entityString, WxResult.class);
            } else {
                throw new CustomException("微信一键登录返回值异常Failed : " + statusCode);
            }
        } catch (IOException e) {
            throw new CustomException("微信一键登录返回值异常Failed : " + e.getMessage());
        }


    }



}
