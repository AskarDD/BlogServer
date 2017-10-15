package Service;

import Model.POJOs.User;

/**
 * Created by Сайида on 11.07.2017.
 */
public interface IAuthorizationService {
    User auth(String login, String password);
    boolean validation(String login, String password);
}
