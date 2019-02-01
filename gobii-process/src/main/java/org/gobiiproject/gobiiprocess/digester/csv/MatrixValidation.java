package org.gobiiproject.gobiiprocess.digester.csv;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.utils.validation.DigestMatrix;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
            setError("Exception in processing matrix file. Irregular size matrix. Expected: " + noOfElements + " actual: " + inputRowList.size());
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
            if (!iupacMatrixToBi.process(rowNo + rowOffset, inputRowList, outputRowList, this)) {
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
            if (!snpSepRemoval.process(rowNo + rowOffset, inputRowList, outputRowList, this)) {
                return false;
            }

        if (isVCF) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(markerFile)))) {
                int lineNo = 0;
                int refPos = -1, altPos = -1;
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (lineNo == 0) {
                        List<String> headerLine = Arrays.asList(line.split("\t"));
                        for (int i = 0; i < headerLine.size(); i++) {
                            if (headerLine.get(i).equalsIgnoreCase("ref"))
                                refPos = i;
                            if (headerLine.get(i).equalsIgnoreCase("alts"))
                                altPos = i;
                        }
                        if (refPos == -1 || altPos == -1) {
                            setError("Exception in processing matrix file. Marker file does not contain ref and alt columns.");
                            return false;
                        }
                        lineNo++;
                    }// As data in the vcf file starts from line with is not zero(generally 10). rowNo will be offset by this amount(10). So we substract offset from rowNo for comparison.
                    // As there is a single header line in digest.marker we are adding one.
                    else if (lineNo == rowNo - (rowOffset) + 1) {
                        List<String> dataLine = Arrays.asList(line.split("\t"));
                        String newLine = dataLine.get(refPos) + "\t" + dataLine.get(altPos);
                        if (!VCFTransformer.transformVCFLine(line, rowNo, inputRowList, outputRowList, this)) {
                            //note, outputRowList here is now a two letter file, which needs to be validated
                            setError("Exception in processing matrix file. Failed in VCF Transformer.");
                            return false;
                        }
                        lineNo++;
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
                datasetType.equalsIgnoreCase("CO_DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("SSR_ALLELE_SIZE") ||
                datasetType.equalsIgnoreCase("NUCLEOTIDE_2_LETTER") || datasetType.equalsIgnoreCase("VCF")) {
            if (datasetType.equalsIgnoreCase("DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("CO_DOMINANT_NON_NUCLEOTIDE") || datasetType.equalsIgnoreCase("SSR_ALLELE_SIZE"))
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
