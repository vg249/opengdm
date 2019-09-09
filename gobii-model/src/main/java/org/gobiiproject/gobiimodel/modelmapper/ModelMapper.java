package org.gobiiproject.gobiimodel.modelmapper;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ModelMapper {

    private static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        return getAllFields(fields, type);
    }

    /**
     * stockoverflow.com/1042798
     * @param fields
     * @param type
     * @return
     */
    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {

        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if(type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    private static Field getDeclaredField(String fieldName, Class<?> type) {
        try {
            return type.getDeclaredField(fieldName);
        }
        catch(NoSuchFieldException noFiEx) {
            if(type.getSuperclass() != null) {
                return getDeclaredField(fieldName, type.getSuperclass());
            }
            return null;
        }
    }

    private static void maper(Object entityInstance, Object dtoInstance, boolean dtoToEntity) {

        try {

            List<Field> allFields = getAllFields(dtoInstance.getClass());

            for (Field dtoField : allFields) {

                if (dtoField.isAnnotationPresent(GobiiEntityMap.class)) {

                    GobiiEntityMap[] entityMaps = dtoField.getAnnotationsByType(GobiiEntityMap.class);

                    for (GobiiEntityMap entityMap : entityMaps) {

                        if (entityMap.entity().equals(entityInstance.getClass())) {

                            String dtoFieldName = dtoField.getName();

                            String entityFieldName = entityMap.paramName();


                            Field entityField = getDeclaredField(entityFieldName, entityInstance.getClass());

                            if(entityField == null) {
                                continue;
                            }

                            dtoField.setAccessible(true);
                            entityField.setAccessible(true);

                            if(dtoToEntity) {
                                entityField.set(entityInstance, dtoField.get(dtoInstance));
                            }
                            else {
                                dtoField.set(dtoInstance, entityField.get(entityInstance));
                            }

                        }
                    }

                }
            }
        }
        catch(Exception e) {

            LoggerFactory.getLogger(ModelMapper.class).error(e.getMessage());

            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Unable to map DTO to Entity");
        }
    }

    public static void mapDtoToEntity(Object dtoInstance, Object entityInstance) throws  GobiiException {
        ModelMapper.maper(entityInstance, dtoInstance, true);
    }

    public static void mapEntityToDto(Object entityInstance, Object dtoInstance) throws GobiiException {
        ModelMapper.maper(entityInstance, dtoInstance, false);
    }

}
