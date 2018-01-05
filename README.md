# Android7.0/8.0系统新特性介绍

# 7.0
## 行为变化

### 1)引入Doze模式，延长电池续航时间，减少内存占用。
<img src="https://developer.android.com/images/android-7.0/doze-diagram-1.png"/>

当手机未接电源并且屏幕关闭一段时间后，系统进入Doze模式，关闭所有网络连接，延迟执行所有的任务和同步工作，当手机静止不动并且此时已进入Doze模式一段时间后，系统会更进一步地限制PowerManger.WakeLock,AlarmManger，GPS,WIFI扫描活动，在Doze期间系统会周期性地开启一个短的维护时间窗口，在此期间app可以允许网络连接或者同步工作，此后再次进入睡眠模式。

### 2）后台优化
移除了3个广播Action，优化电池消耗和内存占用。
* CONNECZZTIVITY_ACTION，网络切换会很频繁，比如WIFI和数据流量的切换也会唤醒，注册此Action的app会频繁唤醒去处理接收到的广播。
* ACTION_NEW_PICTURE和ACTION_NEW_VIDEO，用户拍照或者拍摄视频触发，会频繁唤醒所有注册的app。

### 3）权限变化，收紧了app私有文件的访问权限
* 私有文件权限不再由开发者控制，任何视图通过对私有文件设置MODE_WORLD_READABLE或者MODE_WORLD_WRITEABLE的操作都会触发SecurityException。
* app外通过file://URI 的形式发送文件会导致接受者收到的是无法访问的文件，因此通过file://URI 的形式发送文件会触发FileUriExposedException，推荐的方式是通过FileProvider分享文件。
* DownloadManager无法通过文件名分享私有文件。访问COLUM_LOCAL_FILENAME会触发SecurtiyException。推荐的方式是通过ContentResolver.openFileDescriptor()打开。

### 4）app间文件分享
禁止通过file://URI 方式分享文件，会触发FileUriExposedException。推荐使用content://URI 的方式。

### 5）屏幕缩放功能
可以在屏幕宽度大于sw320dp的手机上进行屏幕的缩放，主要是方便弱视用户使用手机。当用户启动缩放模式时，app会发生如下变化：
* target api <= 23,所有app后台会被杀死，前台会收到configuration change，即跟手机横竖屏转换一样的事件。
* target api >= 27,后台不杀死，前后台均收到configuration change

### 6)NDK app动态链接库问题
检查所有第三方so库或者本地Native代码，任何访问private NDK API而不是public NDK api的app都会crash。target api=27的app不允许访问private NDK API，第三方so库也不行。确保自身的native代码和任何第三方代码没有使用private NDK api。一般来说有三种情况:
* app直接访问了private platform libraries，更改的方法是使用public ndk api或者将private ndk api直接拷贝到本地。
* 第三方so库使用了private platform libraries,尽管自身的app未使用。
* 引入的库未包含在apkn内。比如说licrypto.so，有的Android版本包含了，有的未包含（Android6.0以后不再提供,OpenSSL换成了BoringSSL,会导致之前的so不存在）。

### 7）Retention注解
7.0之前的注解VISIBILITY_BUILD和VISIBILITY_SYSTEM被忽略导致的bug，现已修复。通过使用@Retention(RetentionPolicy.RUNTIME)生效。

### 8）TLS/SSL 默认配置变化
RC4 cipher不再提供，启用新的CHACHA20-POLY1305 cipher suites。

### 9）其他变化
* 系统为7.0但是target api <= 23用户改变了屏幕尺寸（启用了屏幕缩放模式），app的进程会被杀死，需针对这种情况做处理，否则用户从Recents按钮点击回来的时候会发生崩溃。

* 7.0系统以前有个bug,严格模式下对在主线程执行的tcp socket未标记，目前系统已更改，在主线程执行网络访问会抛出NetworkOnMainThreadException。

* Debug.startMethodTracing()等等方法目前会默认将结果存储到包名下面，无需app额外申请WRITE_EXTERNAL_STORAGE权限。

