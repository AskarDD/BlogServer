package Model.dao;

import Model.POJOs.User;
import Model.POJOs.UserDB;
import Model.connection.ConnectionManager;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * Created by Сайида on 06.07.2017.
 */
@Component
public class UserDao implements IUserDao {
    final static Logger LOGGER = Logger.getLogger(ConnectionManager.class);
    static {
        BasicConfigurator.configure();
    }

    @Override
    public User getUserByLoginAndPassword(String login, String password){
        Connection connection = ConnectionManager.getConnection();
        User user = null;

        ResultSet resultSet;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM public.authorizationtable JOIN public.users " +
                            "ON (authorizationtable.login=users.login) WHERE authorizationtable.login='" + login +
                            "' AND authorizationtable.password='" + password + "';"
            );
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getString("login"),
                        resultSet.getInt("role")
                );
                user.setImageUri(resultSet.getString("imageUri"));
                break;
            }
            connection.close();
        } catch (SQLException e) {
            LOGGER.debug("Ошибка запросов к БД при запросе авторизации пользователя");
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean validation(String login, String password){
        Connection connection = ConnectionManager.getConnection();
        boolean valid = false;
        ResultSet resultSet;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM public.authorizationtable WHERE authorizationtable.login=? AND authorizationtable.password=?;"
            );
            statement.setString(1, login);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                valid = true;
                break;
            }
            connection.close();
        } catch (SQLException e) {
            LOGGER.debug("Ошибка запросов к БД при запросе авторизации пользователя");
            e.printStackTrace();
        }

        return valid;
    }

    @Override
    public boolean uploadImageUri(String login, String uri) {
        boolean valid = true;
        Connection connection = ConnectionManager.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET imageuri=? WHERE login=?;"
            );
            statement.setString(1, uri);
            statement.setString(2, login);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.debug("Ошибка запросов к БД загрузке Uri картинки");
            valid = false;
            e.printStackTrace();
        }

        return valid;
    }

    @Override
    public boolean loadImageUri(String login) {
        boolean valid = true;
        Connection connection = ConnectionManager.getConnection();
        ResultSet resultSet;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT users.imageuri FROM users WHERE login=?;"
            );
            statement.setString(1, login);
            statement.executeUpdate();
            statement.close();
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                valid = true;
                break;
            }
            connection.close();
        } catch (SQLException e) {
            LOGGER.debug("Ошибка запросов к БД запросе Uri картинки");
            valid = false;
            e.printStackTrace();
        }

        return valid;
    }

    @Override
    public int setUserDB(UserDB userDB){
        int validInsert = 0;
        User user = getUserByLoginAndPassword(userDB.getLogin(), userDB.getAccount().getPassword());
        if (user != null) {
            return 1;
        }else {
            Connection connection = ConnectionManager.getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO public.users (name, lastname, login, role) VALUES (?, ?, ?, ?);" +
                                "INSERT INTO public.authorizationtable (login, password) VALUES ('" +
                                userDB.getLogin() + "', '" +
                                userDB.getAccount().getPassword() + "');"
                );
                statement.setString(1, userDB.getName());
                statement.setString(2, userDB.getLastname());
                statement.setString(3, userDB.getLogin());
                statement.setInt(4, userDB.getRole());
                statement.executeUpdate();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                LOGGER.debug("Ошибка запросов к БД в методе setUserDB");
                validInsert = 2;
                e.printStackTrace();
            }
            return validInsert;
        }
    }

    @Override
    public boolean deleteUserDB(User user) {
        boolean validDelete = true;
        Connection connection = ConnectionManager.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM public.users WHERE login='"+user.getLogin()+"';" +
                            "DELETE FROM public.authorizationtable WHERE login='"+user.getLogin()+"';"
            );
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.debug("Ошибка запросов к БД в методе deleteUserDB");
            validDelete = false;
            e.printStackTrace();
        }
        return validDelete;
    }

}
