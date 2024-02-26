package yirc.mygoschool.common;

public class ResultCode {
    public static final int SUCCESS = 200; // 成功
    public static final int FAILED = 500; // 失败状态码
    public static final int ERROR = 400;  //错误状态码
    public static final int TOKEN_ERROR = 401;  //错误状态码
    public static final int USER_NOT_EXIST = 401; //用户不存在状态码
    public static final int USER_EXIST = 402; //用户已存在状态码
    public static final int USER_NOT_LOGIN = 403; //用户未登录状态码
    public static final int USER_LOGIN_SUCCESS = 404; //用户登录成功状态码
}
