package entities;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String login;
    private String password;
    private Status status;
    private String strstatus;

    public User() {
    }

    public User(int id, String login, String password, Status status) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.status = status;

        if(status == Status.NOT_BANNED)
            strstatus = "Активен";
        else
            strstatus = "Заблокирован";
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        if(status == Status.NOT_BANNED)
            strstatus = "Активен";
        else
            strstatus = "Заблокирован";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStrstatus() {return strstatus;}

    public void setStrstatus(String fullName) {}
}
