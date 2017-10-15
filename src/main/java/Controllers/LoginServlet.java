package Controllers;

import Model.POJOs.Account;
import Model.POJOs.Article;
import Model.POJOs.User;
import Model.dao.IArticleDao;
import Service.IAuthorizationService;
import com.google.gson.Gson;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Сайида on 06.07.2017.
 */
public class LoginServlet extends HttpServlet {
    final static Logger LOGGER = Logger.getLogger(LoginServlet.class);

    @Autowired
    IAuthorizationService ias;
    @Autowired
    IArticleDao iad;

    static {
        BasicConfigurator.configure();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String reqParam = req.getParameter("param");
        req.setAttribute("parameter", reqParam);

        getServletContext().getRequestDispatcher("/jsp_login.jsp").forward(req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String body = req.getReader().readLine();
        Account account = gson.fromJson(body, Account.class);

        User user = ias.auth(account.getLogin(), account.getPassword());
        if (user != null){
            user.setArticles(iad.getArticles(user.getLogin()));
        }

        String json = gson.toJson(user);
        req.getSession().setAttribute("isOuts", true);
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(json);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }
}