* 使用Binder传递数据时，目前大部分api会执行payload检查，如果数据量过大会抛出TransactionTooLargeExceptions，之前版本只是打印log或者忽略。比如说Activity.onSaveInstanceState()，数据量过大会抛异常。

* View未attached to window，此时给View执行了Runnable任务，目前会将此Runnable任务进入队列，等View attached的时候才会去执行。之前执行此种操作时会产生的问题是Runnable可能会执行在错误的线程上。

* 注册了DELETE_PACKAGE在卸载其他包时，但是是其他app安装了这个包的情况下，系统会弹窗让用户确认。但是系统会发STATUS_PENDING_USER_ACTION告诉结果。

* 不再提供Crypto加密，Crypto基于SHA1PRNG算法，此算法加密性能太弱，用户可以选择其他加密方式。

## 新特性

### 1）多窗口支持
<img src="https://developer.android.com/images/android-7.0/mw-portrait.png">

用户可以在一个页面同时开两app，并且可以移动缩放任意app。

### 2）通知加强
* 通知样式模板更新。
* 通知样式自定义。
* 通知分组支持，按组显示，按组删除或者管理。
* 直接回复，比如及时通讯app可以在通知栏直接回复。

### 3）Just In Time（JIT）
主要是优化app运行速度

### 4）Doze加强

### 5）SurfaceView




# 8.0

## 行为变化

**影响所有app**
### 1) 后台任务限制
当app进入cached状态，并且没有任何活跃控件时，系统会释放app持有的任何wakelock。对于非前台任务，以下行为将受限制(target api>=26)：
* 后台应用对于后台服务的访问将不再像之前那么能自由访问。
* 大部分广播无法静态注册(即manifest注册的将不再生效)。
* startService()会抛异常IllegalStateException，如果用户关闭了服务的创建设置。
* 使用Context.startForegroundService()启动前台服务。如果需要调用startForeground()则必须在服务创建后的5秒钟以内。

### 2）后台定位限制
对于以下API将不会频繁收到更新：
* Fused Location Provider
* Geofencing
* GNSS Measurements
* Location Manager
* Wi-Fi Manager

### 3)快捷方式
* com.android.launcher.action.INSTALL_SHORTCUT广播不再生效，目前已作为私有广播。需要使用ShortcutManager的requestPinShortcut()创建快捷方式。
* ACTION_CREATE_SHORTCUT intent可以通过ShortcutManager创建快捷方式，同时也支持之前版本laucher icon的创建，之前这个intent只适用于老版本的laucher icon创建。
* 通过requestPinShortcut()方式创建的快捷方式目前可以使用ShortcutManager更新。
* 之前的快捷方式仍维持现有功能，但是新版本可以将其手动转换成app shortcut。

### 4）Locales
Local.getDefault()需要替换成Locale.getDefault(Category.DISPLAY)。应用于方法Currency.getDisplayName(),Currency.getSymbol(),
Locale.getDispalyScript()。
Currency.getDisplayName(null)会抛NullPointerException异常。

### 5）警告弹窗
TYPE_PHONE,TYPE_PRIORITY_PHONE,TYPE_SYSTEM_ALERT,TYPE_SYSTEM_OVERLAY,TYPE_SYSTEM_ERROR类型弹窗会始终位于TYPE_APPLICATION_OVERLAY类型下方，如果target api>=27，请使用TYPE_APPLICATION_OVERLAY类型弹窗。

### 6）输入和导航
作为外设的键盘现在支持app内导航，比如通过箭头或者tab键。如果View未设置高亮状态，系统会自动在获取焦点时加水纹效果的高亮显示，当然如果不需要也可以通过xml设置，如果要测试app对键盘导航的支持，可以通过设置的Drawing>Show layout bounds查看，对于处在焦点的控件会显示"X"标记。

