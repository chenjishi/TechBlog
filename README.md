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
* 业务代码跟UI糅杂在一起，无法拆分重用，导致单元测试在Android平台上根本无法进行，同时逻辑不清晰，难以维护。

## MVP
<p align="center">
  <img src="https://github.com/chenjishi/TechBlog/blob/master/mvp.png?raw=true" 
  alt="usite" height="369" width="960"/>
</p>




