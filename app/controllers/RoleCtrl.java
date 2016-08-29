package controllers;

import be.objectify.deadbolt.java.actions.Pattern;
import com.avaje.ebean.Ebean;
import models.SecurityAction;
import models.SecurityPermission;
import models.SecurityRole;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.ctrl.CtrlUtils;
import utils.ctrl.PagingBuilder;
import utils.json.JsonUtils;
import utils.json.View;

import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by Interax on 19/01/2016.
 */
public class RoleCtrl extends Controller{
    public static final String Module = "role";


    @Pattern(value = Module)
    public static Result getAll(String order, Integer limit, Integer page, String search, List<String> fields) {
        return new PagingBuilder()
                .query(SecurityRole.find.query())
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
    public static Result get(Long id){
        SecurityRole entity;
        entity = SecurityRole.find
                .fetch("permissions.module.actions")
                .fetch("permissions.excludedActions")
                .where().idEq(id).findUnique();
        if(entity == null ){
            return notFound();
        }
        return ok(JsonUtils.fromJsonView(entity, View.RoleForm.class));
    }

    @Pattern(value = Module + "." + SecurityAction.UPDATE)
    public static Result put(){
        Form<SecurityRole> form = Form
                .form(SecurityRole.class)
                .bindFromRequest();
        if (form.hasErrors()) {
            return CtrlUtils.responseWithErrors(form, Module);
        }else {

            Ebean.beginTransaction();
            try{
                SecurityRole dto = form.get();
                for(SecurityPermission permission : dto.getPermissions()){
                    permission.getModule().refresh();
                    if(permission.getId() > 0 ) {
                        permission.update();
                    }
                }
                dto.update();
                Ebean.commitTransaction();
            }catch (Exception e){
                Ebean.rollbackTransaction();
                Logger.error("", e);
                return internalServerError();


            }
            return ok();
        }
    }

    @Pattern(value = Module + "." + SecurityAction.CREATE)
    public static Result post(){
        Form<SecurityRole> form = Form
                .form(SecurityRole.class)
                .bindFromRequest();
        if (form.hasErrors()) {
            return CtrlUtils.responseWithErrors(form,Module);
        }else {

            Ebean.beginTransaction();
            try{
                SecurityRole dto = form.get();
                dto.save();
                Ebean.commitTransaction();
            }catch (Exception e){
                Ebean.rollbackTransaction();
                Logger.error("", e);
                return internalServerError();


            }
            return ok();
        }
    }

    @Pattern(value = Module + "." + SecurityAction.DELETE)
    public static Result delete(Long id){
        SecurityRole entity = SecurityRole.find.byId(id);
        if(entity != null){
            entity.delete();
            return ok();
        }else{
            return notFound();
        }
    }
}
