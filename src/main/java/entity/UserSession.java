package entity;

public class UserSession extends Entity{

    private final String username;
    private final int roleId;

    private final int statusId;

    public UserSession(int id, String username, int roleId, int statusId){
        super(id);
        this.username = username;
        this.roleId = roleId;
        this.statusId = statusId;
    }

    public UserSession(User user){
        super(user.getId());
        this.username = user.getUsername();
        this.roleId = user.getRole();
        this.statusId = user.getAccStatus();
    }

    public String getUsername() {
        return username;
    }

    public int getRoleId() {
        return roleId;
    }

    public int getStatusId() {
        return statusId;
    }
}
