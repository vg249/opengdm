package org.gobiiproject.gobiiprocess.digester.utils.validation;

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
     * @param errorList error list
     * @return Map of extensions
     */
    static List<String> getAllowedExtensions(List<String> errorList) throws MaximumErrorsValidationException {
        Map<String, String> allowedExtensions = new HashMap<>();
        Field[] fields = DigesterFileExtensions.class.getDeclaredFields();
        List<String> values = null;
        for (Field field : fields) {
            try {
                allowedExtensions.put(String.valueOf(field.get(null)), field.getName());
                values = allowedExtensions.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            } catch (IllegalAccessException e) {
                addMessageToList("Could not get allowed file extensions.", errorList);
                values = new ArrayList<>();
            }
        }
        return values;

    }

    static void printMissingFieldError(String s1, String s2, List<String> errorList) throws MaximumErrorsValidationException {
        addMessageToList("Condition type defined as " + s1 + " but " + s2 + " not defined.", errorList);
    }

    static void addMessageToList(String msg, List<String> errorList) throws MaximumErrorsValidationException {
        errorList.add(msg);
        if (errorList.size() >= 5) throw new MaximumErrorsValidationException();
    }
}
