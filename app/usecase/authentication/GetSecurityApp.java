package usecase.authentication;

import models.SecurityApp;

/**
 * Created by Pedro Mendoza on 07-Jun-16.
 */
public class GetSecurityApp {

    public static SecurityApp byClientId(String clientId){
        SecurityApp app = SecurityApp.find.byId(clientId);
        return app;
    }
}
