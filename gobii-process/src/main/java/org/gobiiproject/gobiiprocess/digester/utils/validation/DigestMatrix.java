package org.gobiiproject.gobiiprocess.digester.utils.validation;

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
                errorBase = validateDatasetList(iNucl,dataSetType);
                if(errorBase!= null){
                    ErrorLogger.logError("Validate Dataset Matrix", "Invalid data found in post-processed matrix line " + lineNumber+ " column "+iLine.indexOf(errorBase)+" - '" + errorBase + "'");
                    //Don't fail on first error, give a reasonable number of lines of warnings
                    if(++errorCount>=maxInvalidWarnings){
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

    /**
     * Checks a dataset list based on data set type
     * @param rowList List of rows
     * @param dataSetType The dataset type expected for teh data
     * @return the string value of the first invalid element in the list
     */
    private static String validateDatasetList(String[] rowList, String dataSetType){
        List<String> elements = null;
        DataSetType dataSetTypeE = DataSetType.valueOf(dataSetType);
        switch (dataSetTypeE) {
            case NUCLEOTIDE_2_LETTER: case  IUPAC:
                elements = initNucleotide2letterList();
                break;
            case CO_DOMINANT_NON_NUCLEOTIDE:
                elements = initCoDominantList();
                break;
            case DOMINANT_NON_NUCLEOTIDE:
                elements = initDominantList();
                break;
            case SSR_ALLELE_SIZE:
                /***
                 * since ssr data has only digits (upto 8)
                 */
                for (String base : rowList){
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
                    boolean isDigit=base.matches("\\d+");
                    boolean isEightCharatersLong=(base.length() == 8);
                    if(!(isDigit && isEightCharatersLong))return base; // Only accept strings of exactly eight characters
                }
                return null;
            case VCF:
                return null;
            default:
                ErrorLogger.logError("Validate Dataset Matrix","Invalid dataset type " + dataSetType);
                break;
        }
        for (String base: rowList) {
            if (!elements.contains(base) && !base.equals("") && !elements.contains(reverse(base))) {
                return base;
            }
        }
        return null;
    }

    /***
     * Assign data in respective data types.
     * @return
     */
    private static List<String> initNucleotide2letterList(){
        List<String> elements = new LinkedList<String>(Arrays.asList("AA", "TT", "CC", "GG", "AT", "AG", "AC", "TG", "TC", "GC", "NN", "++", "--", "+-","AN","CN","GN","TN"));
        for(char c:"ACGTN".toCharArray()){
            elements.add(c+"+");
            elements.add(c+"-");
        }
        return elements;
    }

    private static List<String> initIupacList(){
        List<String> elements = Arrays.asList("A", "T", "G", "C", "N");
        return elements;
    }

    private static List<String> initDominantList(){
        List<String> elements = Arrays.asList("0", "1", "N");
        return elements;
    }

    private static List<String> initCoDominantList(){
        List<String> elements = Arrays.asList("0","1","2", "N");
        return elements;
    }
}
