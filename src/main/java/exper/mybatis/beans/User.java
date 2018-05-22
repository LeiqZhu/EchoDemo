package exper.mybatis.beans;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String userName;
    private String password;
    private Double account;

    public User() {
        super();
    }

    public User(String username, String password, Double account) {
        super();
        this.userName = username;
        this.password = password;
        this.account = account;
    }

    public User(Integer id, String username, String password, Double account) {
        super();
        this.id = id;
        this.userName = username;
        this.password = password;
        this.account = account;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "UserBean [id=" + id + ", username=" + userName + ", password="
                + password + ", account=" + account + "]";
    }




}
