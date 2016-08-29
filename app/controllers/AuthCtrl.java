package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.SecurityApp;
import models.SecurityToken;
import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import usecase.authentication.GetSecurityApp;
import usecase.authentication.CreateAuthToken;
import usecase.authentication.GetExcludedActions;
import usecase.model.GetUser;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by Interax on 13/01/2016.
 */
public class AuthCtrl extends Controller {

    private static final String AUTH_TOKEN = "authToken";

    public static User getUser() {
        return (User) Http.Context.current().args.get("user");
    }

    public static Result login() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(loginForm.errorsAsJson());
        }


        //Data validations
        Login login = loginForm.get();

        SecurityApp app = GetSecurityApp.byClientId(login.clientId);

        if(app == null){
            return unauthorized("Client app key not found");
        }

        User user = GetUser.byEmailAndPassword(login.email, login.password);
        if (user == null) {
            return unauthorized();
        }
        else {
            SecurityToken authToken = CreateAuthToken.create(user,app);
            authToken.save();
            ObjectNode node = Json.newObject();
            //Name Object
            ObjectNode name = Json.newObject();
            name.put("givenName",user.getGivenName());
            name.put("familyName",user.getFamilyName());
            name.put("formatted",user.getGivenName() + " " + user.getFamilyName());
            node.put("name",name);
            //Auth Object
            ObjectNode auth = Json.newObject();
            auth.put("token",authToken.getToken());
            node.put("auth",auth);
            // Permissions Object
            Map<String,Set<String>> permissions = GetExcludedActions.getAsMap(user);
            ObjectMapper mapper =  new ObjectMapper();
            ArrayNode permissionsNode = node.putArray("permissions");
            for(Map.Entry<String,Set<String>> entry : permissions.entrySet()){
                ObjectNode permissionNode = Json.newObject();
                permissionNode.put("name",entry.getKey());
                ArrayNode excludedActions = mapper.valueToTree(Collections.unmodifiableSet(entry.getValue()));
                permissionNode.putArray("excludedActions").addAll(excludedActions);
                permissionsNode.add(permissionNode);
            }
            //Session and cookies
            response().setCookie(AUTH_TOKEN, authToken.getToken());
            session().put(AUTH_TOKEN,authToken.getToken());
            return ok(node);
        }
    }


    public static Result logout() {
        response().discardCookie(AUTH_TOKEN);
        return redirect("/");
    }


    public static Result preflight(String all) {
        response().setHeader("Access-Control-Allow-Origin", "*");
        response().setHeader("Allow", "*");
        response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response().setHeader("Access-Control-Allow-Headers", "Authorization, Origin, X-Requested-With, Content-Type, Accept, Referer, User-Agent");
        return ok();
    }

    public static class Login {
        @Constraints.Required
        public String email;

        @Constraints.Required
        public String password;

        @Constraints.Required
        public String clientId;
    }
}
