package security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import models.User;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Optional;

/**
 * Created by Interax on 02/02/2016.
 */
public class JunitDeadboltHandler extends AbstractDeadboltHandler {

    @Override
    public F.Promise<Optional<Result>> beforeAuthCheck(Http.Context context) {
        return F.Promise.promise(() -> Optional.ofNullable((Result) null));
    }

    @Override
    public F.Promise<Optional<Subject>> getSubject(Http.Context context) {
        User user = User.find.byId(1l);
        return F.Promise.promise(() -> Optional.ofNullable(user));
    }

    @Override
    public F.Promise<Result> onAuthFailure(Http.Context context, String s) {
        return F.Promise.pure((Result) Controller.unauthorized());
    }
}