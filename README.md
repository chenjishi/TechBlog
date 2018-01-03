# Android7.0/8.0系统新特性介绍

## 7.0


## 8.0

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

<img src="https://developer.android.com/images/ui/notifications/badges-3.png" width="396" height="355">

* 可以设置通知超时时间，到期自动消失，在此之前开发者也可以消失通知。

* 支持通知栏背景颜色设置，比如一些紧急通知可以设置颜色与其他普通通知区分开来。

### 2)Auto Fill

<img src="https://raw.githubusercontent.com/googlesamples/android-AutofillFramework/master/screenshots/2_SampleLoginEditTexts.png" width="270" height="480">

系统级服务，表单自动填写，节省时间，减少输入错误。
* 支持重要性级别标识，比如验证码之类的可以标记为不自动填写，其他的用户名或者信用卡号等等还可以通过标记字段让系统自动区分。
* 支持自定义View接入Auto Fill服务。
* 支持网站数据和app数据联通。

### 2)Downloadable fonts
可下载字体，不需要将字体打包进apk，同时还可以实现多app共享字体，Android Support Library 26也支持此特性。
<img src="https://developer.android.com/guide/topics/ui/images/look-and-feel/downloadable-fonts/downloadable-fonts-process.png" width="371" height="355">





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



