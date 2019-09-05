package org.gobiiproject.gobiimodel.modelmapper;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ModelMapper {

    public static void mapDtoToEntity(Object dtoInstance, Object entityInstance) {

        try {

            for (Field field : dtoInstance.getClass().getDeclaredFields()) {

                if (field.isAnnotationPresent(GobiiEntityMap.class)) {

                    GobiiEntityMap[] entityMaps = field.getAnnotationsByType(GobiiEntityMap.class);

                    for (GobiiEntityMap entityMap : entityMaps) {

                        if (entityMap.entity().equals(entityInstance.getClass())) {

                            String dtoFieldName = field.getName();

                            String entityFieldName = entityMap.paramName();


                            Field entityField = entityInstance.getClass().getDeclaredField(entityFieldName);

                            field.setAccessible(true);
                            entityField.setAccessible(true);

                            entityField.set(entityInstance, field.get(dtoInstance));

                        }
                    }

                }
            }
        }
        catch(Exception e) {

        }
    }

    public static String getterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() +
                fieldName.substring(1).toString();
    }

    public static String setterName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() +
                fieldName.substring(1).toString();
    }

}
