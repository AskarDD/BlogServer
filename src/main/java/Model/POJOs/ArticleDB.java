package Model.POJOs;

import java.io.Serializable;

/**
 * Created by Сайида on 17.07.2017.
 */
public class ArticleDB extends Article implements Serializable {
    private Account account;
    public ArticleDB(String title, String text, Account account) {
        super(title, text);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
