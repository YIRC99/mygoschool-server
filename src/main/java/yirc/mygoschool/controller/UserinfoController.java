package yirc.mygoschool.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.Dto.PageInfoUser;
import yirc.mygoschool.Utils.MyUtil;
import yirc.mygoschool.anno.AdminRequest;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.common.WxResult;
import yirc.mygoschool.domain.Shop;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.exception.CustomException;
import yirc.mygoschool.service.UserinfoService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private MyUtil myUtil;

    // admin登录接口
    @PostMapping("/admin/login")
    public Result adminLogin(@RequestBody Map<String, String> map){
        String username = map.get("username");
        String password = map.get("password");
        if(Objects.isNull(password) || Objects.isNull(username)) return Result.error("账号或密码错误");
        Userinfo byId = userinfoService.getById(123456123456L);
        if(Objects.isNull(byId))return Result.error("登录用户账号不存在!");
        boolean flag = myUtil.checkPassword(password, byId.getAvatar());
        if(!flag) return Result.error("账号或密码错误");
        //  登录成功之后 成一个JWT令牌 令牌时间30分钟过期 不纯在任何地方
        Map<String, Object> mp = new HashMap<>();
        mp.put("openId", byId.getOpenid());
        //  然后将特殊的userid存在jwt令牌里面 在filter中对 固定的请求进行过滤
        String jwt = myUtil.generateJwt(mp, 30 * 60 * 1000L);
        String key = "adminJWT:"+byId.getOpenid();
        redisTemplate.opsForValue().set(key,jwt);
        return Result.success(jwt);
    }

    @PostMapping("/black")
    @Transactional
    @AdminRequest
    public Result blackUser(@RequestBody Userinfo user){
        if(Objects.isNull(user.getUserid())){
            return Result.error("请求参数错误: id为空");
        }

        Userinfo byId = userinfoService.getById(user);
        if(Objects.isNull(byId)){
            return Result.error("该用户不存在");
        }
        if("2022020926".equals(user.getOpenid()) || "2022020926".equals(user.getUserid())){
            return Result.error("该用户为管理员");
        }

        String key = "userBlack:" + byId.getOpenid();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key)) || byId.getIsblack() == 1){
            return Result.error("该用户已被拉黑");
        }
        redisTemplate.opsForValue().set(key, "blacked");
        user.setIsblack(1);
        userinfoService.updateById(user);
        return Result.success("拉黑成功");
    }

    @PostMapping("/white")
    @Transactional
    @AdminRequest
    public Result whiteUser(@RequestBody Userinfo user){
        if(Objects.isNull(user.getUserid())){
            return Result.error("请求参数错误: id为空");
        }
        Userinfo byId = userinfoService.getById(user);
        if(Objects.isNull(byId)){
            return Result.error("该用户不存在");
        }
        String key = "userBlack:" + user.getOpenid();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key)) || byId.getIsblack() == 0){
            return Result.error("该用户未在黑名单中");
        }
        redisTemplate.delete(key);
        user.setIsblack(0);
        userinfoService.updateById(user);
        return Result.success("成功移除黑名单");
    }

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
        // TODO 这里有问题 前端不管点击谁的头像都会进入同一个主页
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

        if(user.getToken() == null){
            // 存储openid 生成jwt
            Map<String, Object> mp = new HashMap<>();
            mp.put("openId", user.getOpenid());
            String jwt = myUtil.generateJwt(mp);
            user.setToken(jwt);
            userinfoService.updateById(user);
        }

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
