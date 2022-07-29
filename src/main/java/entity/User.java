package entity;

import java.io.Serializable;

public class User extends Entity implements Serializable {

    private String username;
    private String passwordHash;
    private int role;
    private int accStatus;

    public User(String username, String passwordHash){
        this.username = username;
        this.passwordHash = passwordHash;
    }
    public User(int id, String username, String passwordHash, int roleId, int accStatus){
        super(id);
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = roleId;
        this.accStatus = accStatus;
    }

    public int getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(int accStatus) {
        this.accStatus = accStatus;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public int getRole() {
        return role;
    }

}
