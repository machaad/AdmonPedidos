package controllers;

import java.util.List;

import models.Etiqueta;
import models.SecurityAction;
import models.User;
import be.objectify.deadbolt.java.actions.Pattern;
import play.mvc.Controller;
import play.mvc.Result;
import play.Logger;
import utils.ctrl.PagingBuilder;
import utils.json.JsonUtils;
import utils.json.View;
import static play.libs.Json.toJson;

public class EtiquetasCtrl extends Controller {

    public static final String Module = "etiquetas";
    
    @Pattern(value = Module)
    public static Result getAll(String order, Integer limit, Integer page, String search, List<String> fields) {
        if(order==null)order = "name";
        if(order.isEmpty())order = "name";
        return new PagingBuilder()
                .query(Etiqueta.find.query())
                .order(order)
                .limit(limit)
                .page(page)
                .search(search,fields)
                .viewClass(View.Public.class)
                .getResult();
    }

    public static Result getAllSimple() {
        return new PagingBuilder()
                .query(Etiqueta.find.query())
                .order("name")
//                .singleList(true)
                .viewClass(View.Public.class)
                .getResult();
    }

    @Pattern(value = Module)
    public static Result get(long id){
        Etiqueta entity;
        entity = Etiqueta.find.byId(id);
        if(entity == null ){
            return notFound();
        }
        return ok(JsonUtils.fromJsonView(entity, View.UserForm.class));
    }

    @Pattern(Module + "." + SecurityAction.UPDATE)
    public static Result put(){

            return ok("Succeded");
    }

    @Pattern(Module + "." + SecurityAction.CREATE)
    public static  Result post(){
            return ok();
    }

    @Pattern(value = Module + "." + SecurityAction.DELETE)
    public static  Result delete(Long id){
            return ok();
        }

}


	