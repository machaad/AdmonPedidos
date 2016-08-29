package usecase.authentication;

import models.SecurityAction;
import models.SecurityPermission;
import models.SecurityRole;
import models.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Pedro Mendoza on 07-Jun-16.
 */
public class GetExcludedActions {

    public static Map<String,Set<String>> getAsMap(User user){
        /**
         * Map<Permission,ExcludedActions>
         */
        Map<String,Set<String>> permissions = new HashMap<String,Set<String>>();
        for(SecurityRole role : user.getRoles()){
            for(SecurityPermission permission : role.getPermissions()){
                Set<String> previousExcludedActions = permissions.get(permission.getModule().getId());
                Set<String> excludedActions = new HashSet<String>();
                for(SecurityAction action : permission.getExcludedActions()){
                    excludedActions.add(action.getId());
                }
                Set<String> mergedActions = new HashSet<String>();
                /**
                 * Convina Array 1 y Array 2, solo se queda los elementos que se repitan en ambos
                 */
                if(previousExcludedActions != null){
                    for(String action : previousExcludedActions){
                        if(excludedActions.contains(action)){
                            mergedActions.add(action);
                        }
                    }
                }else{
                    mergedActions = excludedActions;
                }
                permissions.put(permission.getModule().getId(),mergedActions);
            }
        }
        return permissions;
    }
}
