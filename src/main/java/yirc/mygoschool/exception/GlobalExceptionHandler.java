package yirc.mygoschool.exception;


import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yirc.mygoschool.common.Result;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ResponseBody
@RestControllerAdvice
public class GlobalExceptionHandler  {

    // 参数就表示需要处理的异常类型
    @ExceptionHandler(CustomException.class)
    public Result handleCustomException(CustomException e) {
        log.error("自定义异常处理: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    // 参数就表示需要处理的异常类型
    @ExceptionHandler(NullPointerException.class)
    public Result handleCustomException(NullPointerException e) {
        log.error("空指针异常: {}", e.getMessage().split("because")[1]);
        return Result.error("空指针异常" + e.getMessage().split("because")[1]);
    }

    // 参数就表示需要处理的异常类型
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result handleCustomException(SQLIntegrityConstraintViolationException e) {
        log.error("用户名已存在，请选择其他用户名: {}", e.getMessage());
        return Result.error("用户名已存在，请选择其他用户名");
    }

    // 参数就表示需要处理的异常类型
    @ExceptionHandler(ExpiredJwtException.class)
    public void handleCustomException(ExpiredJwtException e) {
        log.error("jwt令牌超时: {}", e.getMessage());
    }

    // MySQL报错
    @ExceptionHandler(MysqlDataTruncation.class)
    public Result MysqlDataTruncation(MysqlDataTruncation e) {
        log.error("MYSQL报错: {}", e.getMessage());
        return Result.error("传入数据格式有错,请检查");
    }

}
