package usecase.model;

import models.User;
import usecase.authentication.GetSha512;

/**
 * Created by Pedro Mendoza on 07-Jun-16.
 */
public class GetUser {

    public static User byEmailAndPassword(String email, String password) {
        return User.find.where()
                .eq("email", email)
                .eq("shaPassword", GetSha512.execute(password))
                .findUnique();
    }

    public static User byEmail(String email) {
        return User.find.where().eq("email", email).findUnique();
    }

}
