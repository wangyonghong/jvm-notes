public class StringInternTest {
    public static void main(String[] args) {
        String s1 = new String("1");
        s1.intern(); // 调用此方法前，字符串常量池中已经存在了
        String s2 = "1";
        System.out.println(s1 == s2); // JDK 6 false ; JDK 7/8 false

        String s3 = new String("1") + new String("1"); // s3 记录的地址为 new String("11")
        // 执行完上一行代码后，没有
        s3.intern(); // 在字符串常量池中生成 "11"，但是常量池中已经有 "11" 了
        String s4 = "11";
        System.out.println(s3 == s4); // JDK 6 false ; JDK 7/8 true
    }
}
