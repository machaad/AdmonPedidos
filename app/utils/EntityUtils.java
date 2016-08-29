package utils;

import models.BaseEntity;
import models.SecurityToken;
import models.User;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.mvc.Http;
import security.SecurityDeadboltHandler;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilerias para entidades Ebean
 * Created by pmendoza on 10/22/15.
 */
public class EntityUtils {

    @Documented
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NullifyOnDelete {
    }


    public static void startNullifying(BaseEntity base){

        try {
            Map<Field,PropertyDescriptor> set = ReflectionUtils.findFieldsWithAnnotation(base.getClass(), NullifyOnDelete.class);
            for(Map.Entry<Field,PropertyDescriptor> entry : set.entrySet()){
                try {
                    Object obj = entry.getValue().getReadMethod().invoke(base);
                    if (obj instanceof List) {
                        List list = (List) obj;
                        if (list.isEmpty()) {
                            return;
                        }
                        Class childClass = list.get(0).getClass();
                        PropertyDescriptor setter = null;
                        for (PropertyDescriptor cpd : Introspector.getBeanInfo(childClass).getPropertyDescriptors()) {
                            if(cpd.getPropertyType().isAssignableFrom(base.getClass())){
                                setter = cpd;
                                break;
                            }
                        }
                        if (setter == null) return;
                        for (Object child : list) {
                            Logger.error("ssiii");
                            if(child instanceof BaseEntity) {
                                Logger.error("AAAAAhh " + child.toString());
                                setter.getWriteMethod().invoke(child, new Object[]{null});
                                ((BaseEntity)child).save();
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
