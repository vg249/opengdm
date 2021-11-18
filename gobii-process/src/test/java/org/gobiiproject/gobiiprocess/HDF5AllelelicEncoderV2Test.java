package org.gobiiproject.gobiiprocess;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.StringJoiner;

public class HDF5AllelelicEncoderV2Test {

    private final String elementSeparator = "\t";
    private final String alleleSeparator = "/";

    @Test
    public void testEncodeDecode() throws IOException {
        File input = new File("encodeinput");
        File encoded = new File("encodeddata");
        File lookup = new File("lookupfile");
        File output = new File("decodedinput");

        input.deleteOnExit();
        encoded.deleteOnExit();
        lookup.deleteOnExit();
        output.deleteOnExit();

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

        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator, alleleSeparator);
        encoder.createEncodedFile(input, encoded, lookup);
        encoder.createDecodedFile(encoded, lookup, output);


        BufferedReader encodedReader = new BufferedReader(new FileReader(encoded));
        BufferedReader outReader = new BufferedReader(new FileReader(output));
        String line1 = outReader.readLine();
        String line2 = outReader.readLine();
        String line3 = outReader.readLine();
        String line4 = outReader.readLine();
        String line5 = outReader.readLine();

        BufferedReader lookupReader = new BufferedReader(new FileReader(lookup));

        Assert.assertEquals("1\tIACAT;IGAG",lookupReader.readLine());
        Assert.assertEquals("3\tIGAG;IGAGA",lookupReader.readLine());

        Assert.assertEquals("AC\tGT\tNN",encodedReader.readLine());
        //Assert.assertEquals((char)128 + ""+(char)129+"\t"+"\t"+"AA",encodedReader.readLine());

