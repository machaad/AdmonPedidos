package utils;

import models.*;
import play.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Carga inicial de la informacion, setup de usuarios y permisos.
 * Created by Pmendoza on 13/01/2016.
 */
public class StartupData {

    private List<SecurityAction> allActions = new ArrayList<SecurityAction>();
    private List<SecurityModule> allModules = new ArrayList<SecurityModule>();

    public void load() {
        if(SecurityAction.find.all().isEmpty()){
            createAppKeys();
            createActions();
            createModules();
            createSuperUser();
            createSettings();
            createProducts();
            createClients();
            createDummyUsers();
            createDummyRoles();
        }else{
            Logger.info("No es necesario hacer carga inicial de informacion");
        }
    }

    private void createAppKeys(){
        SecurityApp app = new SecurityApp();
        app.setDescription("Default web app key");
        app.setName("My App - key");
        app.setLicenseKey("qAV40pgxhpXMNmLqgLJ9VmMmhrKxRoK1P9tzsdNH");
        app.save();
        app = new SecurityApp();
        app.setDescription("Mobile web Key");
        app.setName("Mobile Web App - key");
        app.setLicenseKey("J4QG1yXQBYIfdbockKaaVzISSbdLGGW7Y1eGWnDK");
        app.save();
    }
    private void createActions(){
        SecurityAction action;
        action  = new SecurityAction();
        action.setId(SecurityAction.CREATE);
        action.save();
        allActions.add(action);
        action  = new SecurityAction();
        action.setId(SecurityAction.UPDATE);
        action.save();
        allActions.add(action);
        action  = new SecurityAction();
        action.setId(SecurityAction.DELETE);
        action.save();
        allActions.add(action);
    }

    private void createModules(){
        SecurityModule module;

        module = new SecurityModule();
        module.setId("user");
        module.setActions(allActions);
        module.save();
        allModules.add(module);

        module = new SecurityModule();
        module.setId("role");
        module.setActions(allActions);
        module.save();
        allModules.add(module);

        module = new SecurityModule();
        module.setId("settings");
        module.setActions(allActions);
        module.save();
        allModules.add(module);

        module = new SecurityModule();
        module.setId("producto");
        module.setActions(allActions);
        module.save();
        allModules.add(module);

        module = new SecurityModule();
        module.setId("clients");
        module.setActions(allActions);
        module.save();
        allModules.add(module);


    }

    private void createClients(){
      for(int i = 1; i <= 10; i ++) {
          String name = "Ciente" + i;
          String lastName = "lastName" + i;
          String maidenName = "maidenName" + i;
          String address = "address" + i;
          String email = "email" + i;

          Clients clients= new Clients(name,lastName,maidenName,address,email);
          clients.save();
           }

    }

    private void createProducts(){
      for(int i = 1; i <= 10; i ++) {
          String name = "Producto" + i;
          String desc = "description" + i;
          String sku = "SKU" + i;

          Producto producto= new Producto(name,desc,sku);
          producto.save();
           }

    }

    private void createSuperUser(){
        SecurityRole superUserRole = new SecurityRole();
        superUserRole.setName("Super User");
        superUserRole.save();

        List<SecurityPermission> srms = new ArrayList<SecurityPermission>();
        for(SecurityModule module : allModules){
            SecurityPermission srm = new SecurityPermission();
            srm.setRole(superUserRole);
            srm.setModule(module);
            srm.save();
            srms.add(srm);
        }
        superUserRole.save();

        User user = new User("superuser@demo.com","password","Super","User");
        List<SecurityRole> roles = new ArrayList<>();
        roles.add(superUserRole);
        user.setRoles(roles);
        user.save();
    }

    private void createDummyUsers(){
        for(int i = 1; i <= 10; i ++) {
            String name = "user" + i;
            User user = new User(name + "@demo.com", "xxxxxx", name,"lastname" + i );
            user.save();
        }
    }
    private void createDummyRoles(){
        for(int i = 1; i <= 10; i ++) {
            SecurityRole superUserRole = new SecurityRole();
            superUserRole.setName("Rol " + i);
            superUserRole.save();
        }
    }

    public void createSettings(){
        Settings s;
        //MAIL
        s = new Settings();
        s.setGroup(Settings.Group.EMAIL);
        s.setId("mail.smtp.host");
        s.setValue("smtp.gmail.com");
        s.save();

        s = new Settings();
        s.setGroup(Settings.Group.EMAIL);
        s.setType(Settings.Type.NUMBER);
        s.setId("mail.smtp.port");
        s.setValue("587");
        s.save();


        s = new Settings();
        s.setGroup(Settings.Group.EMAIL);
        s.setId("mail.smtp.starttls.enable");
        s.setType(Settings.Type.BOOLEAN);
        s.setValue("true");
        s.save();


        s = new Settings();
        s.setGroup(Settings.Group.EMAIL);
        s.setType(Settings.Type.BOOLEAN);
        s.setId("mail.smtp.auth");
        s.setValue("true");
        s.save();

        s = new Settings();
        s.setGroup(Settings.Group.EMAIL);
        s.setId("mail.user");
        s.setValue("no-reply@interax.com.mx");
        s.save();

        s = new Settings();
        s.setGroup(Settings.Group.EMAIL);
        s.setId("mail.password");
        s.setValue("inetmx1981");
        s.save();

        s = new Settings();
        s.setGroup(Settings.Group.EMAIL);
        s.setId(Settings.EMAIL_ALIAS);
        s.setValue("My App");
        s.save();

        s = new Settings();
        s.setGroup(Settings.Group.EMAIL);
        s.setId(Settings.EMAIL_MOCK);
        s.setType(Settings.Type.BOOLEAN);
        s.setValue("false");
        s.save();
    }
}
