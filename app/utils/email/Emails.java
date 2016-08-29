package utils.email;

import models.User;
import play.Logger;
import play.i18n.Messages;

import javax.mail.MessagingException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by Pmendoza on 11/02/2016.
 */
public class Emails {

    public static boolean passwordRecovery(User user, String password){

        return Sender.sendEmail( Messages.get("email.passwordRecovery"),
              //  views.html.email.passwordRecovery.render(user.getGivenName(),password).toString(),
//                user.getEmail()
                "mengon05@gmail.com"
        );
    }

    public static boolean testEmail(User user, Properties properties) throws UnsupportedEncodingException, MessagingException {
        StringWriter writer = new StringWriter();
        properties.list(new PrintWriter(writer));
        String propStr =  writer.getBuffer().toString();
        propStr = propStr.replace("\n","<br/>");
        //TODO quitar el correo hardcoded y poner el del usuario
        try {
            return Sender.sendEmail(properties,false, Messages.get("email.passwordRecovery"),
                  //  views.html.email.testEmail.render(user.getGivenName(),propStr).toString(),
                    //                user.getEmail()
                    "mengon05@gmail.com"
            );
        } catch (Exception e) {
            Logger.error("Error al enviar correo",e);
            return false;
        }
    }
}
