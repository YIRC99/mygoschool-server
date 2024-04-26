package yirc.mygoschool;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import yirc.mygoschool.service.ShopService;

/**
 * @Version v1.0
 * @DateTime 2024/4/5 1:01
 * @Description redis测试类
 * @Author 一见如初
 */
@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private ShopService shopService;


    @Scheduled(cron = "0 0 0 * * ?")
    @Test
    public void persistCacheToDatabase() {
        // 实现持久化逻辑，将缓存数据保存到数据库中
        System.out.println("实现持久化逻辑，将缓存数据保存到数据库中");
        shopService.list().forEach(shop -> {
            String key = "shopBrowse:" + shop.getId();
            String value = redisTemplate.opsForValue().get(key);
            if (value != null){
                shop.setBrowse(Integer.parseInt(value));
                shopService.updateById(shop);
            }
        });
    }


}
