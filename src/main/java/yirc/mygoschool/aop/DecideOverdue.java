package yirc.mygoschool.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yirc.mygoschool.Dto.CarshareorderDto;
import yirc.mygoschool.common.OrderStatus;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Carshareorder;
import yirc.mygoschool.service.CarshareorderService;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @Version v1.0
 * @DateTime 2024/3/8 14:37
 * @Description 订单超时AOP实现
 * @Author 一见如初
 */
@Slf4j
@Aspect
@Component
public class DecideOverdue {

    @Autowired
    private CarshareorderService carshareorderService;

    /***
     * 如果订单触发时间已经过了 并且没有人接受 就把订单的状态改为 已超时
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(yirc.mygoschool.anno.OrderOverdue)")
    public Result DecideOverdue(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("AOP 执行了");
        Object proceed = joinPoint.proceed();
        Field[] fields = proceed.getClass().getDeclaredFields();
        Object returnRuResult = null;
        for (Field field : fields) {
            if(field.getName().equals("data")){
                returnRuResult = field.get(proceed);
            }
        }
        ArrayList<?> arrayList = (ArrayList<?>) returnRuResult;
        assert arrayList != null;
        for (Object o : arrayList) {
            if (o instanceof CarshareorderDto order){
                if (order.getReceiveuserid() == null && LocalDateTime.now().isAfter(order.getStartdatetime())){
                    order.setStatus(OrderStatus.OVERTIME);
                    Carshareorder car = new Carshareorder();
                    car.setOrderid(order.getOrderid());
                    car.setStatus(OrderStatus.OVERTIME);
                    carshareorderService.updateById(car);
                }
            }
        }
        return Result.success(arrayList);
    }
}
