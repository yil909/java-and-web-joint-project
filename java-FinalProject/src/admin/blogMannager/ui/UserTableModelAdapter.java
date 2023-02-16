package admin.blogMannager.ui;

import admin.blogMannager.user.UserPortfolio;
import admin.blogMannager.user.UserWithCertainInformation;
import admin.blogMannager.web.API;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;


public class UserTableModelAdapter extends AbstractTableModel implements UserListener {
//    private List<UserWithCertainInformation> users;
    private UserPortfolio portfolio;

    public UserTableModelAdapter( UserPortfolio portfolio) {
        this.portfolio = portfolio;
        this.portfolio.addUserPortfolioListener(this);
    }

    private static String[] COLUMN_NAMES = {"ID", "Username", "First name", "Last name", "Email"};

    @Override
    public int getRowCount() {
        int rowCount = 0;
        try {
            rowCount = API.getInstance().getAllUsersList().size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        UserWithCertainInformation user = portfolio.getUserList().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return user.getId();
            case 1:
                return user.getUsername();
            case 2:
                return user.getFname();
            case 3:
                return user.getLname();
            case 4:
                return user.getEmail();
            default:
                throw new IllegalArgumentException("Unexpected columnIndex");
        }
    }

    @Override
    public String getColumnName(int index) {
        return COLUMN_NAMES[index];
    }

    @Override
    public void update() {
        fireTableDataChanged();
    }
}
