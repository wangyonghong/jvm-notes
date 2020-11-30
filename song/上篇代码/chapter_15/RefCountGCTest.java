/**
 * -XX:PrintGCDetails
 */
public class RefCountGCTest {

    Object obj = null;
    private byte[] bigByte = new byte[10 * 1024 * 1024]; // 10M

    public static void main(String[] args) {
        RefCountGCTest r1 = new RefCountGCTest();
        RefCountGCTest r2 = new RefCountGCTest();

        r1.obj = r1;
        r2.obj = r1;

        r1 = null;
        r2 = null;

        System.gc();
    }
}
