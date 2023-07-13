import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UserPage {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 设置样式
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
        JFrame frame = new JFrame("欢迎来到云南大学周边购物 ！");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 606);
        frame.setLayout(new BorderLayout());

        // 获取屏幕的大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // 计算窗口的位置坐标
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;

        // 设置窗口的位置
        frame.setLocation(x, y);

        // 创建一个面板用于放置登录组件
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        // 创建个人和购物标签
        JLabel personLabel = new JLabel("<html><b><font color='white'>个人</font></b></html>");
        JLabel shoppingLabel = new JLabel("<html><b><font color='white'>购物</font></b></html>");
        personLabel.setFont(personLabel.getFont().deriveFont(26f)); // 设置字体大小为26
        shoppingLabel.setFont(shoppingLabel.getFont().deriveFont(26f)); // 设置字体大小为26

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(15, 30, 15, 30);

        // 将个人标签添加到面板
        panel.add(personLabel, constraints);

        constraints.gridx = 4;

        // 将购物标签添加到面板
        panel.add(shoppingLabel, constraints);

        // 创建个人操作按钮
        JButton changePasswordButton = new JButton("修改密码");
        JButton resetPasswordButton = new JButton("重置密码");
        JButton logoutButton = new JButton("登出");
        changePasswordButton.setFont(changePasswordButton.getFont().deriveFont(22f)); // 设置字体大小为22
        resetPasswordButton.setFont(resetPasswordButton.getFont().deriveFont(22f));
        logoutButton.setFont(logoutButton.getFont().deriveFont(22f));

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(10, 30, 10, 30);

        // 创建面板用于放置个人操作按钮
        JPanel personButtonPanel = new JPanel(new GridBagLayout());
        personButtonPanel.setOpaque(false);

        GridBagConstraints personButtonConstraints = new GridBagConstraints();
        personButtonConstraints.gridx = 0;
        personButtonConstraints.gridy = 0;
        personButtonConstraints.anchor = GridBagConstraints.LINE_START;
        personButtonConstraints.insets = new Insets(10, 30, 10, 30);

        // 将个人操作按钮添加到面板
        personButtonPanel.add(changePasswordButton, personButtonConstraints);

        personButtonConstraints.gridy = 1;
        personButtonPanel.add(resetPasswordButton, personButtonConstraints);

        personButtonConstraints.gridy = 2;
        personButtonPanel.add(logoutButton, personButtonConstraints);

        // 将个人操作按钮面板添加到面板
        panel.add(personButtonPanel, constraints);

        // 创建购物操作按钮
        JButton viewProductsButton = new JButton("查看商品");
        JButton viewHistoryButton = new JButton("历史纪录");
        viewProductsButton.setFont(viewProductsButton.getFont().deriveFont(22f)); // 设置字体大小为22
        viewHistoryButton.setFont(viewHistoryButton.getFont().deriveFont(22f));

        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.insets = new Insets(10, 30, 10, 30);

        // 创建面板用于放置购物操作按钮
        JPanel shoppingButtonPanel = new JPanel(new GridBagLayout());
        shoppingButtonPanel.setOpaque(false);

        GridBagConstraints shoppingButtonConstraints = new GridBagConstraints();
        shoppingButtonConstraints.gridx = 4;
        shoppingButtonConstraints.gridy = 0;
        shoppingButtonConstraints.anchor = GridBagConstraints.LINE_START;
        shoppingButtonConstraints.insets = new Insets(10, 30, 10, 30);

        // 将购物操作按钮添加到面板
        shoppingButtonPanel.add(viewProductsButton, shoppingButtonConstraints);

        shoppingButtonConstraints.gridy = 1;
        shoppingButtonPanel.add(viewHistoryButton, shoppingButtonConstraints);

        // 将购物操作按钮面板添加到面板
        panel.add(shoppingButtonPanel, constraints);

        // 创建一个JLabel并设置为透明
        JLabel background = new JLabel();
        background.setOpaque(true);
        background.setBackground(new Color(0, 0, 0, 0));

        // 加载背景图片
        ImageIcon backgroundImage = new ImageIcon(UserPage.class.getResource("/MainBackground.jpg")); // 获取图片的位置

        // 设置背景图片
        background.setIcon(backgroundImage);

        // 将背景标签添加到窗口的内容面板
        frame.add(background, BorderLayout.CENTER);

        // 将面板添加到背景标签中
        background.setLayout(new GridBagLayout());
        background.add(panel);

        // 设置窗口为不可缩放
        frame.setResizable(false);

        frame.setVisible(true);

        changePasswordButton.addActionListener(e -> {
            // 弹出对话框，让用户输入当前密码和新密码
            JPasswordField currentPasswordField = new JPasswordField();
            JPasswordField newPasswordField = new JPasswordField();
            Object[] fields = {"当前密码:", currentPasswordField, "新密码:", newPasswordField};
            int result = JOptionPane.showConfirmDialog(frame, fields, "修改密码", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String currentPassword = new String(currentPasswordField.getPassword());
                String newPassword = new String(newPasswordField.getPassword());
                if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    boolean success = updatePassword(currentPassword, newPassword, UserLoginAction.id); // 调用数据库操作方法更新密码
                    if (success) {
                        JOptionPane.showMessageDialog(frame, "密码修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "密码修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        resetPasswordButton.addActionListener(e -> {
            // 在这里编写重置密码的逻辑代码
            boolean success = resetPassword("123", UserLoginAction.id); // 将密码重置为固定值 "123"，并传递当前登录用户的用户名
            if (success) {
                JOptionPane.showMessageDialog(frame, "密码重置成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "密码重置失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });


        // 添加登出按钮的点击事件
        logoutButton.addActionListener(e -> {
            // 弹出确认对话框
            int confirm = JOptionPane.showConfirmDialog
                    (frame, "确定登出？", "登出", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // 在这里编写登出的逻辑代码
                frame.dispose(); // 关闭当前窗口
                UserLoginAction.main(null); // 调用登录页面的主函数重新打开登录页面
            }
        });

        // 添加查看商品按钮的点击事件
        viewProductsButton.addActionListener(e ->{
            // 在这里编写登出的逻辑代码
            frame.dispose(); // 关闭当前窗口
            ShopPage.main(null); // 调用登录页面的主函数重新打开登录页面
        });

        viewHistoryButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement statement = conn.createStatement()) {
                // 从 MemoryDatabase 中读取历史购买记录
                String selectQuery = "SELECT orderInfo FROM memory";
                ResultSet resultSet = statement.executeQuery(selectQuery);

                // 构建历史购买记录字符串
                StringBuilder historyBuilder = new StringBuilder();
                double totalAmount = 0.0;

                while (resultSet.next()) {
                    String orderInfo = resultSet.getString("orderInfo");
                    String[] orderInfoParts = orderInfo.split(" - ");
                    String amountString = orderInfoParts[0].replace("总价格: $", "");
                    double amount = Double.parseDouble(amountString);
                    totalAmount += amount;

                    String productNameString = orderInfoParts[1].replace("商品名称: ", "");
                    String[] productNames = productNameString.split("\n");

                    // 添加商品名称到历史购买记录字符串
                    for (String productName : productNames) {
                        historyBuilder.append(productName).append("\n");
                    }
                }

                // 构建历史购买记录的展示字符串
                StringBuilder displayBuilder = new StringBuilder();
                displayBuilder.append("总金额: $").append(totalAmount).append("\n");
                displayBuilder.append("商品名称:\n").append(historyBuilder.toString());

                // 弹出对话框展示历史购买记录
                JOptionPane.showMessageDialog(frame, displayBuilder.toString(), "历史购买记录", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                System.out.println("获取历史购买记录失败: " + ex.getMessage());
            }
        });

    }

    private static final String DB_URL = "jdbc:sqlite:data.db";
    private static final String UPDATE_PASSWORD_QUERY = "UPDATE users SET password = ? WHERE name = ?";
    private static final String RESET_PASSWORD_QUERY = "UPDATE users SET password = ?";
    private static final String AUTHENTICATION_QUERY = "SELECT * FROM users WHERE name = ? AND password = ?";

    private static boolean updatePassword(String currentPassword, String newPassword, String username) {
        // 首先验证当前密码是否正确
        if (!authenticateUser(username, currentPassword)) {
            System.out.println("当前密码验证失败");
            return false;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = conn.prepareStatement(UPDATE_PASSWORD_QUERY)) {
            statement.setString(1, newPassword);
            statement.setString(2, username);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("密码更新失败: " + e.getMessage());
        }
        return false;
    }

    private static boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = conn.prepareStatement(AUTHENTICATION_QUERY)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // 如果存在匹配的用户名和密码，则返回 true
            }
        } catch (SQLException e) {
            System.out.println("用户认证失败: " + e.getMessage());
        }
        return false;
    }

    private static boolean resetPassword(String newPassword, String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = conn.prepareStatement(UPDATE_PASSWORD_QUERY)) {
            statement.setString(1, newPassword);
            statement.setString(2, username);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("密码重置失败: " + e.getMessage());
        }
        return false;
    }
}
