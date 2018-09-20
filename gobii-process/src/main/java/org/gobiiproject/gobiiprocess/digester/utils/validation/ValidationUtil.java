package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;

import java.util.List;

class ValidationUtil {
    static boolean isNullAndEmpty(String value) {
        return value == null || value.trim().equalsIgnoreCase("");
    }

    static void printMissingFieldError(String s1, String s2, String reason, List<Failure> errorList) throws MaximumErrorsValidationException {
        Failure failure = new Failure();
        failure.reason = reason;
        failure.values.add("Condition type defined " + s1 + " but " + s2 + " not defined.");
        addMessageToList(failure, errorList);
    }

    /**
     * Adds a failure to the failure list.
     * If the same error exists append the values else create a new one.
     * If max errors are more than 5 throw exception
     *
     * @param failure     failure
     * @param failureList failure list
     * @throws MaximumErrorsValidationException exception when maximum number of failures occured
     */
    static void addMessageToList(Failure failure, List<Failure> failureList) throws MaximumErrorsValidationException {
        failureList.add(failure);
        String reason = failure.reason;
        boolean matchNotFound = true;
        for (Failure fail : failureList)
            if (fail.reason.equalsIgnoreCase(reason)) {
                // Both the columns are not null and they are equal. If they are null even thought they are same errors they are not related
                if (failure.columnName.size() > 0 && fail.columnName.size() > 0 && fail.columnName.containsAll(failure.columnName) && failure.columnName.containsAll(fail.columnName)) {
                    fail.values.addAll(failure.values);
                    matchNotFound = false;
                }
            }
        if (matchNotFound) failureList.add(failure);
        int noOfFailures = 0;
        for (Failure fail : failureList) {
            noOfFailures = noOfFailures + fail.values.size();
        }
        if (noOfFailures >= 5) throw new MaximumErrorsValidationException();
    }
}
