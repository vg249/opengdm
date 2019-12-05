package org.gobiiproject.gobiiprocess.digester.csv.matrixValidation;

import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * A separation routine for multi-allelic nucleotides for GDM-446. Takes a delimited string and converts it into an
 * undelimited N character string of alleles, with N being the number of alleles its looking for.
 */
class NucleotideSeparatorSplitter {

    private int nucleotideCount;
    private String unknownAllele = "N";
    private Set<String> validAlleles = new HashSet<>(Arrays.asList(
            "A",
            "C",
            "G",
            "T",
            unknownAllele,
            "+",
            "-"
    ));
    private Set<String> validSeparators = new HashSet<>(Arrays.asList(
            ",",
            "/",
            "|"
    ))

    private List<String>  missingAlts, missingFromFile;

    NucleotideSeparatorSplitter(List<String> missingFileElements) {
        alleles = new ArrayList<>();
        alleles.add("A");
        alleles.add("C");
        alleles.add("G");
        alleles.add("T");
        alleles.add("N");
        alleles.add("+");
        alleles.add("-");
        alleles.add("0");
        alleles.add("");

        missingAlts = new ArrayList<>();
        missingAlts.add("0");

        missingFromFile = new ArrayList<>();
        missingFromFile.addAll(missingFileElements);
        missingFromFile.remove("");//Just in case

        //fileMissingElements
        missingAlts.addAll(missingFromFile);
        alleles.addAll(missingFromFile);
    }

    public NucleotideSeparatorSplitter(int nucleotideCount){
        this.nucleotideCount = nucleotideCount;
    }

    boolean process(int rowNo, List<String> inrow, List<String> outrow, MatrixErrorUtil matrixErrorUtil) {
        boolean returnStatus = true;
        for (String element : inrow) {
            if (element.length() < 1) {
                String errMsg = "SNPSepRemoval Unsupported empty Allele Call in row " + rowNo;
                matrixErrorUtil.setError(errMsg);
                returnStatus = false;
            } else {
                if (missingFromFile.contains(element)) {
                    outrow.add("NN");
                } else {
                    char firstChar = element.charAt(0);
                    char lastChar = element.charAt(element.length() - 1);
                    final String s = "SNPSepRemoval Unsupported Allele Call " + firstChar + " " + lastChar + " in row " + rowNo;

                    String allele1, allele2;
                    if (alleles.contains(String.valueOf(firstChar)) && alleles.contains(String.valueOf(lastChar))) {
                        allele1 = String.valueOf(firstChar);
                        allele2 = String.valueOf(lastChar);
                        if (missingAlts.contains(allele1)) allele1 = "N";
                        if (missingAlts.contains(allele2)) allele2 = "N";
                        outrow.add(allele1 + allele2);
                    } else {
                        matrixErrorUtil.setError(s);
                        returnStatus = false;
                    }
                }
            }
        }
        return returnStatus;
    }
    private String processElement(String element){
        if(missingFromFile.contains(element)){
            return StringUtils.repeat(unknownAllele,nucleotideCount); // A full string of undelimited unknown characters. 4 and N -> NNNN
        }
        int expectedLengthWithSeparators = (nucleotideCount * 2) - 1;
        int length = element.length();
        if(length != nucleotideCount && (length != expectedLengthWithSeparators)){
            return null; // incorrect length
        }
        boolean noSeparators=(length == nucleotideCount);
        if(!noSeparators && length > 1) { //If nucleotideCount=1, there's no separator character
            char separator = element.charAt(1);
            if(!validSeparators.contains(separator)){
                return null; // incorrect separator character OR wrongly sized element
                //Expected separator from <list>, recieved garbage
            }
            element = element.replaceAll(""+separator,"");
        }
        StringBuilder outElement = new StringBuilder();
        //element is now separatorless
        for(char c: element.toCharArray()){
            if()
            outElement.append(c);
        }

        return outElement.toString();
    }
}