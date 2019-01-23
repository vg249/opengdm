package org.gobiiproject.gobiiprocess.digester.csv;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.utils.validation.DigestMatrix;
import org.gobiiproject.gobiiprocess.digester.vcf.VCFTransformer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MatrixValidation {

    private static final int MAX_ERRORS = 20;
    private final String datasetType, missingFile, markerFile;
    private int errorCount;
    private int noOfElements;
    private IUPACmatrixToBi iupacMatrixToBi;
    private SNPSepRemoval snpSepRemoval;

    int getErrorCount() {
        return errorCount;
    }

    MatrixValidation(String datasetType, String missingFile, String markerFile) {
        this.datasetType = datasetType;
        this.missingFile = missingFile;
        this.markerFile = markerFile;
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

    boolean validate(int rowNo, int rowOffset, List<String> inputRowList, List<String> outputRowList, boolean isVCF) {
        if (noOfElements == 0) noOfElements = inputRowList.size();
        else if (noOfElements != inputRowList.size()) {
            errorCount++;
            ErrorLogger.logError("CSVReader", "Exception in processing matrix file. Irregular size matrix. Expected: " + noOfElements + " actual: " + inputRowList.size());
            return false;
        }

        /*
         *   IUPAC conversion
         *   Tried to convert IUPAC to BI.
         *   If it fails for a row that semi processed row is added.
         *   Reason being, as it is already a failure we are processing further to identify all errors once.
         *
         * */
        if (datasetType.equalsIgnoreCase("IUPAC"))
            if (!iupacMatrixToBi.process(rowNo + rowOffset, inputRowList, outputRowList)) {
                setError("Exception in processing matrix file. Failed in IUPAC conversion.");
                return false;
            }

        /*
         *   SNP sep removal
         *   Tried SNP Separator Removal.
         *   If it fails for a row that semi processed row is added.
         *   Reason being, as it is already a failure we are processing further to identify all errors once.
         *
         * */
        if (datasetType.equalsIgnoreCase("NUCLEOTIDE_2_LETTER") && !isVCF)
            if (!snpSepRemoval.process(rowNo, inputRowList, outputRowList)) {
                setError("Exception in processing matrix file. Failed in SNPSepRemoval.");
                return false;
            }

        //TODO: Handle the case of VCF validation
        //TODO: figure out how to get a line of the digest.marker file, which is required to exist for VCF loads
        if (isVCF) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(markerFile)))) {
                int lineNo = 0;
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (lineNo == rowNo) {
                        if (!VCFTransformer.transformVCFLine(line, rowNo, inputRowList, outputRowList)) {
                            //note, outputRowList here is now a two letter file, which needs to be validated
                            setError("Exception in processing matrix file. Failed in VCF Transformer.");
                            return false;
                        }
                    } else lineNo++;
                }

            } catch (FileNotFoundException e) {
                setError("Could not find marker file");
                return false;
            } catch (IOException e) {
                setError("Could not read marker file" + e.getMessage());
                return false;
            }

        }

        /*
         * VALIDATION.
         * Validates each element is a valid value or not based on the datasetType.
         * */
        if (datasetType.equalsIgnoreCase("DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("IUPAC") ||
                datasetType.equalsIgnoreCase("CO_DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("SSR_ALLELE_SIZE")) {
            if (datasetType.equalsIgnoreCase("DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("CO_DOMINANT_NON_NUCLEOTIDE"))
                outputRowList.addAll(inputRowList);
            return DigestMatrix.validateDatasetList(rowNo + rowOffset, outputRowList, datasetType, this);
        } else return true;
    }

    public void setError(String s) {
        errorCount++;
        ErrorLogger.logError("CSVReader", s);
    }


    boolean stopProcessing() {
        return errorCount > MAX_ERRORS;
    }
}
