import java.io.*;
import java.util.ArrayList;

public class Database {
    static String adminPath = "./version1.2/Database/excel/administrator.csv";
    static String customerPath = "./version1.2/Database/excel/customer.csv";
    static String productsPath = "./version1.2/Database/excel/products.csv";

    static ArrayList<administrator> readAdministrators() {
        ArrayList<administrator> administrators = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(adminPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] adminInfo = line.split(",");
                    String name = adminInfo[0];
                    String password = adminInfo[1];
                    administrators.add(new administrator(name, password));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return administrators;
    }

    static ArrayList<customer> readCustomers() {
        ArrayList<customer> customers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(customerPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] custInfo = line.trim().split(",");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customers;
    }

    static ArrayList<product> readProducts() {
        ArrayList<product> products = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(productsPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.trim().split(",");
                String id = fields[0];
                String name = fields[1];
                String producer = fields[2];
                String produceTime = fields[3];
                String type = fields[4];
                double purchasePrice = Double.parseDouble(fields[5]);
                double retailPrice = Double.parseDouble(fields[6]);
                int amount = Integer.parseInt(fields[7]);

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }
}
