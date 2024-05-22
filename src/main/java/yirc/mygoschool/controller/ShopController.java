package yirc.mygoschool.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import yirc.mygoschool.Dto.PageInfoShop;
import yirc.mygoschool.Utils.BaseContext;
import yirc.mygoschool.anno.AdminRequest;
import yirc.mygoschool.common.Result;
import yirc.mygoschool.domain.Shop;
import yirc.mygoschool.domain.Userinfo;
import yirc.mygoschool.exception.CustomException;
import yirc.mygoschool.service.MyimgService;
import yirc.mygoschool.service.ShopService;

import java.beans.beancontext.BeanContext;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Version v1.0
 * @DateTime 2024/4/2 0:00
 * @Description
 * @Author 一见如初
 */
@RestController
@Slf4j
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SensitiveWordBs sensitiveWordBs;

    @Autowired
    private MyimgService myimgService;


    @PostMapping("/addbrowse")
    public Result browseAdd(@RequestBody Shop shop) {
        String key = "shopBrowse:" + shop.getId();
        //先判断一下这个商品的key是不是存在
        long browseCount = 0;
        if (redisTemplate.hasKey(key)) {
            //如果存在 那就直接加一 然后有效期加10分钟 然后返回
            browseCount = redisTemplate.opsForValue().increment(key);
            // 获取当前过期时间
            Long expireTime = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            if (expireTime != null && expireTime > 0)
                redisTemplate.expire(key, expireTime + 600, TimeUnit.SECONDS);
        } else {
            //如果不存在那就查询数据库 将返回值作为value的初始值 然后设置有效期1天
            Shop byId = shopService.getById(shop.getId());
            if (byId != null) {
                browseCount = byId.getBrowse() + 1;
                redisTemplate.opsForValue().set(key, String.valueOf(browseCount), 1, TimeUnit.DAYS);
            }

        }
        return Result.success(browseCount);
    }

    // 每天定时持久化缓存数据到数据库
    @Scheduled(cron = "0 0 0 * * ?")
    public void persistCacheToDatabase() {
        // 实现持久化逻辑，将缓存数据保存到数据库中
        System.out.println("实现持久化逻辑，将缓存数据保存到数据库中");
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop::getIsdelete, 0)
                .eq(Shop::getStatus, 0); //这个状态0在售 1下架
        wrapper.gt(Shop::getCancelTime, LocalDateTime.now());
        shopService.list(wrapper).forEach(shop -> {
            String key = "shopBrowse:" + shop.getId();
            String value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                shop.setBrowse(Integer.parseInt(value));
                shopService.updateById(shop);
            }
        });
    }


    @PostMapping("/add")
    public Result addShop(@RequestBody Shop shop) {
        log.info("shop:{}", shop);
        Thread.ofVirtual().start(() -> {
            shopService.SaveWeChatImg(shop);
        });
        // 添加图片在数据库中的引用
        myimgService.MyAddImgUseList(shop.getImgs());
        boolean save = shopService.save(shop);
        if (save)
            return Result.success("添加成功");
        return Result.error("添加失败");
    }

    @PostMapping("/page")
    public Result list(@RequestBody PageInfoShop pageInfo) {
        if (Objects.isNull(pageInfo.getPageNum()) ||
                Objects.isNull(pageInfo.getPageSize()) ||
                Objects.isNull(pageInfo.getAddressCodeArr()) ||
                (pageInfo.getAddressCodeArr().length == 0) ) {
            return Result.error("分页参数错误");
        }
        log.info("/page/list 分页查询的参数为: {} {} {}",
                pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getAddressCodeArr());

        Page<Shop> page = shopService.listByPage(pageInfo);
        return Result.success(page);
    }

    @PostMapping("/search")
    public Result search(@RequestBody PageInfoShop pageInfo) {
        log.info("/search/ 搜索的参数为: {}", Arrays.toString(pageInfo.getTarget()));
        Page<Shop> page = shopService.Search(pageInfo);
        return Result.success(page);
    }

    @PostMapping("/byUserId")
    public Result listByUserId(@RequestBody Userinfo user) {
        log.info("/byUserId/list 查询的参数为: {}", user);
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop::getCreateuserid, user.getOpenid())
                .orderByDesc(Shop::getCreateAt)
                .eq(Shop::getIsdelete,0);
        List<Shop> list = shopService.byUserId(user.getOpenid());
        return Result.success(list);
    }

    @PostMapping("/update")
    public Result updateShop(@RequestBody Shop shop) {
        if (Objects.isNull(shop.getId()))
            return Result.error("商品id不能为空");

        if (shopService.MyUpdateById(shop)) {
            return Result.success("修改成功");
        }else{
            return Result.error("修改失败");
        }
    }

    @PostMapping("/delete")
    public Result deleteShop(@RequestBody Shop shop) {
        if (shop.getId() == null)
            throw new CustomException("商品id不能为空");
        Shop byId = shopService.getById(shop);
        // 查询一下要删除的shop是不是自己创建的 如果不是那就禁止删除
        if(!byId.getCreateuserid().equals(BaseContext.getCurrentUserId())){
            return Result.error("信息有误 无法删除");
        }
        shop.setIsdelete(1);
        // 删除图片在数据中的引用
        myimgService.MydeleteImgUseList(shop.getImgs());
        return Result.success(shopService.updateById(shop));
    }

    @PostMapping("/admin/delete")
    @AdminRequest
    public Result adminDeleteShop(@RequestBody Shop shop) {
        if (Objects.isNull(shop.getId()))
            throw new CustomException("商品id不能为空");
        shop.setIsdelete(1);
        // 删除图片在数据中的引用
        myimgService.MydeleteImgUseList(shop.getImgs());
        return Result.success(shopService.updateById(shop));
    }

    //获取用户发布的闲置 第三者的视角 过滤掉过期的
    @PostMapping("/getbyuserid/up")
    public Result getUpOrderByUserId(@RequestBody Userinfo user) {
        log.info("/getbyuserid/up 分页查询的参数为: {}", user);
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop::getCreateuserid, user.getOpenid())
                .eq(Shop::getIsdelete, 0)
                .eq(Shop::getStatus, 0)
                .gt(Shop::getCancelTime, LocalDateTime.now());
        List<Shop> list = shopService.list(wrapper);
        list.forEach(i ->{
            i.setDetail(sensitiveWordBs.replace(i.getDetail()));
        });
        return Result.success(list);
    }

    @PostMapping("/receive")
    public Result getReceiveByUserId(@RequestBody Shop shop) {
        Shop byId = shopService.getById(shop.getId());
        if(byId.getIsdelete() == 0){
            return Result.success("success");
        }
        return Result.error("商品不存在或已删除");
    }


}
