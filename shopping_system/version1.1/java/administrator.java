import java.util.Scanner;
import java.util.regex.Pattern;

// 管理员
class administrator extends user{
    Scanner scanner = new Scanner(System.in);
    private final int type = 0; // 用户属性
    private String name; // 用户名称
    private String password; // 用户密码
    administrator(String username, String password){
        this.name = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    administrator() {
        super();
    }

    // 密码管理
        // 管理员-修改自身密码
        void changeAdminPassword(){
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
                for (administrator admin : system.administrators) {
                    if (admin.getName().equals(system.getUsername())) {
                        admin.setPassword(newPassword);
                        break;
                    }
                }
            }
        // 管理员-重置用户密码
        void resetCustomersPassword(){
            // 展示所有用户
            System.out.println("当前全部客户名称信息: ");
            int i = 1;
            for(customer cust : system.customers){
                System.out.println("Customer[" + i + "] ->" + cust.getUsername());
                i++;
            }
            System.out.print("请输入要重置密码的用户的客户名: ");
            String username = scanner.nextLine();
            boolean flag = false;
            for(customer cust : system.customers){
                if (cust.getUsername().equals(username)){
                    flag = true;
                    cust.setPassword("Ynu123456.");
                    System.out.println("修改成功!");
                    break;
                }
            }
            if(!flag)
                System.out.println("未找到该用户!");
        }
    // 客户管理
        // 管理员-列出所有客户信息
        void listAllCustomers(){
            if (system.customers == null){
                System.out.println("暂无客户信息");
                return;
            }
            // 展示所有用户
            System.out.println("当前全部客户名称信息: ");
            int i = 1;
            for(customer cust : system.customers){
                System.out.println("Customer[" + i + "] ->" + cust);
                i++;
            }
        }
        // 管理员-删除客户信息
        void deleteCustomer(){
            // 展示所有用户
            System.out.println("当前全部用户名称信息: ");
            int i = 1;
            for(customer cust : system.customers){
                System.out.println("Customer[" + i + "] ->" + cust.getUsername());
                i++;
            }
            System.out.print("请输入要删除的用户的用户名: ");
            String username = scanner.nextLine();
            // 检查用户名是否存在
            boolean found = false;
            customer customerToDelete = null;
            for (customer customer : system.customers) {
                if (customer.getUsername().equals(username)) {
                    found = true;
                    customerToDelete = customer;
                    break;
                }
            }
            if (found) {
                // 给出警告提示
                System.out.print("确定要删除用户 " + username + " 吗?(Y?): ");
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("Y")) {
                    system.customers.remove(customerToDelete);
                    System.out.println("用户已删除。");
                } else {
                    System.out.println("删除操作已取消。");
                }
            } else {
                System.out.println("未找到用户名。");
            }
        }
        // 管理员-查询客户信息
        void searchCustomer(){
            System.out.println("请选择查询方式:");
            System.out.println("1. 根据ID查询");
            System.out.println("2. 根据用户名查询");
            System.out.println("3. 查询所有客户");
            System.out.print("请输入选项:");
            String str = scanner.nextLine();
            while(normalized_Input(str)){
                System.out.print("请重新输入选项:");
                str = scanner.nextLine();
            }
            int choice = Integer.parseInt(str);
            switch (choice){
                case 1 -> {
                    System.out.print("请输入要查询的客户ID: ");
                    String idQuery = scanner.nextLine();
                    searchById(idQuery);
                }
                case 2 -> {
                    System.out.print("请输入要查询的客户用户名: ");
                    String usernameQuery = scanner.nextLine();
                    searchByUsername(usernameQuery);
                }
                case 3 -> searchAllCustomers();
                default -> {
                    System.out.println("无效选项...");
                    searchCustomer();
                }
            }
        }
            // 根据用户ID查找
            private static void searchById(String id) {
                for (customer customer : system.customers) {
                    if (customer.getId().equals(id)) {
                        System.out.println("查询结果:");
                        System.out.println(customer);
                        return;
                    }
                }
                System.out.println("未找到对应ID的客户。");
            }
            // 根据用户名称查找
            private static void searchByUsername(String username) {
                for (customer customer : system.customers) {
                    if (customer.getUsername().equals(username)) {
                        System.out.println("查询结果:");
                        System.out.println(customer);
                        return;
                    }
                }
                System.out.println("未找到对应用户名的客户。");
            }
            // 查找所有用户
            private static void searchAllCustomers() {
                System.out.println("查询结果:");
                for (customer customer : system.customers) {
                    System.out.println(customer);
                }
            }
            // 规范话输入
            boolean normalized_Input(String str){
                if(str.matches("\\d+"))
                    return false;
                else {
                    System.out.println("输入不合规范");
                    return true;
                }
            }
    // 商品管理
        // 管理员-列出所有商品信息
        void listAllProducts(){
            if (system.customers == null){
                System.out.println("暂无客户信息");
                return;
            }
            System.out.println("所有商品信息:");
            for(product pro : system.products){
                System.out.println(pro);
            }
        }
        // 管理员-添加商品信息
        void addProduct(){
            System.out.println("请输入商品信息:");
            System.out.print("商品编号: ");
            String id = scanner.nextLine();
            System.out.print("商品名称: ");
            String name = scanner.nextLine();
            System.out.print("生产厂家: ");
            String producer = scanner.nextLine();
            System.out.print("生产日期: ");
            String productionDate = scanner.nextLine();
            System.out.print("型号: ");
            String type = scanner.nextLine();
            System.out.print("进货价: ");
            double purchasePrice = scanner.nextDouble();
            System.out.print("零售价格: ");
            double retailPrice = scanner.nextDouble();
            System.out.print("数量: ");
            int amount = scanner.nextInt();
            product product = new product(id, name, producer, productionDate, type,
                    purchasePrice, retailPrice, amount);
            system.products.add(product);
            System.out.println("商品信息已添加。");
        }
        // 管理员-修改商品信息
        void updateProduct(){
            System.out.print("请输入要修改的商品编号: ");
            String idToModify = scanner.nextLine();

            product targetProduct = null;
            for (product product : system.products) {
                if (product.getId().equals(idToModify)) {
                    targetProduct = product;
                    break;
                }
            }
            if (targetProduct == null) {
                System.out.println("找不到指定编号的商品。");
                return;
            }
            System.out.println("请输入修改后的商品信息:");
            System.out.print("商品名称: ");
            String name = scanner.nextLine();
            System.out.print("生产厂家: ");
            String producer = scanner.nextLine();
            System.out.print("生产日期: ");
            String productionDate = scanner.nextLine();
            System.out.print("型号: ");
            String type = scanner.nextLine();
            System.out.print("进货价: ");
            double purchasePrice = scanner.nextDouble();
            System.out.print("零售价格: ");
            double retailPrice = scanner.nextDouble();
            System.out.print("数量: ");
            int amount = scanner.nextInt();
            targetProduct.updateInfo(name, producer, productionDate, type, purchasePrice, retailPrice, amount);
            System.out.println("商品信息已修改。");
        }
        // 管理员-删除商品信息
        void deleteProduct(){
            System.out.print("请输入要删除的商品编号: ");
            String idToDelete = scanner.nextLine();
            product targetProduct = null;
            for (product product : system.products) {
                if (product.getId().equals(idToDelete)) {
                    targetProduct = product;
                    break;
                }
            }
            if (targetProduct == null) {
                System.out.println("找不到指定编号的商品。");
                return;
            }
            System.out.println("您确定要删除以下商品吗？");
            System.out.println(targetProduct);
            System.out.print("请输入 'y' 确认删除，或其他任意键取消: ");
            String confirm = scanner.nextLine();
            if (confirm.equalsIgnoreCase("y")) {
                system.products.remove(targetProduct);
                System.out.println("商品已删除。");
            } else {
                System.out.println("删除操作已取消。");
            }
        }
        // 管理员-查询商品信息
        void searchProduct() {
            System.out.println("1. 根据商品名称查询");
            System.out.println("2. 根据生产厂家查询");
            System.out.println("3. 根据零售价格查询");
            System.out.println("4. 组合查询");
            System.out.print("请选择查询方式:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 清除输入缓冲区中的换行符
            switch (choice) {
                case 1 -> {
                    System.out.print("请输入商品名称: ");
                    String nameQuery = scanner.nextLine();
                    searchByName(nameQuery);
                }
                case 2 -> {
                    System.out.print("请输入生产厂家: ");
                    String producerQuery = scanner.nextLine();
                    searchByProducer(producerQuery);
                }
                case 3 -> {
                    System.out.print("请输入零售价格范围（例如：100-500）: ");
                    String priceRange = scanner.nextLine();
                    String[] prices = priceRange.split("-");
                    if (prices.length == 2) {
                        double minPrice = Double.parseDouble(prices[0]);
                        double maxPrice = Double.parseDouble(prices[1]);
                        searchByPriceRange(minPrice, maxPrice);
                    } else {
                        System.out.println("无效的价格范围格式。");
                    }
                }
                case 4 -> {
                    System.out.print("请输入商品名称: ");
                    String nameQueryCombined = scanner.nextLine();
                    System.out.print("请输入生产厂家: ");
                    String producerQueryCombined = scanner.nextLine();
                    System.out.print("请输入最低零售价格: ");
                    double minPriceCombined = scanner.nextDouble();
                    searchCombined(nameQueryCombined, producerQueryCombined, minPriceCombined);
                }
                default -> System.out.println("无效的选项。");
            }
        }
            // 按照名称查询
            public static void searchByName(String name) {
                for (product product : system.products) {
                    if (product.getName().contains(name)) {
                        System.out.println(product);
                    }
                }
            }
            // 按照生产商查询
            public static void searchByProducer(String producer) {
                for (product product : system.products) {
                    if (product.getProducer().contains(producer)) {
                        System.out.println(product);
                    }
                }
            }
            // 按照价格范围查询
            public static void searchByPriceRange(double minPrice, double maxPrice) {
                for (product product : system.products) {
                    if (product.getRetPrice() >= minPrice && product.getRetPrice() <= maxPrice) {
                        System.out.println(product);
                    }
                }
            }
            // 按照多约束查询
            public static void searchCombined(String name, String producer, double minPrice) {
                for (product product : system.products) {
                    if (product.getName().contains(name) && product.getProducer().contains(producer)
                            && product.getRetPrice() >= minPrice) {
                        System.out.println(product);
                    }
                }
            }
}