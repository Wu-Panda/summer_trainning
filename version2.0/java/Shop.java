import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;

public class Shop {
    private static final String DB_URL = "jdbc:sqlite:data.db";
    private static final String PRODUCT_FILE_PATH = "../data/ProductDatabase.txt";
    private static final String BACKGROUND_IMAGE_PATH = "/ShopBackground.jpg";
    private static final String MEMORY_DATABASE_PATH = "../data/MemoryDatabase.txt";
    private static boolean isLastWindowClosed = false;

    private static JFrame frame;
    private static JPanel productPanel;
    private static JPanel cartPanel;
    private static JTextArea cartTextArea;
    private static double totalPrice = 0.0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            createGUI();
        });
    }

    public static void createGUI() {
        frame = new JFrame("周边Shop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 1000);
        frame.setLayout(new BorderLayout());
        //屏幕居中
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
        JLabel background = new JLabel();
        background.setOpaque(true);
        background.setBackground(new Color(0, 0, 0, 0));
        //背景图片
        ImageIcon backgroundImage = new ImageIcon(Shop.class.getResource(BACKGROUND_IMAGE_PATH));
        background.setIcon(backgroundImage);
        frame.add(background, BorderLayout.CENTER);

        // 返回按钮
        JButton backButton = new JButton("返回");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                Users.createGUI();
            }
        });

        // 设置返回按钮的约束条件
        GridBagConstraints gbcBackButton = new GridBagConstraints();
        gbcBackButton.gridx = 0;
        gbcBackButton.gridy = 0;
        gbcBackButton.anchor = GridBagConstraints.NORTHWEST;
        gbcBackButton.insets = new Insets(10, 10, 10, 10);

        // 创建一个面板作为返回按钮的容器
        JPanel backButtonPanel = new JPanel(new GridBagLayout());
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton, gbcBackButton);

        // 将返回按钮容器添加到主窗口
        frame.add(backButtonPanel, BorderLayout.NORTH);


        // 商品列表面板
        productPanel = new JPanel();
        productPanel.setOpaque(false);
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        JScrollPane productScrollPane = new JScrollPane(productPanel);

        // 购物车面板
        cartPanel = new JPanel();
        cartPanel.setOpaque(false);
        cartPanel.setLayout(new BorderLayout());
        JLabel cartLabel = new JLabel("购物车");
        cartTextArea = new JTextArea(10, 30);
        cartTextArea.setEditable(false);
        JScrollPane cartScrollPane = new JScrollPane(cartTextArea);
        cartPanel.add(cartLabel, BorderLayout.NORTH);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        JButton submitButton = new JButton("提交订单");
        JButton clearButton = new JButton("一键清除");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitOrder();
            }
        });
        clearButton.addActionListener(e ->{
            cartTextArea.setText("");
            totalPrice = 0.0;
        });
        cartPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建一个面板作为右侧容器
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        // 设置商品列表面板在右侧容器的位置和大小
        GridBagConstraints gbcProductPanel = new GridBagConstraints();
        gbcProductPanel.gridx = 0;
        gbcProductPanel.gridy = 0;
        gbcProductPanel.weighty = 0.75; // 占据75%的高度
        gbcProductPanel.fill = GridBagConstraints.BOTH;
        rightPanel.add(productScrollPane, gbcProductPanel);

        // 设置购物车面板在右侧容器的位置和大小
        GridBagConstraints gbcCartPanel = new GridBagConstraints();
        gbcCartPanel.gridx = 0;
        gbcCartPanel.gridy = 1;
        gbcCartPanel.weighty = 0.25; // 占据25%的高度
        gbcCartPanel.fill = GridBagConstraints.BOTH;
        rightPanel.add(cartPanel, gbcCartPanel);

        // 将右侧容器添加到主窗口的右侧
        frame.add(rightPanel, BorderLayout.EAST);


        loadProducts();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void loadProducts() {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement statement = conn.createStatement();
                 BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE_PATH))) {

                // 读取产品数据
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    String name = data[0].trim();
                    double price = Double.parseDouble(data[1].trim());
                    String description = data[2].trim();
                    insertProduct(conn, name, price, description);

                    // 创建商品面板
                    JPanel productEntry = new JPanel();
                    productEntry.setOpaque(false);
                    productEntry.setLayout(new BorderLayout());
                    JLabel nameLabel = new JLabel(name);
                    JLabel priceLabel = new JLabel("价格: $" + price);
                    JLabel descriptionLabel = new JLabel("介绍: " + description);
                    JButton addButton = new JButton("添加到购物车");
                    addButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addToCart(name, price);
                        }
                    });
                    productEntry.add(nameLabel, BorderLayout.NORTH);
                    productEntry.add(priceLabel, BorderLayout.CENTER);
                    productEntry.add(descriptionLabel, BorderLayout.SOUTH);
                    productEntry.add(addButton, BorderLayout.EAST);
                    productEntry.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    productPanel.add(productEntry);
                }
            } catch (Exception e) {
                System.out.println("加载产品数据失败: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("找不到SQLite的JDBC驱动程序: " + e.getMessage());
        }
    }

    public static void insertProduct(Connection conn, String name, double price, String description) throws SQLException {
        String insertQuery = "INSERT INTO product (name, price, description) VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = conn.prepareStatement(insertQuery)) {
            insertStatement.setString(1, name);
            insertStatement.setDouble(2, price);
            insertStatement.setString(3, description);
            insertStatement.executeUpdate();
        }
    }

    public static void addToCart(String name, double price) {
        cartTextArea.append(name + " - 价格: $" + price + "\n");
        totalPrice += price;
    }

    public static void submitOrder() {
        String orderInfo = "总价格: $" + totalPrice + " - 商品名称: " + cartTextArea.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement statement = conn.createStatement()) {


            String insertOrderQuery = "INSERT INTO memory (orderInfo) VALUES (?)";
            try (PreparedStatement insertStatement = conn.prepareStatement(insertOrderQuery)) {
                insertStatement.setString(1, orderInfo);
                insertStatement.executeUpdate();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMORY_DATABASE_PATH, true))) {
                writer.write(orderInfo);
                writer.newLine();
            }
        } catch (SQLException e) {
            System.out.println("提交订单失败: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("写入订单文件失败: " + e.getMessage());
        }

        JOptionPane.showMessageDialog(frame, "订单提交成功！总价: $" + totalPrice);
        cartTextArea.setText("");
        totalPrice = 0.0;
    }
}
