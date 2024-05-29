package yirc.mygoschool.interceptor;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import yirc.mygoschool.Utils.BaseContext;
import yirc.mygoschool.anno.AdminRequest;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.common.ResultCode;

import java.lang.reflect.Method;

/**
 * @Version v1.0
 * @DateTime 2024/5/22 9:56
 * @Description 管理端拦截器
 * @Author 一见如初
 */
@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(AdminRequest.class)){
                log.info("IP为:{} 正在请求: {}地址", getIP(request),request.getRequestURL());
                String jwt = request.getHeader("Authorization");
                String key = "adminJWT:" + BaseContext.getCurrentUserId();
                if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                    // key不存在直接返回不可调用
                    log.info("IP为:{} 访问: {}地址 权限不足", getIP(request),request.getRequestURL());
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(JSON.toJSONString(Result.error("权限不足,无法调用", ResultCode.TOKEN_IS_NULL)));
                    return false;
                }

                String value = redisTemplate.opsForValue().get(key);
                if (!value.equals(jwt)) {
                    // 如果jwt不匹配直接返回 权限不足无法调用
                    log.info("IP为:{} 访问: {}地址 权限不足 jwt value 不一致", getIP(request),request.getRequestURL());
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(JSON.toJSONString(Result.error("权限不足,无法调用", ResultCode.TOKEN_ERROR)));
                    return false;
                }
            }
        }
        return true;
    }

    private String getIP(HttpServletRequest request){
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
