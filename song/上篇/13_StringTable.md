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

3.**只要其中有一个是变量，结果就在堆中。变量拼接的原理是 StringBuilder** （特例，如果都是字符串常量引用，比如 final，使用编译期优化）

4.如果拼接的结果调用 intern() 方法，则主动将常量池中还没有的字符串对象放入池中，并返回此对象地址。

5.使用 StringBuilder 的 append() 的方式（只创建一个 StringBuilder）添加字符串的效率要远高于使用 String 的字符串拼接方式（每次创建一个 StringBuilder）。如果可以确定字符串长度不高于某个值，可以使用 new  StringBuilder(size) 优化。

```java
public void test() {
    String s1 = "a";
    String s2 = "b";
    String s3 = "ab";
    String s4 = s1 + s2;
    /*
    s1 + s2 的执行细节
    StringBuilder s = new StringBuilder();
    s.append("a");
    s.append("b");
    s.toString();   ---> 约等于 new String("ab");
    在 JDK 5.0 之前使用的是 StringBuffer，在 JDK 5.0 之后使用的是 StringBuilder
    */
    System.out.println(s3 == s4); // false
}
```



## 5 - intern() 的使用

如果不使用双引号声明的 String 对象，可以使用 String 提供 的 intern 方法：intern 方法会从字符串常量池中查询当前字符串是否存在，若不存在就会将当前字符串放入常量池中。

比如：String myInfo = new String("abc").intern();

也就是说，如果在任意字符串上调用 String.intern 方法，那么其返回结果所指向的那个类实例，必须和直接以常理形式出现的字符串实例完全相同。因此，下列表达式的值必定是 true

("a" + "b" + "c").intern == "abc"

通俗地讲，Interned String 就是确保字符串在内存里只有一份拷贝，这样可以节约内存空间，加快字符串操作任务的执行速度。注意，这个值会被存放在字符串常量池中。

### new String("ab") 会创建几个对象？

看字节码是两个对象。

按顺序出现：第一个是 new 关键字创建的对象，第二个是字符串常量池中的对象。

```java
public class StringNewTest {

    public static void main(String[] args) {
        String s = new String("ab");
    }
}

/*
 0 new #2 <java/lang/String> // new String() 操作
 3 dup
 4 ldc #3 <ab>  // 从常量池中取出 ab，所以常量池里也有一个
 6 invokespecial #4 <java/lang/String.<init>>
 9 astore_1
10 return
*/
```



### new String("a") + new String("b") 会创建几个对象呢？

1 个 StringBuilder 对象，5 个 String 对象（常量池两个，new 关键字在堆中三个），按照如下顺序出现：

- ①new StringBuilder 对象
- ②new String 对象在堆中申请空间，此时还未初始化完成。
- ③加载 "a" 到字符串常量池中
- ②的 String 对象执行 init 方法，初始化完成。
- ④new String 对象在堆中申请空间，此时还未初始化完成。
- ⑤加载 "b" 到字符串常量池中
- ④的 String 对象执行 init 方法，初始化完成。
- ⑥StringBuilder 的 toString 方法，此时并没有放入字符串常量池

```java
 public class StringNewTest {

    public static void main(String[] args) {
        String s = new String("a") + new String("b");
    }
}

 
 /*
 0 new #2 <java/lang/StringBuilder>
 3 dup
 4 invokespecial #3 <java/lang/StringBuilder.<init>>
 7 new #4 <java/lang/String>
10 dup
11 ldc #5 <a>
13 invokespecial #6 <java/lang/String.<init>>
16 invokevirtual #7 <java/lang/StringBuilder.append>
19 new #4 <java/lang/String>
22 dup
23 ldc #8 <b>
25 invokespecial #6 <java/lang/String.<init>>
28 invokevirtual #7 <java/lang/StringBuilder.append>
31 invokevirtual #9 <java/lang/StringBuilder.toString>
34 astore_1
35 return
*/
```

### StringBuilder 的 toString 方法

toString() 方法不会在常量池中生成字符串。

### intern 在 JDK 6 / JDK 7、8 中的不同

对象按如下顺序出现：

