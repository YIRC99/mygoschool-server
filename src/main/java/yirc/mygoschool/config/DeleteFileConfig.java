package yirc.mygoschool.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import yirc.mygoschool.domain.Carshareorder;
import yirc.mygoschool.domain.Feekback;
import yirc.mygoschool.domain.Myimg;
import yirc.mygoschool.domain.Shop;
import yirc.mygoschool.service.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Version v1.0
 * @DateTime 2024/4/24 12:01
 * @Description 自动删除文件的配置类
 * @Author 一见如初
 */
@Configuration
@EnableScheduling
@Slf4j
public class DeleteFileConfig {


    @Autowired
    private ShopService shopService;

    @Autowired
    private CarshareorderService carshareorderService;

    @Autowired
    private FeekbackService feekbackService;

    @Autowired
    private MynsfwService mynsfwService;

    @Autowired
    private MyimgService myimgService;

    @Value("${yirc99.filePath}")
    private String filePath;

    private String projectRootPath = new File("").getAbsolutePath();
    //时间为31天之前 要删除的数据就是31天之前的数据
    private LocalDateTime deleteTime = LocalDateTime.now().minusDays(31);




    //项目启动之后 配置一个定时任务 每个月1号0点 查询31天之前的所有删除的数据 然后删除掉
    @Scheduled(cron = "0 0 0 1 * ?")
    public void deleteOldData() {
        // 这里编写删除数据的逻辑
        log.info("开始执行每月1号0点的定时任务：删除过期数据");
        selectAbandonFile();
        log.info("定时任务执行完毕");
    }

    //查询整个项目所有已经被删除的文件
    private void selectAbandonFile() {
        /**
         * 下面这个四个方法是直接查询不同的类型然后查询31天前的数据然后去删除
         * 但是因为这样查询不到未删除的图片 所以就不用了 废弃掉
         * 下面我更新了一个更好更简单的方法
         */
//        deleteShopFile();
//        deleteCarOrderFile();
//        deleteFeedbackFile();
//        deleteNSFWFile();
        // 新方法 添加了一张条优化删除逻辑
        deleteNotUseImg();
    }

    private void deleteNotUseImg(){
        // 一次性删除数据库中的数据 准备的ids
        List<Long> deleteIdList = new ArrayList<>();
        // 查询所以未使用的图片 每一次查询5000条数据 然后执行删除
        LambdaQueryWrapper<Myimg> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Myimg::getIsuse, 0);
        //为了防止并发问题 删除文件的过程中，有其他线程向数据库中插入了新的数据 所以只查询3天前的数据
        wrapper.lt(Myimg::getCreateat, LocalDateTime.now().minusDays(3));
        Page<Myimg> page = myimgService.page(new Page<>(1, 5000), wrapper);
        List<Myimg> imgList = page.getRecords();
        // 因为查出来的直接是全路径 所以直接删除
        // 遍历查询结果，逐个删除图片文件并添加要删除的数据库id
        for (Myimg img : imgList) {
            File imgFile = new File(img.getImgpath());
            if (imgFile.exists() && imgFile.isFile()) {
                imgFile.delete();
                log.warn("删除图片文件成功：{}", imgFile.getAbsolutePath());
                deleteIdList.add(img.getId());
            }else{
                log.error("删除图片文件失败 文件不存在或者是文件夹：{}", imgFile.getAbsolutePath());
            }
        }

