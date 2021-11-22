package org.gobiiproject.gobiiprocess.encoder.utils;

import org.gobiiproject.gobiiprocess.encoder.HDF5AllelicEncoder;
import org.junit.Test;

import java.io.*;
import java.util.StringJoiner;

public class ByteLineReaderTest {
    File encoded = new File("/Users/err87/Desktop/slow-encoding/12-col_10-row_old-enc.tmp");

    @Test
    public void testBlr() throws Exception {
        ByteLineReader blr = new ByteLineReader(encoded, "\t");
        int[][] line;
        while ((line = blr.readLineUBytes()) != null) {
            StringJoiner output = new StringJoiner(",");
//            output.add("line length = " + line.length);
//            UnsignedBytes ub = new UnsignedBytes(line[0][0]);
            for (int[] ba : line) {
                StringJoiner elt = new StringJoiner("/");
                for (int b : ba) {
                    elt.add(b + "");
                }
                output.merge(elt);
            }
            System.err.println(output);
        }
    }

    @Test
    public void foo() throws Exception {
        File input = new File("encodeinput");
        File encoded = new File("encodeddata");
        File lookup = new File("lookupfile");
        File output = new File("decodedinput");

        try (FileWriter inputwriter = new FileWriter(input)) {
            inputwriter.write("A/C\tG/T\tN/N");
            inputwriter.write(System.lineSeparator());
            inputwriter.write("ACAT/GAG\t/\tA/A");
            inputwriter.write(System.lineSeparator());
            inputwriter.write("A/C\tG/T\tN/N");
            inputwriter.write(System.lineSeparator());
            inputwriter.write("A/GAG\tG/GAGA\tA/C");
            inputwriter.write(System.lineSeparator());
            inputwriter.write("A/C\tG/T\tN/N");
            inputwriter.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        HDF5AllelicEncoder.createEncodedFile(input,encoded,lookup,"/","\t");
        ByteLineReader blr = new ByteLineReader(encoded, "\t");
        byte[][] byteLine;
        while ((byteLine = blr.readLine()) != null) {
//            System.err.println(byteLine.length);
        }
    }
}