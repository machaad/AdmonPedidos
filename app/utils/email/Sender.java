package utils.email;

import models.Settings;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.Play;
import usecase.model.GetSettings;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.List;
import java.util.Properties;

/**
 * Created by Pmendoza on 11/02/2016.
 */
public class Sender {

    private static Properties getEmailProperties(){
        Properties properties = new Properties();
        List<Settings> settings = GetSettings.byGroup(Settings.Group.EMAIL);
        for(Settings setting : settings){
            properties.setProperty(setting.getId(),setting.getValue());
        }
        return properties;
    }

    public static boolean sendEmail(final Properties properties,boolean catchExceptions, String subject,String htmlMessage, String... mailAddress) throws Exception {
        if(Play.isTest()){
            mock(subject,htmlMessage,mailAddress);
            return true;
        }
        if("true".equals(properties.getProperty(Settings.EMAIL_MOCK))){
            mock(subject,htmlMessage,mailAddress);
            return true;
        }
        String alias = properties.getProperty(Settings.EMAIL_ALIAS);
        properties.getProperty(Settings.EMAIL_MOCK);
        if(StringUtils.isNotBlank(alias)){
            properties.remove(Settings.EMAIL_ALIAS);
        }else{
            alias = "No reply";
        }

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(properties.get("mail.user").toString(),
                                properties.get("mail.password").toString());
                    }
                });
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.get("mail.user").toString(),alias));
            message.addRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(StringUtils.join(mailAddress,","))
            );
//            new String(subject.getBytes(Charset.forName("UTF-8")),Charset.forName("cp1252"));
            message.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
            // Send the actual HTML message, as big as you like
            message.setContent(htmlMessage,"text/html");
            Transport.send(message);
            return true;
        }catch (Exception mex) {
            Logger.error("Error al enviar correo", mex);
            if(catchExceptions){
                return false;
            }else{
                throw mex;
            }

        }
    }

    public static boolean sendEmail(String subject,String htmlMessage, String... mailAddress){
        try {
            return sendEmail(getEmailProperties(),true,subject,htmlMessage,mailAddress);
        } catch (Exception e) {
            return false;
        }
    }

    private static void mock(String subject,String htmlMessage, String... mailAddress){
        Logger.info("Send email\nSubject: " + subject + "\nTarget: " + StringUtils.join(mailAddress,",") + "\nMessage: \n" + htmlMessage);
    }
}
