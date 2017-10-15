package Model.POJOs;

import java.io.Serializable;

/**
 * Created by Сайида on 10.07.2017.
 */
public class UserDB extends User implements Serializable {
    private Account account;
    public UserDB(String name, String lastname, String login, String password, int role) {
        super(name, lastname, login, role);
        this.account = new Account(login, password);
    }

    public Account getAccount() {
        return account;
    }
}
