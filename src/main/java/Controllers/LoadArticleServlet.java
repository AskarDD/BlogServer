package Controllers;

import Model.POJOs.Account;
import Model.POJOs.Article;
import Model.POJOs.ArticleDB;
import Model.dao.IArticleDao;
import Model.dao.IUserDao;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Сайида on 17.07.2017.
 */
public class LoadArticleServlet extends HttpServlet {
    @Autowired
    IUserDao iud;
    @Autowired
    IArticleDao iad;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        List<Article> articles = iad.getArticles(login);

        Gson gson = new Gson();
        String json = gson.toJson(articles);
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String body = req.getReader().readLine();
        ArticleDB articleDB = gson.fromJson(body, ArticleDB.class);
        boolean permission = iud.validation(articleDB.getAccount().getLogin(), articleDB.getAccount().getPassword());
        if (permission){
            iad.addArticle(articleDB.getAccount().getLogin(), articleDB);
            Article article = new Article(articleDB.getTitle(), articleDB.getText());
            String json = gson.toJson(article);
            resp.setCharacterEncoding("utf-8");
            resp.getWriter().write(json);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }
}
