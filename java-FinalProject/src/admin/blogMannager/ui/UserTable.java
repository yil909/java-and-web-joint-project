package admin.blogMannager.ui;
import admin.blogMannager.user.UserPortfolio;
import admin.blogMannager.user.UserWithCertainInformation;
import admin.blogMannager.web.API;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

public class UserTable extends JFrame {
    private JButton logout;
    private JButton delete;
    private UserPortfolio portfolio;
    private List<UserWithCertainInformation> usersCertainInfoList;

    private JTable jTable;

    private JButton login2;

    public UserTable() throws IOException, InterruptedException {
        initGui();
        setTitle("User Information");
        setPreferredSize(new Dimension(680, 320));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void initGui() throws IOException, InterruptedException {
        this.jTable = new JTable();
        JPanel buttonPanel = new JPanel();

        logout = new JButton("Logout");
        delete = new JButton("Delete");
        delete.setEnabled(false);
        login2 = new JButton("Login again");

        buttonPanel.add(delete);
        buttonPanel.add(logout);
        buttonPanel.add(login2);
        login2.setVisible(false);

        this.usersCertainInfoList = API.getInstance().getAllUsersList();

        this.portfolio = new UserPortfolio(usersCertainInfoList);

        UserTableModelAdapter userTableModelAdapter = new UserTableModelAdapter(this.portfolio);

        this.portfolio.addUserPortfolioListener(userTableModelAdapter);

        JScrollPane tablePane = new JScrollPane(jTable);

        jTable.setModel(userTableModelAdapter);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(tablePane, BorderLayout.CENTER);
        c.add(buttonPanel, BorderLayout.SOUTH);

        deleteBtnEnable(jTable, delete);

        logout.addActionListener(new ActionListener() {

            int statusCode = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    statusCode = API.getInstance().logout();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                if (statusCode == 204){

                    jTable.setModel(new DefaultTableModel(null, new String[]{"ID", "Username", "First name", "Last name", "Email"}));
                    logout.setVisible(false);
                    login2.setVisible(true);
                    delete.setVisible(false);
                }
            }
        });

        login2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c.setVisible(false);
                Login.createAndShowGUI();
            }
        });
    }

    public void deleteBtnEnable(JTable jTable, JButton deleteBtn) {
        jTable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e) {
                int rowIndex= jTable.getSelectedRow();
                Object valueAt = jTable.getValueAt(rowIndex, 0);
                String userID = valueAt.toString();
                deleteBtn.setEnabled(true);
                try {
                    deleteData(Integer.parseInt(userID), jTable);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void deleteData(int userID, JTable jTable) throws IOException, InterruptedException {
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    API.getInstance().deleteUser(userID);

                    for (int i = 0; i < usersCertainInfoList.size(); i++) {
                        UserWithCertainInformation user  = usersCertainInfoList.get(i);
                        if (user.getId() == userID){
                            usersCertainInfoList.remove(user);
                        }
                    }

                    portfolio.adjustTable();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                delete.setEnabled(false);
            }
        });
    }

    public static void start(){
        SwingUtilities.invokeLater(() -> {
            UserTable userTable = null;
            try {
                userTable = new UserTable();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            userTable.setVisible(true);
        });
    }
}
