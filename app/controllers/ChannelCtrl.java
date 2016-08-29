package controllers;

import static play.libs.Json.toJson;
import be.objectify.deadbolt.java.actions.Pattern;

import com.avaje.ebean.Ebean;

import models.Channel;
import models.SecurityAction;
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

/**
 * Controlador para las acciónes relacionadas con
 * los canales de venta del sistema.
 *
 * @author Adan Mtz on 03/03/2016.
 */
public class ChannelCtrl extends Controller {

    public static final String Module = "channel";

    @Pattern(value = Module)
    public static Result getAll(String order, Integer limit, Integer page, String search, List<String> fields) {
        return new PagingBuilder()
                .query(Channel.find.query())
                .order(order)
                .limit(limit)
                .page(page)
                .search(search,fields)
                .viewClass(View.Public.class)
                .getResult();
    }

    public static Result getAll() {
        return Controller.ok(toJson(Channel.find.query().findList()));
    }

    @Pattern(value = Module)
    public static Result get(long id){
        Channel entity;
        entity = Channel.find.byId(id);
        if(entity == null ){
            return notFound();
        }
        return ok(JsonUtils.fromJsonView(entity, View.UserForm.class));
    }

    @Pattern(Module + "." + SecurityAction.UPDATE)
    public static Result put(long id){
        Form<Channel> form = Form.form(Channel.class).bindFromRequest();

        if (form.hasErrors()) {
            return CtrlUtils.responseWithErrors(form, Module);
        }else {

            Ebean.beginTransaction();
            try{
                Channel dto = form.get();
                CtrlUtils.checkUnique(Channel.find,"name",dto);
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
            return ok("Succeded");
        }
    }

    @Pattern(Module + "." + SecurityAction.CREATE)
    public static  Result post(){
        Form<Channel> form = Form.form(Channel.class).bindFromRequest();

        if (form.hasErrors()) {
            return CtrlUtils.responseWithErrors(form,Module);
        }else {

            Ebean.beginTransaction();
            try{
                Channel dto = form.get();
                if(CtrlUtils.checkUnique(Channel.find,"name",dto)){
                    dto.save();
                    Ebean.commitTransaction();
                }
                else {
                   // return CtrlUtils.responseWithErrors("Ya existe un registro con la información");
                }

            }catch (Exception e){
                Ebean.rollbackTransaction();
                if(e instanceof CtrlUtils.FormException){
                    //return CtrlUtils.responseWithErrors((CtrlUtils.FormException)e,Module);
                    return internalServerError();
                }else{
                    Logger.error("", e);
                    return internalServerError();
                }

            }
            return ok();
        }
    }

    @Pattern(value = Module + "." + SecurityAction.DELETE)
    public static  Result delete(Long id){
        Channel entity = Channel.find.byId(id);
        if(entity != null){
            entity.delete();
            return ok();
        }else{
            return notFound();
        }
    }

}
