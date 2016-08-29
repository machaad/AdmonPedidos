package controllers;

import be.objectify.deadbolt.java.actions.Pattern;
import com.avaje.ebean.Ebean;
import models.SecurityAction;
import models.User;
import org.apache.commons.lang3.RandomStringUtils;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import usecase.model.GetUser;
import utils.ctrl.CtrlUtils;
import utils.ctrl.PagingBuilder;
import utils.email.Emails;
import utils.json.JsonUtils;
import utils.json.View;

import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by Interax on 14/01/2016.
 */
public class UserCtrl extends Controller {


    public static final String Module = "user";


    @Pattern(value = Module)
    public static Result getAll(String order, Integer limit, Integer page, String search, List<String> fields) {
        return new PagingBuilder()
                .query(User.find.query())
                .order(order)
                .limit(limit)
                .page(page)
                .search(search,fields)
                .viewClass(View.Public.class)
                .getResult();
    }

    public static Result getAll() {
        return Controller.ok(toJson(User.find.query().findList()));
    }


    @Pattern(value = Module)
    public static Result get(long id){
        User entity;
        entity = User.find.byId(id);
        if(entity == null ){
            return notFound();
        }
        return ok(JsonUtils.fromJsonView(entity, View.UserForm.class));
    }

    @Pattern(Module + "." + SecurityAction.UPDATE)
    public static Result put(){
        Form<User> form = Form
                .form(User.class)
                .bindFromRequest();
        if (form.hasErrors()) {
            return CtrlUtils.responseWithErrors(form, Module);
        }else {

            Ebean.beginTransaction();
            try{
                User dto = form.get();
                CtrlUtils.checkUnique(User.find,"email",dto);
                dto.update();
                Ebean.commitTransaction();
            }catch (Exception e){
                Ebean.rollbackTransaction();
                if(e instanceof CtrlUtils.FormException){
                    return CtrlUtils.responseWithErrors((CtrlUtils.FormException)e,Module);
                }else{
                    Logger.error("", e);
                    return internalServerError();
                }

            }
            return ok();
        }
    }

    @Pattern(Module + "." + SecurityAction.CREATE)
    public static Result post(){
        Form<User> form = Form
                .form(User.class)
                .bindFromRequest();
        if (form.hasErrors()) {
            return CtrlUtils.responseWithErrors(form,Module);
        }else {

            Ebean.beginTransaction();
            try{
                User dto = form.get();
                dto.setPassword(dto.getPassword());
                CtrlUtils.checkUnique(User.find,"email",dto);
                dto.save();
                Ebean.commitTransaction();
            }catch (Exception e){
                Ebean.rollbackTransaction();
                if(e instanceof CtrlUtils.FormException){
                    return CtrlUtils.responseWithErrors((CtrlUtils.FormException)e,Module);
                }else{
                    Logger.error("", e);
                    return internalServerError();
                }

            }
            return ok();
        }
    }

    @Pattern(value = Module + "." + SecurityAction.DELETE)
    public static Result delete(Long id){
        User entity = User.find.byId(id);
        if(entity != null){
            entity.delete();
            return ok();
        }else{
            return notFound();
        }
    }

    public static Result passwordRecovery(String email){
        User entity = GetUser.byEmail(email);
        if(entity == null){
            return CtrlUtils.responseWithErrors("password","recovery.notFound",Module);
        }else{
            String password = RandomStringUtils.randomAlphanumeric(8);
            if(Emails.passwordRecovery(entity,password)){
                entity.setPassword(password);
                entity.save();
            }
            return ok();
        }
    }


}