- ①new String 对象在堆中申请空间，此时还未初始化完成。
- ②加载 "1" 到字符串常量池中
- ①的 String 对象执行 init 方法，初始化完成，地址给 s1
- ②字符串常量池中的 "1" 对象地址给 s2
- 
- ③new StringBuilder 对象
- ④new String 对象在堆中申请空间，此时还未初始化完成。
- ②加载 "1" 到字符串常量池中，已经加载过了
- ④的 String 对象执行 init 方法，初始化完成
- ③④ StringBuilder append
- ⑤new String 对象在堆中申请空间，此时还未初始化完成。
- ②加载 "1" 到字符串常量池中，已经加载过了
- ⑤的 String 对象执行 init 方法，初始化完成
- ③⑤ StringBuilder append
- ⑥StringBuilder toString 方法，在堆空间中创建 "11" 对象，地址给 s3
- ⑦s3.intern 此处就不一样了，JDK 6 中，是复制一份到字符串常量池，JDK 7/8 中直接使用 s3 的地址，即和⑥是一样的
- ⑦字符串常量池中的 "11" 赋值给 s4

```java
public class StringInternTest {
    public static void main(String[] args) {
        String s1 = new String("1");
        s1.intern();
        String s2 = "1";
        System.out.println(s1 == s2); // JDK 6 false ; JDK 7/8 false

        String s3 = new String("1") + new String("1");
        s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4); // JDK 6 false ; JDK 7/8 true
    }
}
```

变形1

```java
String s3 = new String("1") + new String("1");
// 此时字符串常量池中没有 "11"
String s4 = "11"; // 在字符串常量池中生成对象 "11"
String s5 = s3.intern();
System.out.println(s3 == s4); // false
System.out.println(s5 == s4); // true
```

变形2

```java
String s1 = new String("a") + new String("b");
String s2 = s1.intern(); // 在 JDK 6 中：在串池中创建一个字符串 "ab" ；在 JDK 7/8 中串池中没有创建字符串 "ab"，而是创建了一个引用，指向StringBuilder toString 方法中的 new String("ab")
System.out.println(s1 == "ab"); // JDK 6 false  ;  JDK 7/8  true
System.out.println(s2 == "ab"); // JDK 6 true   ;  JDK 7/8  true
```



总结：

JDK 1.6 中，将这个字符串对象尝试放入字符串池中

- 如果字符串池中有，则并不会放入。返回已有的字符串常量池中的对象的地址。
- 如果没有，**会把此对象复制一份**，放入字符串常量池，并返回池中的对象地址。

JDK 1.7 起，将这个字符串对象尝试放入字符串池中

- 如果字符串池中有，则并不会放入。返回已有的字符串常量池中的对象的地址。
- 如果没有，则会**把对象的引用地址复制一份**，放入字符串池中，并返回池中的引用地址

### 空间效率

对于程序中大量存在的字符串，尤其其中存在很多重复字符串时，使用 intern 方法可以节省内存空间，提升效率。

大的网站平台，需要内存中存储大量的字符串。比如社交网站，很多都存储：北京市、海淀区等信息。这个时候使用 intern 方法，就会明显降低内存的大小。



## 6 - StringTable 的垃圾回收

`-XX:+PrintStringTableStatistics` 

和其他对象回收类似



## 7 - G1 中的 String 去重操作

背景：对许多 Java 应用（有大的也有小的）做的测试得出以下结果：

- 堆存活数据集合里面 String 对象占了 25%
- 堆存活数据集合里面重复的 String 对象有 13.5%
- String 对象的平均长度是 45

许多大规模的 Java 应用的瓶颈在于内存，测试表明，在这些类型的应用里面，Java 堆中存活的数据集合差不多 25% 是 String 对象。更进一步，这里面差不多一半 String 对象是重复的，重复的意思是说：string1.equals(string2) == true。堆上存在重复的 String 对象必然是一种内存的浪费。这个项目将在 G1 垃圾收集器中实现自动持续对重复的 String 对象进行去重，这样就能避免浪费内存。

实现：

- 当垃圾收集器工作的时候，会访问堆上存活的对象。对每一个访问的对象都会检查是否是候选的要去重的 String 对象。
- 如果是，把这个对象的一个引用插入到队列中等待后续的处理。一个去重的线程在后台运行，处理这个队列。处理队列的一个元素意味着从队列删除这个元素，然后尝试去重它引用的 String 对象。
- 使用一个 HashTable 来记录所有的被 String 对象使用的不重复的 char 数组，当去重的时候，会查这个 HashTable，来看堆上是否已经存在一个一模一样的 char 数组。
- 如果存在，String 对象会被调整引用那个数组，释放对原来的数组的引用，最终会被垃圾收集器回收掉。
- 如果查找失败，char 数组会被插入到 HashTable 中，这样以后的时候就可以共享这个数组了。



命令行选项：

- UserStringDeduplication(bool)：开启 String 去重，默认是不开启的，需要手动开启。
- PrintStringDeduplicationStatistics(bool)：打印详细的去重统计信息
- StringDeduplicationAgeThreshold(uintx)：达到这个年龄的 String 对象被认为是去重的候选对象。



