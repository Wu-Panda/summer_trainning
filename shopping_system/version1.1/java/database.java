import java.io.*;
import java.util.ArrayList;

public class database {
    static String users_path = "./version1.1/Database/txt/users.txt";
    static String products_path = "./version1.1/Database/txt/products.txt";
//    public static void main(String[] args) {
//        // 控制台输出防止中文乱码
//        OutputStream originalOut = System.out;
//        System.setOut(new PrintStream(originalOut, true, StandardCharsets.UTF_8));
//
//        // 在 main 方法中调用 readAdministrator() 和 readCustomers() 方法
//        ArrayList<administrator> administrators = readAdministrator();
//        ArrayList<customer> customers = readCustomers();
//
//        // 可以在这里进行后续操作，比如打印读取到的管理员和客户信息
//        for (administrator admin : administrators) {
//            System.out.println("Administrator: " + admin.getName() + ", " + admin.getPassword());
//        }
//        for (customer cust : customers) {
//            System.out.println("Customer: " + cust.getUsername() + ", " + cust.getPassword() + ", " + cust.getId());
//        }
//    }
    // 读取管理员信息
    // 读取管理员信息
    static ArrayList<administrator> readAdministrator() {
        ArrayList<administrator> administrators = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(users_path))) {
            String line;
            boolean isAdministratorSection = false;

            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("//Administrator")) {
                    isAdministratorSection = true;
                    continue;
                }

                if (isAdministratorSection && !line.trim().isEmpty()) {
                    if (line.startsWith("//example:")) {
                        continue; // Skip the example line
                    }

                    String[] adminInfo = line.split(",");
                    if (adminInfo.length == 2) {
                        String name = adminInfo[0];
                        String password = adminInfo[1];
                        administrators.add(new administrator(name, password));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return administrators;
    }
    // 读取客户信息
    static ArrayList<customer> readCustomers() {
        ArrayList<customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(users_path))) {
            String line;
            boolean isCustomerSection = false;

            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("//Customer")) {
                    isCustomerSection = true;
                    continue;
                }

                if (isCustomerSection && !line.trim().isEmpty()) {
                    if (line.startsWith("//example:")) {
                        continue; // Skip the example line
                    }

                    String[] custInfo = line.split(",");
                    if (custInfo.length == 8) {
                        String name = custInfo[0];
                        String password = custInfo[1];
                        String id = custInfo[2];
                        int level = Integer.parseInt(custInfo[3]);
                        String time = custInfo[4];
                        double expense = Double.parseDouble(custInfo[5]);
                        String phone = custInfo[6];
                        String email = custInfo[7];
                        customers.add(new customer(name, password, id, level, time, expense, phone, email));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customers;
    }
    // 读取商品信息
    static ArrayList<product> readProducts() {
        ArrayList<product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(products_path))) {
            String line;
            boolean isProductSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("//Products")) {
                    isProductSection = true;
                    continue;
                }

                if (isProductSection && !line.trim().isEmpty()) {
                    if (line.startsWith("//example:")) {
                        continue; // Skip the example line
                    }
                    // 根据数据格式进行分割
                    String[] fields = line.split(",");
                    if (fields.length == 8) {
                        String id = fields[0];
                        String name = fields[1];
                        String producer = fields[2];
                        String produceTime = fields[3];
                        String type = fields[4];
                        double purchasePrice = Double.parseDouble(fields[5]);
                        double retailPrice = Double.parseDouble(fields[6]);
                        int amount = Integer.parseInt(fields[7]);

                        // 创建 product 对象并添加到列表中
                        product newProduct = new product();
                        newProduct.setId(id);
                        newProduct.setName(name);
                        newProduct.setProducer(producer);
                        newProduct.setProTime(produceTime);
                        newProduct.setType(type);
                        newProduct.setPurPrice(purchasePrice);
                        newProduct.setRetPrice(retailPrice);
                        newProduct.setAmount(amount);
                        products.add(newProduct);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for(product pro : products)
//            System.out.println(pro);
        return products;
    }
//    public static void main(String[] args) {
//        // 控制台输出防止中文乱码
//        OutputStream originalOut = System.out;
//        System.setOut(new PrintStream(originalOut, true, StandardCharsets.UTF_8));
//        ArrayList<product> products = database.readProducts();
//    }
}
