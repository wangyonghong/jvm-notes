import java.util.ArrayList;
import java.util.List;

public class StopTheWorldTest {

    public static void main(String[] args) {
        WorkThread workThread = new WorkThread();
        PrintThread printThread = new PrintThread();
        workThread.start();
        printThread.start();
    }

    public static class WorkThread extends Thread {
        List<byte[]> list = new ArrayList<>();

        @Override
        public void run() {
            while (true) {
                for (int i = 0; i < 1000; i++) {
                    byte[] buffer = new byte[1000];
                    list.add(buffer);
                }
                if (list.size() > 10000) {
                    list.clear();
                    System.gc(); // 触发 Full GC，引发 STW
                }
            }
        }
    }

    public static class PrintThread extends Thread {
        public final long startTime = System.currentTimeMillis();

        @Override
        public void run() {
            try {
                while (true) {
                    long t = System.currentTimeMillis() - startTime;
                    System.out.println(t / 1000 + "." + t % 1000);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
