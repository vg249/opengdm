package org.gobiiproject.gobiimodel.utils;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiAspectTable;

public class AspectUtils {
    public static String getTableName(Class<?> entity) throws NullPointerException {
        return entity.getDeclaredAnnotation(GobiiAspectTable.class).name();
    }

}
