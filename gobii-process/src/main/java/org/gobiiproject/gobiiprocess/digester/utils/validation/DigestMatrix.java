package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.reverse;


public class DigestMatrix {
    private static int maxInvalidWarnings=20; //Or reasonable number of choice
    private static long startTime, endTime, duration;
    private static String fSep="\t";

    /***
     * Validating digest.matrix(output of CSVReaderV2) for invalid data types.
     * @param inFile - digest.matrix
     * @param dataSetType - loaderInstruction dataset type
     * @return If the validation was successful. If False, ErrorLogger will already have the problem
     */
    public static boolean validatematrix (File inFile, String dataSetType){
        startTime = System.currentTimeMillis();
        int errorCount=0;
        try (BufferedReader buffIn=new BufferedReader(new FileReader(inFile))){
            String iLine;
            int lineNumber=0;
            String errorBase;
            while ((iLine = buffIn.readLine()) != null) {
                lineNumber++;
                if(iLine.equals("matrix")) continue;
                String[] iNucl = iLine.split(fSep);
                if (validateDatasetList(lineNumber, Arrays.asList(iNucl), dataSetType)) {
                    //Don't fail on first error, give a reasonable number of lines of warnings
                    if (++errorCount>=maxInvalidWarnings) {
                        ErrorLogger.logError("Validate Dataset Matrix", "Reached max warnings for Invalid data");
                        return false;
                    }
                }
            }
            buffIn.close();
            endTime = System.currentTimeMillis();
            duration = endTime-startTime;
            ErrorLogger.logTrace("Validation Matrix","Time taken:"+ duration/1000 + " Secs");
        }
        catch (FileNotFoundException e){
            ErrorLogger.logError("Validation Matrix", "File not found.", e);
        }
        catch (IOException e){
            ErrorLogger.logError("Validation Matrix", "Unable to open the matrix file.", e);
        }
        catch(Exception e){
            ErrorLogger.logError("Digest Matrix",e);
        }
        return (errorCount==0);//true if no Invalid Data warnings
    }

   
    public static boolean validateDatasetList(int lineNumber, List<String> rowList, String type) {
        List<String> allowedCharacters;
        DataSetType dataSetType = DataSetType.valueOf(type);
        switch (dataSetType) {
            case NUCLEOTIDE_2_LETTER:
            case IUPAC:
                allowedCharacters = initNucleotide2letterList();
                break;
            case CO_DOMINANT_NON_NUCLEOTIDE:
                allowedCharacters = initCoDominantList();
                break;
            case DOMINANT_NON_NUCLEOTIDE:
                allowedCharacters = initDominantList();
                break;
            case SSR_ALLELE_SIZE:
                /*
                 * since ssr data has only digits (upto 8)
                 */
                for (String element : rowList) {
                    /*
                    TODO - Since no filtering on {1-4}d/{1-4}d, don't support that format now
                    if (base.contains("/")){
                        String[] bases = base.split("/");
                        for(String digit: bases){
                            if(!digit.matches("\\d+") && !digit.equals("N") && (digit.length() > 8)){//Checks of the data length if more that 8 digits. (to save it from HDF5)
                                return digit;
                            }
                        }
                    }
                    else{
                        */
                    // Only accept numerical strings of exactly eight characters
                    if (!(StringUtils.isNumeric(element) && element.length() == 8)) {
                        ErrorLogger.logError("Validate Dataset Matrix", "Invalid data found in post-processed matrix line: " + lineNumber + " Data:" + element);
                        return false;
                    }
                }
                return true;
            case VCF:
                return false;
            default:
                ErrorLogger.logError("Validate Dataset Matrix", "Invalid dataset type " + dataSetType);
                return false;
        }
        for (String element : rowList)
            if (element == null || element.equals("") || !allowedCharacters.contains(element)) {
                ErrorLogger.logError("Validate Dataset Matrix", "Invalid data found in post-processed matrix line: " + lineNumber + " Data:" + element);
                return false;
            }
        return true;
    }

    /***
     * Assign data in respective data types.
     */
    private static List<String> initNucleotide2letterList() {
        List<String> elements = new LinkedList<>(Arrays.asList("AA", "TT", "CC", "GG", "AT", "AG", "AC", "TG", "TC", "GC", "NN", "++", "--", "+-", "-+", "AN", "CN", "GN", "TN",
                "TA", "GA", "CA", "GT", "CT", "CG", "NA", "NC", "NG", "NT"));
        for (char c : "ACGTN".toCharArray()) {
            elements.add(c + "+");
            elements.add(c + "-");
            elements.add("+" + c);
            elements.add("-" + c);
        }
        return elements;
    }

    private static List<String> initDominantList() {
        return Arrays.asList("0", "1", "N");
    }

    private static List<String> initCoDominantList() {
        return Arrays.asList("0", "1", "2", "N");
    }
}
