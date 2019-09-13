package org.gobiiproject.gobiimodel.modelmapper;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.*;

public class ModelMapper {

    private static List<Field> getAllDeclaredFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        return getAllDeclaredFields(fields, type);
    }

    /**
     * stockoverflow.com/1042798
     * @param fields
     * @param type
     * @return
     */
    private static List<Field> getAllDeclaredFields(List<Field> fields, Class<?> type) {

        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if(type.getSuperclass() != null) {
            getAllDeclaredFields(fields, type.getSuperclass());
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

            List<Field> allFields = getAllDeclaredFields(dtoInstance.getClass());

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

    public static Map<String, EntityFieldBean> getDtoEntityMap(Class dtoClassName) {

        Map<String, EntityFieldBean> returnVal = new HashMap<>();

        try {

            for(Field field : dtoClassName.getDeclaredFields()) {

                if(field.isAnnotationPresent(GobiiEntityMap.class)) {

                    GobiiEntityMap gobiiEntityMap = field.getAnnotation(GobiiEntityMap.class);

                    String entityParamName = gobiiEntityMap.paramName();

                    Class entityClass = gobiiEntityMap.entity();

                    if(entityParamName != null & entityClass != null) {

                        Field entityField;

                        try {
                            entityField = entityClass.getDeclaredField(entityParamName);
                        }
                        catch(NoSuchFieldException noFiEx) {
                            continue;
                        }

                        if(entityField.isAnnotationPresent(Column.class)) {

                            Column jpaEntity = entityField.getAnnotation(Column.class);

                            String dbColumnName = jpaEntity.name();

                            EntityFieldBean entityFieldBean = new EntityFieldBean();

                            entityFieldBean.setColumnName(dbColumnName);

                            returnVal.put(field.getName(), entityFieldBean.);

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

        return returnVal;

    }

}