        // 删除成功之后再去查询下一个5000条数据 直到查出0条数据
        while(page.hasNext()){
            page = myimgService.page(page, wrapper);
            imgList = page.getRecords();
            for (Myimg img : imgList) {
                File imgFile = new File(img.getImgpath());
                if (imgFile.exists() && imgFile.isFile()) {
                    imgFile.delete();
                    log.warn("删除图片文件成功：{}", imgFile.getAbsolutePath());
                    deleteIdList.add(img.getId());
                }else{
                    log.error("删除图片文件失败 文件不存在或者是文件夹：{}", imgFile.getAbsolutePath());
                }
            }
        }
        // 删除之后 然后再去数据库中删除这些数据 减少存储压力
        if(!deleteIdList.isEmpty()){
            boolean deleteResult = myimgService.removeByIds(deleteIdList);
            if(!deleteResult)
                log.error("删除数据库中无用的引用数据失败");
        }
    }

    private void deleteNSFWFile() {
        // 因为nsfw图片都是给管理员看的，要不要删除在考虑考虑
    }

    private void deleteFeedbackFile() {
        //查询要删除的数据列表
        LambdaQueryWrapper<Feekback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Feekback::getStatus,1)
                .lt(Feekback::getCreateat, deleteTime)
                .isNotNull(Feekback::getImglist);

        List<Feekback> list = feekbackService.list(wrapper);
        if(list.isEmpty()) return;
        //获取项目根目录
        String relativePath = filePath + "feedback" + File.separator; // 设置相对路径
        String resultPath = projectRootPath + File.separator + relativePath;
        // 构造要删除文件的 路径list
        for (Feekback feekback : list) {
            String[] split = feekback.getImglist().split(",");
            for (String s : split) {
                File file = new File(resultPath + s);
                if(file.exists()){
                    file.delete();
                    feekback.setIsDelete(3);
                    log.error("自动删除文件 路径为: {}", resultPath + s);
                    //  这里考虑一下 删除文件需不需要存一个日志
                }
            }
        }
        // 将删除的数据在数据库中记录
        feekbackService.updateBatchById(list);

    }

    private void deleteCarOrderFile() {
        LambdaQueryWrapper<Carshareorder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Carshareorder::getIsDelete, 1)
                .lt(Carshareorder::getCreateat, deleteTime);
        List<Carshareorder> list = carshareorderService.list(wrapper);


        //配置图片路径
        String relativePath = filePath + "QRcode" + File.separator; // 设置相对路径
        String resultPath = projectRootPath  + relativePath;
        //删除
        for (Carshareorder order : list) {
            File wechatImg = new File(resultPath + order.getWechatImg());
            File receiveImg = new File(resultPath + order.getReceiveUserWechatImg());
            if(wechatImg.exists()){
                log.error("自动删除文件 路径为: {}", wechatImg.toPath());
                order.setIsDelete(3); //删除成功一个就改一个 3就表示文件已经删除
                wechatImg.delete();
            }
            if(receiveImg.exists()){
                log.error("自动删除文件 路径为: {}", receiveImg.toPath());
                order.setIsDelete(3); //删除成功一个就改一个 3就表示文件已经删除
                receiveImg.delete();
            }
        }

        // 将isDelete字段改成3  表示文件已经删除 // 将删除的数据在数据库中记录
        carshareorderService.updateBatchById(list);
    }

    private void deleteShopFile() {
        LambdaQueryWrapper<Shop> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Shop::getIsdelete, 1)
                .lt(Shop::getCreateAt, deleteTime);
        List<Shop> list = shopService.list(wrapper);

        //配置商品图片路径
        String relativePath = filePath + "shop" + File.separator; // 设置相对路径
        String resultPath = projectRootPath  + relativePath;
        //配置QRCode图片路径
        String relativePath2 = filePath + "QRcode" + File.separator; // 设置相对路径
        String QRCodeResultPath = projectRootPath  + relativePath2;
        //遍历拿到所有的shop图片路径然后删除
        list.forEach(shop -> {
            for (String img : shop.getImgs().split(",")) {
                File file = new File(resultPath + img);
                if(file.exists()){
                    file.delete();
                    shop.setIsdelete(3);
                    log.error("自动删除文件 路径为: {}", file.toPath());
                }
            }
            //删除QRCode图片
            File QRCodeImg = new File(QRCodeResultPath + shop.getWechatimg());
            if (QRCodeImg.exists()) {
                QRCodeImg.delete();
                shop.setIsdelete(3);
                log.error("自动删除文件 路径为: {}", QRCodeImg.toPath());
            }
        });
        // 数据库记录删除
        shopService.updateBatchById(list);
    }

}
