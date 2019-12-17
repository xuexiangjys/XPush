# XPush

[![](https://jitpack.io/v/xuexiangjys/XPush.svg)](https://jitpack.io/#xuexiangjys/XPush)
[![api](https://img.shields.io/badge/API-14+-brightgreen.svg)](https://android-arsenal.com/api?level=14)
[![I](https://img.shields.io/github/issues/xuexiangjys/XPush.svg)](https://github.com/xuexiangjys/XPush/issues)
[![Star](https://img.shields.io/github/stars/xuexiangjys/XPush.svg)](https://github.com/xuexiangjys/XPush)

一个轻量级、可插拔的Android消息推送框架。一键集成推送（极光推送、友盟推送、信鸽推送、华为、小米推送等），提供有效的保活机制，支持推送的拓展，充分解耦推送和业务逻辑，解放你的双手！

在提issue前，请先阅读[【提问的智慧】](https://xuexiangjys.blog.csdn.net/article/details/83344235)，并严格按照[issue模板](https://github.com/xuexiangjys/XPush/issues/new/choose)进行填写，节约大家的时间。

在使用前，请一定要仔细阅读[使用说明文档](https://github.com/xuexiangjys/XPush/wiki),重要的事情说三遍！！！

在使用前，请一定要仔细阅读[使用说明文档](https://github.com/xuexiangjys/XPush/wiki),重要的事情说三遍！！！

在使用前，请一定要仔细阅读[使用说明文档](https://github.com/xuexiangjys/XPush/wiki),重要的事情说三遍！！！

## 关于我

[![github](https://img.shields.io/badge/GitHub-xuexiangjys-blue.svg)](https://github.com/xuexiangjys)   [![csdn](https://img.shields.io/badge/CSDN-xuexiangjys-green.svg)](http://blog.csdn.net/xuexiangjys)   [![简书](https://img.shields.io/badge/简书-xuexiangjys-red.svg)](https://www.jianshu.com/u/6bf605575337)   [![掘金](https://img.shields.io/badge/掘金-xuexiangjys-brightgreen.svg)](https://juejin.im/user/598feef55188257d592e56ed)   [![知乎](https://img.shields.io/badge/知乎-xuexiangjys-violet.svg)](https://www.zhihu.com/people/xuexiangjys) 

----

## 特征

* 集成方便。只需几行代码即可实现推送的集成，目前已经提供极光、友盟等推送渠道，除此之外还可以根据自己的需要进行扩展。

* 兼容性强。目前已完美支持Android 9.0。

* 功能强大。支持推送相关的注册、注销，标签的增加、删除、获取，别名的绑定、解绑、获取，推送的连接状态获取等操作，并能返回响应的结果；支持接收推送通知、通知的点击事件、自定义消息等推送类型。

* 统一的消息订阅。框架提供了统一的消息订阅渠道，无论你使用了何种推送方式，都可以在任何地方进行推送消息的订阅和取消订阅，方便消息的接收和处理。

* 支持增加消息过滤器。类似OkHttp中的拦截器，可以对接收的消息进行全局过滤，过滤出那些我们真正需要的推送消息。

* 提供有效的保活机制。保证接入XPush的应用消息推送的到达率和稳定性，这也是很多推送框架所做不到的。

## 组成结构

> 本框架借鉴了[OnePush（目前已不维护了）](https://github.com/pengyuantao/OnePush)中的部分思想，加之我3年消息推送的经验，形成了如下几个部分：

* 消息推送客户端`IPushClient`：主要提供消息推送平台的主要API。

* 消息推送事件转发器`IPushDispatcher`：主要用于将第三方的消息推送事件转发为XPush可识别的事件。

* 消息推送接收器`IPushReceiver`：统一接收IPushDispatcher转发过来的事件，是事件的接收中心。

* 推送消息的被观察者`IMessageObservable`：主要负责管理推送消息的订阅和转发。

* 推送消息的过滤策略`IMessageFilterStrategy`：主要负责推送消息的过滤处理和管理。

以上5个组成部分可以根据你自身的业务需求进行自定义。

## 消息推送流程

在后台发出一则推送消息后：

```
第三方推送平台 --- (消息) ---> 第三方推送平台内部的接收消息的Receiver --->（重写其接收的方法）---> IPushDispatcher ---> (转发消息内容为XPushMsg/XPushCommand）---> IPushReceiver ---> (如不使用XPushManager提供的消息管理，这里直接结束）

    【使用XPushManager提供的消息管理】：---IPushReceiver---> XPushManager -----> IMessageFilterStrategy --->（对消息进行过滤处理）---> IMessageObservable ---> （消息转发到具体订阅的地方）

```

## 为什么要做这个项目

做过Android消息推送的人都知道，Android不仅设备碎片化严重，推送平台也是五花八门的。早在2017年工信部就号召所有的厂商来制定统一的Android消息推送平台，可到现在也没有下文（究其原因还是这其中的利益太大了，谁也不想妥协）。

可是我们也不能将希望全都寄托在这个完全没有定数的事件上，代码终归要写，功能终归要上，与其受制于人，不如自己革命，搞一个自己能控制的消息推送全平台解决方案来得靠谱。

可能有人又会说，现在友盟和信鸽都支持厂商推送的集成，为何你自己还要搞一套呢？如果你对推送的及时性和到达率都没什么要求的话，其实也是无所谓的（实践证明，友盟并不好用，信鸽还可以）。在这里我需要说明的是，你不可能把自己的命运交到别人的手里，推送有别于其他的业务，相对来说比较复杂，需要处理大批量的事件消息，对服务器的要求比较大，你愿意把你的推送消息交给第三方推送平台去处理？再说了，你能强制你们后台接入指定第三方的推送平台？如果都不能，与其受制于人，何不把这些命运把握在自己的手上，那么写出来的功能自己心安啊。

之前在QQ交流群里一直有人希望我开源一个消息推送框架，其实我在上一家公司的时候就写了一个推送框架，只不过捆绑业务太深，加之避开泄密之嫌，也就没有开源的必要。此次的推送框架完全是重新写了一个，加之全新的设计，会使框架更加通用，灵活。

## 演示（请star支持）

### 程序演示

![](./art/demo.gif)

### Demo下载

#### XPush全平台集成Demo

[![XPushDemo](https://img.shields.io/badge/downloads-4.2M-blue.svg)](https://github.com/xuexiangjys/XPush/blob/master/apk/xpushdemo.apk?raw=true)

![](./art/download_xpush.png)

#### 信鸽厂商集成Demo

[![信鸽demo](https://img.shields.io/badge/downloads-2.2M-blue.svg)](https://github.com/xuexiangjys/XPush/blob/master/apk/xgdemo.apk?raw=true)

![](./art/download_xgdemo.png)


----

## 快速集成指南

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
  implementation 'com.github.xuexiangjys.XPush:xpush-core:1.0.1'
  //推送保活库
  implementation 'com.github.xuexiangjys.XPush:keeplive:1.0.1'
}
```

3.添加第三方推送依赖（根据自己的需求进行添加，当然也可以全部添加）

```
dependencies {
  ...
  //选择你想要集成的推送库
  implementation 'com.github.xuexiangjys.XPush:xpush-jpush:1.0.1'
  implementation 'com.github.xuexiangjys.XPush:xpush-umeng:1.0.1'
  implementation 'com.github.xuexiangjys.XPush:xpush-huawei:1.0.1'
  implementation 'com.github.xuexiangjys.XPush:xpush-xiaomi:1.0.1'
  implementation 'com.github.xuexiangjys.XPush:xpush-xg:1.0.1'
}
```

### 初始化XPush配置

1.注册消息推送接收器。方法有两种，选其中一种就行了。

* 如果你想使用`XPushManager`提供的消息管理，直接在AndroidManifest.xml中注册框架默认提供的`XPushReceiver`。当然你也可以继承`XPushReceiver`，并重写相关方法。

* 如果你想实现自己的消息管理，可继承`AbstractPushReceiver`类，重写里面的方法，并在AndroidManifest.xml中注册。

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

注意，如果你的Android设备是8.0及以上的话，静态注册的广播是无法正常生效的，解决的方法有两种：

* 动态注册消息推送接收器

* 修改推送消息的发射器

```
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    //Android8.0静态广播注册失败解决方案一：动态注册
    XPush.registerPushReceiver(new CustomPushReceiver());

    //Android8.0静态广播注册失败解决方案二：修改发射器
    XPush.setIPushDispatcher(new Android26PushDispatcherImpl(CustomPushReceiver.class));
}
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
    
<!--如果引入了xpush-huawei库-->
<meta-data
    android:name="XPush_HuaweiPush_1002"
    android:value="com.xuexiang.xpush.huawei.HuaweiPushClient" />

<!--如果引入了xpush-xiaomi库-->
<meta-data
    android:name="XPush_MIPush_1003"
    android:value="com.xuexiang.xpush.xiaomi.XiaoMiPushClient" />
    
<!--如果引入了xpush-xg库-->
<meta-data
    android:name="XPush_XGPush_1004"
    android:value="@string/xpush_xg_client_name" />
```

3.添加第三方AppKey和AppSecret.

这里的AppKey和AppSecret需要我们到各自的推送平台上注册应用后获得。注意如果使用了xpush-xiaomi,那么需要在AndroidManifest.xml添加小米的AppKey和AppSecret（注意下面的“\ ”必须加上，否则获取到的是float而不是String，就会导致id和key获取不到正确的数据）。


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
    
<!--华为HMS推送静态注册-->
<meta-data
    android:name="com.huawei.hms.client.appid"
    android:value="101049475"/>

<!--小米推送静态注册，下面的“\ ”必须加上，否则将无法正确读取-->
<meta-data
    android:name="MIPUSH_APPID"
    android:value="\ 2882303761518134164"/>
<meta-data
    android:name="MIPUSH_APPKEY"
    android:value="\ 5371813415164"/>
    
<!--信鸽推送静态注册-->
<meta-data
    android:name="XGPUSH_ACCESS_ID"
    android:value="2100343759" />
<meta-data
    android:name="XGPUSH_ACCESS_KEY"
    android:value="A7Q26I8SH7LV" />
```

4.在Application中初始化XPush

初始化XPush的方式有两种，根据业务需要选择一种方式就行了：

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
            String romName = RomUtils.getRom().getRomName();
            if (romName.equals(SYS_EMUI)) {
                return platformCode == HuaweiPushClient.HUAWEI_PUSH_PLATFORM_CODE && platformName.equals(HuaweiPushClient.HUAWEI_PUSH_PLATFORM_NAME);
            } else if (romName.equals(SYS_MIUI)) {
                return platformCode == XiaoMiPushClient.MIPUSH_PLATFORM_CODE && platformName.equals(XiaoMiPushClient.MIPUSH_PLATFORM_NAME);
            } else {
                return platformCode == JPushClient.JPUSH_PLATFORM_CODE && platformName.equals(JPushClient.JPUSH_PLATFORM_NAME);
            }
        }
    });
    XPush.register();
}
```

---

## 如何使用XPush

### 1、推送的注册和注销

* 通过调用`XPush.register()`，即可完成推送的注册。

* 通过调用`XPush.unRegister()`，即可完成推送的注销。

* 通过调用`XPush.getPushToken()`，即可获取消息推送的Token(令牌)。

* 通过调用`XPush.getPlatformCode()`，即可获取当前使用推送平台的码。

### 2、推送的标签（tag）处理

* 通过调用`XPush.addTags()`，即可添加标签（支持传入多个）。

* 通过调用`XPush.deleteTags()`，即可删除标签（支持传入多个）。

* 通过调用`XPush.getTags()`，即可获取当前设备所有的标签。

需要注意的是，友盟推送和信鸽推送目前暂不支持标签的获取，华为推送不支持标签的所有操作，小米推送每次只支持一个标签的操作。

### 3、推送的别名（alias）处理

* 通过调用`XPush.bindAlias()`，即可绑定别名。

* 通过调用`XPush.unBindAlias()`，即可解绑别名。

* 通过调用`XPush.getAlias()`，即可获取当前设备所绑定的别名。

需要注意的是，友盟推送和信鸽推送目前暂不支持别名的获取，华为推送不支持别名的所有操作。

### 4、推送消息的接收

* 通过调用`XPushManager.get().register()`方法，注册消息订阅`MessageSubscriber`，即可在任意地方接收到推送的消息。

* 通过调用`XPushManager.get().unregister()`方法，即可取消消息的订阅。

这里需要注意的是，消息订阅的回调并不一定是在主线程，因此在回调中如果进行了UI的操作，一定要确保切换至主线程。下面演示代码中使用了我的另一个开源库[XAOP](https://github.com/xuexiangjys/XAOP),只通过`@MainThread`注解就能自动切换至主线程,可供参考。

```
/**
 * 初始化监听
 */
@Override
protected void initListeners() {
    XPushManager.get().register(mMessageSubscriber);
}

private MessageSubscriber mMessageSubscriber = new MessageSubscriber() {
    @Override
    public void onMessageReceived(CustomMessage message) {
        showMessage(String.format("收到自定义消息:%s", message));
    }

    @Override
    public void onNotification(Notification notification) {
        showMessage(String.format("收到通知:%s", notification));
    }
};

@MainThread
private void showMessage(String msg) {
    tvContent.setText(msg);
}


@Override
public void onDestroyView() {
    XPushManager.get().unregister(mMessageSubscriber);
    super.onDestroyView();
}
```

### 5、推送消息的过滤处理

* 通过调用`XPushManager.get().addFilter()`方法，可增加对订阅推送消息的过滤处理。对于一些我们不想处理的消息，可以通过消息过滤器将它们筛选出来。

* 通过调用`XPushManager.get().removeFilter()`方法，即可去除消息过滤器。

```
/**
 * 初始化监听
 */
@Override
protected void initListeners() {
    XPushManager.get().addFilter(mMessageFilter);
}

private IMessageFilter mMessageFilter = new IMessageFilter() {
    @Override
    public boolean filter(Notification notification) {
        if (notification.getContent().contains("XPush")) {
            showMessage("通知被拦截");
            return true;
        }
        return false;
    }

    @Override
    public boolean filter(CustomMessage message) {
        if (message.getMsg().contains("XPush")) {
            showMessage("自定义消息被拦截");
            return true;
        }
        return false;
    }
};

@Override
public void onDestroyView() {
    XPushManager.get().removeFilter(mMessageFilter);
    super.onDestroyView();
}
```

### 6、推送通知的点击处理

> 对于通知的点击事件，我们可以处理得更优雅，自定义其点击后的动作，打开我们想让用户看到的页面。

我们可以在全局消息推送的接收器`IPushReceiver`中的`onNotificationClick`回调中，增加打开指定页面的操作。

```
@Override
public void onNotificationClick(Context context, XPushMsg msg) {
    super.onNotificationClick(context, msg);
    //打开自定义的Activity
    Intent intent = IntentUtils.getIntent(context, TestActivity.class, null, true);
    intent.putExtra(KEY_PARAM_STRING, msg.getContent());
    intent.putExtra(KEY_PARAM_INT, msg.getId());
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    ActivityUtils.startActivity(intent);
}
```

需要注意的是，这需要你在消息推送平台推送的通知使用的是`自定义动作`或者`打开指定页面`类型，并且传入的Intent uri 内容满足如下格式:

* title：通知的标题

* content：通知的内容

* extraMsg：通知附带的拓展字段，可存放json或其他内容

* keyValue：通知附带的键值对

```
xpush://com.xuexiang.xpush/notification?title=这是一个通知&content=这是通知的内容&extraMsg=xxxxxxxxx&keyValue={"param1": "1111", "param2": "2222"}
```

当然你也可以自定义传入的Intent uri 格式，具体可参考项目中的[XPushNotificationClickActivity](https://github.com/xuexiangjys/XPush/blob/master/xpush-core/src/main/java/com/xuexiang/xpush/core/XPushNotificationClickActivity.java)和[AndroidManifest.xml](https://github.com/xuexiangjys/XPush/blob/master/xpush-core/src/main/AndroidManifest.xml)

---

## 推送平台说明

### 目前已支持的推送平台

推送平台 | 平台名 | 平台码 | 模块名 | 客户端类
:-|:-:|:-:|:-:|:-
[极光推送](https://www.jiguang.cn/) | JPush | 1000 | xpush-jpush | com.xuexiang.xpush.jpush.JPushClient
[友盟推送](https://www.umeng.com/push) | UMengPush | 1001 | xpush-umeng | com.xuexiang.xpush.umeng.UMengPushClient
[华为推送](https://developer.huawei.com/consumer/cn/service/hms/pushservice.html) | HuaweiPush | 1002 | xpush-huawei | com.xuexiang.xpush.huawei.HuaweiPushClient
[小米推送](https://dev.mi.com/console/appservice/push.html) | MIPush | 1003 | xpush-xiaomi | com.xuexiang.xpush.xiaomi.XiaoMiPushClient
[信鸽推送](https://xg.qq.com/) | XGPush | 1004 | xpush-xg | com.xuexiang.xpush.xg.XGPushClient

除此之外，如果你使用MQTT协议来做消息推送的话，我也同样提供了案例给你：[XPush-MQTT](https://github.com/xuexiangjys/XPush-MQTT)

### 推送平台的注意事项

> 极光推送平台所有特性都支持。

#### 友盟推送

* 友盟推送在进行XPush初始化的时候，除了在主进程中注册，还需要在channel中注册。

* 友盟推送不支持Tag和alias的获取

* 友盟推送不支持监听推送的连接状态。

#### 信鸽推送

* 信鸽推送不支持Tag和alias的获取

* 信鸽推送不支持监听推送的连接状态。

#### 华为推送

* 华为推送在注册之前需要安装最新的推送服务，否则将无法注册成功（库会自动弹出升级或者安装提示）

* 华为推送不支持所有Tag和alias的操作。

* 华为推送不支持接收通知到达事件。

#### 小米推送

* 小米推送一次只能操作一个Tag。

* 小米推送注销无结果反馈。

* 小米推送不支持监听推送的连接状态。

### 如何拓展第三方推送

> 由于Android推送平台的众多，目前本项目不可能也没必要提供所有推送平台的集成库。如果你想使用的推送平台在我这没有找到对应的集成库的话，那么就需要你自己写一个了。

其实拓展一个第三方推送库也不是很难，只要遵循以下4步骤就可以完成了：

* 1.新建一个Android Library Module，然后将你准备集成的推送平台的依赖内容导入进来。这里包括引入推送依赖库或SDK、配置`AndroidManifest.xml`。

* 2.创建该推送平台的客户端XXXClient，实现IPushClient接口，并且重写对应的方法。其中`init`、`register`、`unRegister`、`getPlatformCode`、`getPlatformName`这5个方法是必须重写的。

IPushClient接口方法详细如下：

```
public interface IPushClient {
    /**
     * 初始化【必须】
     *
     * @param context
     */
    void init(Context context);
    /**
     * 注册推送【必须】
     */
    void register();
    /**
     * 注销推送【必须】
     */
    void unRegister();
    /**
     * 绑定别名【别名是唯一的】
     *
     * @param alias 别名
     */
    void bindAlias(String alias);
    /**
     * 解绑别名
     *
     * @param alias 别名
     */
    void unBindAlias(String alias);
    /**
     * 获取别名
     */
    void getAlias();
    /**
     * 增加标签
     *
     * @param tag 标签
     */
    void addTags(String... tag);
    /**
     * 删除标签
     *
     * @param tag 标签
     */
    void deleteTags(String... tag);
    /**
     * 获取标签
     */
    void getTags();
    /**
     * @return 获取推送令牌
     */
    String getPushToken();
    /**
     * 注意千万不要重复【必须】
     * @return 获取平台码
     */
    int getPlatformCode();
    /**
     * 注意千万不要重复【必须】
     * @return 获取平台名
     */
    String getPlatformName();
}

```

* 3.创建和重写三方消息推送的消息接收器（一般是重写Receiver）。重写三方推送的的接收透传消息和通知的方法，调用`XPush`的transmitXXX方法，将通知、透传消息、通知点击事件、以及其他事件，转发到XPush。

主要调用以下五个方法：

(1)XPush.transmitMessage(): 转发自定义(透传)消息.

(2)XPush.transmitNotification(): 转发通知到达消息.

(3)XPush.transmitNotificationClick(): 转发通知点击事件.

(4)XPush.transmitCommandResult(): 转发IPushClient命令执行结果.

(5)XPush.transmitConnectStatusChanged(): 转发推送连接状态发生改变的事件.

* 4.增加该推送平台对应的代码混淆配置信息。

以上即完成了推送平台的集成。剩下的就是在初始化XPush的时候对推送平台进行选择了.如果你看完了还是不会的话，你可以参考项目中的[xpush-xiaomi](https://github.com/xuexiangjys/XPush/tree/master/xpush-xiaomi)和[xpush-huawei](https://github.com/xuexiangjys/XPush/tree/master/xpush-huawei).


## 保活机制说明

> 这里提供的应用保活机制也是借鉴了前人终结出来的各种方案的混合处理。目前在9.0及以下版本都能有很好的保活效果（只要你不主动杀死程序），如果你的应用希望能够一直在后台运行（比如推送服务）而不被系统自动杀死的话，可以尝试一下。需要注意的是，程序保活并不代表能做到程序杀不死，除非你把你的应用做成系统应用或者加入到系统的白名单内，否则也只是提高了程序的优先级权重，减少程序被系统回收杀死的概率而已。

关于保活机制的使用可以参考[保活机制使用](https://github.com/xuexiangjys/XPush/wiki/%E4%BF%9D%E6%B4%BB%E6%9C%BA%E5%88%B6%E4%BD%BF%E7%94%A8)

---

## 实体介绍

### XPushMsg

> 推送消息转译实体，携带消息的原始数据

字段名 | 类型 | 备注
:-|:-:|:-
mId | int | 消息ID / 状态
mTitle | String | 通知标题
mContent | String | 通知内容
mMsg | String | 自定义（透传）消息
mExtraMsg | String | 消息拓展字段
mKeyValue | String | 消息键值对


### Notification

> 推送通知，由XPushMsg转化而来

字段名 | 类型 | 备注
:-|:-:|:-
mId | int | 消息ID / 状态
mTitle | String | 通知标题
mContent | String | 通知内容
mExtraMsg | String | 消息拓展字段
mKeyValue | String | 消息键值对


### CustomMessage

> 自定义（透传）消息，由XPushMsg转化而来

字段名 | 类型 | 备注
:-|:-:|:-
mMsg | String | 自定义（透传）消息
mExtraMsg | String | 消息拓展字段
mKeyValue | String | 消息键值对


### XPushCommand

> IPushClient执行相关命令的结果信息实体

字段名 | 类型 | 备注
:-|:-:|:-
mType | int | 命令类型
mResultCode | int | 结果码
mContent | String | 命令内容
mExtraMsg | String | 拓展字段
mError | String | 错误信息


## 常量介绍

### CommandType

> 命令的类型

命令名 | 命令码 | 备注
:-|:-:|:-
TYPE_REGISTER | 2000 | 注册推送
TYPE_UNREGISTER | 2001 | 注销推送
TYPE_ADD_TAG | 2002 | 添加标签
TYPE_DEL_TAG | 2003 | 删除标签
TYPE_GET_TAG | 2004 | 获取标签
TYPE_BIND_ALIAS | 2005 | 绑定别名
TYPE_UNBIND_ALIAS | 2006 | 解绑别名
TYPE_GET_ALIAS | 2007 | 获取别名
TYPE_AND_OR_DEL_TAG | 2008 | 添加或删除标签

### ResultCode

> 命令的结果码

结果名 | 结果码 | 备注
:-|:-:|:-
RESULT_OK | 0 | 成功
RESULT_ERROR | 1 | 失败

### ConnectStatus

> 推送连接状态

状态名 | 状态码 | 备注
:-|:-:|:-
DISCONNECT | 10 | 已断开
CONNECTING | 11 | 连接中
CONNECTED | 12 | 已连接

---

## 混淆配置

```
# XPush的混淆
-keep class * extends com.xuexiang.xpush.core.IPushClient{*;}
-keep class * extends com.xuexiang.xpush.core.receiver.IPushReceiver{*;}

# 极光推送混淆
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
-keep class * extends cn.jpush.android.service.JPushMessageReceiver{*;}

# umeng推送
-dontwarn com.umeng.**
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn com.meizu.**
-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class com.meizu.** {*;}
-keep class org.apache.thrift.** {*;}
-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

# 信鸽推送
-keep class com.tencent.android.tpush.** {*;}
-keep class com.tencent.mid.** {*;}
-keep class com.qq.taf.jce.** {*;}
-keep class com.tencent.bigdata.** {*;}

# 华为推送
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.android.hms.agent.**{*;}

# 小米推送
-keep class * extends com.xiaomi.mipush.sdk.PushMessageReceiver{*;}

```

## 特别感谢

* [OnePush](https://github.com/pengyuantao/OnePush)
* [keeplive](https://github.com/fanqieVip/keeplive)
* [HelloDaemon](https://github.com/xingda920813/HelloDaemon)

## 如果觉得项目还不错，可以考虑打赏一波

> 你的打赏是我维护的动力，我将会列出所有打赏人员的清单在下方作为凭证，打赏前请留下打赏项目的备注！

![](https://github.com/xuexiangjys/Resource/blob/master/img/pay/alipay.jpeg) &emsp; ![](https://github.com/xuexiangjys/Resource/blob/master/img/pay/weixinpay.jpeg)

## 联系方式

[![](https://img.shields.io/badge/点击一键加入QQ交流群-602082750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=9922861ef85c19f1575aecea0e8680f60d9386080a97ed310c971ae074998887)

![](https://github.com/xuexiangjys/XPage/blob/master/img/qq_group.jpg)