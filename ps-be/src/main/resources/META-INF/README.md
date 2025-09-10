# 欢迎使用 Captcha Starter
## 一、简介
topinfo-platform-captcha-starter 是是图片校验组件，引入后，即可直接使用：  

## 二、使用

### 1、获取滑动图片请求地址：   
请求： http://ip:端口/项目名/platform/captcha/get-slider-captcha  
返回：
```json
{
  "code": 1,
  "message": "成功",
  "data": {
    "clientUuid": "59c047c7759f4b16b2fae086d9c7d434",
    "backgroundImage": "背景图base64",
    "bgImageWidth": 590,
    "bgImageHeight": 360,
    "randomX": "",
    "randomY": "",
    "type": "slider",
    "sliderImage": "抠图base64",
    "sliderImageWidth": 110,
    "sliderImageHeight": 360
  },
  "expand": ""
}
```


### 2、获取旋转图片请求地址：
请求： http://ip:端口/项目名/platform/captcha/get-rotate-captcha  
返回：
```json
{
  "code": 1,
  "message": "成功",
  "data": {
    "clientUuid": "59c047c7759f4b16b2fae086d9c7d434",
    "backgroundImage": "背景图base64",
    "bgImageWidth": 590,
    "bgImageHeight": 360,
    "randomX": "",
    "randomY": "",
    "type": "rotate",
    "sliderImage": "抠图base64",
    "sliderImageWidth": 110,
    "sliderImageHeight": 360
  },
  "expand": ""
}
```


### 2、验证验证码：
请求： http://ip:端口/项目名/platform/captcha/check-captcha  
请求参数：
- clientUuid：客户端ID
- startSlidingTime：客户端ID
- endSlidingTime：客户端ID
- type：验证码类型： rotate:旋转， slider:滑块
- x：验证坐标

返回：票据

后续通过票据进行登录。


注入验证服务：
```java
    /**
     * 验证码业务处理对象
     */
    @Autowired
    private CaptchaService captchaService;
```

调用验证方法：
```java
     // 验证
     boolean  bool = captchaService.checkTicket(ticket);

```

## 二、前端修改

1、替换 login-form-drag-img.vue 文件， 目录：src/components/login/login-form-drag-img.vue  
2、替换 SystemApplicationLogin2.vue文件，目录：src/views/main/login/SystemApplicationLogin2.vue

完成以上两个步骤后，执行fb pull，然后再fb dev 启动前端。

> 说明：  
> 在SystemApplicationLogin2.vue文件中，修改  captcha-type="slider" 的类型来控制验证码的类型。  
> rotate:旋转， slider:滑块