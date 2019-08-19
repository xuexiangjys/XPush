# XPush

[![](https://jitpack.io/v/xuexiangjys/XPush.svg)](https://jitpack.io/#xuexiangjys/XPush)
[![api](https://img.shields.io/badge/API-14+-brightgreen.svg)](https://android-arsenal.com/api?level=14)
[![I](https://img.shields.io/github/issues/xuexiangjys/XPush.svg)](https://github.com/xuexiangjys/XPush/issues)
[![Star](https://img.shields.io/github/stars/xuexiangjys/XPush.svg)](https://github.com/xuexiangjys/XPush)

一键集成推送（极光推送、友盟推送等），提供有效的保活机制，支持推送的拓展，充分解耦推送和业务逻辑，解放你的双手！

## 关于我

[![github](https://img.shields.io/badge/GitHub-xuexiangjys-blue.svg)](https://github.com/xuexiangjys)   [![csdn](https://img.shields.io/badge/CSDN-xuexiangjys-green.svg)](http://blog.csdn.net/xuexiangjys)   [![简书](https://img.shields.io/badge/简书-xuexiangjys-red.svg)](https://www.jianshu.com/u/6bf605575337)   [![掘金](https://img.shields.io/badge/掘金-xuexiangjys-brightgreen.svg)](https://juejin.im/user/598feef55188257d592e56ed)   [![知乎](https://img.shields.io/badge/知乎-xuexiangjys-violet.svg)](https://www.zhihu.com/people/xuexiangjys) 

----

## 特征

* 集成方便。只需几行代码即可实现推送的集成，目前已经提供极光、友盟等推送渠道，除此之外还可以根据自己的需要进行扩展。

* 功能强大。支持推送相关的注册、注销，标签的增加、删除、获取，别名的绑定、解绑、获取，推送的连接状态获取等操作，并能返回响应的结果；支持接收推送通知、通知的点击事件、自定义消息等推送类型。

* 统一的消息订阅。框架提供了统一的消息订阅渠道，无论你使用了何种推送方式，都可以在任何地方进行推送消息的订阅和取消订阅，方便消息的接收和处理。

* 支持增加消息过滤器。类似OkHttp中的拦截器，可以对接收的消息进行全局过滤，过滤出那些我们真正需要的推送消息。

* 提供有效的保活机制。保证接入XPush的应用消息推送的到达率和稳定性，这也是很多推送框架所做不到的。


----

## 如何使用

### 添加Gradle依赖

1.先在项目根目录的 build.gradle 的 repositories 添加:
```
allprojects {
     repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.然后在dependencies添加:

```
dependencies {
  ...
  //推送核心库
  implementation 'com.github.xuexiangjys.XPush:xpush-core:1.0.0'
   //推送保活库
  implementation 'com.github.xuexiangjys.XPush:keeplive:1.0.0'
}
```

### 初始化XPush设置




