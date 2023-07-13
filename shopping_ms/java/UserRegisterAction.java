import java.awt.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class UserRegisterAction {
    private static final String DB_URL = "jdbc:sqlite:data.db";
    private static final String SELECT_USER_QUERY = "SELECT * FROM users WHERE name = ?";
    private static final String INSERT_USER_QUERY = "INSERT INTO users (name, password, authCode) VALUES (?, ?, ?)";
    private static final String EXPORT_FILE_PATH = "../data/UsersDatabase.txt";
    private static final String PRODUCT_EXPORT_FILE_PATH = "../data/ProductDatabase.txt";
    private static final String MEMORY_EXPORT_FILE_PATH = "../data/MemoryDatabase.txt";
    private static boolean isLastWindowClosed = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->{
            //设置样式
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            }catch(Exception e) {
                System.out.println(e);
            }
            createGUI();
        });
    }

    public static void createGUI() {
        JFrame frame = new JFrame("注册");//窗口名字
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

        // 创建用户名标签和输入框
        JLabel usernameTextLabel = new JLabel("<html><b><font color='white'>用户</font></b></html>");
        JTextField usernameField = new JTextField(10);
        usernameTextLabel.setFont(usernameTextLabel.getFont().deriveFont(22f)); // 设置字体大小为18
        usernameField.setFont(usernameField.getFont().deriveFont(22f)); // 设置字体大小为18

        // 创建密码标签和输入框
        JLabel passwordTextLabel = new JLabel("<html><b><font color='white'>密码</font></b></html>");
        JTextField passwordField = new JTextField(10);
        passwordTextLabel.setFont(passwordTextLabel.getFont().deriveFont(22f)); // 设置字体大小为18
        passwordField.setFont(passwordField.getFont().deriveFont(22f)); // 设置字体大小为18

        // 创建密码标签和输入框
        JLabel confirmTextLabel = new JLabel("<html><b><font color='white'>验证</font></b></html>");
        JTextField confirmField = new JTextField(10);
        confirmTextLabel.setFont(confirmTextLabel.getFont().deriveFont(22f)); // 设置字体大小为18
        confirmField.setFont(confirmField.getFont().deriveFont(22f)); // 设置字体大小为18

        // 创建注册和返回按钮
        JButton registerButton = new JButton("注册");
        JButton backButton = new JButton("返回");
        registerButton.setFont(registerButton.getFont().deriveFont(22f)); // 设置字体大小为18
        backButton.setFont(backButton.getFont().deriveFont(22f)); // 设置字体大小为18

        // 设置组件的居中对齐
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.insets = new Insets(5, 5, 5, 5);

        // 将用户名文本标签添加到面板
        panel.add(usernameTextLabel, constraints);

        // 设置组件的居中对齐
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(5, 5, 5, 5);

        // 将用户名输入框添加到面板
        panel.add(usernameField, constraints);

        // 设置组件的居中对齐
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.insets = new Insets(5, 5, 5, 5);

        // 将密码文本标签添加到面板
        panel.add(passwordTextLabel, constraints);

        // 设置组件的居中对齐
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(5, 5, 5, 5);

        // 将密码输入框添加到面板
        panel.add(passwordField, constraints);

        // 设置组件的居中对齐
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.insets = new Insets(5, 5, 5, 5);

        // 将验证码文本标签添加到面板
        panel.add(confirmTextLabel, constraints);

        // 设置组件的居中对齐
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(5, 5, 5, 5);

        // 将验证码输入框添加到面板
        panel.add(confirmField, constraints);

        // 设置组件的居中对齐
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 0, 0, 0);

        // 创建面板用于放置注册和返回按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        // 将注册和返回按钮添加到面板
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        // 将按钮面板添加到面板
        panel.add(buttonPanel, constraints);

        // 创建一个JLabel并设置为透明
        JLabel background = new JLabel();
        background.setOpaque(true);
        background.setBackground(new Color(0, 0, 0, 0));

        // 加载背景图片
        ImageIcon backgroundImage = new ImageIcon(UserRegisterAction.class.getResource("/MainBackground.jpg")); // 获取图片的位置

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

        // 在按钮面板中为返回按钮添加动作监听器
        backButton.addActionListener(e -> {
            // 创建新的Login对象以打开Login界面
            UserLoginAction userLoginAction = new UserLoginAction();
            userLoginAction.createGUI();
            // 关闭当前的Register界面
            isLastWindowClosed = true;
            frame.dispose();
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String authCode = confirmField.getText().trim(); // 从用户输入框中获取验证码

            if (username.isEmpty() || password.isEmpty() || authCode.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "请填写所有字段", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                // 检查用户名是否已存在
                if (checkUserExists(username)) {
                    JOptionPane.showMessageDialog(frame, "该用户名已存在", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    // 添加用户数据到数据库
                    if (addUser(username, password, authCode)) {
                        JOptionPane.showMessageDialog(frame, "注册成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                        // 跳转到登录界面
                        UserLoginAction.main(null);
                        isLastWindowClosed = true;
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "注册失败", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        //页面关闭检测
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                // 判断是否为最后一次关闭窗口
                if (isLastWindowClosed) {
                    // 清空文件
                    clearFile(EXPORT_FILE_PATH);
                    clearFile(PRODUCT_EXPORT_FILE_PATH);
                    clearFile(MEMORY_EXPORT_FILE_PATH);

                    // 将users表数据导出到文件
                    exportTableToFile("users", EXPORT_FILE_PATH);

                    // 将product表数据导出到文件
                    exportTableToFile("product", PRODUCT_EXPORT_FILE_PATH);

                    // 将memory表数据导出到文件
                    exportTableToFile("memory", MEMORY_EXPORT_FILE_PATH);
                }

            }
        });

    }

    // 检测用户是否存在
    private static boolean checkUserExists(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = conn.prepareStatement(SELECT_USER_QUERY)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("查询用户失败: " + e.getMessage());
        }
        return false;
    }

    // 添加用户至数据库
    private static boolean addUser(String username, String password, String authCode) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = conn.prepareStatement(INSERT_USER_QUERY);
            BufferedWriter writer = new BufferedWriter(new FileWriter(EXPORT_FILE_PATH, true))) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, authCode);
            int rowsAffected = statement.executeUpdate();
            // 用户添加成功，将用户信息写入UserDatabase.txt文件
            String userEntry = username + "," + password + "," + authCode + "\n";
            writer.write(userEntry);
            writer.flush(); // 刷新缓冲区，确保写入文件
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("添加用户失败: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 清空文件的方法
    private static void clearFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 清空文件内容
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 将数据库表数据导出到文件的方法
    private static void exportTableToFile(String tableName, String filePath) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 写入表头
            for (int i = 1; i <= columnCount; i++) {
                writer.write(metaData.getColumnName(i));
                if (i < columnCount) {
                    writer.write(",");
                }
            }
            writer.newLine();

            // 写入数据
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.write(resultSet.getString(i));
                    if (i < columnCount) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }

            System.out.println("数据导出成功：" + filePath);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

