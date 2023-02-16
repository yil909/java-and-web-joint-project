package admin.blogMannager.ui;
import admin.blogMannager.web.API;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Login extends JPanel implements ActionListener{
    private static JFrame frame;
    private JTextField username;
    private JPasswordField password;
    private JButton login;

    public Login() {
        JLabel inputUsername = new JLabel("Username");
        JLabel inputPassword = new JLabel("Password");
        login = new JButton("Login");
        username = new JTextField(15);
        password = new JPasswordField(15);
        login.addActionListener(this);
        GridBagLayout layoutMgr = new GridBagLayout();
        setLayout(layoutMgr);

        addComponent(inputUsername, layoutMgr, 0, 0, 1, 1, GridBagConstraints.WEST);
        addComponent(username, layoutMgr, 0, 1, 1, 1, GridBagConstraints.WEST);
        addComponent(inputPassword, layoutMgr, 1, 0, 1, 1, GridBagConstraints.WEST);
        addComponent(password, layoutMgr, 1, 1, 1, 1, GridBagConstraints.WEST);
        addComponent(login, layoutMgr, 1, 2, 1, 1, GridBagConstraints.CENTER);
    }

    private void addComponent (Component c, GridBagLayout layout, int row, int col, int width, int height, int alignment) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = col;
        constraints.gridy = row;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = alignment;
        layout.setConstraints(c,  constraints);
        add(c);
    }

    public static void createAndShowGUI() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent newContentPane = new Login();
        frame.add(newContentPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {

            String usernameInput = username.getText();
            String passwordInput = String.valueOf(password.getPassword());
            int statusCode = 0;
            try {
                statusCode = API.getInstance().login(usernameInput, passwordInput);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            if (statusCode == 204){
                frame.setVisible(false);
                frame.dispose();
                UserTable.start();
            }else {
                JOptionPane.showMessageDialog(frame, "Wrong username or password, please login again");
            }
        }
    }
}
