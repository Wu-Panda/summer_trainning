import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class User {
    private String username;
    private String passwordHash;
    private String password;

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }


    public boolean verifyPassword(String password) {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(passwordHash);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}

class Product {
    private String name;
    private double price;
    private String description;

    public Product(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}

class ShoppingCart {
    private List<Product> products;

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public void updateProduct(int index, Product newProduct) {
        products.set(index, newProduct);
    }

    public void clear() {
        products.clear();
    }
}

public class Menu {
    private static List<User> users = new ArrayList<>();
    private static List<Product> products = new ArrayList<>();
    private static ShoppingCart currentUserCart = new ShoppingCart();
    private static User currentUser = null;
    private static final String USER_DATA_FILE = "../data/users.csv";
    private static final String PRODUCT_DATA_FILE = "../data/product.csv";
    private static final String HISTORY_DATA_FILE = "../data/history.csv";

    public static void main(String[] args) {
        readDataFromCSV();
        readProductDataFromCSV();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (currentUser == null) {
                System.out.println("\n当前位置: [初始操作]->用户操作->购物操作");
                System.out.println("======初始操作菜单======");
                System.out.println("\t\t1.登录");
                System.out.println("\t\t2.注册");
                System.out.println("\t\t3.退出");
                System.out.print("请输入选项: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符

                switch (choice) {
                    case 1:
                        login(scanner);
                        break;
                    case 2:
                        register(scanner);
                        break;
                    case 3:
                        writeDataToCSV();
                        System.out.println("正在退出...");
                        return;
                    default:
                        System.out.println("无效选项,请重新输入.");
                }
            } else {
                System.out.println("\n当前位置: 初始操作->[用户操作]->购物操作");
                System.out.println("======用户操作菜单======");
                System.out.println("\t1.修改密码");
                System.out.println("\t2.重置密码");
                System.out.println("\t3.购物");
                System.out.println("\t0.退出登录");
                System.out.print("请输入选项: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符

                switch (choice) {
                    case 1:
                        changePassword(scanner);
                        break;
                    case 2:
                        resetPassword(scanner);
                        break;
                    case 3:
                        shoppingMenu(scanner);
                        break;
                    case 0:
                        currentUser = null;
                        break;
                    default:
                        System.out.println("无效选项,请重新输入.");
                }
            }
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        if (username.equals("admin") && currentUser == null) {
            for (User user : users) {
                if (user.getUsername().equals(username) && user.verifyPassword(password)) {
                    currentUser = user;
                    adminMenu(scanner);
                    return;
                }
            }
        } else {
            for (User user : users) {
                if (user.getUsername().equals(username) && user.verifyPassword(password)) {
                    currentUser = user;
                    System.out.println("登录成功! ");
                    return;
                }
            }
        }

        System.out.println("用户名或密码无效，请重试.");
    }


    private static void register(Scanner scanner) {
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();

        // 检查用户名是否已存在
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("该用户名已被注册,请选择其他用户名.");
                return;
            }
        }

        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        String passwordHash = hashPassword(password);

        User newUser = new User(username, passwordHash);
        users.add(newUser);
        currentUser = newUser;
        System.out.println("注册成功! ");
    }

    private static void changePassword(Scanner scanner) {
        System.out.print("请输入当前密码: ");
        String currentPassword = scanner.nextLine();

        if (currentUser.verifyPassword(currentPassword)) {
            System.out.print("请输入新密码: ");
            String newPassword = scanner.nextLine();
            System.out.print("请再次输入新密码: ");
            String confirmNewPassword = scanner.nextLine();

            if (newPassword.equals(confirmNewPassword)) {
                String newPasswordHash = hashPassword(newPassword);
                currentUser.setPasswordHash(newPasswordHash);
                System.out.println("密码修改成功! ");
            } else {
                System.out.println("新密码输入不一致,请重试.");
            }
        } else {
            System.out.println("当前密码错误,请重试.");
        }
    }

