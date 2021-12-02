package org.gobiiproject.gobiiprocess;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class HDF5AllelicEncoderTest {

    @Test
    public void testEncodeDecode() throws IOException {

        TempFiles tf = new TempFiles();

        try (FileWriter inputwriter = new FileWriter(tf.inputFile)) {
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

        HDF5AllelicEncoder.createEncodedFile(tf.inputFile, tf.encodedFile, tf.lookupFile,"/","\t");
        HDF5AllelicEncoder.createDecodedFile(tf.encodedFile, tf.lookupFile, tf.decodedFile,"/","\t");


        BufferedReader encodedReader = new BufferedReader(new FileReader(tf.encodedFile));
        BufferedReader outReader = new BufferedReader(new FileReader(tf.decodedFile));
        String line1 = outReader.readLine();
        String line2 = outReader.readLine();
        String line3 = outReader.readLine();
        String line4 = outReader.readLine();
        String line5 = outReader.readLine();

        BufferedReader lookupReader = new BufferedReader(new FileReader(tf.lookupFile));

        Assert.assertEquals("1\tIACAT;IGAG;D",lookupReader.readLine());
        Assert.assertEquals("3\tIGAG;IGAGA",lookupReader.readLine());

        Assert.assertEquals("AC\tGT\tNN",encodedReader.readLine());
        //Assert.assertEquals((char)128 + ""+(char)129+"\t"+"\t"+"AA",encodedReader.readLine());

        Assert.assertEquals("A/C\tG/T\tN/N",line1);
        Assert.assertEquals("ACAT/GAG\t/\tA/A",line2); //given there's no 'length' to the missing data, there is no separator
        Assert.assertEquals("A/C\tG/T\tN/N",line3);
        Assert.assertEquals("A/GAG\tG/GAGA\tA/C",line4);
        Assert.assertEquals("A/C\tG/T\tN/N",line5);


//        input.delete();
//        encoded.delete();
//        lookup.delete();
//        output.delete();
    }

    @Test
    public void testEncodeDecode4letRWExample() throws IOException {
        File input = new File("encodeinput2");
        File encoded = new File("encodeddata2");
        File lookup = new File("lookupfile2");
        File output = new File("decodedinput2");

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

        HDF5AllelicEncoder.createEncodedFile(input, encoded, lookup,null,"\t");
        HDF5AllelicEncoder.createDecodedFile(encoded, lookup, output,"/","\t", true);


        BufferedReader encodedReader = new BufferedReader(new FileReader(encoded));
        BufferedReader outReader = new BufferedReader(new FileReader(output));
        String line1 = outReader.readLine();
        String line2 = outReader.readLine();
        String line3 = outReader.readLine();

        Assert.assertEquals("A/A/A/A\tA/A/C/T",line1);

//        input.delete();
//        encoded.delete();
//        lookup.delete();
//        output.delete();
    }

    private Timings timeEncodeDecode(int rows, int cols) throws Exception {
        if (rows * cols > 1e9) {
            System.err.println("Refusing to test a billion data points.");
            return null;
        }
        String alleleSeparator = "/";
        String elementSeparator = "\t";
        TempFiles tempFiles = new TempFiles();
        tempFiles.createLargeInputFile(rows, cols);
        Timings timings = new Timings();
        long lastTime = System.currentTimeMillis();
        long stepTime;
        HDF5AllelicEncoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile, alleleSeparator, elementSeparator);
        timings.encode = (stepTime = System.currentTimeMillis()) - lastTime;
        lastTime = stepTime;
        HDF5AllelicEncoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile, alleleSeparator, elementSeparator);
        timings.decode = System.currentTimeMillis() - lastTime;
        assertFilesAreEqual(tempFiles.inputFile, tempFiles.decodedFile);
        return timings;
    }

    @Test
    public void test10MDatapoints() throws Exception {
        Timings timings = timeEncodeDecode(10_000, 1_000);
        System.err.println(timings);
    }

    @Test
    public void test100MDatapoints() throws Exception {
        Timings timings = timeEncodeDecode(10_000, 10_000);
        System.err.println(timings);
    }

    @Test
    public void testLargeFileEncoding() throws Exception {
//                              copying_array   reading_array   simple_encoding simpler_encoding    ssr_check
//        dimensions	enc(s)	dec(s)          dec(s)          enc(s)	dec(s)  enc(s)	dec(s)      enc(s)	dec(s)
//        (1*1000000)	0.578	1.438           1.396           0.503	1.412   0.567	1.439       0.511	1.397
//        (10*100000)	0.322	1.376           1.335           0.273	1.358   0.283	1.344       0.276	1.327
//        (100*10000)	0.259	1.372           1.335           0.238	1.354   0.236	1.338       0.236	1.304
//        (1000*1000)	0.272	1.378           1.387           0.239	1.36    0.238	1.329       0.239	1.319
//        (10000*100)	0.293	1.373           1.324           0.252	1.34    0.259	1.312       0.254	1.312
//        (100000*10)	0.431	1.815           1.706           0.353	1.683   0.381	1.663       0.353	1.645
//        (1000000*1)	1.75	6.526           6.33            1.339	5.851   1.308	5.843       1.287	5.778

        System.err.println("dimensions\tenc(s)\tdec(s)");
        for (int i = 0; i < 7; i ++) {
            int rows = (int) Math.round(Math.pow(10, i));
            int j = 6 - i;
            int cols = (int) Math.round(Math.pow(10, j));
            Timings timings = timeEncodeDecode(rows, cols);
            if (timings == null) continue;
            System.err.println("(" + rows + "*" +  cols + ")\t" + (timings.encode / 1e3) + "\t" + (timings.decode / 1e3) + "");
        }
    }

    private void assertFilesAreEqual(File file1, File file2) throws IOException {
        BufferedReader r1 = new BufferedReader(new FileReader(file1));
        BufferedReader r2 = new BufferedReader(new FileReader(file2));
        String line1 = null;
        String line2 = null;
        while ((line1 = r1.readLine()) != null && (line2 = r2.readLine()) != null) {
            assert line1.equals(line2);
        }
        if (line1 == null && r2.readLine() != null) {
            throw new AssertionError();
        }
    }

    static class TempFiles {
        public File inputFile;
        public File lookupFile;
        public File encodedFile;
        public File decodedFile;
        public File sampleFastEncodedFile;
        public File sampleFastInputFile;
        private final String timestamp;

        TempFiles() { this(false); }

        TempFiles(boolean debug) {
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HHmmssnnnnnn"));
            inputFile             = createTempFile("input-mf");
            lookupFile            = createTempFile("lookup");
            encodedFile           = createTempFile("encoded-mf");
            decodedFile           = createTempFile("decoded");
            sampleFastEncodedFile = createTempFile("encoded-sf");
            sampleFastInputFile   = createTempFile("input-sf");
            if (debug) {
                System.err.println(
                        "Input file: "        + inputFile             + "\n" +
                                "Input file (sf): "   + sampleFastInputFile   + "\n" +
                                "Lookup file: "       + lookupFile            + "\n" +
                                "Encoded file: "      + encodedFile           + "\n" +
                                "Encoded file (sf): " + sampleFastEncodedFile + "\n" +
                                "Decoded file: "      + decodedFile
                );
            }
        }

        private File createTempFile(String prefix) {
            String tmpDir = System.getProperty("java.io.tmpdir");
            String path = tmpDir + "/" + timestamp + "_" + prefix + ".tmp";
            return new File(path);
        }

        private void createLargeInputFile(int rows, int cols) throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile));
            int j = 0;
            int i = 0;
            String sameLine = null;
            while (i < rows) {
                if (sameLine == null) {
                    StringJoiner lineJoiner = new StringJoiner("\t");
                    while (j < cols) {
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
        }

        private void createRandomInputFile(int rows, int cols) throws IOException {
            String[] alleles = {"A", "C", "AC", "ACG"};
            createRandomInputFile(rows, cols, 2, alleles);
        }

        private void createRandomInputFile(int rows, int cols, int ploidy) throws IOException {
            String[] alleles = {"A", "C", "AC", "ACG"};
            createRandomInputFile(rows, cols, ploidy, alleles);
        }

        private void createRandomInputFile(int rows, int cols, int ploidy, String[] alleles) throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile));
            for (int i = 0; i < rows; i++) {
                String row = createRandomRow(cols, alleles, ploidy, "\t", "/");
                writer.append(row);
                writer.append(System.lineSeparator());
            }
            writer.close();
        }

        public Stream<String> getTransposedFileStream(File file) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int nRow = reader.readLine().split("\t").length;
            int nCol = (int) reader.lines().count() + 1;
            String[][] transposed = new String[nRow][nCol];
            reader.close();
            reader = new BufferedReader(new FileReader(file));
            String inputLine;
            for (int j = 0; j < nCol; j++) {
                inputLine = reader.readLine();
                if (inputLine == null) throw new IOException("Unexpected end of file.");
                String[] splitLine = inputLine.split("\t");
                for (int i = 0; i < nRow; i++) {
                    transposed[i][j] = splitLine[i];
                }
            }
            reader.close();
            return Arrays.stream(transposed)
                    .map((line) -> String.join("\t", line));
