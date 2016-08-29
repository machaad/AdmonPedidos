package utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import play.Logger;
import play.libs.Json;

import java.io.IOException;
import java.util.List;

/**
 * Utilerias para el manejo de Jsons
 * Created by Pmendoza on 16/02/2016.
 */
public class JsonUtils {
    private static final ObjectMapper viewMapper = new ObjectMapper();
    static {
        viewMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    }
    /**
     * Transforma un objeto a un String siguiendo la consiguracion de un JsonView
     * @param obj Objeto
     * @param viewClass JsonView class
     * @return Json string
     */
    public static String fromJsonView(Object obj, Class<?> viewClass){
        try {
            return viewMapper
                    .writerWithView(viewClass)
                    .writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            Logger.error("",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Transforma una lista de Objetos a un ArrayNode siguiendo la configuracion de un JsonView
     * @param list Lista de Objetos
     * @param viewClass JsonView class
     * @return ArrayNode con el resultado
     */
    public static ArrayNode applyWithView(List<?> list, Class<?> viewClass){
        ArrayNode array = Json.newArray();
        for (Object obj : list){
            try {
                array.add(viewMapper.readTree(fromJsonView(obj,viewClass)));
            } catch (IOException e) {
                Logger.error("Error while parsing Json string",e);
            }
        }
        return array;
    }

    public static ObjectMapper getViewMapper() {
        return viewMapper;
    }
}
