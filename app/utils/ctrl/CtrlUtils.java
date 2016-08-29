package utils.ctrl;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utilerias comunes utilizadas por los controllers
 * Created by Pmendoza on 10/8/15.
 */
public class CtrlUtils {

    /**
     * Apartir de los errores en un formularios genera una respuesta generica con los mensajes
     * de cada campo y el contraint violado.
     *
     * Rertona un Result con codigo BAD_REQUEST
     * @param dtoForm Formulario
     * @param module Module
     * @return Result
     */
    public static Result responseWithErrors(Form<?> dtoForm, String module){
        List<RequestError> errors = new ArrayList<RequestError>();
        if(dtoForm.hasErrors()){
            for(Map.Entry<String, List<ValidationError>> entry : dtoForm.errors().entrySet()){
                for(ValidationError vError : entry.getValue()){
                    errors.add(new RequestError(entry.getKey(),vError.message(),module));
                }
            }
        }
        return Results.badRequest(Json.toJson(new RequestErrors(errors)));
    }

    public static Result responseWithErrors(FormException fe, String module){
        List<RequestError> errors = new ArrayList<RequestError>();
        errors.add(new RequestError(fe.field,fe.message,module));
        return Results.badRequest(Json.toJson(new RequestErrors(errors)));
    }


    public static Result responseWithErrors(String field, String message, String module){
        List<RequestError> errors = new ArrayList<RequestError>();
        errors.add(new RequestError(field, message, module));
        return Results.badRequest(Json.toJson(new RequestErrors(errors)));
    }

    /**
     * Verfica si el valor del campo es unico en la base de datos. Tiene en cuenta si se encuentra un valor repetido
     * pero pertence a la misma entidad no lo marque como repetido
     * @param finder Finder sobre el que se va a hacer la consulta
     * @param fieldName Nombre del campo a consultar si es unico
     * @param object Valor que se verifica si es unico
     * @return True si no hay repetidos - False si hay repetidos
     * @throws FormException
     */
    public static boolean checkUnique(Model.Finder finder, String fieldName, Object object) throws FormException {
        ExpressionList exp;
        try {
            if(object != null){
                PropertyDescriptor pd = new PropertyDescriptor(fieldName, object.getClass());
                exp = finder.where().eq(fieldName, pd.getReadMethod().invoke(object));
                Field idField = null;
                for(Field field : object.getClass().getDeclaredFields()){
                    if(field.isAnnotationPresent(javax.persistence.Id.class)){
                        idField = field;
                        break;
                    }
                }
                if(idField == null){
                    throw new IllegalArgumentException("No existe un campo con anotacion javax.persistence.Id en la clase " + object.getClass().getSimpleName());
                }
                pd = new PropertyDescriptor(idField.getName(), object.getClass());
                exp.ne(idField.getName(),pd.getReadMethod().invoke(object));
                Object temp = exp.findUnique();
                if(temp != null){
                    throw new FormException(fieldName, "notUnique");
                }
            }
        } catch (Exception e) {
            if(e instanceof FormException){
                throw (FormException)e;
            }
        }

        return true;
    }


    public static class FormException extends Exception{
        private String field;
        private String message;

        /**
         *
         * @param field Field name. Example: email
         * @param message Message key. Example: error.require
         */
        public FormException(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }


    public static class RequestError {
        private String field;
        private String message;
        private String module;

        public RequestError(String field, String message,String module) {
            this.field = field;
            this.message = message;
            this.module = module;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }
    }

    public  static class RequestErrors {
        private List<RequestError> errors = new ArrayList<RequestError>();

        public RequestErrors(List<RequestError> errors) {
            this.errors = errors;
        }

        public List<RequestError> getErrors() {
            return errors;
        }

        public void setErrors(List<RequestError> errors) {
            this.errors = errors;
        }
    }



}
