import io.netty.util.Version;

public class CommonTest {
    public static void main(String[] args) {
        System.out.println(String.format("Netty/%s (%s)",
                Version.identify()
                        .get("nett-common")
                        .artifactVersion(),
                System.getProperties()
                        .getProperty("os.name")));
        System.out.println(DateUtil.nowGMT());
        DateUtil.nowGMT();
        test1();
    }
    public static String test1(){
        return "";
    }
}
