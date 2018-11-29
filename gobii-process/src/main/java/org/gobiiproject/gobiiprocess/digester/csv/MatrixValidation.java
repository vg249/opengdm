package org.gobiiproject.gobiiprocess.digester.csv;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.utils.validation.DigestMatrix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class MatrixValidation {

    private static final int MAX_ERRORS = 20;
    private final String datasetType, missingFile;
    private int errorCount;
    private int noOfElements;
    private IUPACmatrixToBi iupacMatrixToBi;
    private SNPSepRemoval snpSepRemoval;

    public int getErrorCount() {
        return errorCount;
    }

    MatrixValidation(String datasetType, String missingFile) {
        this.datasetType = datasetType;
        this.missingFile = missingFile;
        errorCount = 0;
        noOfElements = 0;
        iupacMatrixToBi = new IUPACmatrixToBi();
    }

    boolean setUp() {
        List<String> missingFileElements = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(missingFile))) {
            stream.forEach(missingFileElements::add);
        } catch (IOException e) {
            errorCount++;
            ErrorLogger.logError("SNPSepRemoval", "Exception in reading SNSSepRemoval missing values.");
            return false;
        }
        snpSepRemoval = new SNPSepRemoval(missingFileElements);
        return true;
    }

    boolean validate(int rowNo, List<String> inputRowList, List<String> outputRowList) {
        boolean returnStatus = true;
        if (noOfElements == 0) noOfElements = inputRowList.size();
        else if (noOfElements != inputRowList.size()) {
            errorCount++;
            ErrorLogger.logError("CSVReader", "Exception in processing matrix file. Irregular size matrix. Expected: " + noOfElements + " actual: " + inputRowList.size());
            returnStatus = false;
        }

        /*
         *   IUPAC conversion
         *   Tried to convert IUPAC to BI.
         *   If it fails for a row that semi processed row is added.
         *   Reason being, as it is already a failure we are processing further to identify all errors once.
         *
         * */
        if (datasetType.equalsIgnoreCase("IUPAC"))
            if (!iupacMatrixToBi.process(rowNo, inputRowList, outputRowList)) {
                returnStatus = setError("Exception in processing matrix file. Failed in IUPAC conversion.");
            }

        /*
         *   SNP sep removal
         *   Tried SNP Separator Removal.
         *   If it fails for a row that semi processed row is added.
         *   Reason being, as it is already a failure we are processing further to identify all errors once.
         *
         * */
        if (datasetType.equalsIgnoreCase("NUCLEOTIDE_2_LETTER"))
            if (!snpSepRemoval.process(rowNo, inputRowList, outputRowList)) {
                returnStatus = setError("Exception in processing matrix file. Failed in SNPSepRemoval.");
            }

        //TODO: Handle the case of VCF validation
            VCF validation goes here

        /*
         * VALIDATION.
         * Validates each element is a valid value or not based on the datasetType.
         * */
        if (datasetType.equalsIgnoreCase("DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("IUPAC") ||
                datasetType.equalsIgnoreCase("CO_DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("SSR_ALLELE_SIZE"))
            if (!DigestMatrix.validateDatasetList(rowNo, outputRowList, datasetType)) {
                returnStatus = setError("Exception in processing matrix file. Failed in file validation.");
            }
        return returnStatus;
    }

    private boolean setError(String s) {
        errorCount++;
        ErrorLogger.logError("CSVReader", s);
        return false;
    }


    boolean stopProcessing() {
        return errorCount > MAX_ERRORS;
    }
}
