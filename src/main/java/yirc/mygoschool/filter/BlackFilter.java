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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.GenericFilterBean;
import yirc.mygoschool.Utils.BaseContext;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.common.ResultCode;

import java.io.IOException;

/**
 * @Version v1.0
 * @DateTime 2024/5/2 20:34
 * @Description 黑名单过滤器
 * @Author 一见如初
 */
@Slf4j
@WebFilter(filterName = "BlackFilter", urlPatterns = "/*")
@ControllerAdvice
@Order(2)
public class BlackFilter extends GenericFilterBean implements Filter {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // 获取请求和响应
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType("application/json;charset=UTF-8");// 设置返回值字符编码

        String openId = BaseContext.getCurrentUserId();
        String key = "userBlack:" + openId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            log.error("黑名单用户 拦截访问openId为: {}",openId);
            response.getWriter().write(JSON.toJSONString(Result.error("账号异常请联系开发者: YIRC99", ResultCode.USER_IS_BLACK)));
            return;
        }
        chain.doFilter(req, res);
    }
}
