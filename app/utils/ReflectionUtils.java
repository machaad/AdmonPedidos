package utils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilerias para el manejo de java reflection
 * Created by pmendoza on 09/02/2016.
 */
public class ReflectionUtils {


    private static final Map<CacheReflection,Map<Field,PropertyDescriptor>> cache = new HashMap<CacheReflection,Map<Field,PropertyDescriptor>>();

    /**
     * Obtiene todos los campos con annotacions con sus respectivos
     * descriptores para poder ser utilizados en reflection
     * @return null safe set
     */
    public static Map<Field,PropertyDescriptor> findFieldsWithAnnotation(Class<?> classs, Class<? extends Annotation> ann) {
        //First check if it is saved in cache
        CacheReflection cacheReflection = new CacheReflection(classs,ann);
        Map<Field,PropertyDescriptor> fieldDescriptors = cache.get(cacheReflection);
        if(fieldDescriptors != null){
            return fieldDescriptors;
        }
        //If not start the reflection
        fieldDescriptors = new HashMap<Field,PropertyDescriptor>();
        Map<String,Field> temp = new HashMap<String,Field>();
        Class<?> c = classs;
        try {
            while (c != null) {
                for(Field field : c.getDeclaredFields()){
                    if (field.isAnnotationPresent(ann)) {
                        temp.put(field.getName(),field);
                    }
                }
                c = c.getSuperclass();
            }
            c = classs;
            while (c != null) {
                for (PropertyDescriptor pd : Introspector.getBeanInfo(c).getPropertyDescriptors()) {
                    if(temp.containsKey(pd.getName())){
                        fieldDescriptors.put(temp.get(pd.getName()),pd);
                    }
                }
                c = c.getSuperclass();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        cache.put(cacheReflection,fieldDescriptors);
        return fieldDescriptors;
    }

    /**
     * Clase para el control interno de cache de refleccion para el metodo findFieldsWithAnnotation
     */
    private static class CacheReflection{
        private Class<?> entityClass;
        private Class<? extends Annotation> annotationClass;

        public CacheReflection(Class<?> entityClass, Class<? extends Annotation> annotationClass) {
            this.entityClass = entityClass;
            this.annotationClass = annotationClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheReflection that = (CacheReflection) o;

            if (entityClass != null ? !entityClass.equals(that.entityClass) : that.entityClass != null) return false;
            return annotationClass != null ? annotationClass.equals(that.annotationClass) : that.annotationClass == null;

        }

        @Override
        public int hashCode() {
            int result = entityClass != null ? entityClass.hashCode() : 0;
            result = 31 * result + (annotationClass != null ? annotationClass.hashCode() : 0);
            return result;
        }
    }
}
