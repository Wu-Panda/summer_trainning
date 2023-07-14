import java.awt.*;
import javax.swing.*;
import java.sql.*;


public class LoginAction {
    public static String id;
    private static final String DB_URL = "jdbc:sqlite:data.db";

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
        InitializeDatabase.importDataAndExport();
        JFrame frame = new JFrame("登录");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 606);
        frame.setLayout(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JLabel usernameTextLabel = new JLabel("<html><b><font color='white'>用户</font></b></html>");
        JTextField usernameField = new JTextField(10);
        usernameTextLabel.setFont(usernameTextLabel.getFont().deriveFont(22f));
        usernameField.setFont(usernameField.getFont().deriveFont(22f));

        JLabel passwordTextLabel = new JLabel("<html><b><font color='white'>密码</font></b></html>");
        JTextField passwordField = new JTextField(10);
        passwordTextLabel.setFont(passwordTextLabel.getFont().deriveFont(22f));
        passwordField.setFont(passwordField.getFont().deriveFont(22f));

        JButton loginButton = new JButton("登录");
        JButton registerButton = new JButton("注册");
        loginButton.setFont(loginButton.getFont().deriveFont(22f));
        registerButton.setFont(registerButton.getFont().deriveFont(22f));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(usernameTextLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(usernameField, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(passwordTextLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(passwordField, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 0, 0, 0);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        panel.add(buttonPanel, constraints);

        JLabel background = new JLabel();
        background.setOpaque(true);
        background.setBackground(new Color(0, 0, 0, 0));

        ImageIcon backgroundImage = new ImageIcon(LoginAction.class.getResource("/MainBackground.jpg"));
        background.setIcon(backgroundImage);

        frame.add(background, BorderLayout.CENTER);
        background.setLayout(new GridBagLayout());
        background.add(panel);

        frame.setResizable(false);
        frame.setVisible(true);

        registerButton.addActionListener(e -> {
            RegisterAction registerAction = new RegisterAction();
            registerAction.createGUI();
            frame.dispose();
        });

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (authenticateUser(username, password)) {
                if(username.equals("admin")){
                    Administrator.createGUI();
                }
                else {
                    Users.createGUI();
                }
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "用户名或密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
            }
        });


    }

    //登录检测
    public static boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM users WHERE name = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // 用户名和密码匹配成功
                    id = username;
                    return true;
                } else {
                    // 用户名和密码不匹配
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("验证用户失败: " + e.getMessage());
            return false;
        }
    }
}