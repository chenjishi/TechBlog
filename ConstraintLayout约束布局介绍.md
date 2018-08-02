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


## * 居中和偏移

居中：

```
<android.support.constraint.ConstraintLayout>
             <Button android:id="@+id/button"
                 app:layout_constraintLeft_toLeftOf="parent"
                 app:layout_constraintRight_toRightOf="parent/>
</>

```

效果如下：

<div align='center'><img src='https://developer.android.com/reference/android/support/constraint/resources/images/centering-positioning.png' width='325' height='75'/></div>

除非ConstraintLayout跟里面的Button尺寸一样，默认情况下Button都是居中的，两边各50%，可以想象Button两边各有一个弹簧在拉扯B，势均力敌，让B能一直保持在中间，竖直方向同理。

当然很多情况下，我们并不总是希望元素居中，这时候偏移属性就派上用场了，有两个偏移属性，分为水平和竖直方向：

* layout_constraintHorizontal_bias
* layout_constraintVertical_bias

```
<android.support.constraint.ConstraintLayout>
             <Button android:id="@+id/button"
                 app:layout_constraintHorizontal_bias="0.3"
                 app:layout_constraintLeft_toLeftOf="parent"
                 app:layout_constraintRight_toRightOf="parent/>
</>
```
<div align='center'><img src='https://developer.android.com/reference/android/support/constraint/resources/images/centering-positioning-bias.png' width='325' height='75'/></div>
这个布局代表左边占30%右边占70%


## * 环形定位

<div align='center'><img src='https://developer.android.com/reference/android/support/constraint/resources/images/circle1.png' width='325' height='325'/></div>

这种方式可以是一个元素绕着另一个元素呈圆形环绕，主要是通过半径和角度来确定俩元素的位置关系，有三个属性：

* layout_constraintCircle : 参考元素的id
* layout_constraintCircleRadius : 距离另一元素中心的距离
* layout_constraintCircleAngle : 与另一元素的偏移角度(度数，0~360)

```
<Button android:id="@+id/buttonA"/>
<Button android:id="@+id/buttonB"
        app:layout_constraintCircle="@+id/buttonA"
        app:layout_constraintCircleRadius="100dp"
        app:layout_constraintCircleAngle="45" />
```

实现效果如下：

<div align='center'><img src='https://developer.android.com/reference/android/support/constraint/resources/images/circle2.png' width='325' height='325'/></div>


## * 尺寸约束

在ConstraintLayout尺寸为WRAP_CONTENT的情况下， 我们可以定义ConstraintLayout的最大和最小尺寸，共有四个：

* android:minWidth
* android:minHeight
* android:maxWidth
* android:maxHeight

对于ConstraintLayout里面的元素来说，有三种方式设置尺寸，定值(123dp)，WRAP_CONTENT， 0dp(跟MATCH_CONSTRAINT一样的效果)。

注意：不推荐使用MATCH_PARENT，而应该使用MATCH_CONSTRAINT结合"parent"的left/right/top/bottom位置。

ConstraintLayout的WRAP_CONTENT有个需要注意的地方，其他的布局如果设置了WRAP_CONTENT，意味着元素可以想多大就多大，超过父元素也可以，但是有些情况下我们需要对WRAP_CONTENT进行约束，让其不超过ConstraintLayout的大小，可以进行如下属性设置：
* app:layout_constrainedWidth=”true|false”
* app:layout_constrainedHeight=”true|false”

如果元素尺寸设置为MATCH_CONSTRAINT，则意味着元素会占据整个布局所有可用的空间，但是可以对这个元素进行最大最小尺寸的限定：

* layout_constraintWidth_min/layout_constraintHeight_min:最小宽高限定
* layout_constraintWidth_max/layout_constraintHeight_max:最大宽高限定
* layout_constraintWidth_percent/layout_constraintHeight_percent:根据比例进行宽高限定

根据比例设定宽高的时候，width或者height必须为MATCH_CONSTRAINT并且需要设置app:layout_constraintWidth_default="percent"或者 app:layout_constraintHeight_default="percent"，同时layout_constraintWidth_percent/layout_constraintHeight_percent的值应该为0~1之间。

## * 比例

可以让宽根据高的比例来确定或者让高根据宽的比例来确定，如果使用这种模式必须保证宽/高至少有一个为0dp(MATCH_CONSTRAINT)，并且设定layout_constraintDimensionRatio的值为两者的比例，值的类型可以为float型，代表width和height的比例，或者以width:height的形式：

```
<Button android:layout_width="wrap_content"
        android:layout_height="0dp"
        app:layout_constraintDimensionRatio="1:1" />
```
这个会让宽高大小一样。

当然如果width和height都为0dp(MATCH_CONSTRAINT)，同样可以通过比例来确定width和height，系统会选取最大尺寸的一边作为约束，从而使另一边根据比例进行约束，如果你想人为限定width或者height，可以在比例前加W或者H的前缀来限定宽或者高，以逗号跟后面的比例隔开：

```
<Button android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintDimensionRatio="H,16:9"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>
```

这种情况会让width占据整个ConstraintLayout的宽度，高度根据16：9的比例来确定。


## * 链式布局

一个很重要的概念，应用在一组元素上面。链可以水平方向布局也可以竖直方向布局。元素之间通过双向连接实现链式布局。

<div align='center'><img src='https://developer.android.com/reference/android/support/constraint/resources/images/chains.png' width='325' height='90'/></div>
















