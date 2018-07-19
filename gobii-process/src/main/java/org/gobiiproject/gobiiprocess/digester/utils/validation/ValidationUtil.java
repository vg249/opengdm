package org.gobiiproject.gobiiprocess.digester.utils.validation;

public class ValidationUtil {
    static boolean isNullAndEmpty(String value) {
        return value == null || value.trim().equalsIgnoreCase("");
    }
}
