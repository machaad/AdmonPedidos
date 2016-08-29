package controllers;

import be.objectify.deadbolt.java.actions.Pattern;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Settings;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import usecase.authentication.GetCurrentUser;
import usecase.model.GetSettings;
import utils.EntityUtils;
import utils.ctrl.CtrlUtils;
import utils.email.Emails;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by Interax on 11/02/2016.
 */
public class SettingsCtrl extends Controller {

    private static final String Module = "settings";
    @Pattern(Module)
    public  static Result get(){
        ObjectNode root = JsonNodeFactory.instance.objectNode();
        for(Settings.Group group : Settings.Group.values()){
            List<Settings> settings = GetSettings.byGroup(group);
            ObjectNode typeNode = JsonNodeFactory.instance.objectNode();
            for(Settings setting : settings){
                if(Settings.Type.NUMBER.equals(setting.getType())){
                    typeNode.put(setting.getId(), Double.parseDouble(setting.getValue()));
                }else if(Settings.Type.BOOLEAN.equals(setting.getType())){
                    typeNode.put(setting.getId(), "true".equals(setting.getValue()));
                }else{
                    typeNode.put(setting.getId(), setting.getValue());
                }
            }
            root.put(group.name().toLowerCase(),typeNode);
        }
        return ok(root);
    }

    @Pattern(Module)
    public static Result put(){
        JsonNode root = Http.Context.current().request().body().asJson();
        for(Settings.Group group : Settings.Group.values()){
            JsonNode args = root.get(group.toString().toLowerCase());
            List<Settings> settings = GetSettings.byGroup(group);
            for(Settings setting : settings){
                JsonNode arg = args.get(setting.getId());
                String newValue = arg.asText();
                if(!setting.getValue().equals(newValue)){
                    if(Settings.Type.NUMBER.equals(setting.getType())){
                        setting.setValue(String.valueOf(Double.parseDouble(arg.asText())));
                    }else if(Settings.Type.BOOLEAN.equals(setting.getType())){
                        setting.setValue(String.valueOf("true".equals(arg.asText())));
                    }else {
                        setting.setValue(arg.asText());
                    }
                    setting.save();
                }
            }
        }
        return ok();
    }

    @Pattern(Module)
    public static Result testEmailSettings(){
        JsonNode root = Http.Context.current().request().body().asJson();
        Properties properties = new Properties();
        Iterator<String> nodeIterator = root.fieldNames();
        while(nodeIterator.hasNext()){
            String name = nodeIterator.next();
            properties.put(name,root.get(name).asText());
        }
        try{
            if(Emails.testEmail(GetCurrentUser.execute(),properties)){
                return ok();
            }else{
                return CtrlUtils.responseWithErrors("emailTest","wtf",Module);
            }
        }catch (Exception e){
            return CtrlUtils.responseWithErrors("emailTest",e.getMessage(),Module);
        }
    }
}
