package yirc.mygoschool.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import yirc.mygoschool.exception.CustomException;

import java.io.IOException;

/**
 * @Version v1.0
 * @DateTime 2024/3/4 19:15
 * @Description TODO
 * @Author 一见如初
 */
@Configuration
@EnableScheduling // 开启定时任务
@EnableCaching // 开启缓存支持（可选，用于将AccessToken缓存）
@Service
public class AccessTokenService {

    @Autowired
    private CacheManager cacheManager; // 如果你选择缓存AccessToken

    @Value("${yirc99.WxSecret}")
    private String AppSecret;
    private String accessToken;

    @PostConstruct // 项目启动时执行
    public void init() throws IOException {
        refreshAccessToken();
    }

    @Scheduled(cron = "0 0 * * * ?") // 每小时执行一次，cron表达式根据实际需求调整
    public void refreshAccessToken() throws IOException {
        // 这里是获取AccessToken的逻辑，假设为getAccessTokenFromServer()
        accessToken = getAccessTokenFromServer();
        if(accessToken == null) throw new CustomException("获取微信access_token失败");

            // 将accessToken放入缓存，便于其他服务获取
        if (cacheManager != null) {
            cacheManager.getCache("accessToken").put("access_token", accessToken);
        }
    }

    public String getAccessToken() {
        // 若使用缓存，则优先从缓存获取
        if (cacheManager != null) {
            accessToken = (String) cacheManager.getCache("accessToken").get("access_token").get();
        }

        // 返回最新的AccessToken
        return accessToken;
    }

    private String getAccessTokenFromServer() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/cgi-bin/stable_token");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("grant_type", "client_credential");
        jsonObject.put("appid", "wxbc78397b3ae22e5b");
        jsonObject.put("secret", AppSecret);
        StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(httpPost);

        HttpEntity responseEntity = response.getEntity();
        if (responseEntity != null) {
            String content = EntityUtils.toString(responseEntity, "UTF-8");
            // 解析JSON字符串，提取access_token
            JSONObject object = JSON.parseObject(content);
            String token = object.getString("access_token");
            return token;
        }

        return null;
    }
}
