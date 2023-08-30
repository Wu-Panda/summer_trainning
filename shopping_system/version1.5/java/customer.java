import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

// 客户类
class customer extends user{
    Scanner scanner = new Scanner(System.in);
    private final int type = 1; // 客户属性
    private String username; // 客户名称
    private String password; // 客户密码
    private String Id; // 客户id
    private int level; // 客户等级
    private String reTime; //客户注册时间
    private double totalCost; // 客户总共花费
    private String phoneNumber; // 客户手机号
    private String email; // 客户邮箱
    private ArrayList<product> productCar; // 购物车
    private ArrayList<shopHistory> shopHistory; //购物历史
    customer(){
        super();
    }
    customer(String username, String password,
             String Id, int level, String reTime,
             double totalCost, String phoneNumber, String email){
        this.username = username; this.password = password;
        this.Id = Id; this.level = level;
        this.reTime = reTime; this.totalCost = totalCost;
        this.phoneNumber = phoneNumber; this.email = email;
    }

    public customer(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setReTime(String reTime) {
        this.reTime = reTime;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return Id;
    }

    public int getLevel() {
        return level;
    }

    public String getReTime() {
        return reTime;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    // 基本功能
        // 注册功能
        void register() {
            String customerName = null;
            String password = null;
            String phone = null;
            String email = null;
            int i = 0;
            int flag = 0;

            while (i <= 10) {
                while (flag == 0 && i < 9) {
                    i = i + 1;
                    System.out.print("请输入用户名：");
                    customerName = scanner.nextLine();

                    // 检查用户名是否已存在
                    boolean exists = false;
                    for (customer cust : system.customers) {
                        if (cust.getUsername().equals(customerName)) {
                            exists = true;
                            break;
                        }
                    }

                    if (exists) {
                        System.out.println("用户名已存在，请重新输入。");
                    } else if (customerName == null) {
                        System.out.println("用户名不能为空");
                    } else if (customerName.length() < 5) {
                        System.out.println("用户名长度必须不少于5个字符。");
                    } else {
                        flag = flag + 1;
                    }
                }

                while (flag == 1 && i < 9) {
                    i = i + 1;
                    System.out.print("请输入密码：");
                    password = scanner.nextLine();
                    if (isValidPassword(password)) {
                        System.out.println("密码长度必须大于8个字符，且必须是大小写字母、数字和标点符号的组合。");
                    } else {
                        flag = flag + 1;
                    }
                }

                while (flag == 2 && i < 9) {
                    i = i + 1;
                    System.out.print("请输入手机号码：");
                    phone = scanner.nextLine();
                    if (!isValidPhone(phone)) {
                        System.out.println("请输入有效的手机号码。");
                    } else {
                        flag = flag + 1;
                    }
                }

                while (flag == 3 && i < 9) {
                    i = i + 1;
                    System.out.print("请输入邮箱：");
                    email = scanner.nextLine();
                    if (!isValidEmail(email)) {
                        System.out.println("请输入有效的邮箱地址。");
                    } else {
                        flag = flag + 1;
                    }
                }

                if (flag == 4) {
                    customer newCustomer = new customer();
                    newCustomer.setUsername(customerName);
                    newCustomer.setPassword(password);
                    newCustomer.setId(Integer.toString(system.customers.size()-1));
                    newCustomer.setLevel(0);
                    LocalDateTime currentTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd"); // 定义格式化模式
                    String formattedDate = currentTime.format(formatter); // 格式化时间并转换为字符串
                    newCustomer.setReTime(formattedDate);
                    newCustomer.setTotalCost(0.0);
                    newCustomer.setPhoneNumber(phone);
                    newCustomer.setEmail(email);
                    system.customers.add(newCustomer);
                    System.out.println("注册成功！欢迎 " + customerName + "!");
                    break;
                }

                if (i == 9) {
                    System.out.println("连续多次输入失败! 目前系统异常, 请稍后再试!");
                    System.exit(-1);
                }
            }
        }
            // 验证密码是否满足要求
            static boolean isValidPassword(String password) {
                if (password == null || password.equals(""))
                    return true;
                if (password.length() <= 8) {
                    return true;
                }

                boolean hasLowercase = Pattern.compile("[a-z]").matcher(password).find();
                boolean hasUppercase = Pattern.compile("[A-Z]").matcher(password).find();
                boolean hasDigit = Pattern.compile("\\d").matcher(password).find();
                boolean hasPunctuation = Pattern.compile("[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]").matcher(password).find();

                return !hasLowercase || !hasUppercase || !hasDigit || !hasPunctuation;
            }
            // 验证电子邮件格式是否有效
            boolean isValidEmail(String email) {
                // 此正则表达式用于简单检查电子邮件格式
                String regex = "^[A-Za-z\\d+_.-]+@(.+)$";
                return email.matches(regex);
            }
            // 验证手机号码格式是否有效
            boolean isValidPhone(String phone) {
                // 此正则表达式用于简单检查手机号码格式
                String regex = "^(\\d{11})$"; // 手机号码是11位数字
                return phone.matches(regex);
            }
        // 找回密码功能
        void forget_password(customer customer){
            System.out.print("请输入注册时使用的邮箱地址: ");
            String email = scanner.nextLine();
            // 检查邮箱地址是否正确，可以根据自己的逻辑来判断
            boolean emailValid = checkEmailValidity(customer.getUsername(),email);
            if(!emailValid) {
                System.out.println("请确认后再试。");
                return;
            }
            // 生成随机密码
            String newPassword = generateRandomPassword();
            updatePassword(newPassword);
            // 发送密码到指定邮箱（模拟操作）
            sendPasswordByEmail(email);
            System.out.println("正在发送新密码到您的邮箱，请查收...");
            System.out.println("发送成功!您可以使用新密码登录，请尽快登录修改密码。");
        }
            // 检查邮箱地址是否正确
            private boolean checkEmailValidity(String username, String email) {
                // 根据逻辑判断邮箱地址是否正确
                // 返回 true 或 false
                String regex = "^[A-Za-z\\d+_.-]+@(.+)$";
                if(!email.matches(regex)){
                    System.out.println("邮箱格式错误!");
                    return false;
                }
                String userEmail = "";
                for(customer cust : system.customers){
                    if(cust.getUsername().equals(username)){
                        userEmail = cust.getEmail();
                    }
                }
                if(!email.equals(userEmail)){
                    System.out.println("邮箱错误!");
                    return false;
                }
                return true;
            }
            // 生成随机密码
            private String generateRandomPassword() {
                // 生成随机密码的逻辑
                // 返回随机密码
                String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                Random random = new Random();
                StringBuilder password = new StringBuilder();
                for (int i = 0; i < 10; i++) {
                    int index = random.nextInt(chars.length());
                    password.append(chars.charAt(index));
                }
                return password.toString();
            }
            // 发送密码到指定邮箱（模拟操作）
            private void sendPasswordByEmail(String email) {
                // 发送密码到指定邮箱的逻辑
                // 这里只是模拟操作，实际上需要调用邮件发送接口
                System.out.println("已发送密码到邮箱: " + email);
            }
    // 密码管理
        // 用户-修改密码
        void changePassword(){
            System.out.print("请输入旧密码: ");
            String oldPassword;
            oldPassword = scanner.nextLine();
            // 检查旧密码是否匹配
            while(!checkOldPassword(oldPassword)) {
                System.out.println("旧密码输入错误。");
                System.out.print("请输入旧密码: ");
                oldPassword = scanner.nextLine();
            }
            // 输入新密码
            System.out.print("请输入新密码: ");
            String newPassword;
            newPassword = scanner.nextLine();
            // 检查新密码格式是否有效
            while(isValidPassword(newPassword)) {
                System.out.println("密码长度必须大于8个字符，且必须是大小写字母、数字和标点符号的组合。");
                System.out.print("请输入新密码: ");
                newPassword = scanner.nextLine();
            }
            // 确认新密码
            System.out.print("请确认新密码: ");
            String conPassword;
            conPassword = scanner.nextLine();
            while(!newPassword.equals(conPassword)){
                System.out.println("请确认新密码: ");
                conPassword = scanner.nextLine();
            }
            // 更新密码
            updatePassword(newPassword);
            System.out.println("密码已成功修改. ");
        }
            // 检查旧密码是否匹配
            private boolean checkOldPassword(String oldPassword) {
                if(system.getPassword() != null) {
                    return system.getPassword().equals(oldPassword);
                }
                return false;
            }
            // 更新密码
            private void updatePassword(String newPassword) {
                // 更新 ArrayList<customer> 中的密码
                for (customer cust : system.customers) {
                    if (cust.getUsername().equals(system.getUsername())) {
                        cust.setPassword(newPassword);
                        break;
                    }
                }
            }
    // 购物
        // 用户-展示所有商品
        void displayProducts(){
            if(system.products == null){
                System.out.println("商品信息获取失败!");
                return;
            }
            for(product pro:system.products){
                System.out.println(pro);
            }
        }
        // 用户-展示购物车
        void displayProductCar(customer customer){
            if(customer.productCar != null){
                System.out.println("购物车详情: ");
                for (int i = 0; i < customer.productCar.size(); i++) {
                    System.out.println(customer.productCar.get(i).toString());
                }
            }
            else
                System.out.println("这里什么都没有~ 快去购物吧! ");
        }
        // 用户-添加商品进购物车
        void addToCart(customer customer){
            // 显示商品列表供用户选择
            displayProducts();
            System.out.print("请输入要添加的商品编号: ");
            String str;
            boolean flag = false;
            str = scanner.nextLine();
            int productIndex = Integer.parseInt(str);
            while(!flag){
                if(str.equals("")){
                    System.out.println("输入不能为空!");
                    System.out.print("请重输商品编号: ");
                    str = scanner.nextLine();
                    productIndex = Integer.parseInt(str);
                }
                else if(!str.matches("\\d+")){
                    System.out.println("输入格式错误!");
                    System.out.print("请重输商品编号: ");
                    str = scanner.nextLine();
                    productIndex = Integer.parseInt(str);
                }
                // 验证商品编号是否有效
                else if(productIndex < 1 || productIndex >= system.products.size()){
                    System.out.println("无效的商品数量");
                    System.out.print("请重输商品数量: ");
                    str = scanner.nextLine();
                    productIndex = Integer.parseInt(str);
                }
                else
                    flag = true;
            }
            product selectedProduct = system.products.get(productIndex - 1);
            flag = false;
            int quantity; // 商品数量
            System.out.print("请输入要添加的商品数量: ");
            str = scanner.nextLine();
            quantity = Integer.parseInt(str);
            while (!flag) {
                if(str.equals("")){
                    System.out.println("输入不能为空!");
                    System.out.print("请重输商品数量: ");
                    str = scanner.nextLine();
                    quantity = Integer.parseInt(str);
                }
                // 验证输入是否正确
                else if(!str.matches("\\d+")){
                    System.out.println("输入格式错误!");
                    System.out.print("请重输商品数量: ");
                    str = scanner.nextLine();
                    quantity = Integer.parseInt(str);
                }
                // 验证商品数量是否有效
                else if (quantity <= 0){
                    System.out.println("无效的商品数量");
                    System.out.print("请重输商品数量: ");
                    str = scanner.nextLine();
                    quantity = Integer.parseInt(str);
                }
                else
                    flag = true;
            }
            // 创建购物车
            if (customer.productCar == null) {
                customer.productCar = new ArrayList<>();
            }
            // 将商品添加到购物车
            selectedProduct.setAmount(quantity);
            customer.productCar.add(selectedProduct);
            System.out.println("已将商品添加到购物车。");
            // 展示购物车
            displayProductCar(customer);
        }
        // 用户-删除商品出购物车
        void removeFromCart(customer customer){
            // 显示购物车中的商品供用户选择
            displayProductCar(customer);
            if (customer.productCar == null || customer.productCar.isEmpty()) {
                System.out.println("购物车为空，无需移除商品。");
                return;
            }
            String str;
            boolean flag = false;
            int productIndex = 1;
            System.out.print("请输入要移除的商品编号: ");
            str = scanner.nextLine();
            while(!flag){
                if(str.equals("")){
                    System.out.println("输入不能为空!");
                    System.out.print("请重新输入: ");
                    str = scanner.nextLine();
                    productIndex = scanner.nextInt();
                }
                else if(!str.matches("\\d+")){
                    System.out.println("输入格式错误!");
                    System.out.print("请重新输入: ");
                    str = scanner.nextLine();
                    productIndex = scanner.nextInt();
                }
                // 验证商品编号是否有效
                else if(productIndex < 1 || productIndex > customer.productCar.size()){
                    System.out.println("无效的商品编号");
                    System.out.print("请重新输入: ");
                    str = scanner.nextLine();
                    productIndex = scanner.nextInt();
                }
                else
                    flag = true;
            }
            product selectedProduct = customer.productCar.get(productIndex - 1);
            // 提示用户确认移除操作
            System.out.print("确认要移除 " + selectedProduct.getName() + " 吗?(Y?): ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("Y")) {
                // 执行移除操作
                customer.productCar.remove(selectedProduct);
                if (customer.productCar.isEmpty()) {
                    customer.productCar = null;
                }
                System.out.println("已从购物车中移除 " + selectedProduct.getName() + "。");
            } else {
                System.out.println("取消移除操作。");
            }
        }
        // 用户-修改购物车中的商品
        void updateCartProduct(customer customer){
            // 显示购物车中的商品供用户选择
            if (customer.productCar == null || customer.productCar.isEmpty()) {
                System.out.println("购物车为空，无法修改商品数量。");
                return;
            }
            displayProductCar(customer);
            String str;
            boolean flag = false;
            int productIndex = 1;
            System.out.print("请输入要修改的商品编号: ");
            str = scanner.nextLine();
            while(!flag){
                if(str.equals("")){
                    System.out.println("输入不能为空!");
                    System.out.print("请重新输入: ");
                    str = scanner.nextLine();
                    productIndex = scanner.nextInt();
                }
                else if(!str.matches("\\d+")){
                    System.out.println("输入格式错误!");
                    System.out.print("请重新输入: ");
                    str = scanner.nextLine();
                    productIndex = scanner.nextInt();
                }
                // 验证商品编号是否有效
                else if(productIndex < 1 || productIndex > customer.productCar.size()){
                    System.out.println("无效的商品编号");
                    System.out.print("请重新输入: ");
                    str = scanner.nextLine();
                    productIndex = scanner.nextInt();
                }
                else
                    flag = true;
            }
            product selectedProduct = customer.productCar.get(productIndex - 1);
            flag = false;
            int newQuantity; // 商品数量
            System.out.print("请输入要修改的商品数量: ");
            str = scanner.nextLine();
            newQuantity = Integer.parseInt(str);
            while (!flag) {
                if(str.equals("")){
                    System.out.println("输入不能为空!");
                    System.out.print("请重输商品数量: ");
                    str = scanner.nextLine();
                    newQuantity = Integer.parseInt(str);
                }
                // 验证输入是否正确
                else if(!str.matches("\\d+")){
                    System.out.println("输入格式错误!");
                    System.out.print("请重输商品数量: ");
                    str = scanner.nextLine();
                    newQuantity = Integer.parseInt(str);
                }
                // 验证商品数量是否有效
                else if (newQuantity <= 0){
                    System.out.println("商品数量必须大于0。将从购物车中移除该商品。");
                    customer.productCar.remove(selectedProduct);
                }
                else{
                    selectedProduct.setAmount(newQuantity);
                    System.out.println("商品数量已更新。");
                    flag = true;
                }
            }
        }
        // 用户-付账
        void checkout(customer customer){
            // 显示购物车中的商品供用户确认
            if (customer.productCar == null || customer.productCar.isEmpty()) {
                System.out.println("购物车为空，无法进行付账操作。");
                return;
            }
            displayProductCar(customer);
            // 模拟支付操作（在实际应用中，可以调用支付接口）
            System.out.println("付款成功！订单已生成。");
            // 创建购物历史记录并添加到列表中
            shopHistory history = new shopHistory(new ArrayList<>(customer.productCar)); // 创建购物历史记录
            if(customer.shopHistory==null){
                customer.shopHistory = new ArrayList<>();
            }
            customer.shopHistory.add(history);
            writeHistory(history);
            // 更新购物车中商品的数量
            updateProductQuantities(customer);
            // 清空购物车
            customer.productCar.clear();
            customer.productCar = null;
        }
            // 更新购物车中商品的数量
            private void updateProductQuantities(customer customer) {
                for (product cartProduct : customer.productCar) {
                    for (product dbProduct : system.products) {
                        if (cartProduct.getId().equals(dbProduct.getId())) {
                            dbProduct.setAmount(dbProduct.getAmount() - cartProduct.getAmount());
                            break;
                        }
                    }
                }
            }
        // 用户-查看历史
        void viewHistory(customer customer){
            if (customer.shopHistory == null || customer.shopHistory.isEmpty()) {
                System.out.println("没有购物历史记录。");
                return;
            }
            System.out.println("购物历史记录数量: " + customer.shopHistory.size());
            int i = 1;
            for (shopHistory history : customer.shopHistory) {
                System.out.println("------------------");
                System.out.println(" --购物清单 [" + i +"] --");
                System.out.println("购物时间: " + history.getTime());
                System.out.println("购买的商品清单:");
                for (ProductPurchase p : history.getProducts()) {
                    System.out.println(p.getName() + " -- " + p.getQuantity());
                }
                i++;
                System.out.println("------------------");
            }
        }
        // 将历史记录写入文本中
        void writeHistory(shopHistory history){
            String shopHistory_path = "./version1.5/Database/excel/shopHistory.csv";
            // 更新文本中的商品信息
            StringBuilder result = new StringBuilder("");
            for (ProductPurchase p : history.getProducts()) {
                result.append("\t").append(p.getName()).append(" -- ").append(p.getQuantity());
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(shopHistory_path,true))) {
                writer.println("用户["+getUsername()+"]购物记录: " +
                        history.getTime() + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    @Override
    public String toString() {
        return "Name: " + getUsername() +
                ", Password: ******" +
                ", ID: " + getId() +
                ", Level: " + getLevel() +
                ", Time: " + getReTime() +
                ", Expense: " + getTotalCost() +
                ", Phone: " + getPhoneNumber() +
                ", Email: " + getEmail();
    }
}