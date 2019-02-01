package org.gobiiproject.gobiiprocess.digester.csv.matrixValidation;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

public class MatrixErrorUtil {
    private static final int MAX_ERRORS = 20;
    private int errorCount;

    MatrixErrorUtil() {
        errorCount = 0;
    }

    int getErrorCount() {
        return errorCount;
    }

    void incrementErrorCount() {
        errorCount++;
    }

    public void setError(String s) {
        errorCount++;
        ErrorLogger.logError("CSVReader", s);
    }

    boolean stopProcessing() {
        return errorCount > MAX_ERRORS;
    }
}
