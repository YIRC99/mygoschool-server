# 栀子花墙介绍

栀子花墙小程序是一个开源的, 服务于九江职业大学学生的小程序项目,目前主要功能就有两个, 闲置物品的发布以及校园拼车的功能

简洁的干净的UI设计, 完善的功能, 以及自动的内容审核,完善的前后和管理端, 微信模板通知,在便利的同时也可以用来学习

![输入图片说明](src/main/resources/static/default/logo.jpg)


## 小程序端介绍

简洁的干净的UI设计, 完善的功能, 以及自动的内容审核,完善的前后和管理端, 微信模板通知,在便利的同时也可以用来学习

小程序端整体使用`uniapp`开发`vue2`框架  数据解密使用`crypto-js` 组件基本使用`uv-ui` 完成

附上小程序端 开源地址

[Gitee](https://gitee.com/yirc99/mygoshool)  https://gitee.com/yirc99/mygoshool

### 安装启动须知

```
npm install 
随后直接在Hbuiler里面更换小程序appID 便可直接使用小程序启动
```

## 服务端介绍

服务端采用`SpringBoot3.2.0`开发 `jdk`版本为21 配置文件加密使用`jasypt` 文本内容审核采用开源项目`sensitive-word`[GieHub链接]([houbb/sensitive-word: 👮‍♂️The sensitive word tool for java.(敏感词/违禁词/违法词/脏词。基于 DFA 算法实现的高性能 java 敏感词过滤工具框架。请勿发布涉及政治、广告、营销、翻墙、违反国家法律法规等内容。高性能敏感词检测过滤组件，附带繁体简体互换，支持全角半角互换，汉字转拼音，模糊搜索等功能。) (github.com)](https://github.com/houbb/sensitive-word)) 缓存使用`redis`

图片自动检测使用雅虎开源的NSFW模型`opennsfw2`配合python一起调用  数据加密采用AES加密CBC模式 还有`mybatis plus`

python版本为` 3.10.11`  

附上服务端开源地址

https://gitee.com/yirc99/mygoschool-server

### 安装启动须知

```
git clone https://gitee.com/yirc99/mygoschool-server.git
然后需要修改yml配置文件中 mysql连接 sql文件开源在项目的根目录下 
因为项目使用到了微信消息推送 如果需要使用完整的功能还需要配置WxSecret和微信模板id 这些都可以在yml文件中看到注释
还有python的路径和py的路径 以及jwt的密钥和aes密钥 还有将redis服务添加到环境变量中 项目启动时会自动检查redis启动情况并自动启动
配置完毕即可一键启动项目
```

## 管理端介绍

管理端暂时还未能完全完善 使用暂时无法开源出来

## 项目图片

![输入图片说明](src/main/resources/static/default/11.png)![输入图片说明](src/main/resources/static/22.png)

