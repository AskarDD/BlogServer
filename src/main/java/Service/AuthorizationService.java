package Service;

import Model.POJOs.User;
import Model.dao.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Сайида on 06.07.2017.
 */

@Component
public class AuthorizationService implements IAuthorizationService {
    @Autowired
    IUserDao iud;

    public User auth (String login, String password){
        return iud.getUserByLoginAndPassword(login,password);
    }

    public boolean validation(String login, String password){

        return false;
    }
}