### 7）网页的自动填充功能
WebSettings的getSaveFormData()现在返回fasle，之前返回true，同时setSaveFormData()不再生效。
WebViewDatabase的clearFormData()不再生效，hasFormData()返回fasle，之前有表单数据的情况下会返回true。

### 8）网络
* 不带body的OPTIONS请求里面现在会有个Content-Length:0的头字段。之前没有此字段。
* HttpURLConnection会自动格式化URL，将空的路径以/代替，例如http://example.com 格式化成http://exaple.com/。
* URL不可以包含空标签(empty labels，之前版本hostname是可以带的，但是这种形式的URL是不规范的，所以8.0以后不再允许这样的URL)
* HttpsURLConnection不再执行非安全的TLS/SSL协议。

### 9）WIFI连接
稳定性和可靠性提供，更直观更容易理解的WIFI设置UI，单独的WIFI设置菜单，兼容设备会自动连接附近质量更高的热点。

### 10）安全
* 不再支持SSLv3
* 当服务器没有正确完善TLS协议的情况下，HttpsURLConnection不再执行连接建立。
* WebView可以在多个多个进程工作了。
对于未知来源过来的未知app而言，增加了安全性。之前的INSTALL_NON_MARKET_APPS目前始终为1，可以通过canReqeustPackageInstalls()方法来判断是否可安装。如果想通过setSecureSettings()来改变INSTALL_NON_MARKET_APPS的值会抛异常。

### 11）findViewById()
此方法不再返回View实例，返回的是<T extends View>，也就是说不再需要强转了。。。。。（好api!）。
 
### 12）通讯录
对于TIMES_CONTACTED，TIMES_USED,LAST_TIME_CONTACTED,LAST_TIME_USED信息，系统不再返回精确数据，只是给一个近似数据，精确数据由系统内部维护。

## 新特性

### 1)画中画模式
<img src="https://developer.android.com/images/pip.gif" height="495" width="250"/>

 Activity未销毁 | Activity销毁后
---- | ---
<img src="https://github.com/chenjishi/TechBlog/blob/master/android7/pip2.png" height="640" width="360"/> | <img src="https://github.com/chenjishi/TechBlog/blob/master/android7/pip3.png" height="640" width="360"/>

横屏状态
<p>
<img src="https://github.com/chenjishi/TechBlog/blob/master/android7/pip4.png" height="360" width="640"/>
</p>

PIP模式为多窗口模式的一种特例，之前应用于Android TV系统上，API26将其引入手机平台，主要应用于视频的播放(其他场景比如Google Maps的导航小窗口，用户浏览其他app时候，导航小窗口可以一直显示)，系统默认在屏幕右下角显示视频的小窗口，可以调节窗口比例，用户可以随意拖动小窗口到任意位置，这个小窗口位于最顶层，所有在小窗口发生的触摸事件均不会向下面的Activity传导，用户触摸小窗口默认会显示一个“放大”和“关闭”的按钮。

**用法：**

PIP默认不开启，需要在manifest注册
```
android:supportsPictureInPicture="true"
```

进入画中画模式
```
getActivity().enterPictureInPictureMode()
```

通过PictureInPitureParams进行PIP的调整，比如视频比例的更新，用户点击home或者recent按钮，或者进入、退出PIP模式Activity或者Fragment都会有回调，通知用户更新UI。处于PIP模式的时候系统会调用Activity的onPause方法，此时不应该暂停视频播放，真正暂停或者恢复播放的时机应该是在onStop和onStart方法里面，如果非要在onPause的时候暂停，需要调用isInPictureInPictureMode方法判断当前是否在PIP模式。

### 2)Notification Channel
* 可以单独设置Notification Channel，以channel id识别，每一个channel可以设置不同的行为模式，比如震动模式，声音，闪光灯颜色等等。还可以根据category设置不同channel notification的样式，并且每一个channel都可以让用户单独进入系统设置进行行为的设置，应用场景比如说聊天app，每个人的会话通知可以设置为同一个channel id。

