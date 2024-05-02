package yirc.mygoschool.filter;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.GenericFilterBean;
import yirc.mygoschool.Utils.BaseContext;
import yirc.mygoschool.Utils.MyUtil;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.common.ResultCode;


import java.io.IOException;
import java.util.Map;
import java.util.Objects;


@Slf4j
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
@ControllerAdvice
public class LoginFilter extends GenericFilterBean implements Filter {

    @Autowired
    private MyUtil myUtil;

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        // 获取请求和响应
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType("application/json;charset=UTF-8");// 设置返回值字符编码
        log.info("请求的路径为: {}", request.getRequestURL());
        // 配置不需要验证token的请求
        String[] urls = new String[]{
                "/user/login",
                "/shop/page",
                "/shop/search/**",
                "/carshareorder/page",
                "/affiche",
                "/common/download",
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

        //查询
        String token = request.getHeader("Authorization");
        log.info("拿到的token值为: {}", token);
        if (Objects.isNull(token)) {
            // 没有token，返回未授权状态码
            response.getWriter().write(JSON.toJSONString(Result.error("token 为空")));
            return;
        }

        Map<String, Object> claims = null;
        try {
            claims = myUtil.parseJWT(token);
        }catch (SignatureException e) {
            // 解析失败
            response.getWriter().write(JSON.toJSONString(
                    Result.error("token 签名无效 请重新登录", ResultCode.USER_NOT_LOGIN)));
            return;
        } catch (ExpiredJwtException e) {
            // 解析失败
            response.getWriter().write(JSON.toJSONString(
                    Result.error("token jwt令牌超时 请重新登录", ResultCode.USER_NOT_LOGIN)));
            return;
        } catch (Exception e) {
            // 解析失败
            response.getWriter().write(JSON.toJSONString(
                    Result.error("token 解析失败 请重新登录", ResultCode.USER_NOT_LOGIN)));
            return;
        }

        // 当前请求的userId 用来识别当前请求是谁
        String userId = (String)claims.get("UserId");
        BaseContext.setCurrentUserId(userId);

        chain.doFilter(req, res);
    }


}
