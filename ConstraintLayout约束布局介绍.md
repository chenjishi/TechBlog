# ConstraintLayout约束布局

ConstraintLayout，约束布局，由android support包提供(com.android.support.constraint)，最低兼容至Android API 9(2.3)，为大型和复杂的布局提供扁平化结构解决方案(内部无嵌套)，类似于RelativeLayout，根据父元素或者相邻元素的位置关系来确定位置，但是比RelativeLayout更灵活更强大！

如果做过iOS开发会发现这个布局的思路跟iOS的Autolayout很相似，都是通过定义约束条件constraints来实现元素的定位，而且IntelliJ IDEA和Android Studio在新建布局xml的时候会默认将根布局设置为ConstraintLayout，可见这种布局提供了更高效、更扁平的布局模式。这篇文章我们主要从以下几个方面来介绍
ConstraintLayout的使用。

## * 相对定位

提供两个方向的相对定位
* 水平方向：left, right, start, end
* 竖直方向：top, bottom, text baseline

元素位置关系模型：

<div align='center'><img src='https://developer.android.com/reference/android/support/constraint/resources/images/relative-positioning-constraints.png' width='350' height='90'/></div>

假如我们有两个元素A，B，需要让B位于A的右边，如下图所示：

<div align='center'><img src='https://developer.android.com/reference/android/support/constraint/resources/images/relative-positioning.png' width='300' height='148'/></div>

我们可以这么写：


```
<Button android:id="@+id/buttonA" />
<Button android:id="@+id/buttonB"
        app:layout_constraintLeft_toRightOf="@+id/buttonA" />

```
layout_constraintLeft_toRightOf让B元素的左边界跟A元素的右边界对齐，从而实现B位于A右边，约束规则的第一个left代表当前元素，第二个right代表作为参考的元素，值可以是参考元素的id或者父元素，如果是父元素的话那么值就是"parent",约束规则会让两个元素的相应边界位于同一个位置，如上图中A的右边和B的左边位于同一位置。布局可用的定位属性如下：

* layout_constraintLeft_toLeftOf
* layout_constraintLeft_toRightOf
* layout_constraintRight_toLeftOf
* layout_constraintRight_toRightOf
* layout_constraintTop_toTopOf
* layout_constraintTop_toBottomOf
* layout_constraintBottom_toTopOf
* layout_constraintBottom_toBottomOf
* layout_constraintBaseline_toBaselineOf
* layout_constraintStart_toEndOf
* layout_constraintStart_toStartOf
* layout_constraintEnd_toStartOf
* layout_constraintEnd_toEndOf

## * Margins

<div align='center'><img src='https://developer.android.com/reference/android/support/constraint/resources/images/relative-positioning-margin.png' width='325' height='96'/></div>

可用的margin属性：
* android:layout_marginStart
* android:layout_marginEnd
* android:layout_marginLeft
* android:layout_marginTop
* android:layout_marginRight
* android:layout_marginBottom

这里需要注意的是margin只支持正数或者0，不支持负数,关于这个属性有个很重要的特性就是支持元素为GONE的情况下依然保持位置不变，如下图：
<div align='center'><img src='https://developer.android.com/reference/android/support/constraint/resources/images/visibility-behavior.png' width='350' height='170'/></div>


A被设置为GONE,B在重新布局的时候会往左偏移，占据A的位置，ConstraintLayout有gone margin属性，当A为GONE的时候，我们可以设置layout_goneMarginLeft从而保持B的位置不变，需要注意的是A为GONE的时候，A的宽度变为0，margin变为0，同时B相对于A的margin也变为0。如果A的宽度为64，marginLeft为16，B的marginLeft为16，那么B的layout_goneMarginLeft=64+16+16，这样才可以保持A为GONE的时候B位置不变。可用的gone margin属性有：

* layout_goneMarginStart
* layout_goneMarginEnd
* layout_goneMarginLeft
* layout_goneMarginTop
* layout_goneMarginRight
* layout_goneMarginBottom