        Assert.assertEquals("A/C\tG/T\tN/N",line1);
        Assert.assertEquals("ACAT/GAG\t\tA/A",line2);//given there's no 'length' to the missing data, there is no separator
        Assert.assertEquals("A/C\tG/T\tN/N",line3);
        Assert.assertEquals("A/GAG\tG/GAGA\tA/C",line4);
        Assert.assertEquals("A/C\tG/T\tN/N",line5);

    }

    @Test
    public void testEncodeDecode4letRWExample() throws IOException {
        File input = new File("encodeinput2");
        File encoded = new File("encodeddata2");
        File lookup = new File("lookupfile2");
        File output = new File("decodedinput2");

        input.deleteOnExit();
        encoded.deleteOnExit();
        lookup.deleteOnExit();
        output.deleteOnExit();

        try (FileWriter inputwriter = new FileWriter(input)) {
            inputwriter.write("A/A/A/A\tA/A/C/T");
            inputwriter.write(System.lineSeparator());
            inputwriter.write("A/C/G/T\tUncallable");
            inputwriter.write(System.lineSeparator());
            inputwriter.write("T/T/T/T\tUNKNOWN");
            inputwriter.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }

        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator, alleleSeparator);
        encoder.createEncodedFile(input, encoded, lookup);
        encoder.createDecodedFile(encoded, lookup, output);


        BufferedReader encodedReader = new BufferedReader(new FileReader(encoded));
        BufferedReader outReader = new BufferedReader(new FileReader(output));
        String line1 = outReader.readLine();
        String line2 = outReader.readLine();
        String line3 = outReader.readLine();

        Assert.assertEquals("A/A/A/A\tA/A/C/T",line1);
    }

    @Test
    public void testEncodeLargeFiles() throws IOException {
        testEncodeLargeFile(100, 100);
        testEncodeLargeFile(1000, 1000);
        testEncodeLargeFile(1000, 10000);
        testEncodeLargeFile(10000, 1000);
    }

    @Test
    public void testLargeFileEncoding() throws IOException {
        long startTime = System.currentTimeMillis();
        TempFiles tempFiles = new TempFiles();
        int rows = 20000;
        int cols = 5000;
        tempFiles.createLargeInputFile(rows, cols);
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator, alleleSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        encoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile);
        assertFilesAreEqual(tempFiles.inputFile, tempFiles.decodedFile);
        long totalTime = System.currentTimeMillis() - startTime;
        assert totalTime < 60000; // should be able to encode + decode 1M data points in under a minute
    }

    public void timeLargeFileEncoding() throws IOException {
        long startTime = System.currentTimeMillis();
        long lastTime = startTime;
        long stepTime;
        TempFiles tempFiles = new TempFiles();
        int rows = 200000;
        int cols = 5000;
        System.err.print("Creating input file (" + rows + " * " + cols + ")...");
        tempFiles.createLargeInputFile(rows, cols);
        stepTime = System.currentTimeMillis();
        System.err.println("took " + ((stepTime - lastTime) / 1000.0) + "s");
        lastTime = stepTime;
        System.err.print("Encoding file...");
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator, alleleSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        stepTime = System.currentTimeMillis();
        System.err.println("took " + ((stepTime - lastTime) / 1000.0) + "s");
        lastTime = stepTime;
        System.err.print("Decoding file...");
        encoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile);
        stepTime = System.currentTimeMillis();
        System.err.println("took " + ((stepTime - lastTime) / 1000.0) + "s");
        assertFilesAreEqual(tempFiles.inputFile, tempFiles.decodedFile);
        stepTime = System.currentTimeMillis();
        System.err.println("Total time was " + ((stepTime - startTime) / 1000.0) + "s");
    }

    private void testEncodeLargeFile(int rows, int cols) throws IOException {
        TempFiles tempFiles = new TempFiles();
        tempFiles.createLargeInputFile(rows, cols);
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator, alleleSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        encoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile);
        assertFilesAreEqual(tempFiles.inputFile, tempFiles.decodedFile);
    }

    public void assertFilesAreEqual(File file1, File file2) throws IOException {
        BufferedReader r1 = new BufferedReader(new FileReader(file1));
        BufferedReader r2 = new BufferedReader(new FileReader(file2));
        String line1;
        String line2;
        int i = 0;
        while ((line1 = r1.readLine()) != null && (line2 = r2.readLine()) != null) {
            if (!line1.equals(line2)) {
                String message = "Found difference between files on line " + i + ":\n\t" + file1 + "\n\t" + file2;
                System.err.println(line1);
                System.err.println(line2);
                throw new AssertionError(message);
            }
            i++;
        }
    }

    static class TempFiles {
        public File inputFile;
        public File lookupFile;
        public File encodedFile;
        public File decodedFile;
        public String encodedRow;
        public String decodedRow;
        public String lookupRow;

        TempFiles() throws IOException {
            inputFile = File.createTempFile("input", ".temp");
            lookupFile = File.createTempFile("lookup", ".temp");
            encodedFile = File.createTempFile("encoded", ".temp");
            decodedFile = File.createTempFile("decoded", ".temp");
            inputFile.deleteOnExit();
            lookupFile.deleteOnExit();
            encodedFile.deleteOnExit();
            decodedFile.deleteOnExit();
        }

        public void createLargeInputFile(int rows, int cols) throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile));
            int j = 0;
            int i = 0;
            String sameLine = null;
            while (i <= rows) {
                if (sameLine == null) {
                    StringJoiner lineJoiner = new StringJoiner("\t");
                    while (j <= cols) {
                        lineJoiner.add("A/A")      ;
                        lineJoiner.add("ACGT/ACGT");
                        lineJoiner.add("ACGTA/ACG");
                        lineJoiner.add("TTTT/T")   ;
                        j += 4;
                    }
                    sameLine = lineJoiner.toString();
                }
                writer.append(sameLine);
                writer.append(System.lineSeparator());
                i++;
            }
            writer.close();
            this.decodedRow = sameLine;
            this.lookupRow = Math.min(10, rows) + "\t" + "IACGT;IACGTA;IACG;ITTTT";
        }
    }

    static class TempFileAndRow {
        public File inputFile;
        public File lookupFile;
        public File encodedFile;
        public File decodedFile;
        public String[] row;


        public TempFileAndRow(File inputFile, File lookupFile, File encodedFile, File decodedFile, String[] row) {
            this.inputFile = inputFile;
            this.lookupFile = lookupFile;
            this.encodedFile = encodedFile;
            this.decodedFile = decodedFile;
            this.row = row;
        }
    }

}


