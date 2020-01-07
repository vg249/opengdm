package org.gobiiproject.gobiiprocess.digester.csv.matrixValidation;

import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * A separation routine for multi-allelic nucleotides for GDM-446. Takes a delimited string and converts it into an
 * undelimited N character string of alleles, with N being the number of alleles its looking for.
 */
public class NucleotideSeparatorSplitter implements RowProcessor {

    private int nucleotideCount;
    private static String unknownAllele = "N";
    private String unknownSegment;
    private static Set<String> validAlleles = new HashSet<>(Arrays.asList(
            "A",
            "C",
            "G",
            "T",
            unknownAllele,
            "+",
            "-"
    ));
    private static Set<String> validSeparators = new HashSet<>(Arrays.asList(
            ",",
            "/",
            "|"
    ));

    private Set<String> missingSegmentsFromFile;

    public NucleotideSeparatorSplitter(int nucleotideCount, Set<String> missingSegmentsFromFile){
        this.nucleotideCount = nucleotideCount;
        this.missingSegmentsFromFile = missingSegmentsFromFile;
        this.unknownSegment = StringUtils.repeat(unknownAllele,nucleotideCount);
    }

    public boolean process(int rowNo, List<String> inrow, List<String> outrow, MatrixErrorUtil matrixErrorUtil) {
        boolean returnStatus = true;
        for (String element : inrow) {
            if (element.length() < 1) {
                String errMsg = "SNPSepRemoval Unsupported empty Allele Call in row " + rowNo;
                matrixErrorUtil.setError(errMsg);
                returnStatus = false;
            } else {
                ProcessResult result = processElement(element);
                if (result.success) {
                    outrow.add(result.element);
                } else {
                    String baseErrMsg = result.errorMsg + "  in row " + rowNo;
                    matrixErrorUtil.setError(baseErrMsg);
                    outrow.add(result.element);
                    returnStatus = false;
                }
            }
        }
        return returnStatus;
    }

    // Important assumptions - element is already whitespace stripped.
    //missingSegmentsFromFile is all lowercase, so 'contains' becomes case insensitive
    private ProcessResult processElement(String element){
        if(missingSegmentsFromFile.contains(element.toLowerCase())){
            return new ProcessResult(true,unknownSegment,null); // A full string of undelimited unknown characters. 4 and N -> NNNN
        }
        int expectedLengthWithSeparators = (nucleotideCount * 2) - 1;
        int length = element.length();
        if(length != nucleotideCount && (length != expectedLengthWithSeparators)){
            return new ProcessResult(false,unknownSegment,"Unexpected Length Element " + element); // incorrect length
        }
        boolean noSeparators=(length == nucleotideCount);
        if(!noSeparators && length > 1) { //If nucleotideCount=1, there's no separator character
            char separator = element.charAt(1);
            if(!validSeparators.contains(""+separator)){
                return new ProcessResult(false,unknownSegment,"Unexpected Separator in " + element + ": " + separator + " Expected:" + Arrays.deepToString(validSeparators.toArray())); // incorrect separator character OR wrongly sized element
                //Expected separator from <list>, recieved garbage
            }
            element = element.replaceAll(""+separator,"");
            if(element.length() != nucleotideCount){
                return new ProcessResult(false,unknownSegment,"Unexpected Segment Count after split in " + element);
            }
        }
        StringBuilder outElement = new StringBuilder();
        //element is now separatorless
        for(char c: element.toCharArray()) {
            if (validAlleles.contains("" + c)) {
                outElement.append(c);
            } else {
                return new ProcessResult(false, null, "Unexpected allele " + c + " in " + element );
            }
        }
        return new ProcessResult(true,outElement.toString(),null);
    }

    private class ProcessResult{
        boolean success;
        String element;
        String errorMsg;
        ProcessResult(boolean success, String element, String errorMsg){
            this.success = success;
            this.element = element;
            this.errorMsg = errorMsg;
        }
    }
}