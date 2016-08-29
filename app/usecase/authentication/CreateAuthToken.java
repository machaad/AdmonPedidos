package usecase.authentication;

import models.SecurityApp;
import models.SecurityToken;
import models.User;
import usecase.GetRandomString;

/**
 * Created by Pedro Mendoza on 07-Jun-16.
 */
public class CreateAuthToken {

    public static SecurityToken create(User user, SecurityApp app){
        SecurityToken token = SecurityToken.find.where()
                .eq("user",user)
                .eq("app",app)
                .findUnique();
        if(token == null){
            token = new SecurityToken();
            token.setApp(app);
            token.setUser(user);
            token.setCreatedBy(user);
            token.setUpdatedBy(user);
            token.setToken(GetRandomString.get());
        }
        return token;
    }

}
