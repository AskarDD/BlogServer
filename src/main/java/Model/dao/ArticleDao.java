package Model.dao;

import Model.POJOs.Article;
import Model.connection.ConnectionManager;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Сайида on 17.07.2017.
 */
@Component
public class ArticleDao implements IArticleDao {
    final static Logger LOGGER = Logger.getLogger(ConnectionManager.class);
    static {
        BasicConfigurator.configure();
    }

    @Override
    public Article getArticle(String login, String title) {
        Connection connection = ConnectionManager.getConnection();
        Article article = null;
        ResultSet resultSet;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM articles WHERE articles.login=? AND articles.title=?;");
            statement.setString(1, login);
            statement.setString(2, title);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                article = new Article(resultSet.getString("title"), resultSet.getString("text"));
                break;
            }
            connection.close();
        } catch (SQLException e) {
            LOGGER.debug("Ошибка запросов к БД при запросе статьи автора \"" + login + "\" c заголовком \"" + title + "\"");
            e.printStackTrace();
        }
        return article;
    }

    @Override
    public List<Article> getArticles(String login) {
        Connection connection = ConnectionManager.getConnection();
        List<Article> articles = new ArrayList<>();
        ResultSet resultSet;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM articles WHERE articles.login=?;");
            statement.setString(1, login);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                articles.add(new Article(resultSet.getString("title"), resultSet.getString("text")));
            }
            connection.close();
        } catch (SQLException e) {
            LOGGER.debug("Ошибка запросов к БД при запросе статей автора \"" + login + "\"");
            e.printStackTrace();
        }
        return articles;
    }

    @Override
    public void addArticle(String login, Article article) {
        Article art = getArticle(login, article.getTitle());
        Connection connection = ConnectionManager.getConnection();
        try {
            PreparedStatement statement = null;
            if (art == null) {
                statement = connection.prepareStatement(
                        "INSERT INTO articles (login, title, text) VALUES (?,?,?);"
                );
                statement.setString(1, login);
                statement.setString(2, article.getTitle());
                statement.setString(3, article.getText());
            }else{
                statement = connection.prepareStatement(
                        "UPDATE articles SET text=? WHERE login=? AND title=?;"
                );
                statement.setString(1, article.getText());
                statement.setString(2, login);
                statement.setString(3, article.getTitle());
            }
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.debug("Ошибка запросов к БД в методе addArticle");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteArticle(String login, String title) {
        Connection connection = ConnectionManager.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM articles WHERE login=? AND title=?;"
            );
            statement.setString(1,login);
            statement.setString(2,title);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            LOGGER.debug("Ошибка запросов к БД в методе deleteArticle");
            e.printStackTrace();
        }

    }

    @Override
    public void update(String login, Article article) {

    }
}
