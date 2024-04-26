package yirc.mygoschool.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.Dto.PageInfoUser;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.common.WxResult;
import yirc.mygoschool.domain.Shop;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.exception.CustomException;
import yirc.mygoschool.service.UserinfoService;

import java.io.IOException;
import java.util.Objects;

/**
 * @Version v1.0
 * @DateTime 2024/2/26 21:53
 * @Description 用户控制器
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

    @Autowired
    private SensitiveWordBs sensitiveWordBs;

    @PostMapping("/page")
    public Result list(@RequestBody PageInfoUser pageInfo) {
        log.info("/user/list 分页查询的参数为: {} {} {}",
                pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getIsBlack());

        if (Objects.isNull(pageInfo.getPageNum()) ||
                Objects.isNull(pageInfo.getPageSize())) {
            return Result.error("分页参数错误");
        }
        Page<Userinfo> page = userinfoService.listByPage(pageInfo);
        return Result.success(page);
    }

    @GetMapping("/wxImg")
    public Result wxImg(@RequestParam String userid) {
        if (Objects.isNull(userid))
            return Result.error("请求参数错误");
        Userinfo userinfo = userinfoService.getById(userid);
        if (Objects.isNull(userinfo))
            return Result.error("请求参数不存在");
        return Result.success(userinfo.getUserWxImg());
    }

    @PostMapping("/byuserid")
    public Result getByUserId(@RequestBody Userinfo user) {
        Userinfo userinfo = userinfoService.getById(user);
        userinfo.setUsername(sensitiveWordBs.replace(userinfo.getUsername()));
        return Result.success(userinfo);
    }

    @PostMapping("/byopenid")
    public Result getByOpenId(@RequestBody Userinfo user) {
        LambdaQueryWrapper<Userinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Userinfo::getOpenid, user.getOpenid());
        Userinfo userinfo = userinfoService.getOne(wrapper);
        userinfo.setUsername(sensitiveWordBs.replace(userinfo.getUsername()));
        return Result.success(userinfo);
    }


    @PostMapping("/login")
    public Result login(@RequestParam String code) {
        log.info("code:{}", code);
        // 发起get请求 获取微信一键登录需要的值
        var wxResult = WxLogin(code);
        if (wxResult == null)
            return Result.error("一键登录失败");
        Userinfo user = userinfoService.getByOpenId(wxResult);
        log.warn("wxResult:{}", wxResult);
        user.setUsername(sensitiveWordBs.replace(user.getUsername()));
        return Result.success(user);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Userinfo user) {
        log.info("传入的需要修改的数据为: {}", user);
        userinfoService.updateById(user);
        return Result.success("success");
    }

    private WxResult WxLogin(String code) {
        // 创建HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String apiUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=wxbc78397b3ae22e5b&" +
                "secret=" + AppSecret + "&js_code=" + code + "&grant_type=authorization_code";
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
