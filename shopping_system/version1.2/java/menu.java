import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
// 菜单类
class menu{
    Scanner scanner = new Scanner(System.in);
    // 初级菜单
    void main_menu(){
        user user = new user();
        customer customer = new customer();
        System.out.println("\n当前位置: [首页]");
        System.out.println("======初始操作菜单======");
        System.out.println("\t\t1.登录");
        System.out.println("\t\t2.注册");
        System.out.println("\t\t3.退出");
        System.out.print("请输入选项:");
        String str = scanner.nextLine();
        while(normalized_Input(str)){
            System.out.print("请重新输入选项:");
            str = scanner.nextLine();
        }
        int choice = Integer.parseInt(str);
        switch (choice) {
            case 1 -> user.login();
            case 2 -> {
                customer.register();
                user.login();
            }
            case 3 -> {
                System.out.println("正在退出系统...");
                saveExcel();
                System.exit(0);
            }
            default -> System.out.println("无效选项,请重新输入.");
        }
    }
    // 二级菜单
    // 用户菜单
    void customers_menu(){
        customer customer= new customer(system.getUsername(),system.getPassword());
        System.out.println("\n当前位置: 初始操作->[客户操作]");
        System.out.println("======客户操作菜单======");
        System.out.println("\t1.修改密码");
        System.out.println("\t2.忘记密码");
        System.out.println("\t3.购物");
        System.out.println("\t0.退出登录");
        System.out.print("请输入选项:");
        String str = scanner.nextLine();
        while(normalized_Input(str)){
            System.out.print("请重新输入选项:");
            str = scanner.nextLine();
        }
        int choice = Integer.parseInt(str);
        switch (choice) {
            case 1 -> {
                customer.changePassword();
                customers_menu();
            }
            case 2 -> {
                customer.forget_password(customer);
                customers_menu();
            }
            case 3 -> {
                shop_menu(customer);
                customers_menu();
            }
            case 0 -> {
                System.out.println("正在退出登录...");
                main_menu();
            }
            default -> {
                System.out.println("无效选项...");
                customers_menu();
            }
        }
    }
    // 管理员菜单
    void administrator_menu(){
        administrator administrator = new administrator();
        System.out.println("\n当前位置: [首页]->[管理员操作]");
        System.out.println("======管理员操作菜单======");
        System.out.println("\t1.密码管理");
        System.out.println("\t2.客户管理");
        System.out.println("\t3.商品管理");
        System.out.println("\t4.退出登录");
        System.out.print("请输入选项:");
        String str = scanner.nextLine();
        while(normalized_Input(str)){
            System.out.print("请重新输入选项:");
            str = scanner.nextLine();
        }
        int choice = Integer.parseInt(str);
        switch (choice) {
            case 1 -> {
                passwordManagement_menu(administrator);
                administrator_menu();
            }
            case 2 -> {
                customersManagement_menu(administrator);
                administrator_menu();
            }
            case 3 -> {
                productsManagement_menu(administrator);
                administrator_menu();
            }
            case 4 -> {
                System.out.println("正在退出登录...");
                main_menu();
            }
            default -> {
                System.out.println("无效选项...");
                administrator_menu();
            }
        }
    }
    // 三级菜单
    // 管理员-1.密码管理菜单
    void passwordManagement_menu(administrator admin){
        System.out.println("\n当前位置: [首页]->[管理员操作]->[密码管理]");
        System.out.println("======密码管理======");
        System.out.println("1.修改自身密码");
        System.out.println("2.重置客户密码");
        System.out.println("3.返回上一级菜单");
        System.out.print("请输入选项:");
        String str = scanner.nextLine();
        while(normalized_Input(str)){
            System.out.print("请重新输入选项:");
            str = scanner.nextLine();
        }
        int choice = Integer.parseInt(str);
        switch (choice) {
            case 1 -> {
                admin.changeAdminPassword();
                passwordManagement_menu(admin);
            }
            case 2 -> {
                admin.resetCustomersPassword();
                passwordManagement_menu(admin);
            }
            case 3 -> administrator_menu();
            default -> {
                System.out.println("无效选项...");
                passwordManagement_menu(admin);
            }
        }
    }
    // 管理员-2.客户管理菜单
    void customersManagement_menu(administrator admin){
        System.out.println("\n当前位置: [首页]->[管理员操作]->[客户管理]");
        System.out.println("======客户管理======");
        System.out.println("1.列出所有客户信息");
        System.out.println("2.删除客户信息");
        System.out.println("3.查询客户信息");
        System.out.println("4.返回上一级菜单");
        System.out.print("请输入选项:");
        String str = scanner.nextLine();
        while(normalized_Input(str)){
            System.out.print("请重新输入选项:");
            str = scanner.nextLine();
        }
        int choice = Integer.parseInt(str);
        switch (choice) {
            case 1 -> {
                admin.listAllCustomers();
                customersManagement_menu(admin);
            }
            case 2 -> {
                admin.deleteCustomer();
                customersManagement_menu(admin);
            }
            case 3 -> {
                admin.searchCustomer();
                customersManagement_menu(admin);
            }
            case 4 -> administrator_menu();
            default -> {
                System.out.println("无效选项...");
                customersManagement_menu(admin);
            }
        }
    }
    // 管理员-3.商品管理菜单
    void productsManagement_menu(administrator admin){
        System.out.println("当前位置: [首页]->[管理员操作]->[商品管理]");
        System.out.println("======商品管理======");
        System.out.println("1.列出所有商品信息");
        System.out.println("2.添加商品");
        System.out.println("3.修改商品");
        System.out.println("4.删除商品");
        System.out.println("5.查询商品");
        System.out.println("6.返回上一级菜单");
        System.out.print("请输入选项:");
        String str = scanner.nextLine();
        while(normalized_Input(str)){
            System.out.print("请重新输入选项:");
            str = scanner.nextLine();
        }
        int choice = Integer.parseInt(str);
        switch (choice) {
            case 1 -> {
                admin.listAllProducts();
                productsManagement_menu(admin);
            }
            case 2 -> {
                admin.addProduct();
                productsManagement_menu(admin);
            }
            case 3 -> {
                admin.updateProduct();
                productsManagement_menu(admin);
            }
            case 4 -> {
                admin.deleteProduct();
                productsManagement_menu(admin);
            }
            case 5 -> {
                admin.searchProduct();
                productsManagement_menu(admin);
            }
            case 6 -> administrator_menu();
            default -> {
                System.out.println("无效选项...");
                productsManagement_menu(admin);
            }
        }
    }
    // 用户-购物菜单
    void shop_menu(customer customer){
        System.out.println("\n当前位置: [首页]->[用户操作]->[购物]");
        System.out.println("======购物操作菜单======");
        System.out.println("\t1.查看商品列表");
        System.out.println("\t2.添加商品到购物车");
        System.out.println("\t3.从购物车移除商品");
        System.out.println("\t4.修改购物车中的商品");
        System.out.println("\t5.查看购物车");
        System.out.println("\t6.结账");
        System.out.println("\t7.查看历史结账信息");
        System.out.println("\t0.返回上级菜单");
        System.out.print("请输入选项:");
        String str = scanner.nextLine();
        while(normalized_Input(str)){
            System.out.print("请正确输入选项:");
            str = scanner.nextLine();
        }
        int choice = Integer.parseInt(str);
        switch (choice) {
            case 1 -> {
                customer.displayProducts();
                shop_menu(customer);
            }
            case 2 -> {
                customer.addToCart(customer);
                shop_menu(customer);
            }
            case 3 -> {
                customer.removeFromCart(customer);
                shop_menu(customer);
            }
            case 4 -> {
                customer.updateCartProduct(customer);
                shop_menu(customer);
            }
            case 5 -> {
                customer.displayProductCar(customer);
                shop_menu(customer);
            }
            case 6 -> {
                customer.checkout(customer);
                shop_menu(customer);
            }
            case 7 -> {
                customer.viewHistory(customer);
                shop_menu(customer);
            }
            case 0 -> customers_menu();
            default -> {
                System.out.println("无效选项...");
                shop_menu(customer);
            }
        }
    }
    // 输入规范化判定
    boolean normalized_Input(String str){
        if(str.matches("\\d+"))
            return false;
        else {
            System.out.println("输入不合规范");
            return true;
        }
    }
    // 写入文本
    void saveExcel(){
        String product_path = "./version2.0/Database/excel/products.csv";
        String admin_path = "./version2.0/Database/excel/administrator.csv";
        String customer_path = "./version2.0/Database/excel/customer.csv";

        // 更新文本中的商品信息
        try (PrintWriter writer = new PrintWriter(new FileWriter(product_path))) {
            for (product product : system.products) {
                writer.println(product.getId() + "," + product.getName() + "," + product.getProducer() +
                        "," + product.getProTime() + "," + product.getType() + "," + product.getPurPrice() +
                        "," + product.getRetPrice() + "," + product.getAmount());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 更新文本中的用户信息
        try (PrintWriter writer = new PrintWriter(new FileWriter(admin_path))) {
            for (administrator admin : system.administrators) {
                writer.println(admin.getName() + "," + admin.getPassword());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 更新文本中的用户信息
        try (PrintWriter writer = new PrintWriter(new FileWriter(customer_path))) {
            for(customer customer : system.customers) {
                writer.println(customer.getUsername() + "," + customer.getPassword() + "," +
                        customer.getId() + "," + customer.getLevel() + "," + customer.getReTime() + "," +
                        customer.getTotalCost() + "," + customer.getPhoneNumber() + "," + customer.getEmail());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
