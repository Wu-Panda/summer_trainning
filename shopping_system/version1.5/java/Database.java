import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static final String adminPath = "./version1.5/Database/excel/administrator.csv";
    private static final String customerPath = "./version1.5/Database/excel/customer.csv";
    private static final String productsPath = "./version1.5/Database/excel/products.csv";
    private static final String databaseURL = "jdbc:sqlite:D:/CodeApps/IntelliJ IDEA/Program/shopping_system/version3.0/Database/data";

    private static Connection connection;

    public static void initializeDatabase() throws SQLException {
        // 创建或连接到数据库
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(databaseURL);
        }catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        // 创建管理员表
        String createAdminTableQuery = "CREATE TABLE IF NOT EXISTS Administrators (name TEXT, password TEXT)";

        // 创建顾客表
        String createCustomerTableQuery = "CREATE TABLE IF NOT EXISTS Customers (name TEXT, password TEXT, id TEXT, " +
                "level INTEGER, time TEXT, expense REAL, phone TEXT, email TEXT)";

        // 创建产品表
        String createProductTableQuery = "CREATE TABLE IF NOT EXISTS Products (id TEXT, name TEXT, producer TEXT, " +
                "produceTime TEXT, type TEXT, purchasePrice REAL, retailPrice REAL, amount INTEGER)";
        try(Statement statement = connection.createStatement()){
            statement.execute(createAdminTableQuery);
            statement.execute(createCustomerTableQuery);
            statement.execute(createProductTableQuery);
            // 从文件中加载用户数据到数据库
            loadAdministrators(statement);
            loadCustomers(statement);
            // 从文件中加载商品数据到数据库
            loadProducts(statement);
        }
    }

    public static void loadAdministrators(Statement statement) {
        try (BufferedReader reader = new BufferedReader(new FileReader(adminPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] adminInfo = line.split(",");
                    String name = adminInfo[0];
                    String password = adminInfo[1];
                    // 检查数据库中是否已存在相同的用户名
                    String selectQuery = "SELECT name FROM Administrators  WHERE name = '" + name + "'";
                    ResultSet resultSet = statement.executeQuery(selectQuery);
                    if (resultSet.next()) {
                        // 用户名已存在，跳过插入操作
                        // System.out.println("用户名 " + name + " 已存在，跳过插入操作。");
                        continue;
                    }
                    String insertQuery = "INSERT INTO Administrators (name, password) " +
                            "VALUES ('" + name + "', '" + password + "')";
                    statement.executeUpdate(insertQuery);
                }
                System.out.println("用户数据已成功加载到数据库。");
                }
            } catch (IOException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void loadCustomers(Statement statement) {
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
                // 检查数据库中是否已存在相同的用户名
                String selectQuery = "SELECT name FROM Customers  WHERE name = '" + name + "'";
                ResultSet resultSet = statement.executeQuery(selectQuery);
                if (resultSet.next()) {
                    // 用户名已存在，跳过插入操作
                    // System.out.println("用户名 " + name + " 已存在，跳过插入操作。");
                    continue;
                }
                String insertQuery = "INSERT INTO Customers (name, password, id, level, time, expense, phone, email) " +
                        "VALUES ('" + name + "', '" + password + "', '" + id + "', '" + level +
                        "', '" + time + "', '" + expense + "', '" + phone + "', '" + email + "')";
                statement.executeUpdate(insertQuery);
            }
        }catch (IOException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void loadProducts(Statement statement) {
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
                String selectQuery = "SELECT name FROM Products  WHERE name = '" + name + "'";
                ResultSet resultSet = statement.executeQuery(selectQuery);
                if (resultSet.next()) {
                    // 商品名已存在，跳过插入操作
                    // System.out.println("商品 " + name + " 已存在，跳过插入操作。");
                    continue;
                }
                String insertQuery = "INSERT INTO Products (id, name, producer, `produceTime`, type, purchasePrice, retailPrice, amount)" +
                        " VALUES ('" + id + "', '" + name + "', '" + producer + "', '" + produceTime + "', '" + type + "', " +
                        "'" + purchasePrice + "', '" + retailPrice + "', '" + amount + "')";
                statement.executeUpdate(insertQuery);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<administrator> getAdministrators() {
        ArrayList<administrator> administrators = new ArrayList<>();

        String query = "SELECT name, password FROM administrators";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                administrators.add(new administrator(name, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return administrators;
    }

    public static ArrayList<customer> getCustomers() {
        ArrayList<customer> customers = new ArrayList<>();

        String query = "SELECT name, password, id, level, time, expense, phone, email FROM customers";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String id = resultSet.getString("id");
                int level = resultSet.getInt("level");
                String time = resultSet.getString("time");
                double expense = resultSet.getDouble("expense");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                customers.add(new customer(name, password, id, level, time, expense, phone, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public static ArrayList<product> getProducts() {
        ArrayList<product> products = new ArrayList<>();

        String query = "SELECT id, name, producer, produceTime, type, purchasePrice, retailPrice, amount FROM products";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String producer = resultSet.getString("producer");
                String produceTime = resultSet.getString("produceTime");
                String type = resultSet.getString("type");
                double purchasePrice = resultSet.getDouble("purchasePrice");
                double retailPrice = resultSet.getDouble("retailPrice");
                int amount = resultSet.getInt("amount");
                products.add(new product(id, name, producer, produceTime, type, purchasePrice, retailPrice, amount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
