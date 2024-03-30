package yirc.mygoschool.common;

/**
 * @Version v1.0
 * @DateTime 2024/3/8 15:18
 * @Description 订单的状态
 * @Author 一见如初
 */
public class OrderStatus {
    //订单状态 0已发布  1已接收  2已完成 3已过期
    public static final int OVERTIME = 3; // 订单超时
    public static final int SUCCESS = 2;
    public static final int PUBLISH = 0;
    public static final int RECEIVE = 1;
}