    private static void resetPassword(Scanner scanner) {
        System.out.print("请输入当前密码: ");
        String currentPassword = scanner.nextLine();

        if (currentUser.verifyPassword(currentPassword)) {
            String newPasswordHash = hashPassword("123"); // 重置为默认密码 123
            currentUser.setPasswordHash(newPasswordHash);
            System.out.println("密码重置成功! ");
        } else {
            System.out.println("当前密码错误,请重试.");
        }
    }

    private static void shoppingMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n当前位置: 初始操作->用户操作->[购物操作]");
            System.out.println("======购物操作菜单======");
            System.out.println("\t1.查看商品列表");
            System.out.println("\t2.加入购物车");
            System.out.println("\t3.从购物车移除商品");
            System.out.println("\t4.修改购物车中的商品");
            System.out.println("\t5.结账");
            System.out.println("\t6.查看历史结账信息");
            System.out.println("\t0.返回上级菜单");
            System.out.print("请输入选项:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    displayProducts();
                    break;
                case 2:
                    addToCart(scanner);
                    break;
                case 3:
                    removeFromCart(scanner);
                    break;
                case 4:
                    updateCartProduct(scanner);
                    break;
                case 5:
                    checkout(scanner);
                    break;
                case 6:
                    viewHistory();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效选项,请重新输入.");
            }
        }
    }

    private static void displayProducts() {
        System.out.println("商品列表: ");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println("编号: " + (i + 1));
            System.out.println("名称: " + product.getName());
            System.out.println("价格: " + product.getPrice());
            System.out.println("介绍: " + product.getDescription());
            System.out.println();
        }
    }

    private static void addToCart(Scanner scanner) {
        System.out.print("请输入要添加到购物车的商品编号: ");
        int productIndex = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        if (productIndex >= 1 && productIndex <= products.size()) {
            Product selectedProduct = products.get(productIndex - 1);
            currentUserCart.addProduct(selectedProduct);
            System.out.println("商品已成功添加到购物车.");
        } else {
            System.out.println("无效的商品编号,请重试.");
        }
    }

    private static void removeFromCart(Scanner scanner) {
        if (currentUserCart.getProducts().isEmpty()) {
            System.out.println("购物车为空,无法移除商品.");
            return;
        }

        System.out.print("请输入要从购物车移除的商品编号: ");
        int productIndex = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        if (productIndex >= 1 && productIndex <= currentUserCart.getProducts().size()) {
            Product selectedProduct = currentUserCart.getProducts().get(productIndex - 1);
            currentUserCart.removeProduct(selectedProduct);
            System.out.println("商品已成功从购物车移除.");
        } else {
            System.out.println("无效的商品编号,请重试.");
        }
    }

    private static void updateCartProduct(Scanner scanner) {
        if (currentUserCart.getProducts().isEmpty()) {
            System.out.println("购物车为空,无法修改商品.");
            return;
        }

        System.out.print("请输入要修改的商品编号: ");
        int productIndex = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        if (productIndex >= 1 && productIndex <= currentUserCart.getProducts().size()) {
            Product selectedProduct = currentUserCart.getProducts().get(productIndex - 1);

            System.out.println("请选择要进行的操作: ");
            System.out.println("\t\t1.修改商品名称");
            System.out.println("\t\t2.修改商品价格");
            System.out.println("\t\t3.修改商品介绍");
            System.out.println("\t\t0.返回上级菜单");
            System.out.print("请输入选项: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    System.out.print("请输入新的商品名称: ");
                    String newName = scanner.nextLine();
                    Product updatedNameProduct = new Product(newName, selectedProduct.getPrice(), selectedProduct.getDescription());
                    currentUserCart.updateProduct(productIndex - 1, updatedNameProduct);
                    System.out.println("商品名称已更新.");
                    break;
                case 2:
                    System.out.print("请输入新的商品价格: ");
                    double newPrice = scanner.nextDouble();
                    scanner.nextLine(); // 消耗换行符
                    Product updatedPriceProduct = new Product(selectedProduct.getName(), newPrice, selectedProduct.getDescription());
                    currentUserCart.updateProduct(productIndex - 1, updatedPriceProduct);
                    System.out.println("商品价格已更新.");
                    break;
                case 3:
                    System.out.print("请输入新的商品介绍: ");
                    String newDescription = scanner.nextLine();
                    Product updatedDescriptionProduct = new Product(selectedProduct.getName(), selectedProduct.getPrice(), newDescription);
                    currentUserCart.updateProduct(productIndex - 1, updatedDescriptionProduct);
                    System.out.println("商品介绍已更新。");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("无效选项，请重新输入.");
            }
        } else {
            System.out.println("无效的商品编号,请重试.");
        }
    }

    private static void checkout(Scanner scanner) {
        if (currentUserCart.getProducts().isEmpty()) {
            System.out.println("购物车为空，无法进行结账。");
            return;
        }

        double totalAmount = 0.0;

        System.out.println("购物车商品: ");
        for (int i = 0; i < currentUserCart.getProducts().size(); i++) {
            Product product = currentUserCart.getProducts().get(i);
            System.out.println("编号: " + (i + 1));
            System.out.println("名称: " + product.getName());
            System.out.println("价格: " + product.getPrice());
            System.out.println("介绍: " + product.getDescription());
            totalAmount += product.getPrice();
            System.out.println();
        }

        System.out.println("总金额: " + totalAmount);

        System.out.println("是否确认结账?");
        System.out.println("\t\t1.确认结账");
        System.out.println("\t\t0.取消结账");
        System.out.print("请输入选项: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        if (choice == 1) {
            // 保存结账信息
            String checkoutInfo = "用户: " + currentUser.getUsername() + ",总金额: " + totalAmount;
            saveCheckoutInfo(checkoutInfo);
            System.out.println("结账成功! ");
            currentUserCart.clear();
        } else {
            System.out.println("取消结账.");
        }
    }

    private static void viewHistory() {
        System.out.println("历史结账信息: ");
        List<String> history = loadCheckoutInfo();
        for (String info : history) {
            System.out.println(info);
        }
        if (history.isEmpty()) {
            System.out.println("暂无历史结账信息.");
        }
    }

    // 启动程序时从CSV文件中读取数据
    private static void readDataFromCSV() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String username = userData[0];
                String passwordHash = userData[1];
                User user = new User(username, passwordHash);
                users.add(user);
            }
            reader.close();

            System.out.println("数据已成功从CSV文件读取。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 退出程序时将数据写入CSV文件
    private static void writeDataToCSV() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE));
            for (User user : users) {
                writer.write(user.getUsername() + "\t" + user.getPasswordHash());
                writer.newLine();
            }
            writer.close();

            System.out.println("数据已成功写入CSV文件。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 启动程序时从CSV文件中读取商品数据
    private static void readProductDataFromCSV() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_DATA_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] productData = line.split(",");
                String name = productData[0];
                double price = Double.parseDouble(productData[1]);
                String description = productData[2];
                Product product = new Product(name, price, description);
                products.add(product);
            }
            reader.close();

            System.out.println("商品数据已成功从CSV文件读取。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 保存结账信息到CSV文件
    private static void saveCheckoutInfo(String info) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_DATA_FILE, true));
            writer.write(info);
            writer.newLine();
            writer.close();
            System.out.println("结账信息保存成功。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从CSV文件中加载结账信息
    private static List<String> loadCheckoutInfo() {
        List<String> history = new ArrayList<>();
        try {
            File file = new File(HISTORY_DATA_FILE);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    history.add(line);
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return history;
    }


    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void adminMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n当前位置: [管理菜单]->xx管理");
            System.out.println("======管理员操作菜单======");
            System.out.println("\t1.密码管理");
            System.out.println("\t2.客户管理");
            System.out.println("\t3.商品管理");
            System.out.println("\t4.退出登录");
            System.out.print("请输入选项：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符
            switch (choice) {
                case 1:
                    passwordManagement(scanner);
                    break;
                case 2:
                    customerManagement(scanner);
                    break;
                case 3:
                    productManagement(scanner);
                    break;
                case 4:
                    currentUser = null;
                    return;
                default:
                    System.out.println("无效选项，请重新输入。");
            }
        }
    }

    // 密码管理
    private static void passwordManagement(Scanner scanner) {
        System.out.println("\n当前位置: 管理菜单->[密码管理]");
        System.out.println("======密码管理======");
        System.out.println("1. 修改自身密码");
        System.out.println("2. 重置所有用户密码为123");
        System.out.print("请输入选项：");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                changeOwnPassword(scanner);
                break;
            case 2:
                resetAllPasswords(scanner);
                break;
            default:
                System.out.println("无效选项，请重新输入。");
        }
    }

    private static void changeOwnPassword(Scanner scanner) {
        System.out.print("请输入当前密码：");
        String currentPassword = scanner.nextLine();
        if (!currentUser.verifyPassword(currentPassword)) {
            System.out.println("密码验证失败，无法修改密码。");
            return;
        }

        System.out.print("请输入新密码：");
        String newPassword = scanner.nextLine();

        currentUser.setPassword(newPassword);
        writeDataToCSV();
        System.out.println("密码修改成功。");
    }

    private static void resetAllPasswords(Scanner scanner) {
        System.out.print("确认重置所有用户密码为123吗？（Y/N）");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("Y")) {
            for (User user : users) {
                user.setPassword("123");
            }
            writeDataToCSV();
            System.out.println("所有用户密码已重置为123。");
        } else {
            System.out.println("操作已取消。");
        }
    }

    // 客户管理
    private static void customerManagement(Scanner scanner) {
        System.out.println("\n当前位置: 管理菜单->[客户管理]");
        System.out.println("======客户管理======");
        System.out.println("1.列出所有用户信息");
        System.out.println("2.删除用户信息");
        System.out.println("3.查询用户信息");
        System.out.print("请输入选项：");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                listAllUsers();
                break;
            case 2:
                deleteUser(scanner);
                break;
            case 3:
                searchUser(scanner);
                break;
            default:
                System.out.println("无效选项，请重新输入。");
        }
    }

    private static void listAllUsers() {
        System.out.println("所有用户信息：");
        for (User user : users) {
            System.out.println("用户名：" + user.getUsername());
            System.out.println("密码：" + user.getPassword());
            System.out.println();
        }
    }

    private static void deleteUser(Scanner scanner) {
        System.out.print("请输入管理员密码：");
        String adminPassword = scanner.nextLine();
        if (!currentUser.verifyPassword(adminPassword)) {
            System.out.println("管理员密码验证失败，无法删除用户。");
            return;
        }

        System.out.print("请输入要删除的用户名：");
        String username = scanner.nextLine();

        boolean found = false;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                found = true;
                users.remove(user);
                writeDataToCSV();
                System.out.println("用户 " + username + " 已成功删除。");
                break;
            }
        }

        if (!found) {
            System.out.println("未找到用户 " + username + " ，删除失败。");
        }
    }

    private static void searchUser(Scanner scanner) {
        System.out.print("请输入搜索关键字：");
        String keyword = scanner.nextLine();

        System.out.println("相关用户信息：");
        boolean found = false;
        for (User user : users) {
            if (user.getUsername().contains(keyword)) {
                found = true;
                System.out.println("用户名：" + user.getUsername());
                System.out.println("密码：" + user.getPassword());
                System.out.println();
            }
        }

        if (!found) {
            System.out.println("未找到与关键字匹配的用户。");
        }
    }

    private static void saveProductData() {
        try {
            FileWriter writer = new FileWriter(PRODUCT_DATA_FILE);
            for (Product product : products) {
                writer.write(product.getName() + "," + product.getPrice() + "," + product.getDescription() + "\n");
            }
            writer.close();
            System.out.println("商品数据保存成功。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 商品管理
    private static void productManagement(Scanner scanner) {
        System.out.println("当前位置: 管理菜单->[商品管理]");
        System.out.println("======商品管理======");
        System.out.println("\t1.列出所有商品信息");
        System.out.println("\t2.添加商品");
        System.out.println("\t3.修改商品");
        System.out.println("\t4.删除商品");
        System.out.println("\t5.查询商品");
        System.out.print("请输入选项: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        switch (choice) {
            case 1:
                listAllProducts();
                break;
            case 2:
                addProduct(scanner);
                break;
            case 3:
                updateProduct(scanner);
                break;
            case 4:
                deleteProduct(scanner);
                break;
            case 5:
                searchProduct(scanner);
                break;
            default:
                System.out.println("无效选项,请重新输入.");
        }
    }

    private static void listAllProducts() {
        System.out.println("所有商品信息: ");
        for (Product product : products) {
            System.out.println("名称: " + product.getName());
            System.out.println("价格: " + product.getPrice());
            System.out.println("介绍: " + product.getDescription());
            System.out.println();
        }
    }

    private static void addProduct(Scanner scanner) {
        System.out.print("请输入商品名称: ");
        String name = scanner.nextLine();
        System.out.print("请输入商品价格: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // 消耗换行符
        System.out.print("请输入商品介绍: ");
        String description = scanner.nextLine();

        Product newProduct = new Product(name, price, description);
        products.add(newProduct);
        saveProductData();
        System.out.println("商品已成功添加.");
    }

    private static void updateProduct(Scanner scanner) {
        System.out.print("请输入要修改的商品名称: ");
        String productName = scanner.nextLine();

        boolean found = false;
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                found = true;

                System.out.println("请选择要进行的操作: ");
                System.out.println("1. 修改商品名称");
                System.out.println("2. 修改商品价格");
                System.out.println("3. 修改商品介绍");
                System.out.println("0. 返回上级菜单");
                System.out.print("请输入选项：");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符

                switch (choice) {
                    case 1:
                        System.out.print("请输入新的商品名称：");
                        String newName = scanner.nextLine();
                        product.setName(newName);
                        saveProductData();
                        System.out.println("商品名称已更新。");
                        break;
                    case 2:
                        System.out.print("请输入新的商品价格：");
                        double newPrice = scanner.nextDouble();
                        scanner.nextLine(); // 消耗换行符
                        product.setPrice(newPrice);
                        saveProductData();
                        System.out.println("商品价格已更新。");
                        break;
                    case 3:
                        System.out.print("请输入新的商品介绍：");
                        String newDescription = scanner.nextLine();
                        product.setDescription(newDescription);
                        saveProductData();
                        System.out.println("商品介绍已更新。");
                        break;
                    case 0:
                        return; // 返回上级菜单
                    default:
                        System.out.println("无效选项，请重新输入。");
                }

                break;
            }
        }

        if (!found) {
            System.out.println("未找到商品 " + productName + " ，修改失败。");
        }
    }

    private static void deleteProduct(Scanner scanner) {
        System.out.print("请输入管理员密码：");
        String adminPassword = scanner.nextLine();
        if (!currentUser.verifyPassword(adminPassword)) {
            System.out.println("管理员密码验证失败，无法删除商品。");
            return;
        }

        System.out.print("请输入要删除的商品名称：");
        String productName = scanner.nextLine();

        boolean found = false;
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                found = true;
                products.remove(product);
                saveProductData();
                System.out.println("商品 " + productName + " 已成功删除。");
                break;
            }
        }

        if (!found) {
            System.out.println("未找到商品 " + productName + " ，删除失败。");
        }
    }

    private static void searchProduct(Scanner scanner) {
        System.out.print("请输入搜索关键字：");
        String keyword = scanner.nextLine();

        System.out.println("相关商品信息：");
        boolean found = false;
        for (Product product : products) {
            if (product.getName().contains(keyword) || product.getDescription().contains(keyword)) {
                found = true;
                System.out.println("名称：" + product.getName());
                System.out.println("价格：" + product.getPrice());
                System.out.println("介绍：" + product.getDescription());
                System.out.println();
            }
        }

        if (!found) {
            System.out.println("未找到与关键字匹配的商品。");
        }
    }

}
