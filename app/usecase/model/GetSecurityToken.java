package usecase.model;

import models.SecurityToken;

/**
 * Created by Pedro Mendoza on 07-Jun-16.
 */
public class GetSecurityToken {

    public static SecurityToken byToken(String token){
        return SecurityToken.find.fetch("user").where().eq("token",token).findUnique();
    }

}
