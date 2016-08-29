package utils.ctrl;

import com.avaje.ebean.Junction;
import com.avaje.ebean.PagedList;
import com.avaje.ebean.Query;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.json.JsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.libs.Json.toJson;

/**
 * Clase de ayuda que permite construir una consulta paginada a una tabla en especifico utilizando de base una entidad
 * de Ebean y su objeto Query
 * Created by Pmendoza on 10/17/15.
 */
public class PagingBuilder {
    private Query<?> query;
    private String order;
    private int limit;
    private int page;
    private Map<String,String> search;
    private Class<?> viewClass;

    /**
     *
     * @param query Objecto Query ininicializado apartir de un entidad EBean
     * @return PagingBuilder
     */
    public PagingBuilder query(Query<?> query) {
        this.query = query;
        return this;
    }

    /**
     * @param order Nombre del campo de la entidad Ebean por el que se va a ordenar. para ordenar de forma
     *              descendente es necesario <b>ANTE PONER SINGO NEGATIVO(-)</b>
     *              Ejemplo para order de forma ascendente: "Columna1"
     *              Ejemplo para order de forma descendent: "-Columna1"
     * @return PagingBuilder
     */
    public PagingBuilder order(String order) {
        this.order = order;
        return this;
    }

    /**
     * @param limit Numero de registros a obtener por pagina
     * @return Builder
     */
    public PagingBuilder limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * @param page Numero de pagina a consultar
     * @return Builder
     */
    public PagingBuilder page(Integer page) {
        this.page = page;
        return this;
    }

    /**
     * @param search Map[Column,SearchValue] Mapa con los valores a buscar por columna
     * @return Builder
     */
    public PagingBuilder search(Map<String,String> search) {
        this.search = search;
        return this;
    }

    /**
     *
     * @param search Palabra clave a buscar
     * @param fields Campos sobre los que se busca la palabra clave
     * @return PagingBuilder
     */
    public PagingBuilder search(String search, List<String> fields) {
        this.search = toSearchParam(search,fields);
        return this;
    }

    /**
     * Soporte de anotaciones JsonView
     * @param viewClass Tipo de JsonView a transformar el resultado
     * @return PagingBuilder
     */
    public PagingBuilder viewClass(Class<?> viewClass) {
        this.viewClass = viewClass;
        return this;
    }

    /**
     * Transforma una lista de nombres de campos y un string en particular a un Map<Column,SearchValue> requerido para
     * {@link PagingBuilder#search(Map)}
     * @param queryValue Valor a buscar
     * @param fields Campos sobre los que se van a buscar
     * @return
     */
    public static Map<String,String> toSearchParam(String queryValue, List<String> fields){
        Map<String, String> filter = new HashMap<String, String>();
        for(String field : fields){
            filter.put(field, queryValue);
        }
        return filter;
    }

    /**
     * Aparir de los datos proporciados en el {@link PagingBuilder} obtiene un resultado de la consulta y lo transforma
     * a una {@link Result} de play para ser retornado directamente como respuesta a una peticion
     * @return
     */
    public Result getResult() {
        return Controller.ok(getJsonNode());
    }

    public ResultData getResultData(){
        ResultData resultData = new ResultData();
        setupQuery();
        if(limit <= 0 || page <= 0){
            List<?> all = query.findList();
            resultData.setCount(all.size());
            resultData.setList(all);
        }else {
            PagedList<?> pagingList = query.findPagedList(page -1, limit);
            pagingList.getFutureRowCount();
            resultData.setList(pagingList.getList());
            resultData.setCount(pagingList.getTotalRowCount());
            // for play 2.3
            /*
            PagingList<?> pagingList = query.findPagingList(limit);
            pagingList.getFutureRowCount();
            resultData.setList(pagingList.setFetchAhead(true).getPage(page -1).getList());
            resultData.setCount(pagingList.getTotalRowCount());
            */


        }
        return resultData;
    }

    private void setupQuery(){
        if(StringUtils.isNotBlank(order)){
            query.order(fixOrder(order));
        }
        if(search != null && !search.isEmpty()){
            Junction<?> junction = query.where().disjunction();
            for(Map.Entry<String,String> entry : search.entrySet()){
                junction.ilike(entry.getKey(),entry.getValue() + "%");
            }
        }
    }

    /**
     * Aparir de los datos proporciados en el {@link PagingBuilder} obtiene un resultado de la consulta y lo transforma
     * a una {@link JsonNode}
     * @return
     */
    public JsonNode getJsonNode() {
        setupQuery();
        ResultData data = getResultData();
        ObjectNode obj = Json.newObject();
        addData(obj, data.getList(),viewClass);
        obj.put("count", data.getCount());
        return toJson(obj);
    }

    /**
     * Llena el valor del arreglo "list" con los valores de la lista
     * @param obj ObjectNode base sobre el que se agregara la propiedad "list" como arreglo
     * @param data Lista de objetos a transformar
     * @return
     */
    public static ObjectNode addData(ObjectNode obj, List<?> data, Class<?> viewClass){
        if(viewClass == null){
            obj.put("list",toJson(data));
        }else{
            String jsonArrayStr = JsonUtils.fromJsonView(data, viewClass);
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode actualObj = mapper.readTree(jsonArrayStr);
                obj.put("list",actualObj);
            } catch (IOException e) {
                Logger.error("",e);
            }

        }

        return obj;
    }

    /**
     * Corrige el subfijo - en el campo de orden con "desc" para poder ser interpretado por Ebean
     * @param order
     * @return
     */
    private static String fixOrder(String order){
        String fixed;
        if(order.startsWith("-")){
            fixed = order.substring(1,order.length()) + " desc";
        }else{
            fixed = order;
        }
        return fixed;
    }

    public static class ResultData{
        private int count;
        private List<?> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<?> getList() {
            return list;
        }

        public void setList(List<?> list) {
            this.list = list;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ResultData that = (ResultData) o;

            if (count != that.count) return false;
            return list != null ? list.equals(that.list) : that.list == null;

        }

        @Override
        public int hashCode() {
            int result = count;
            result = 31 * result + (list != null ? list.hashCode() : 0);
            return result;
        }
    }
}