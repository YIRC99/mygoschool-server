package yirc.mygoschool.common;

public class ResultCode {
    public static final int SUCCESS = 200; // 成功
    public static final int FAILED = 500; // 失败状态码
    public static final int ERROR = 400;  //错误状态码
    public static final int TOKEN_ERROR = 401;  //token 解析错误状态码
    public static final int TOKEN_IS_NULL = 402;  //token 为空错误状态码
    public static final int TOKEN_TIMEOUT = 403;  //token 超时
    public static final int USER_NOT_EXIST = 405; //用户不存在状态码
    public static final int USER_EXIST = 406; //用户已存在状态码
    public static final int USER_NOT_LOGIN = 407; //用户未登录状态码
    public static final int USER_LOGIN_SUCCESS = 408; //用户登录成功状态码
    public static final int USER_IS_BLACK = 409; // 黑名单用户
}
