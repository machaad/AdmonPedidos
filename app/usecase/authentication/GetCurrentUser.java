package usecase.authentication;

import models.SecurityToken;
import models.User;
import play.mvc.Http;
import usecase.model.GetSecurityToken;

/**
 * Created by Pedro Mendoza on 07-Jun-16.
 */
public class GetCurrentUser {
    public static final String CONTEXT_USER = "context_user";
    public static final String AUTH_AUTHORIZATION = "Authorization";


    public static User execute(){
        try {
            return GetCurrentUser.execute(Http.Context.current());
        }catch (Exception e){
            //NA
        }
        return null;
    }

    public static User execute(Http.Context context){
        User user;
        if (context.args.get(CONTEXT_USER) != null) {
            return (User) context.args.get(CONTEXT_USER);
        }
        String authTokenHeaderValue = GetCurrentUser.getAuthorizationToken(context);
        if (authTokenHeaderValue != null) {
            SecurityToken token = GetSecurityToken.byToken(authTokenHeaderValue);
            if(token == null) return null;
            user = token.getUser();
            if (user != null) {
                context.args.put(CONTEXT_USER, user);
                return user;
            }
        }
        return null;
    }

    private static String getAuthorizationToken(Http.Context context) {
        String[] authTokenHeaderValues = context.request().headers().get(AUTH_AUTHORIZATION.toUpperCase());
        if (authTokenHeaderValues == null) {
            authTokenHeaderValues = context.request().headers().get(AUTH_AUTHORIZATION);
        }
        if (authTokenHeaderValues == null) {
            return null;
        }
        if ((authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)){
            return authTokenHeaderValues[0];
        }else{
            return null;
        }
    }

}
