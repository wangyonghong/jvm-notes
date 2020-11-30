package ref;

public class StrongReferenceTest {
    public static void main(String[] args) {
        StringBuffer str1 = new StringBuffer("Strong Reference");
        StringBuffer str2 = str1;

        str1 = null;
        System.gc();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(str2);
    }
}
