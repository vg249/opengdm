package org.gobiiproject.gobiiprocess;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HDF5AllelicEncoderV2Test {

    private final String elementSeparator = "\t";
    private final String alleleSeparator = "/";

    @Test
    public void testEncodeDecode() throws Exception {
        TempFiles tempFiles = new TempFiles();

        try (FileWriter inputwriter = new FileWriter(tempFiles.inputFile)) {
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
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        encoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile);

        BufferedReader encodedReader = new BufferedReader(new FileReader(tempFiles.encodedFile));
        BufferedReader outReader = new BufferedReader(new FileReader(tempFiles.decodedFile));
        String line1 = outReader.readLine();
        String line2 = outReader.readLine();
        String line3 = outReader.readLine();
        String line4 = outReader.readLine();
        String line5 = outReader.readLine();

        BufferedReader lookupReader = new BufferedReader(new FileReader(tempFiles.lookupFile));

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
    public void testCreateDecodedFileFromList() throws Exception {
        TempFiles tempFiles = new TempFiles();
        tempFiles.createRandomInputFile(10, 10, 2);
        Integer[] positions = {1, 2, 3, 5};
        String posList = Arrays.stream(positions).map(String::valueOf).collect(Collectors.joining("\n"));
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        encoder.createDecodedFileFromList(tempFiles.encodedFile, tempFiles.lookupFile, posList, tempFiles.decodedFile, true);
        BufferedReader r1 = new BufferedReader(new FileReader(tempFiles.decodedFile));
        BufferedReader r2 = new BufferedReader(new FileReader(tempFiles.inputFile));
        List<String> decodedLines = r1.lines().collect(Collectors.toList());
        List<String> originalLines = r2.lines().collect(Collectors.toList());
        List<String> originalPositions = Arrays.stream(positions).map(originalLines::get).collect(Collectors.toList());
        assert decodedLines.containsAll(originalPositions);
        assert originalPositions.containsAll(decodedLines);
        r1.close();
        r2.close();
    }

    @Test
    public void testCreateDecodedFileFromListSampleFast() throws Exception {
        TempFiles tempFiles = new TempFiles();
        tempFiles.createRandomInputFile(10, 10, 2);
        tempFiles.createSampleFastInputFile();
        Integer[] positions = {1, 2, 3, 5};
        String posList = Arrays.stream(positions).map(String::valueOf).collect(Collectors.joining("\n"));
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        tempFiles.createSampleFastEncodedFile();
        encoder.createDecodedFileFromList(tempFiles.sampleFastEncodedFile, tempFiles.lookupFile, posList, tempFiles.decodedFile, false);
        BufferedReader r1 = new BufferedReader(new FileReader(tempFiles.decodedFile));
        BufferedReader r2 = new BufferedReader(new FileReader(tempFiles.sampleFastInputFile));
        List<String> decodedLines = r1.lines().collect(Collectors.toList());
        List<String> originalLines = r2.lines().collect(Collectors.toList());
        List<String> originalPositions = Arrays.stream(positions).map(originalLines::get).collect(Collectors.toList());
        assert decodedLines.containsAll(originalPositions);
        assert originalPositions.containsAll(decodedLines);
        r1.close();
        r2.close();
    }

    @Test
    // 75 encoded alleles - this is as many as we can handle.
    public void testLotsOfAlleles() throws Exception {
        TempFiles tempFiles = new TempFiles();
        String[] alleles = {
                "AAA", "AAC", "AAG", "AAT", "ACA", "ACC", "ACG", "ACT", "AGA", "AGC", "AGG", "AGT", "ATA", "ATC", "ATG", "ATT",
                "CAA", "CAC", "CAG", "CAT", "CCA", "CCC", "CCG", "CCT", "CGA", "CGC", "CGG", "CGT", "CTA", "CTC", "CTG", "CTT",
                "GAA", "GAC", "GAG", "GAT", "GCA", "GCC", "GCG", "GCT", "GGA", "GGC", "GGG", "GGT", "GTA", "GTC", "GTG", "GTT",
                "TAA", "TAC", "TAG", "TAT", "TCA", "TCC", "TCG", "TCT", "TGA", "TGC", "TGG", "TGT", "TTA", "TTC", "TTG", "TTT",
                "AAAA", "AAAC", "AAAG", "AAAT", "AACA", "AACC", "AACG", "AACT", "AAGA", "AAGC", "AAGG"
        };
        tempFiles.createRandomInputFile(100, 100, 2, alleles);
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        encoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile);
        assertFilesAreEqual(tempFiles.inputFile, tempFiles.decodedFile);
    }

    @Test(expected = Exception.class)
    // 76 alleles should fail
    public void testTooManyAlleles() throws Exception {
        TempFiles tempFiles = new TempFiles();
        String[] alleles = {
                "AAA", "AAC", "AAG", "AAT", "ACA", "ACC", "ACG", "ACT", "AGA", "AGC", "AGG", "AGT", "ATA", "ATC", "ATG", "ATT",
                "CAA", "CAC", "CAG", "CAT", "CCA", "CCC", "CCG", "CCT", "CGA", "CGC", "CGG", "CGT", "CTA", "CTC", "CTG", "CTT",
                "GAA", "GAC", "GAG", "GAT", "GCA", "GCC", "GCG", "GCT", "GGA", "GGC", "GGG", "GGT", "GTA", "GTC", "GTG", "GTT",
                "TAA", "TAC", "TAG", "TAT", "TCA", "TCC", "TCG", "TCT", "TGA", "TGC", "TGG", "TGT", "TTA", "TTC", "TTG", "TTT",
                "AAAA", "AAAC", "AAAG", "AAAT", "AACA", "AACC", "AACG", "AACT", "AAGA", "AAGC", "AAGG", "AAGT", "AATA",
        };

        tempFiles.createRandomInputFile(10, 10000, 2, alleles);
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        encoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile);
        assertFilesAreEqual(tempFiles.inputFile, tempFiles.decodedFile);
    }

    @Test
    // all alleles should be encoded into a single byte character
    public void testSingleByteEncoding() throws Exception {
        TempFiles tempFiles = new TempFiles();
        String[] alleles = {
                "AAA", "AAC", "AAG", "AAT", "ACA", "ACC", "ACG", "ACT", "AGA", "AGC", "AGG", "AGT", "ATA", "ATC", "ATG", "ATT",
                "CAA", "CAC", "CAG", "CAT", "CCA", "CCC", "CCG", "CCT", "CGA", "CGC", "CGG", "CGT", "CTA", "CTC", "CTG", "CTT",
                "GAA", "GAC", "GAG", "GAT", "GCA", "GCC", "GCG", "GCT", "GGA", "GGC", "GGG", "GGT", "GTA", "GTC", "GTG", "GTT",
                "TAA", "TAC", "TAG", "TAT", "TCA", "TCC", "TCG", "TCT", "TGA", "TGC", "TGG", "TGT", "TTA", "TTC", "TTG", "TTT",
                "AAAA", "AAAC", "AAAG", "AAAT", "AACA", "AACC", "AACG", "AACT", "AAGA", "AAGC", "AAGG"
        };
        int ploidy = 2;
        tempFiles.createRandomInputFile(1, 10000, ploidy, alleles);
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        BufferedReader br = new BufferedReader(new FileReader(tempFiles.encodedFile));
        String line = br.readLine();
        String[] lineSplit = line.split(elementSeparator);
        int i = 0;
        int j = 0;
//        int maxSb = 0;
        for (String gt : lineSplit) {
            String[] splitAlleles = gt.split("");
            for (String allele : splitAlleles) {
//                maxSb = Math.max(maxSb, allele.charAt(0));
                if (!(allele.getBytes().length == 1)) {
                    System.err.println("row " + i + ", column " + j);
                    System.err.println(lineSplit[i]);
                    System.err.println(splitAlleles[j]);
                    System.err.println(allele);
                    System.err.println(Arrays.toString(allele.getBytes()));
                }
                assert allele.getBytes().length == 1;
                j++;
            }
            i++;
        }
        BufferedReader lookupReader = new BufferedReader(new FileReader(tempFiles.lookupFile));
        String lookupLine = lookupReader.readLine();
        String[] lookupLineSplit = lookupLine.split("\t");
        String[] lookupValues = lookupLineSplit[1].split(";");
        System.err.println(lookupValues.length);
        // highly improbable that we will not get collision for sampling 75 elements 10k times.
        assert lookupValues.length == 75;
    }

    @Test
    public void testSampleFastDecoding() throws Exception {
        TempFiles tempFiles = new TempFiles();
        tempFiles.createRandomInputFile(100, 100, 2);
        tempFiles.createSampleFastInputFile();
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        tempFiles.createSampleFastEncodedFile();
        encoder.createDecodedFile(tempFiles.sampleFastEncodedFile, tempFiles.lookupFile, tempFiles.decodedFile, false);
        assertFilesAreEqual(tempFiles.sampleFastInputFile, tempFiles.decodedFile);
    }

    @Test
    public void testNullSeparator() throws Exception {
        TempFiles tempFiles = new TempFiles();
        tempFiles.createRandomInputFile(100, 100, 2);
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        encoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile);
        assertFilesAreEqual(tempFiles.inputFile, tempFiles.decodedFile);
    }

    @Test
    public void testPloidy() throws Exception {
        TempFiles tempFiles = new TempFiles();
        tempFiles.createRandomInputFile(100, 100, 4);
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        encoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile);
        assertFilesAreEqual(tempFiles.inputFile, tempFiles.decodedFile);
    }

    @Test
    public void testEncodeDecode4letRWExample() throws Exception {
        TempFiles tempFiles = new TempFiles();

        try (FileWriter inputwriter = new FileWriter(tempFiles.inputFile)) {
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
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        encoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile);

        BufferedReader encodedReader = new BufferedReader(new FileReader(tempFiles.encodedFile));
        BufferedReader outReader = new BufferedReader(new FileReader(tempFiles.decodedFile));
        String line1 = outReader.readLine();
        String line2 = outReader.readLine();
        String line3 = outReader.readLine();

        Assert.assertEquals("A/A/A/A\tA/A/C/T",line1);
    }

    @Test
    // should be able to encode + decode 1M data points in under a minute
    public void testLargeFileEncoding() throws Exception {
        TempFiles tempFiles = new TempFiles();
        int rows = 20000;
        int cols = 5000;
        tempFiles.createLargeInputFile(rows, cols);

        long startTime = System.currentTimeMillis();
        HDF5AllelicEncoderV2 encoder = new HDF5AllelicEncoderV2(elementSeparator, alleleSeparator);
        encoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile);
        encoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile);
        assertFilesAreEqual(tempFiles.inputFile, tempFiles.decodedFile);
        long totalTime = System.currentTimeMillis() - startTime;
        assert totalTime < 60000;
    }

    // Purely informational.
    public void timeLargeFileEncoding() throws Exception {
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

    private void testEncodeLargeFile(int rows, int cols) throws Exception {
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
        public File sampleFastEncodedFile;
        public File sampleFastInputFile;
        private final String timestamp;

        TempFiles() {
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HHmmssnnnnnn"));
            inputFile             = createTempFile("input-mf");
            lookupFile            = createTempFile("lookup");
            encodedFile           = createTempFile("encoded-mf");
            decodedFile           = createTempFile("decoded");
            sampleFastEncodedFile = createTempFile("encoded-sf");
            sampleFastInputFile   = createTempFile("input-sf");
            System.err.println(
                "Input file: "        + inputFile             + "\n" +
                "Input file (sf): "   + sampleFastInputFile   + "\n" +
                "Lookup file: "       + lookupFile            + "\n" +
                "Encoded file: "      + encodedFile           + "\n" +
                "Encoded file (sf): " + sampleFastEncodedFile + "\n" +
                "Decoded file: "      + decodedFile
            );
//            inputFile.deleteOnExit();
//            lookupFile.deleteOnExit();
//            encodedFile.deleteOnExit();
//            decodedFile.deleteOnExit();
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
//            BufferedReader reader = new BufferedReader(new FileReader(encodedFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(sampleFastEncodedFile));
//            int nRow = reader.readLine().split("\t").length;
//            int nCol = (int) reader.lines().count() + 1;
//            String[][] transposed = new String[nRow][nCol];
//            reader.close();
//            reader = new BufferedReader(new FileReader(encodedFile));
//            String inputLine;
//            for (int j = 0; j < nCol; j++) {
//                inputLine = reader.readLine();
//                if (inputLine == null) throw new IOException("Unexpected end of file.");
//                String[] splitLine = inputLine.split("\t");
//                for (int i = 0; i < nRow; i++) {
//                    transposed[i][j] = splitLine[i];
//                }
//            }

            getTransposedFileStream(encodedFile).forEach(line -> {
                try {
                    writer.append(line);
                    writer.append(System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

//            reader.close();
//            for (int i = 0; i < nRow; i++) {
//                String joined = String.join("\t", transposed[i]);
//                writer.append(joined);
//                writer.append(System.lineSeparator());
//            }
            writer.close();
//            reader.lines().map((line) -> line.split("\t")).map((line) -> {
//                int i = 0;
//                for (String s :
//                        line) {
//                    transposed[i++][j] = s;
//                }
//                j++;
//            });

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


}


