package Model.dao;

import Model.POJOs.Article;

import java.util.List;

/**
 * Created by Сайида on 17.07.2017.
 */
public interface IArticleDao {
    Article getArticle(String login, String title);
    List<Article> getArticles(String login);
    void addArticle(String login, Article article);
    void deleteArticle(String login, String title);
    void update(String login, Article article);
}
