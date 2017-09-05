# Android结构概览

## MVC
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

## MVP
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

### 缺点：
* 对于比较复杂的业务，Presenter模式无法解决臃肿的问题。
* 对于多个View的场景，需要多个Presenter去实现。
* 额外的代码复杂度和学习成本。
* View层和Presenter层还有一定的耦合度，View层某个UI元素更，那么对于的接口也必须改。


## MVP-Clean
<p align="center">
  <img src="https://github.com/chenjishi/TechBlog/blob/master/clean_architecture.png?raw=true" 
  alt="usite" height="440" width="647"/>
</p>
<p align="center">
  <img src="https://github.com/chenjishi/TechBlog/blob/master/clean.png?raw=true" 
  alt="usite" height="476" width="960"/>
</p>
这种架构是对MVP的一种补充，主要区别是加了一层Domain Layer和User Cases，把原本Presenter中臃肿的逻辑代码移入到了Domain Layer中，减轻了Presenter的体量，而use cases定义了每个业务的具体操作，细化了业务粒度。


## MVVM
<p align="center">
  <img src="https://github.com/chenjishi/TechBlog/blob/master/mvvm-databinding.png?raw=true" 
  alt="usite" height="324" width="761"/>
</p>



