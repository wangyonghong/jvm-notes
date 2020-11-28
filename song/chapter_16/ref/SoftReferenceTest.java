package ref;

import java.lang.ref.SoftReference;

/**
 * -Xms=10m -Xmx=10m -XX:+PrintGCDetails
 */
public class SoftReferenceTest {

    public static void main(String[] args) {
        SoftReference<User> sr = new SoftReference<>(new User(1, "song"));

        System.out.println("Before GC");
        System.out.println(sr.get());

        System.gc();
        System.out.println("After GC");
        System.out.println(sr.get());

        try {
            // 让系统资源认为资源紧张
            byte[] buffer = new byte[(int) (1024 * 6830)];
//            byte[] buffer = new byte[1024 * 1024 * 7];
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("After Trying To Allocate Big Byte Array");
            System.out.println(sr.get()); // OOM 之前，垃圾回收器会回收软引用的可达对象。
        }
    }

    public static class User {

        public int id;
        public String name;

        public User(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
