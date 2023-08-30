import java.util.Scanner;
// 用户类
class user{
    Scanner scanner = new Scanner(System.in);
    // 用户属性: 0为管理员, 1为客户
    private int type; // 用户属性
    private String username; // 用户名
    private String password; // 用户密码

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    void login() {
        String customerName;
        System.out.print("请输入用户名: ");
        customerName = scanner.nextLine();
        while (customerName == null || customerName.equals("")) {
            System.out.println("用户名不能为空!");
            System.out.print("请重输用户名: ");
            customerName = scanner.nextLine();
        }

        int i = 0; // 循环次数判定
        while (i < 5) {
            throwErrorException(i);
            System.out.print("请输入密码: ");
            String password = scanner.nextLine();
            if (!checkInput(customerName, password)) {
                System.out.println("[" + (i + 1) + "次]用户名或密码无效，请重试.");
            } else {
                break;
            }
            i++;
        }
        throwErrorException(i);
    }
    // 用户登录-信息核对
    private boolean checkInput(String username, String password) {
        menu shop = new menu();
        for (customer cust : system.customers) {
            if (cust.getUsername().trim().equals(username.trim()) && cust.getPassword().equals(password)) {
                System.out.println("~~~~欢迎客户 ["  + username + "]~~~~");
                system.setUsername(username);
                system.setPassword(password);
                shop.customers_menu();
                return true;
            }
        }

        for (administrator admin : system.administrators) {
            if (admin.getName().equals(username) && admin.getPassword().equals(password)) {
                System.out.println("~~~~欢迎管理员 [" + username + "]~~~~");
                system.setUsername(username);
                system.setPassword(password);
                shop.administrator_menu();
                return true;
            }
        }

        return false;
    }

    // 用户登录-次数监测
    private void throwErrorException(int i) {
        if (i == 3) {
            System.out.println("连续输入错误密码三次，您还有两次机会！");
        } else if (i == 5) {
            System.out.println("系统错误！请稍后再试！");
            System.exit(-1);
        }
    }


}
