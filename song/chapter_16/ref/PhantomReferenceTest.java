package ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class PhantomReferenceTest {
    public static PhantomReferenceTest obj;
    static ReferenceQueue<PhantomReferenceTest> phantomQueue = null;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("调用 finalize 方法");
        obj = this;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t = new CheckRefQueue();
        t.setDaemon(true); // 设置为守护线程，当程序中没有非守护线程时，守护线程也就执行结束。
        t.start();

        phantomQueue = new ReferenceQueue<>();
        obj = new PhantomReferenceTest();

        PhantomReference<PhantomReferenceTest> phantomRef = new PhantomReference<>(obj, phantomQueue);
        System.out.println(phantomRef.get()); // 不可获取虚引用中的对象

        obj = null;
        System.out.println("第1次 GC");
        System.gc();
        Thread.sleep(1000);
        System.out.println("obj 是否存活：" + (obj != null));

        obj = null;
        System.out.println("第2次 GC");
        System.gc(); // 一旦将 obj 对象回收，就会将此虚引用存放到引用队列中
        Thread.sleep(1000);
        System.out.println("obj 是否存活：" + (obj != null));
    }

    public static class CheckRefQueue extends Thread {
        @Override
        public void run() {
            while (true) {
                if (phantomQueue != null) {
                    PhantomReference<PhantomReferenceTest> obj = null;
                    try {
                        obj = (PhantomReference<PhantomReferenceTest>) phantomQueue.remove();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (obj != null) {
                        System.out.println("追踪垃圾回收过程，PhantomReferenceTest 实例被 GC 了");
                    }
                }
            }
        }
    }
}
