public class SystemGCTest {

    public static void main(String[] args) {
        new SystemGCTest();
        System.gc(); // 提醒 JVM 的垃圾回收器执行 GC

        System.runFinalization(); // 强制调用未被使用对象的 finalize 方法
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("调用了重写的 finalize 方法");
    }
}
