package controllers;

import static play.libs.Json.toJson;

import models.Clients;

import be.objectify.deadbolt.java.actions.Pattern;
import com.avaje.ebean.Ebean;
import java.util.List;
import models.SecurityRole;
import models.SecurityPermission;
import models.SecurityAction;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.ctrl.CtrlUtils;
import utils.ctrl.PagingBuilder;
import utils.json.JsonUtils;
import utils.json.View;


public class ClientsCtrl extends Controller {

  public static final String Module = "clients";


  @Pattern(value = Module)
  public static Result getAll(String order, Integer limit, Integer page, String search, List<String> fields) {
      return new PagingBuilder()
              .query(Clients.find.query())
              .order(order)
              .limit(limit)
              .page(page)
              .search(search,fields)
              .viewClass(View.Public.class)
              .getResult();
  }

  public static Result getAll() {
      return Controller.ok(toJson(Clients.find.query().findList()));
  }

  @Pattern(value = Module)
 public static Result get(long id){
     Clients entity;
     entity = Clients.find.byId(id);
     if(entity == null ){
         return notFound();
     }

  return ok(JsonUtils.fromJsonView(entity, View.ClientsForm.class));
 }



    @Pattern(Module + "." + SecurityAction.UPDATE)
       public static Result put(long id){
           Form<Clients> form = Form.form(Clients.class).bindFromRequest();

           if (form.hasErrors()) {
               return CtrlUtils.responseWithErrors(form, Module);
           }else {

               Ebean.beginTransaction();
               try{
                   Clients dto = form.get();
                   CtrlUtils.checkUnique(Clients.find,"givenName",dto);
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
      Form<Clients> form = Form.form(Clients.class).bindFromRequest();

      if (form.hasErrors()) {
          return CtrlUtils.responseWithErrors(form,Module);
      }else {

          Ebean.beginTransaction();
          try{
              Clients dto = form.get();
                  dto.save();
                  Ebean.commitTransaction();
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
              Clients entity = Clients.find.byId(id);
              if(entity != null){
                  entity.delete();
                  return ok();
              }else{
                  return notFound();
              }
          }


}
