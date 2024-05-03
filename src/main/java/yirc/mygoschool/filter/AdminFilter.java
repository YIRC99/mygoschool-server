package yirc.mygoschool.filter;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.GenericFilterBean;
import yirc.mygoschool.Utils.BaseContext;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.common.ResultCode;

import java.io.IOException;

/**
 * @Version v1.0
 * @DateTime 2024/5/3 13:57
 * @Description 对一些规定接口只能admin调用
 * @Author 一见如初
 */
@Slf4j
@WebFilter(filterName = "AdminFilter", urlPatterns = "/*")
@ControllerAdvice
@Order(3)
public class AdminFilter extends GenericFilterBean implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)  throws IOException, ServletException {
        // 获取请求和响应
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType("application/json;charset=UTF-8");// 设置返回值字符编码

        // 配置需要Admin的请求
        String[] urls = new String[]{
                "/user/black",
                "/user/white",
                "/affiche/add",
                "/sensitive",
                "/nsfw/**"
        };

        //从urls中判断本次的请求是否需要拦截 匹配成功就验证
        for (String url : urls) {
            if (PATH_MATCHER.match(url, request.getRequestURI())) {
                log.info("IP为:{} 正在请求: {}地址", getIP(request),request.getRequestURL());
                // 拿到JWT信息 再拿到用户的id信息 去redis里面查询
                String jwt = request.getHeader("Authorization");
                String key = "adminJWT:"+ BaseContext.getCurrentUserId();
                // 查不到直接返回 权限不足无法调用
                if(Boolean.FALSE.equals(redisTemplate.hasKey(key))){
                    // key不存在直接返回不可调用
                    log.info("IP为:{} 访问: {}地址 权限不足", getIP(request),request.getRequestURL());
                    response.getWriter().write(JSON.toJSONString(Result.error("权限不足,无法调用", ResultCode.TOKEN_IS_NULL)));
                    return ;
                }
                // key纯在但是value不对 就表示不是用的最新的value 已经再次登录过了
                String value = redisTemplate.opsForValue().get(key);
                if(!value.equals(jwt)){
                    // 如果jwt不匹配直接返回 权限不足无法调用
                    log.info("IP为:{} 访问: {}地址 权限不足 jwt value 不一致", getIP(request),request.getRequestURL());
                    response.getWriter().write(JSON.toJSONString(Result.error("权限不足,无法调用", ResultCode.TOKEN_ERROR)));
                    return ;
                }
                chain.doFilter(req, res);
                return;
            }
        }
        // 不是admin接口 就直接放行
        chain.doFilter(req, res);
    }

    private String  getIP(HttpServletRequest request){
        // 获取客户端IP地址
        String clientIp = request.getHeader("X-Forwarded-For");

        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr(); // 如果以上都获取不到，就使用此方法获取
        }

        // 处理多层代理的情况，X-Forwarded-For 可能包含多个IP，第一个非 unknown 的就是客户端真实IP
        if (clientIp != null && !"".equals(clientIp)) {
            String[] ips = clientIp.split(",");
            for (String ip : ips) {
                if (!"unknown".equalsIgnoreCase(ip)) {
                    clientIp = ip.trim();
                    break;
                }
            }
        }

        System.out.println("客户端IP: " + clientIp);
        return clientIp;
    }

}
