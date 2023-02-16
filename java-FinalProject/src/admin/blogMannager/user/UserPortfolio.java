package admin.blogMannager.user;
import admin.blogMannager.ui.UserListener;
import java.io.IOException;
import java.util.List;

public class UserPortfolio {
    private List<UserWithCertainInformation> userList;
    private UserListener _listener;

    public UserPortfolio(List<UserWithCertainInformation> userList) {
        this.userList = userList;
    }
    public void adjustTable(){
        _listener.update();
    }

    public void addUserPortfolioListener(UserListener listener) {
        _listener = listener;
    }

    public List<UserWithCertainInformation> getUserList() {
        return userList;
    }

    public void setUserList(List<UserWithCertainInformation> userList) {
        this.userList = userList;
    }

    public UserListener get_listener() {
        return _listener;
    }

    public void set_listener(UserListener _listener) {
        this._listener = _listener;
    }

    @Override
    public String toString() {
        return "UserPortfolio{" +
                "userList=" + userList +
                ", _listener=" + _listener +
                '}';
    }
}