* Notification dots，桌面图标支持通过app icon显示小圆点来显示是否有通知，长按app icon可以预览通知，支持文件夹显示通知小圆点，支持预览时通知数量的自定义，一般用于显示很重要的通知（不重要的通知显示小圆点无意义）。

<img src="https://developer.android.com/images/ui/notifications/badges-3.png" width="396" height="355"/>

* 可以设置通知超时时间，到期自动消失，在此之前开发者也可以消失通知。

* 支持通知栏背景颜色设置，比如一些紧急通知可以设置颜色与其他普通通知区分开来。

### 3)Auto Fill

<img src="https://raw.githubusercontent.com/googlesamples/android-AutofillFramework/master/screenshots/2_SampleLoginEditTexts.png" width="270" height="480"/>

系统级服务，表单自动填写，节省时间，减少输入错误。
* 支持重要性级别标识，比如验证码之类的可以标记为不自动填写，其他的用户名或者信用卡号等等还可以通过标记字段让系统自动区分。
* 支持自定义View接入Auto Fill服务。
* 支持网站数据和app数据联通。

### 4)Downloadable fonts
可下载字体，不需要将字体打包进apk，同时还可以实现多app共享字体，Android Support Library 26也支持此特性。
<img src="https://developer.android.com/guide/topics/ui/images/look-and-feel/downloadable-fonts/downloadable-fonts-process.png" width="371" height="355"/>

xml可以通过android:fontFamily设置字体。

### 5)Adaptive Icons

可以对不同的设备设置不同的图标样式，比如有的机型可以设置成圆形图标，有的可以设置方形图标，并且支持图标动画，支持快捷方式。

<img src="https://developer.android.com/guide/practices/ui_guidelines/images/Single_Icon_Parallax_Demo_01_2x_ext.gif"
width="311" height="311"/>

### 6)快捷方式
Android7.0后分为三种快捷方式：
* static shortcuts,跟apk打包在一起，无法修改，除非升级。

* dynamic shortcuts，可以在运行时更新、删除，数量有限制，static和dynamic的快捷方式最多五个。

* pinned shortcuts(8.0)运行时添加，无数量限制，创建时需要用户手动确认。

<img src="https://developer.android.com/images/guide/topics/ui/shortcuts/pinned-shortcuts.png" width="421" height="355"/>

### 7)其他
* 多显示器支持，如果Activity支持Multi-window模式则默认支持多显示器。
* xml引入layout_marginVertical，layout_marginHorizontal支持同时设置两边的margin,padding同理。
* 支持app类别的设置，便于用户对app进行类别管理分组。
* AnimatorSet支持seeking和reverse，即动画可以定位到任意时间位置、可以反向播放。
* 智能分享，app分享时，系统根据内容（图片，音频，链接等等）寻找合适的app处理分享的内容。
* Wi-Fi Aware，对有此硬件的手机，无需有网络连接即可进行通信。


# Android架构概览

## 1.MVC
<p align="center">
  <img src="https://github.com/chenjishi/TechBlog/blob/master/mvc.png?raw=true" 
  alt="usite" height="369" width="960"/>
</p>

MVC(Model-View-Controller)是一种自70年代以来就出现的为响应式UI设计的架构，Android系统其实是一个非标准的MVC模式，更准确地来说应该是Model-View的架构。

* Model:实体模型，数据的获取、存储、状态变化维护。
* View:UI视图，Android View
* Controller:对应于Activity和Fragment，处理数据、业务和UI。

这种分层架构本身没有太大的问题，但是随着业务不断迭代，我们慢慢发现了很多问题：

* 由于Android View作为视图的功能太弱，我们大量处理View的逻辑只能写在Activity, Activity充当了Controller和View的角色，里面导致Activity变得臃肿和难以维护，直接导致Activity大爆炸，比如说我们的BrowserActivity和设置Activity，一个类成千上万行代码的情况出现了。
* 耦合太紧密，业务代码跟UI糅杂在一起，无法拆分重用，导致单元测试在Android平台上根本无法进行，同时逻辑不清晰，难以维护。

