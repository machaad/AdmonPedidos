package controllers;

import static play.libs.Json.toJson;

import java.util.List;

import models.Clients;
import be.objectify.deadbolt.java.actions.Pattern;
import play.mvc.Controller;
import play.mvc.Result;
import utils.ctrl.PagingBuilder;
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
  
}
