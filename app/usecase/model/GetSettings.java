package usecase.model;

import models.Settings;
import play.Logger;

import java.util.List;

/**
 * Created by Pedro Mendoza on 07-Jun-16.
 */
public class GetSettings {

    public static List<Settings> byGroup(Settings.Group group){
        return Settings.find.where().eq("group", group).findList();
    }


    public static String asString(String settingName){
        Settings st = Settings.find.byId(settingName);
        if(st == null){
            return "";
        }else{
            return st.getValue();
        }
    }

    public static boolean asBoolean(String settingsName){
        Settings st = Settings.find.byId(settingsName);
        if(st == null){
            return false;
        }else{
            return "true".equalsIgnoreCase(st.getValue());
        }
    }

    public static Integer asInteger(String settingName){
        Settings st = Settings.find.byId(settingName);
        if(st == null){
            return 0;
        }else{
            try{
                return Integer.parseInt(st.getValue());
            }catch (Exception e){
                Logger.error("",e);
                return 0;
            }

        }
    }
    public static Double asDouble(String settingName){
        Settings st = Settings.find.byId(settingName);
        if(st == null){
            return 0d;
        }else{
            try{
                return Double.parseDouble(st.getValue());
            }catch (Exception e){
                Logger.error("",e);
                return 0d;
            }

        }
    }
}