//                    .collect(Collectors.toList());
        }

        private void createSampleFastInputFile() throws Exception {
            BufferedWriter writer = new BufferedWriter(new FileWriter(sampleFastInputFile));
            getTransposedFileStream(inputFile).forEach(line -> {
                try {
                    writer.append(line);
                    writer.append(System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        }

        private void createSampleFastEncodedFile() throws Exception {
            BufferedWriter writer = new BufferedWriter(new FileWriter(sampleFastEncodedFile));
            getTransposedFileStream(encodedFile).forEach(line -> {
                try {
                    writer.append(line);
                    writer.append(System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        }

        String createRandomRow(int length, String[] alleles, int ploidy, String elementSeparator, String alleleSeparator) {
            StringJoiner row = new StringJoiner(elementSeparator);
            Random random = new Random();
            int n = alleles.length;
            for (int i = 0; i < length; i++) {
                StringJoiner gt = new StringJoiner(alleleSeparator);
                for (int j = 0; j < ploidy; j++) {
                    gt.add(alleles[random.nextInt(n)]);
                }
                row.merge(gt);
            }
            return row.toString();
        }
    }

    static class Timings {
        public long encode;
        public long decode;

        public Timings(int encode, int decode) {
            this.encode = encode;
            this.decode = decode;
        }

        public Timings() { this(0, 0); }

        @Override
        public String toString() {
            return "encode: " + (encode / 1e3) + "s" + "\n" +
                   "decode: " + (decode / 1e3) + "s";
        }
    }

    static class Dims {
        public int rows;
        public int cols;

        public Dims(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }
    }

}