package yirc.mygoschool.filter;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.GenericFilterBean;
import yirc.mygoschool.common.Result;


import java.io.IOException;
import java.util.Objects;


@Slf4j
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
@ControllerAdvice
public class LoginFilter extends GenericFilterBean implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        // 获取请求和响应
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType("application/json;charset=UTF-8");// 设置返回值字符编码
        log.info("请求的路径为: {}", request.getRequestURL());
        // 配置不需要
        String[] urls = new String[]{
                "/user/login",
                "/user/logins",
                "/user/register",
                "/static/images/*",
                "/static/avatar/*",
                "/common/download/*",
                "/doc.html",            //设置swagger不被拦截
                "/webjars/**",          //设置swagger的配置
                "/swagger-resources",   //设置swagger的配置
                "/v2/api-docs"          //设置swagger的配置
        };

        //从urls中判断本次的请求是否需要拦截 匹配成功就放行
        for (String url : urls) {
            if (PATH_MATCHER.match(url, request.getRequestURI())) {
                log.info("请求的路径为: {} 直接放行", request.getRequestURL());
                chain.doFilter(req, res);
                return;
            }
        }

//        // 不是登录和注册的请求需要验证token
//        String token = request.getHeader("Authorization");
//        log.info("拿到的token值为: {}", token);
//        if (Objects.isNull(token)) {
//            // 没有token，返回未授权状态码
//            response.getWriter().write(JSON.toJSONString(Result.error("token 为空")));
//            return;
//        }


        chain.doFilter(req, res);



    }


}
