import java.nio.ByteBuffer;
import java.util.Scanner;

public class BufferTest {

    private static final int BUFFER_SIZE = 1024 * 1024 * 1024; // 1 GB

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        System.out.println("直接内存分配完毕，请求指示！");

        Scanner scanner = new Scanner(System.in);
        scanner.next();

        System.out.println("直接内存开始释放！");
        byteBuffer = null;
        System.gc();

        scanner.next();
    }
}
