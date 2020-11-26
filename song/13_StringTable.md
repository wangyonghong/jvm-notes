# 13_StringTable

## 1 - String 的基本特性

### String 基本特性

String：字符串，使用一对 "" 引号来表示

String 声明为 final 的，不可被继承

String 实现了 Serializable 接口：表示字符串是支持序列化的

String 实现了 Comparable 接口：表示字符串可以比较大小

String 在 JDK 8 及以前内部定义了 final char[] value 用于存储字符串数据，JDK 9 时改为 byte[]，参考 [JEP 254](https://openjdk.java.net/jeps/254) 

String 代表不可变的字符序列，简称：不可变性。

- 当对字符串重新赋值时，需要重新指定内存区域赋值，不能使用原有的 value 进行赋值；
- 当对现有的字符串进行连接操作时，也需要重新指定内存区域赋值；
- 当调用 String 的replace() 方法修改指定字符或字符串时，也需要重新指定内存区域赋值，不能使用原有的 value 进行赋值。

通过字面量的方式（区别于 new）给一个字符串赋值，此时的字符串值声明在字符串常量池中。

字符串常量池中是不会存储相同内容的字符串的。

- String 的String Pool 是一个固定大小的 HashTable，默认值大小长度是 1009.如果放进 String Pool 的 String 非常多，就会造成 Hash 冲突严重，从而导致链表会很长，而链表长了后会直接造成的影响就是当调用 String.intern() 时性能会大幅下降。
- 使用 `-XX:StringTableSize` 可以设置 StringTable 长度。
- 在 JDK 6 中StringTable 的长度默认值是 1009，StringTableSize 设置没有要求。
- 在 JDK 7 中，StringTable 的长度默认值是 60013，StringTableSize 设置没有要求。
- 在 JDK 8 中，StringTable 长度默认值是 60013，StringTableSize 可以设置的值是 1009。

#### String 存储结构变更

结论：String 再也不用 char[] 来存储了，改成了 byte[] 加上编码标记，节约了一些空间（有些字符，拉丁字符等，由原来的占用两个字节变为占用一个字节）。

那么 StringBuffer 和 StringBuilder 是否仍无动于衷呢？

基于 String 的类包括 AbstractStringBuilder、StringBuilder、StringBuffer、HotSpot VM 内置的字符串也更新为同样的实现方式。

## 2 - String 的内存分配

在 Java 语言中有 8 种基本数据类型和一种比较特殊的类型 String。这些类型为了使它们在运行过程中速度更快、更节省内存，都提供了一种常量池的概念。

常量池就类似一个 Java 系统级别提供的缓存。8 种基本数据类型的常量池都是系统协调的，String 类型的常量池比较特殊。它的主要使用方法有两种。

- 直接使用双引号声明出来的 String 对象会直接存储在常量池中。
- 如果不是用双引号声明的 String 对象，可以使用 String 提供的 intern() 方法。

在 Java 6 及以前，字符串常量池存放在永久代。

在 Java 7 中，Oracle 的工程师对字符串池的逻辑做了很大的改变，即将字符串常量池的位置调整到 Java 堆内。

- 所有的字符串都保存在堆中，和其他普通对象一样，这样可以让你在进行调优应用时仅需要调整堆大小就可以了。
- 字符串常量池的概念原本使用得比较多，但是这个改动使得我们有足够的理由让我们重新考虑在 Java 7 中使用 String.intern() 方法

在 Java 8 中虽然出现了元空间，但字符串常量在堆中。

### StringTable 为什么要调整？

- PermSize 默认比较小
- 永久代垃圾回收频率低



## 3 - String 的基本操作

Java 语言规范里要求完全相同的字符串字面量，应该包含同样的 Unicode 字符序列，并且必须是指向同一个 String 实例。



## 4 - 字符串拼接操作

1.常量与常量的拼接结果在常量池，原理是编译期优化

2.常量池中不会存在相同内容的常量

3.**只要其中有一个是变量，结果就在堆中。变量拼接的原理是 StringBuilder** 

4.如果拼接的结果调用 intern() 方法，则主动将常量池中还没有的字符串对象放入池中，并返回此对象地址。

## 5 - intern() 的使用



## 6 - StringTable 的垃圾回收



## 7 - G1 中的 String 去重操作



