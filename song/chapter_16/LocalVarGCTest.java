/**
 * -XX:+PrintGCDetails
 */
public class LocalVarGCTest {

    public void fun1() {
        byte[] buffer = new byte[10 * 1024 * 1024]; // 10M
        System.gc(); // 不能回收
    }

    public void fun2() {
        byte[] buffer = new byte[10 * 1024 * 1024]; // 10M
        buffer = null;
        System.gc(); // 可以回收
    }

    public void fun3() {
        {
            byte[] buffer = new byte[10 * 1024 * 1024]; // 10M
        }
        System.gc(); // 不能回收，局部变量表中仍然有引用
    }

    public void fun4() {
        {
            byte[] buffer = new byte[10 * 1024 * 1024]; // 10M
        }
        int value = 10;
        System.gc(); // 可以回收，局部变量表中 value 和 buffer 使用同一个 Slot
    }

    public void fun5() {
        fun1();
        System.gc(); // 可以回收
    }

    public static void main(String[] args) {
        LocalVarGCTest local = new LocalVarGCTest();
        local.fun5();
    }
}
