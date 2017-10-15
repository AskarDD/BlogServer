package Model.dao;

import Model.POJOs.User;
import Model.POJOs.UserDB;

/**
 * Created by Сайида on 11.07.2017.
 */
public interface IUserDao {
    User getUserByLoginAndPassword(String login, String password);
    int setUserDB(UserDB userDB);
    boolean deleteUserDB(User user);
    boolean validation(String login, String password);
    boolean uploadImageUri(String login, String uri);
    boolean loadImageUri(String login);
}
