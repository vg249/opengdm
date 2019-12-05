package org.gobiiproject.gobiiprocess.digester.csv.matrixValidation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author csarma
 * Removes separator from SNP
 */
class SNPSepRemoval {

    private List<String> alleles, missingAlts, missingFromFile;

    SNPSepRemoval(List<String> missingFileElements) {
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
}