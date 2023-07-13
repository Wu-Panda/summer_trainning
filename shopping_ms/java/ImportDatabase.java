import javafx.concurrent.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.*;

public class ImportDatabase {
    private static final String DB_URL = "jdbc:sqlite:data.db";
    private static final String EXPORT_FILE_PATH = "../data/UsersDatabase.txt";
    private static final String PRODUCT_EXPORT_FILE_PATH = "../data/ProductDatabase.txt";
    private static final String MEMORY_EXPORT_FILE_PATH = "../data/MemoryDatabase.txt";

    public static void importDataAndExport() {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement statement = conn.createStatement();) {

                // 清空数据表
                String truncateUsersTableQuery = "DELETE FROM users";
                String truncateProductTableQuery = "DELETE FROM product";
                statement.executeUpdate(truncateUsersTableQuery);
                statement.executeUpdate(truncateProductTableQuery);

                // 创建users表
                String createUsersTableQuery = "CREATE TABLE IF NOT EXISTS users (name TEXT, password TEXT, authCode TEXT)";
                statement.executeUpdate(createUsersTableQuery);
                System.out.println("users表创建成功");

                // 创建product表
                String createProductTableQuery = "CREATE TABLE IF NOT EXISTS product (name TEXT, price REAL, description TEXT)";
                statement.executeUpdate(createProductTableQuery);
                System.out.println("product表创建成功");

                // 创建memory表
                String createMemoryTableQuery = "CREATE TABLE IF NOT EXISTS memory (orderInfo TEXT)";
                statement.executeUpdate(createMemoryTableQuery);

                // 检查数据是否已导入
                if (isDataImported()) {
                    System.out.println("数据已导入，无需重复操作");
                    return;
                }
                // 读取导入文件
                try (BufferedReader reader = new BufferedReader(new FileReader(EXPORT_FILE_PATH))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");
                        String username = data[0].trim();
                        String password = data[1].trim();
                        String authCode = data[2].trim();
                        // 插入到数据库（users表）
                        String insertUserQuery = "INSERT INTO users (name, password, authCode) VALUES (?, ?, ?)";
                        try (PreparedStatement insertUserStatement = conn.prepareStatement(insertUserQuery)) {
                            insertUserStatement.setString(1, username);
                            insertUserStatement.setString(2, password);
                            insertUserStatement.setString(3, authCode);
                            insertUserStatement.executeUpdate();
                        }
                    }
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_EXPORT_FILE_PATH))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");
                        String name = data[0].trim();
                        double price = Double.parseDouble(data[1].trim());
                        String description = data[2].trim();
                        // 插入到数据库（product表）
                        String insertQuery = "INSERT INTO product (name, price, description) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStatement = conn.prepareStatement(insertQuery)) {
                            insertStatement.setString(1, name);
                            insertStatement.setDouble(2, price);
                            insertStatement.setString(3, description);
                            insertStatement.executeUpdate();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("数据导入失败: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("找不到SQLite的JDBC驱动程序: " + e.getMessage());
        }
    }

    public static boolean isDataImported() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users")) {

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (Exception e) {
            System.out.println("检查数据导入失败: " + e.getMessage());
        }

        return false;
    }
}
