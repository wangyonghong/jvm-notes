package ref;

import java.lang.ref.WeakReference;

public class WeekReferenceTest {

    public static void main(String[] args) {
        WeakReference<User> wr = new WeakReference<>(new User(1, "song"));

        System.out.println("Before GC");
        System.out.println(wr.get());

        System.gc();
        System.out.println("After GC");
        System.out.println(wr.get());
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
