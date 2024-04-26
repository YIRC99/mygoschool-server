package yirc.mygoschool.filter;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.GenericFilterBean;
import yirc.mygoschool.Utils.BaseContext;
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
        // 获取请求的header里面的token
        // 未来可以在jwt里面设置UserId
        String token = request.getHeader("Authorization");
        String userId = request.getHeader("UserId"); // TODO 之后这个userid从jwt里面取
        // 当前请求的userId 用来识别当前请求是谁
        BaseContext.setCurrentUUID(userId);

        // TODO 前后端通讯加密

        // TODO token验证

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
