package java.lang;

public class SecurityTest {

    /**
     * Error: A JNI error has occurred, please check your installation and try again
     * Exception in thread "main" java.lang.SecurityException: Prohibited package name: java.lang
     */
    public static void main(String[] args) {
        System.out.println("security");
    }
}
