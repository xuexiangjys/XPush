# XPush

[![](https://jitpack.io/v/xuexiangjys/XPush.svg)](https://jitpack.io/#xuexiangjys/XPush)
[![api](https://img.shields.io/badge/API-14+-brightgreen.svg)](https://android-arsenal.com/api?level=14)
[![I](https://img.shields.io/github/issues/xuexiangjys/XPush.svg)](https://github.com/xuexiangjys/XPush/issues)
[![Star](https://img.shields.io/github/stars/xuexiangjys/XPush.svg)](https://github.com/xuexiangjys/XPush)

一键集成推送（极光推送、友盟推送、华为、小米推送等），提供有效的保活机制，支持推送的拓展，充分解耦推送和业务逻辑，解放你的双手！

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
  implementation 'com.github.xuexiangjys.XPush:xpush-huawei:1.0.0'
  implementation 'com.github.xuexiangjys.XPush:xpush-xiaomi:1.0.0'
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

## 如何拓展第三方推送

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

* 4.增加该推送平台对应的代码混淆配置信息。

以上即完成了推送平台的集成。剩下的就是在初始化XPush的时候对推送平台进行选择了.如果你看完了还是不会的话，你可以参考项目中的[xpush-xiaomi](https://github.com/xuexiangjys/XPush/tree/master/xpush-xiaomi)和[xpush-huawei](https://github.com/xuexiangjys/XPush/tree/master/xpush-huawei).


## 特别感谢

* [OnePush](https://github.com/pengyuantao/OnePush)
* [keeplive](https://github.com/fanqieVip/keeplive)

## 如果觉得项目还不错，可以考虑打赏一波

![](https://github.com/xuexiangjys/Resource/blob/master/img/pay/alipay.jpeg) &emsp; ![](https://github.com/xuexiangjys/Resource/blob/master/img/pay/weixinpay.jpeg)

## 联系方式

[![](https://img.shields.io/badge/点击一键加入QQ交流群-602082750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=9922861ef85c19f1575aecea0e8680f60d9386080a97ed310c971ae074998887)

![](https://github.com/xuexiangjys/XPage/blob/master/img/qq_group.jpg)