## 2.MVP
<p align="center">
  <img src="https://github.com/chenjishi/TechBlog/blob/master/mvp.png?raw=true" 
  alt="usite" height="562" width="960"/>
</p>

MVP(Model-View_Presenter)，通过一个抽象的View接口，不是真正的View层将Presenter和真正的View进行解耦，Presenter持有View接口，对接口进行操作，而不是直接操作View层，View层也可以持有Presenter接口，对Presenter发送命令，比如更新数据等等。这样把视图操作和业务逻辑解耦，从而让Activity成为真正的View层。
* Model:负责检索、存储、操作数据，包括来自网络、数据库、磁盘文件和SharedPreferences的数据。
* View:负责UI的展现和操作，可以是Activity，Fragment，View。
* Presenter:负责业务和数据的处理，作为View和Model的桥梁，从Model层获取数据然后调用View接口去控制View。

### 优点：
* View层和数据层解耦，实现了真正的Model和View真正的完全分离,可以修改View而不影响Model。
* Presenter可以用于多个View，实现复用的功能。
* View可以进行组件化，View不依赖Model，对业务一无所知，它只需要提供一系列接口供上层操作即可。
* 模块职责划分明显，层次清晰。
* 利于单元测试（单元测试用例参见demo的mock操作）。
* 利用团队快速切入，有明确的规范可用。

### 缺点：
* 对于比较复杂的业务，Presenter模式无法解决臃肿的问题。
* 对于多个View的场景，需要多个Presenter去实现。
* 额外的代码复杂度和学习成本。
* View层和Presenter层还有一定的耦合度，View层某个UI元素更，那么对于的接口也必须改。

[MVP_Demo](https://github.com/googlesamples/android-architecture/tree/todo-mvp/)



## 3.MVP-Clean
<p align="center">
  <img src="https://github.com/chenjishi/TechBlog/blob/master/clean_architecture.png?raw=true" 
  alt="usite" height="440" width="647"/>
</p>
<p align="center">
  <img src="https://github.com/chenjishi/TechBlog/blob/master/clean.png?raw=true" 
  alt="usite" height="476" width="960"/>
</p>
这种架构是对MVP的一种补充，主要区别是加了一层Domain Layer和User Cases，把原本Presenter中臃肿的逻辑代码移入到了Domain Layer中，减轻了Presenter的体量，而use cases定义了每个业务的具体操作，细化了业务粒度。


[MVP-Clean_Demo](https://github.com/googlesamples/android-architecture/tree/todo-mvp-clean/)



## 4.MVVM
<p align="center">
  <img src="https://github.com/chenjishi/TechBlog/blob/master/mvvm-databinding.png?raw=true" 
  alt="usite" height="324" width="761"/>
</p>
MVVM(Model-View-ViewModel)思想是跟MVP类似，但是是为了更进一步解耦引入的框架。这个框架引入了Google的Databind框架，结合使用。
* Model:数据模型，包括web service和local repository。
* View:对应于Activity，Fragment,View。
* ViewModel:负责完成View和Model交互。

### 优点：
* 常规开发模式中，数据变化需要更新UI的时候，先要获取UI控件的引用，然后再更新UI，获取用户输入和操作也需要UI控件的引用，mvvm中是通过数据驱动自动完成的，数据变化后自动更新UI，UI的改变也反馈到数据层，databing是双向的，数据成为主导因素，业务逻辑处理的时候只关心数据变化，不需要直接跟UI打交道。
* 耦合度更低，数据是独立于UI的，数据和业务逻辑位于ViewModel中，ViewModel只需要关注数据和业务逻辑，不需要和UI交互，不涉及任何UI相关的事情，不持有UI的引用，解决了MVP的问题。
* 利用团队协作，一个写UI，一个写业务。
 ### 缺点：
 * 学习成本略高。
 
 
 [MVVM-Demo](https://github.com/googlesamples/android-architecture/tree/todo-mvvm-databinding/)



