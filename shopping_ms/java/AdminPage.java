import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

public class AdminPage {

    private static Connection connection;
    private static final String DB_URL = "jdbc:sqlite:data.db";
    private static BufferedWriter writer;
    private static boolean isLastWindowClosed = false;

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
        JFrame frame = new JFrame("欢迎来到云南大学周边购物！");
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
        JLabel passwordLabel = new JLabel("<html><b><font color='white'>密码</font></b></html>");
        JLabel personLabel = new JLabel("<html><b><font color='white'>用户</font></b></html>");
        JLabel shoppingLabel = new JLabel("<html><b><font color='white'>购物</font></b></html>");
        passwordLabel.setFont(passwordLabel.getFont().deriveFont(Font.BOLD, 26f));// 设置字体大小为26
        personLabel.setFont(personLabel.getFont().deriveFont(Font.BOLD, 26f));
        shoppingLabel.setFont(shoppingLabel.getFont().deriveFont(Font.BOLD, 26f));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 30, 0, 30);

        // 将密码标签添加到面板
        panel.add(passwordLabel, constraints);
        constraints.gridx = 3;
        // 将用户标签添加到面板
        panel.add(personLabel, constraints);
        constraints.gridx = 6;
        // 将购物标签添加到面板
        panel.add(shoppingLabel, constraints);

        // 创建密码操作按钮
        JButton changePasswordButton = new JButton("修改密码");
        JButton resetPasswordButton = new JButton("重置密码");
        JButton logoutButton = new JButton("登出");
        changePasswordButton.setFont(changePasswordButton.getFont().deriveFont(22f)); // 设置字体大小为22
        resetPasswordButton.setFont(resetPasswordButton.getFont().deriveFont(22f));
        logoutButton.setFont(logoutButton.getFont().deriveFont(22f));

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(10, 30, 0, 30);
        // 创建面板用于放置密码操作按钮
        JPanel passwordButtonPanel = new JPanel(new GridBagLayout());
        passwordButtonPanel.setOpaque(false);

        GridBagConstraints passwordButtonConstraints = new GridBagConstraints();
        passwordButtonConstraints.gridx = 0;
        passwordButtonConstraints.gridy = 0;
        passwordButtonConstraints.anchor = GridBagConstraints.LINE_START;
        passwordButtonConstraints.insets = new Insets(8, 30, 10, 30);

        // 将密码操作按钮添加到面板
        passwordButtonPanel.add(changePasswordButton, passwordButtonConstraints);

        passwordButtonConstraints.gridy = 1;
        passwordButtonPanel.add(resetPasswordButton, passwordButtonConstraints);

        passwordButtonConstraints.gridy = 2;
        passwordButtonPanel.add(logoutButton, passwordButtonConstraints);

        // 将密码操作按钮面板添加到面板
        panel.add(passwordButtonPanel, constraints);

        // 创建用户操作按钮
        JButton userInfoButton = new JButton("用户信息");
        JButton deleteInfoButton = new JButton("删除信息");
        JButton queryInfoButton = new JButton("查询信息");
        userInfoButton.setFont(userInfoButton.getFont().deriveFont(22f)); // 设置字体大小为22
        deleteInfoButton.setFont(deleteInfoButton.getFont().deriveFont(22f));
        queryInfoButton.setFont(queryInfoButton.getFont().deriveFont(22f));

        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.insets = new Insets(10, 30, 0, 30);

        // 创建面板用于放置用户操作按钮
        JPanel personButtonPanel = new JPanel(new GridBagLayout());
        personButtonPanel.setOpaque(false);
        GridBagConstraints personButtonPanelButtonConstraints = new GridBagConstraints();
        personButtonPanelButtonConstraints.gridx = 3;
        personButtonPanelButtonConstraints.gridy = 0;
        personButtonPanelButtonConstraints.anchor = GridBagConstraints.LINE_START;
        personButtonPanelButtonConstraints.insets = new Insets(8, 30, 10, 30);
        // 将购物操作按钮添加到面板
        personButtonPanel.add(userInfoButton, personButtonPanelButtonConstraints);
        personButtonPanelButtonConstraints.gridy = 1;
        personButtonPanel.add(deleteInfoButton, personButtonPanelButtonConstraints);
        personButtonPanelButtonConstraints.gridy = 2;
        personButtonPanel.add(queryInfoButton, personButtonPanelButtonConstraints);
        // 将购物操作按钮面板添加到面板
        panel.add(personButtonPanel, constraints);

        // 创建购物操作按钮
        JButton productInfoButton = new JButton("商品信息");
        JButton addProductButton = new JButton("添加商品");
        JButton modifyProductButton = new JButton("修改商品");
        JButton deleteProductButton = new JButton("删除商品");
        JButton queryProductButton = new JButton("查询商品");
        productInfoButton.setFont(productInfoButton.getFont().deriveFont(22f)); // 设置字体大小为22
        addProductButton.setFont(addProductButton.getFont().deriveFont(22f));
        modifyProductButton.setFont(modifyProductButton.getFont().deriveFont(22f));
        deleteProductButton.setFont(deleteProductButton.getFont().deriveFont(22f));
        queryProductButton.setFont(queryProductButton.getFont().deriveFont(22f));

        // 创建面板用于放置购物操作按钮
        JPanel shoppingButtonPanel = new JPanel(new GridBagLayout());
        shoppingButtonPanel.setOpaque(false);

        constraints.gridx = 6;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 30, 0, 30);

        GridBagConstraints shoppingButtonConstraints = new GridBagConstraints();
        shoppingButtonConstraints.gridx = 0;
        shoppingButtonConstraints.insets = new Insets(8, 5, 10, 5);

        // 将购物操作按钮添加到面板
        shoppingButtonPanel.add(productInfoButton, shoppingButtonConstraints);

        shoppingButtonConstraints.gridy = 1;
        shoppingButtonPanel.add(addProductButton, shoppingButtonConstraints);

        shoppingButtonConstraints.gridy = 2;
        shoppingButtonPanel.add(modifyProductButton, shoppingButtonConstraints);

        shoppingButtonConstraints.gridx = 1;
        shoppingButtonConstraints.gridy = 0;
        shoppingButtonPanel.add(deleteProductButton, shoppingButtonConstraints);

        shoppingButtonConstraints.gridy = 1;
        shoppingButtonPanel.add(queryProductButton, shoppingButtonConstraints);

        // 将购物操作按钮面板添加到面板
        panel.add(shoppingButtonPanel, constraints);

        // 创建一个JLabel并设置为透明
        JLabel background = new JLabel();
        background.setOpaque(true);
        background.setBackground(new Color(0, 0, 0, 0));

        // 加载背景图片
        ImageIcon backgroundImage = new ImageIcon(AdminPage.class.getResource("/MainBackground.jpg")); // 获取图片的位置

        // 设置背景图片
        background.setIcon(backgroundImage);

        // 将背景标签添加到窗口的内容面板
        frame.add(background, BorderLayout.CENTER);

        // 将面板添加到背景标签中
        background.setLayout(new GridBagLayout());
        background.add(panel);

        // 显示窗口
        frame.setVisible(true);

        // 建立与数据库的连接
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // 修改密码按钮的点击事件监听器
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 弹出对话框输入现在的密码
                String currentPassword = JOptionPane.showInputDialog(frame, "请输入现在的密码:");

                // 查询数据库中的现有密码
                String query = "SELECT password FROM users WHERE name = ?";
                try {
                    PreparedStatement queryStatement = connection.prepareStatement(query);
                    queryStatement.setString(1, UserLoginAction.id);
                    ResultSet resultSet = queryStatement.executeQuery();
                    if (resultSet.next()) {
                        String currentDBPassword = resultSet.getString("password");
                        if (!currentPassword.equals(currentDBPassword)) {
                            JOptionPane.showMessageDialog(frame, "现有密码不正确！");
                        } else {
                            // 输入新密码和确认密码
                            String newPassword1 = JOptionPane.showInputDialog(frame, "请输入新密码:");
                            String newPassword2 = JOptionPane.showInputDialog(frame, "请再次输入新密码:");

                            // 验证新密码两次输入是否一致
                            if (newPassword1.equals(newPassword2)) {
                                // 更新数据库中的密码
                                String updateQuery = "UPDATE users SET password=? WHERE name=?";
                                try {
                                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                                    updateStatement.setString(1, newPassword1);
                                    updateStatement.setString(2, UserLoginAction.id);
                                    updateStatement.executeUpdate();
                                    updateStatement.close();

                                    JOptionPane.showMessageDialog(frame, "密码修改成功！");
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                JOptionPane.showMessageDialog(frame, "新密码两次输入不一致！");
                            }
                        }
                    }
                    queryStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 重置密码按钮的点击事件监听器
        resetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 弹出对话框输入密码
                String adminPassword = JOptionPane.showInputDialog(frame, "请输入管理员密码:");

                // 验证管理员密码
                if (adminPassword != null && adminPassword.equals("admin")) {
                    // 重置数据库中所有用户的密码为123
                    try {
                        PreparedStatement statement = connection.prepareStatement("UPDATE users SET password=?");
                        statement.setString(1, "123");
                        statement.executeUpdate();
                        statement.close();
                        JOptionPane.showMessageDialog(frame, "密码重置成功！");
                        // 关闭数据库连接
                        try {
                            if (connection != null) {
                                connection.close();
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "管理员密码错误！");
                }
            }
        });

        // 添加登出按钮的点击事件
        logoutButton.addActionListener(e -> {
            // 弹出确认对话框
            int confirm = JOptionPane.showConfirmDialog
                    (frame, "确定登出？", "登出", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose(); // 关闭当前窗口
                UserLoginAction.main(null); // 调用登录页面的主函数重新打开登录页面
            }
        });

        // 用户信息按钮功能实现
        userInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建新窗口来显示用户信息
                JFrame userInfoFrame = new JFrame("用户信息");
                userInfoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                userInfoFrame.setSize(600, 300);

                // 获取屏幕的大小
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                // 计算窗口的位置坐标
                int x = (screenSize.width - userInfoFrame.getWidth()) / 2;
                int y = (screenSize.height - userInfoFrame.getHeight()) / 2;

                // 设置窗口的位置
                userInfoFrame.setLocation(x, y);
                // 查询数据库，获取所有用户的信息
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     Statement statement = conn.createStatement();
                     ResultSet resultSet = statement.executeQuery("SELECT name, password, authCode FROM users")) {

                    // 创建表格来显示用户信息
                    String[] columnNames = {"姓名", "密码", "授权码"};
                    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String password = resultSet.getString("password");
                        String authCode = resultSet.getString("authCode");
                        Object[] rowData = {name, password, authCode};
                        tableModel.addRow(rowData);
                    }
                    JTable table = new JTable(tableModel);

                    // 将表格添加到滚动面板中，并将滚动面板添加到窗口中
                    JScrollPane scrollPane = new JScrollPane(table);
                    userInfoFrame.add(scrollPane);

                    // 显示窗口
                    userInfoFrame.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 删除信息按钮功能实现
        deleteInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 弹出对话框要求输入要删除用户的authcode
                String authCode = JOptionPane.showInputDialog(frame, "请输入要删除用户的authcode:");

                // 查询数据库，根据authcode查找对应的用户信息
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE authCode = ?")) {
                    statement.setString(1, authCode);
                    ResultSet resultSet = statement.executeQuery();

                    // 如果找到匹配的用户信息
                    if (resultSet.next()) {
                        String name = resultSet.getString("name");

                        // 弹出确认对话框进行确认删除操作
                        int option = JOptionPane.showConfirmDialog(frame, "确认删除用户 " + name + " 吗?", "确认删除", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            // 执行删除操作，从数据库中删除用户信息
                            PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM users WHERE authCode = ?");
                            deleteStatement.setString(1, authCode);
                            int rowsAffected = deleteStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(frame, "用户删除成功！");
                            } else {
                                JOptionPane.showMessageDialog(frame, "用户删除失败！");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "未找到匹配的用户信息！");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 查询信息按钮功能实现
        queryInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 弹出对话框要求输入要查询的用户信息
                String queryInfo = JOptionPane.showInputDialog(frame, "请输入要查询的用户信息:");

                // 查询数据库，根据输入的信息查询相关的用户信息
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE name LIKE ? OR password LIKE ? OR authCode LIKE ?")) {
                    statement.setString(1, "%" + queryInfo + "%");
                    statement.setString(2, "%" + queryInfo + "%");
                    statement.setString(3, "%" + queryInfo + "%");
                    ResultSet resultSet = statement.executeQuery();

                    // 如果找到匹配的用户信息
                    if (resultSet.next()) {
                        // 显示用户信息
                        StringBuilder userInfoBuilder = new StringBuilder();
                        do {
                            String name = resultSet.getString("name");
                            String password = resultSet.getString("password");
                            String authCode = resultSet.getString("authCode");
                            userInfoBuilder.append("姓名: ").append(name).append("\n");
                            userInfoBuilder.append("密码: ").append(password).append("\n");
                            userInfoBuilder.append("授权码: ").append(authCode).append("\n");
                            userInfoBuilder.append("--------------------\n");
                        } while (resultSet.next());
                        JOptionPane.showMessageDialog(frame, userInfoBuilder.toString());
                    } else {
                        JOptionPane.showMessageDialog(frame, "未找到匹配的用户信息！");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 添加按钮点击事件监听器
        // 商品信息展示
        productInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProductInfo();
            }
        });

        // 添加商品
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        // 修改商品
        modifyProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyProduct();
            }
        });

        // 删除商品
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });

        // 查询商品
        queryProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryProduct();
            }
        });
    }
    // 显示商品信息
    private static void showProductInfo() {
        try {
            String query = "SELECT * FROM product";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            JFrame infoFrame = new JFrame();
            infoFrame.setTitle("商品信息");
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            infoFrame.setSize(600, 400);
            // 获取屏幕的大小
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            // 计算窗口的位置坐标
            int x = (screenSize.width - infoFrame.getWidth()) / 2;
            int y = (screenSize.height - infoFrame.getHeight()) / 2;
            // 设置窗口的位置
            infoFrame.setLocation(x, y);
            // 创建表格模型
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Name");
            tableModel.addColumn("Price");
            tableModel.addColumn("Description");

            // 添加数据到表格模型
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("description");

                tableModel.addRow(new Object[]{name, price, description});
            }

            // 创建表格
            JTable table = new JTable(tableModel);

            // 将表格添加到滚动面板中
            JScrollPane scrollPane = new JScrollPane(table);

            // 将滚动面板添加到窗口中
            infoFrame.getContentPane().add(scrollPane);

            // 显示窗口
            infoFrame.setVisible(true);

            // 关闭结果集和语句
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 添加商品
    private static void addProduct() {
        JFrame addFrame = new JFrame();
        addFrame.setTitle("添加商品");
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setSize(600, 300);

        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("名称:");
        JTextField nameTextField = new JTextField();
        JLabel priceLabel = new JLabel("价格:");
        JTextField priceTextField = new JTextField();
        JLabel descriptionLabel = new JLabel("描述:");
        JTextField descriptionTextField = new JTextField();

        JButton addButton = new JButton("确认");

        addPanel.add(nameLabel);
        addPanel.add(nameTextField);
        addPanel.add(priceLabel);
        addPanel.add(priceTextField);
        addPanel.add(descriptionLabel);
        addPanel.add(descriptionTextField);
        addPanel.add(addButton);

        addFrame.getContentPane().add(addPanel);
        // 获取屏幕的大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 计算窗口的位置坐标
        int x = (screenSize.width - addFrame.getWidth()) / 2;
        int y = (screenSize.height - addFrame.getHeight()) / 2;

        // 设置窗口的位置
        addFrame.setLocation(x, y);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                double price = Double.parseDouble(priceTextField.getText());
                String description = descriptionTextField.getText();

                try {
                    String query = "INSERT INTO product (name, price, description) VALUES (?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, name);
                    statement.setDouble(2, price);
                    statement.setString(3, description);

                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(addFrame, "商品添加成功！");

                    statement.close();
                    addFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        addFrame.setVisible(true);
    }

    // 修改商品
    private static void modifyProduct() {
        JFrame modifyFrame = new JFrame();
        modifyFrame.setTitle("修改商品");
        modifyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        modifyFrame.setSize(600, 300);
        // 获取屏幕的大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 计算窗口的位置坐标
        int x = (screenSize.width - modifyFrame.getWidth()) / 2;
        int y = (screenSize.height - modifyFrame.getHeight()) / 2;
        // 设置窗口的位置
        modifyFrame.setLocation(x, y);
        JPanel modifyPanel = new JPanel();
        modifyPanel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("名称:");
        JTextField nameTextField = new JTextField();
        JLabel attributeLabel = new JLabel("要更改的属性:");
        String[] attributes = {"Price", "Description", "Name"};
        JComboBox<String> attributeComboBox = new JComboBox<>(attributes);
        JLabel valueLabel = new JLabel("新值:");
        JTextField valueTextField = new JTextField();

        JButton modifyButton = new JButton("确认");

        modifyPanel.add(nameLabel);
        modifyPanel.add(nameTextField);
        modifyPanel.add(attributeLabel);
        modifyPanel.add(attributeComboBox);
        modifyPanel.add(valueLabel);
        modifyPanel.add(valueTextField);
        modifyPanel.add(modifyButton);

        modifyFrame.getContentPane().add(modifyPanel);

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String attribute = (String) attributeComboBox.getSelectedItem();
                String value = valueTextField.getText();

                try {
                    String updateQuery = "UPDATE product SET " + attribute + " = ? WHERE name = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, value);
                    updateStatement.setString(2, name);

                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(modifyFrame, "商品修改成功！");
                    } else {
                        JOptionPane.showMessageDialog(modifyFrame, "未找到指定商品！");
                    }

                    updateStatement.close();
                    modifyFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        modifyFrame.setVisible(true);
    }

    // 删除商品
    private static void deleteProduct() {
        JFrame deleteFrame = new JFrame();
        deleteFrame.setTitle("删除商品");
        deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteFrame.setSize(600, 300);
        // 获取屏幕的大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 计算窗口的位置坐标
        int x = (screenSize.width - deleteFrame.getWidth()) / 2;
        int y = (screenSize.height - deleteFrame.getHeight()) / 2;
        // 设置窗口的位置
        deleteFrame.setLocation(x, y);
        JPanel deletePanel = new JPanel();
        deletePanel.setLayout(new GridLayout(2, 2));

        JLabel nameLabel = new JLabel("名称:");
        JTextField nameTextField = new JTextField();

        JButton deleteButton = new JButton("确认");

        deletePanel.add(nameLabel);
        deletePanel.add(nameTextField);
        deletePanel.add(deleteButton);

        deleteFrame.getContentPane().add(deletePanel);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();

                try {
                    String deleteQuery = "DELETE FROM product WHERE name = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setString(1, name);

                    int rowsAffected = deleteStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(deleteFrame, "商品删除成功！");
                    } else {
                        JOptionPane.showMessageDialog(deleteFrame, "未找到指定商品！");
                    }

                    deleteStatement.close();
                    deleteFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteFrame.setVisible(true);
    }

    // 查询商品
    private static void queryProduct() {
        JFrame queryFrame = new JFrame();
        queryFrame.setTitle("查询商品");
        queryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        queryFrame.setSize(600, 300);
        // 获取屏幕的大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 计算窗口的位置坐标
        int x = (screenSize.width - queryFrame.getWidth()) / 2;
        int y = (screenSize.height - queryFrame.getHeight()) / 2;
        // 设置窗口的位置
        queryFrame.setLocation(x, y);
        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new GridLayout(2, 2));

        JLabel keywordLabel = new JLabel("关键字:");
        JTextField keywordTextField = new JTextField();

        JButton queryButton = new JButton("确认");

        queryPanel.add(keywordLabel);
        queryPanel.add(keywordTextField);
        queryPanel.add(queryButton);

        queryFrame.getContentPane().add(queryPanel);

        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = keywordTextField.getText();

                try {
                    String query = "SELECT * FROM product WHERE name LIKE ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, "%" + keyword + "%");
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        JFrame resultFrame = new JFrame();
                        resultFrame.setTitle("查询结果");
                        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        resultFrame.setBounds(100, 100, 600, 400);

                        DefaultTableModel tableModel = new DefaultTableModel();
                        tableModel.addColumn("Name");
                        tableModel.addColumn("Price");
                        tableModel.addColumn("Description");

                        do {
                            String name = resultSet.getString("name");
                            double price = resultSet.getDouble("price");
                            String description = resultSet.getString("description");

                            tableModel.addRow(new Object[]{name, price, description});
                        } while (resultSet.next());

                        JTable table = new JTable(tableModel);
                        JScrollPane scrollPane = new JScrollPane(table);

                        resultFrame.getContentPane().add(scrollPane);

                        resultFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(queryFrame, "未找到相关商品！");
                    }

                    resultSet.close();
                    statement.close();
                    queryFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        queryFrame.setVisible(true);
    }
}