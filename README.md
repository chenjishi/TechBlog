# Android7.0/8.0系统新特性介绍

## 7.0


## 8.0

### 1)画中画模式
<img src="https://developer.android.com/images/pip.gif" height="495" width="250"/>
<img src="https://github.com/chenjishi/TechBlog/blob/master/android7/pip2.png" height="640" width="360"/> | <img src="https://github.com/chenjishi/TechBlog/blob/master/android7/pip3.png" height="640" width="360"/>

底下是Activity | 横屏模式

name | age
---- | ---
<img src="https://github.com/chenjishi/TechBlog/blob/master/android7/pip2.png" height="640" width="360"/> | <img src="https://github.com/chenjishi/TechBlog/blob/master/android7/pip3.png" height="640" width="360"/>
Mike |  32

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



