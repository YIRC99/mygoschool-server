package yirc.mygoschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yirc.mygoschool.Dto.PageInfoShop;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.PageInfo;
import yirc.mygoschool.domain.Shop;
import yirc.mygoschool.exception.CustomException;
import yirc.mygoschool.service.ShopService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Version v1.0
 * @DateTime 2024/4/2 0:00
 * @Description TODO
 * @Author 一见如初
 */
@RestController
@Slf4j
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @PostMapping("/addbrowse")
    public Result browseAdd(@RequestBody Shop shop){
        String key = "shopBrowse:" + shop.getId();
        //先判断一下这个商品的key是不是存在
        long browseCount  = 0;
        if(redisTemplate.hasKey(key)){
            //如果存在 那就直接加一 然后有效期加10分钟 然后返回
            browseCount  = redisTemplate.opsForValue().increment(key);
            // 获取当前过期时间
            Long expireTime = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            if (expireTime != null && expireTime > 0)
                redisTemplate.expire(key, expireTime + 600, TimeUnit.SECONDS);
        }else{
            //如果不存在那就查询数据库 将返回值作为value的初始值 然后设置有效期1天
            Shop byId = shopService.getById(shop.getId());
            if (byId != null){
                browseCount  = byId.getBrowse() + 1;
                redisTemplate.opsForValue().set(key, String.valueOf(browseCount ), 1, TimeUnit.DAYS);
            }

        }
        return Result.success(browseCount );
    }

    // 每天定时持久化缓存数据到数据库
    @Scheduled(cron = "0 0 0 * * ?")
    public void persistCacheToDatabase() {
        // 实现持久化逻辑，将缓存数据保存到数据库中
        System.out.println("实现持久化逻辑，将缓存数据保存到数据库中");
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop::getIsdelete, 0)
                .eq(Shop::getStatus,0); //这个状态0在售 1下架
        wrapper.gt(Shop::getCancelTime, LocalDateTime.now());
        shopService.list(wrapper).forEach(shop -> {
            String key = "shopBrowse:" + shop.getId();
            String value = redisTemplate.opsForValue().get(key);
            if (value != null){
                shop.setBrowse(Integer.parseInt(value));
                shopService.updateById(shop);
            }
        });
    }


    @PostMapping("/add")
    public Result addShop(@RequestBody Shop shop){
        log.info("shop:{}",shop);
        Thread.ofVirtual().start(() -> {
            shopService.SaveWeChatImg(shop);
        });
        boolean save = shopService.save(shop);
        if(save)
            return Result.success("添加成功");
        return Result.error("添加失败");
    }

    @PostMapping("/page")
    public Result list(@RequestBody PageInfoShop pageInfo){
        log.info("/page/list 分页查询的参数为: {} {} {}",
                pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getAddressCodeArr());

        if (Objects.isNull(pageInfo.getPageNum()) ||
            Objects.isNull(pageInfo.getPageSize()) ||
            (pageInfo.getAddressCodeArr().length == 0)){
            return Result.error("分页参数错误");
        }
        Page<Shop> page =  shopService.listByPage(pageInfo);
        return Result.success(page);
    }



}
