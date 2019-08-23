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

2.添加XPush主要依赖:

```
dependencies {
  ...
  //推送核心库
  implementation 'com.github.xuexiangjys.XPush:xpush-core:1.0.0'
  //推送保活库
  implementation 'com.github.xuexiangjys.XPush:keeplive:1.0.0'
}
```

3.添加第三方推送依赖（根据自己的需求进行添加，当然也可以全部添加）

```
dependencies {
  ...
  //选择你想要集成的推送库
  implementation 'com.github.xuexiangjys.XPush:xpush-jpush:1.0.0'
  implementation 'com.github.xuexiangjys.XPush:xpush-umeng:1.0.0'
}
```

### 初始化XPush配置

1.注册消息推送接收器。方法有两种，选其中一种就行了。

* 继承`AbstractPushReceiver`类，重写里面的方法，并在AndroidManifest.xml中注册。

* 直接在AndroidManifest.xml中注册框架默认提供的`XPushReceiver`。

```
    <!--自定义消息推送接收器-->
    <receiver android:name=".push.CustomPushReceiver">
        <intent-filter>
            <action android:name="com.xuexiang.xpush.core.action.RECEIVE_CONNECT_STATUS_CHANGED" />
            <action android:name="com.xuexiang.xpush.core.action.RECEIVE_NOTIFICATION" />
            <action android:name="com.xuexiang.xpush.core.action.RECEIVE_NOTIFICATION_CLICK" />
            <action android:name="com.xuexiang.xpush.core.action.RECEIVE_MESSAGE" />
            <action android:name="com.xuexiang.xpush.core.action.RECEIVE_COMMAND_RESULT" />

            <category android:name="${applicationId}" />
        </intent-filter>
    </receiver>

    <!--默认的消息推送接收器-->
    <receiver android:name="com.xuexiang.xpush.core.receiver.impl.XPushReceiver">
        <intent-filter>
            <action android:name="com.xuexiang.xpush.core.action.RECEIVE_CONNECT_STATUS_CHANGED" />
            <action android:name="com.xuexiang.xpush.core.action.RECEIVE_NOTIFICATION" />
            <action android:name="com.xuexiang.xpush.core.action.RECEIVE_NOTIFICATION_CLICK" />
            <action android:name="com.xuexiang.xpush.core.action.RECEIVE_MESSAGE" />
            <action android:name="com.xuexiang.xpush.core.action.RECEIVE_COMMAND_RESULT" />

            <category android:name="${applicationId}" />
        </intent-filter>
    </receiver>

```

2.在AndroidManifest.xml的application标签下，添加第三方推送客户端实现类.

需要注意的是，这里注册的`PlatformName`和`PlatformCode`必须要和推送客户端实现类中的一一对应才行。

```
<!--name格式：XPush_[PlatformName]_[PlatformCode]-->
<!--value格式：对应客户端实体类的全类名路径-->

<!--如果引入了xpush-jpush库-->
<meta-data
    android:name="XPush_JPush_1000"
    android:value="com.xuexiang.xpush.jpush.JPushClient" />

<!--如果引入了xpush-umeng库-->
<meta-data
    android:name="XPush_UMengPush_1001"
    android:value="com.xuexiang.xpush.umeng.UMengPushClient" />

```

3.添加第三方AppKey和AppSecret.

这里的AppKey和AppSecret需要我们到各自的推送平台上注册应用后获得。

```
<!--极光推送静态注册-->
<meta-data
    android:name="JPUSH_CHANNEL"
    android:value="default_developer" />
<meta-data
    android:name="JPUSH_APPKEY"
    android:value="a32109db64ebe04e2430bb01" />

<!--友盟推送静态注册-->
<meta-data
    android:name="UMENG_APPKEY"
    android:value="5d5a42ce570df37e850002e9" />
<meta-data
    android:name="UMENG_MESSAGE_SECRET"
    android:value="4783a04255ed93ff675aca69312546f4" />
```

4.在Application中初始化XPush

初始化XPush的方式有两种：

* 静态注册

```
/**
 * 静态注册初始化推送
 */
private void initPush() {
    XPush.debug(BuildConfig.DEBUG);
    //静态注册，指定使用友盟推送客户端
    XPush.init(this, new UMengPushClient());
    XPush.register();
}
```

* 动态注册

```
/**
 * 动态注册初始化推送
 */
private void initPush() {
    XPush.debug(BuildConfig.DEBUG);
    //动态注册，根据平台名或者平台码动态注册推送客户端
    XPush.init(this, new IPushInitCallback() {
        @Override
        public boolean onInitPush(int platformCode, String platformName) {
            if (RomUtils.getRom().getRomName().equals(SYS_EMUI)) {
                //华为手机使用华为推送
                return platformCode == HuaweiPushClient.HUAWEI_PUSH_PLATFORM_CODE && platformName.equals(HuaweiPushClient.HUAWEI_PUSH_PLATFORM_NAME);
            } else {
                //未识别到特殊的rom，默认使用极光推送
                return platformCode == JPushClient.JPUSH_PLATFORM_CODE && platformName.equals(JPushClient.JPUSH_PLATFORM_NAME);
            }
        }
    });
    XPush.register();
}
```

## 如何拓展第三方推送

