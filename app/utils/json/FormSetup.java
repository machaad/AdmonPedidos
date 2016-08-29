package utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.Logger;
import play.api.mvc.Request;
import play.data.format.Formatters;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Controles personalizados para la interpretacion de forumalrios utilizado comunmente en los controladores
 * @see {@link play.api.data.Form#bindFromRequest(Request)}
 * Created by pmendoza on 10/14/15.
 */
public class FormSetup {

    public static void setup(){
        Formatters.register(Date.class, dateParserImpl);
    }

    private static final Formatters.AnnotationFormatter<DateParser, Date> dateParserImpl = new Formatters.AnnotationFormatter<DateParser, Date>() {
        @Override
        public Date parse(DateParser annotation, String input, Locale locale) throws ParseException {
            if (input == null || input.trim().isEmpty())
                return null;

            if (annotation.format().isEmpty()) {
                return new Date(Long.parseLong(input));
            } else {
                return new SimpleDateFormat(annotation.format()).parse(input);
            }
        }


        @Override
        public String print(DateParser annotation, Date date, Locale locale) {
            if (date == null)
                return null;

            if (annotation.format().isEmpty())
                return date.getTime() + "";
            else
                return new SimpleDateFormat(annotation.format()).format(date);
        }

    };

}
