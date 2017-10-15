package Controllers;

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

public class DeleteArticleServlet extends HttpServlet {
    @Autowired
    IUserDao iud;
    @Autowired
    IArticleDao iad;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String body = req.getReader().readLine();
        ArticleDB articleDB = gson.fromJson(body, ArticleDB.class);
        boolean permission = iud.validation(articleDB.getAccount().getLogin(), articleDB.getAccount().getPassword());
        if (permission) {
            iad.deleteArticle(articleDB.getAccount().getLogin(), articleDB.getTitle());
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }
}
