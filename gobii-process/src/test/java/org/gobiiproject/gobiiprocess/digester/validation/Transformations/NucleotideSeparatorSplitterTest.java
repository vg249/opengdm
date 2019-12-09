package org.gobiiproject.gobiiprocess.digester.validation.Transformations;


import org.gobiiproject.gobiiprocess.digester.csv.matrixValidation.MatrixErrorUtil;
import org.gobiiproject.gobiiprocess.digester.csv.matrixValidation.NucleotideSeparatorSplitter;

import org.junit.*;



import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class NucleotideSeparatorSplitterTest {

    static boolean runnerHadError = false;
    static List<String> runnerErrorMessage = new ArrayList<String>();
    Set<String> normalMissingSegments = Stream.of("?","uncallable","unc").collect(Collectors.toSet());
    NucleotideSeparatorSplitter nss = new NucleotideSeparatorSplitter(4,normalMissingSegments);

    /**
     * Template code to run the nucleotide splitter
     * @param input
     * @return
     */
    private List<String> runSplitter(List<String> input){
        //Reset error flag and values
        runnerHadError=false;
        runnerErrorMessage = new ArrayList<String>();

        List<String> output = new ArrayList<String>();

        //Override setError to capture error messages
        MatrixErrorUtil dummy = new MatrixErrorUtil(){
            public void setError(String s){
                runnerHadError=true;
                runnerErrorMessage.add(s);
            }
        };

        //run
        nss.process(0,input,output,dummy);

        //return output
        return output;
    }


    @Test
    public void testNormalCase(){
        List<String> input = Arrays.asList("AAAA","ACGT","CCGT","GTTT","ACGT");
        List<String> expectedOutput = Arrays.asList("AAAA","ACGT","CCGT","GTTT","ACGT");

        List<String> output = runSplitter(input);
        assertFalse("No error expected",runnerHadError );
        assertEquals("Transform equals input",expectedOutput,output);

    }
}