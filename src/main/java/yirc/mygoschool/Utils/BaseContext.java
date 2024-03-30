package yirc.mygoschool.Utils;

/**
 * 基于ThreadLocal封装的工具类 保存用户token 用来区分用户
 */
public class BaseContext {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setCurrentUUID(String uuid){
        threadLocal.set(uuid);
    }

    public static String getCurrentUUID(){
        return threadLocal.get();
    }

}
