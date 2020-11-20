import java.net.URL;

public class ClassLoaderTest {

    public static void main(String[] args) {
        System.out.println("********** 启动类加载器 **********");

        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (URL url : urls) {
            System.out.println(url.toExternalForm());
        }

        System.out.println("********** 拓展类加载器 **********");
        String extDirs = System.getProperty("java.ext.dirs");
        for (String path : extDirs.split(":")) {
            System.out.println(path);
        }
    }

}