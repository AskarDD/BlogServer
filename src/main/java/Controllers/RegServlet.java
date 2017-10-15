package Controllers;

import Model.POJOs.Message;
import Model.POJOs.User;
import Model.POJOs.UserDB;
import Model.dao.IUserDao;
import Model.dao.UserDao;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Сайида on 10.07.2017.
 */
public class RegServlet extends HttpServlet {
    @Autowired
    IUserDao iud;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        String body = req.getReader().readLine();
        UserDB userDB = gson.fromJson(body,UserDB.class);
        Message message;
        if (userDB != null) {
            int status = iud.setUserDB(userDB);
            String mess = null;
            switch (status){
                case 0: mess = "Request was successful"; break;
                case 1: mess = "Пользователь с таким логином уже существует"; break;
                case 2: mess = "Ошибка исполнения на сервере.\nПроносим Вам свои извенения"; break;
            }
            message = new Message(mess, status);
        } else {
            message = new Message("Ошибка отправки запроса.\nПожалуйста попробуйте еще раз.", 3);
        }
        String json = gson.toJson(message);

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
