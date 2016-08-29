package usecase.authentication;

import be.objectify.deadbolt.core.models.Permission;
import models.SecurityAction;
import models.SecurityPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pedro Mendoza on 07-Jun-16.
 */
public class GetDeadboltPermissions {

    public static List<? extends Permission> execute(SecurityPermission securityPermission){
        List<DeadboltPermission> deadboltPermissions = new ArrayList<DeadboltPermission>();
        deadboltPermissions.add(new DeadboltPermission(securityPermission.getModule().getId()));
        for(SecurityAction actions : securityPermission.getModule().getActions()){
            if(!securityPermission.getExcludedActions().contains(actions)){
                deadboltPermissions.add(new DeadboltPermission(securityPermission.getModule().getId() + "." + actions.getId()));
            }
        }
        return deadboltPermissions;
    }

    public static class DeadboltPermission implements Permission{
        private String value;
        public DeadboltPermission(String value){
            this.value = value;
        }
        @Override
        public String getValue() {
            return value;
        }
    }
}
