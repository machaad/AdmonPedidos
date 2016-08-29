package controllers;

import com.avaje.ebean.Expr;
import models.SecurityModule;
import models.SecurityPermission;
import org.apache.commons.lang3.StringUtils;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

import static play.libs.Json.toJson;

/**
 * Created by Interax on 27/01/2016.
 */
public class ModuleCtrl extends Controller{

    public static Result getAll(String id) {
        if(StringUtils.isBlank(id)){
            return Controller.ok(toJson(SecurityModule.find.query().fetch("actions").findList()));
        }else {
            List<SecurityPermission> currentPermissions = SecurityPermission
                    .find
                    .select("id")
                    .select("module.id")
                    .where()
                    .eq("role.id", id)
                    .findList();
            List<String> currentModules = new ArrayList<String>();
            for (SecurityPermission permission : currentPermissions) {
                currentModules.add(permission.getModule().getId());
            }
            return Controller.ok(toJson(SecurityModule
                    .find
                    .query()
                    .fetch("actions")
                    .where()
                    .not(Expr.in("id", currentModules))
                    .findList()));
        }
    }
}
