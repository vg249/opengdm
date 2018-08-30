package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.DigesterFileExtensions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ValidationUtil {
    static boolean isNullAndEmpty(String value) {
        return value == null || value.trim().equalsIgnoreCase("");
    }

    /**
     * Gets the list of allowed file extensions
     *
     * @return Map of extensions
     * @throws IllegalAccessException
     */
    static List<String> getAllowedExtensions() {
        Map<String, String> allowedExtensions = new HashMap<>();
        Field[] fields = DigesterFileExtensions.class.getDeclaredFields();
        List<String> values = null;
        for (Field field : fields) {
            try {
                allowedExtensions.put(String.valueOf(field.get(null)), field.getName());
                values = allowedExtensions.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            } catch (IllegalAccessException e) {
                ErrorLogger.logError("Could not get allowed file extensions.", "");
                values = new ArrayList<>();
            }
        }
        return values;

    }

    static void printMissingFieldError(String s1, String s2, List<String> errorList) {
        errorList.add("Condition type defined as " + s1 + " but " + s2 + " not defined.");
    }

}
