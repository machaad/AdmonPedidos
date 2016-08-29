package security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import models.User;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import usecase.authentication.GetCurrentUser;
import usecase.model.GetSecurityToken;

import java.util.Optional;

/**
 * Created by Interax on 13/01/2016.
 */
public class SecurityDeadboltHandler extends AbstractDeadboltHandler {

    @Override
    public F.Promise<Optional<Result>> beforeAuthCheck(Http.Context context) {
        if(getSubject(context) != null){
            return F.Promise.promise(() -> Optional.ofNullable((Result) null));
        }else{
            return F.Promise.promise(() -> Optional.ofNullable((Result) Controller.forbidden()));
        }
    }

    @Override
    public F.Promise<Optional<Subject>> getSubject(Http.Context context) {
        User user =  GetCurrentUser.execute(context);
        if(user == null){
            return F.Promise.promise(() -> Optional.empty());
        }else{
            return F.Promise.promise(() -> Optional.ofNullable(user));
        }
    }

    @Override
    public F.Promise<Result> onAuthFailure(Http.Context context, String s) {
        return F.Promise.pure(Controller.unauthorized());
    }


}