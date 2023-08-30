import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

// 购物系统
public class system {
    // 当前登录的用户信息
    private static String username;
    private static String password;
    public static String getUsername() {
        return username;
    }
    public static void setUsername(String username) {
        system.username = username;
    }
    public static String getPassword() {
        return password;
    }
    public static void setPassword(String password) {
        system.password = password;
    }
    static ArrayList<customer> customers = database.readCustomers(); // 用户信息
    static ArrayList<administrator> administrators = database.readAdministrator(); // 管理员信息
    static ArrayList<product> products = database.readProducts(); // 商品信息
    public static void main(String[] args) {
        // 控制台输出防止中文乱码
        OutputStream originalOut = System.out;
        System.setOut(new PrintStream(originalOut, true, StandardCharsets.UTF_8));
        System.out.println(customers);
        System.out.println("~~~~欢迎来到 购物系统~~~~");
        menu shop = new menu();
        shop.main_menu(); // 系统入口
    }
}
