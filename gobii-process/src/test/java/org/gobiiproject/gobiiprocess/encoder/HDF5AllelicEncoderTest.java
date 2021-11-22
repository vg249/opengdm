package org.gobiiproject.gobiiprocess.encoder;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class HDF5AllelicEncoderTest {

    private final String elementSeparator = "\t";
    private final String alleleSeparator = "/";

    @Test
    public void testEncodeDecode() throws IOException {
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
        HDF5AllelicEncoder.createDecodedFile(encoded,lookup,output,"/","\t");


        BufferedReader encodedReader = new BufferedReader(new FileReader(encoded));
        BufferedReader outReader = new BufferedReader(new FileReader(output));
        String line1 = outReader.readLine();
        String line2 = outReader.readLine();
        String line3 = outReader.readLine();
        String line4 = outReader.readLine();
        String line5 = outReader.readLine();

        BufferedReader lookupReader = new BufferedReader(new FileReader(lookup));

        Assert.assertEquals("1\tIACAT;IGAG;D",lookupReader.readLine());
        Assert.assertEquals("3\tIGAG;IGAGA",lookupReader.readLine());

        Assert.assertEquals("AC\tGT\tNN",encodedReader.readLine());
        //Assert.assertEquals((char)128 + ""+(char)129+"\t"+"\t"+"AA",encodedReader.readLine());

        Assert.assertEquals("A/C\tG/T\tN/N",line1);
//        Assert.assertEquals("ACAT/GAG\t/\tA/A",line2);//given there's no 'length' to the missing data, there is no separator
        Assert.assertEquals("ACAT/GAG\t/\tA/A",line2); //Should be a separator, no?
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

        HDF5AllelicEncoder.createEncodedFile(input,encoded,lookup,null,"\t");
        HDF5AllelicEncoder.createDecodedFile(encoded,lookup,output,"/","\t");


        BufferedReader encodedReader = new BufferedReader(new FileReader(encoded));
        BufferedReader outReader = new BufferedReader(new FileReader(output));
        String line1 = outReader.readLine();
        String line2 = outReader.readLine();
        String line3 = outReader.readLine();

        Assert.assertEquals("A/A/A/A\tA/A/C/T",line1);

        input.delete();
        encoded.delete();
        lookup.delete();
        output.delete();
    }

    @Test
    public void timeLargeFileEncoding() throws Exception {
        long startTime = System.currentTimeMillis();
        long lastTime = startTime;
        long stepTime;
        TempFiles tempFiles = new TempFiles();
        int rows = 10000;
        int cols = 1400;
        System.err.print("Creating input file (" + rows + " * " + cols + ")...");
        tempFiles.createLargeInputFile(rows, cols);
        stepTime = System.currentTimeMillis();
        System.err.println("took " + ((stepTime - lastTime) / 1000.0) + "s");
        lastTime = stepTime;
        System.err.print("Encoding file...");
        HDF5AllelicEncoder.createEncodedFile(tempFiles.inputFile, tempFiles.encodedFile, tempFiles.lookupFile, alleleSeparator, elementSeparator);
        stepTime = System.currentTimeMillis();
        System.err.println("took " + ((stepTime - lastTime) / 1000.0) + "s");
        lastTime = stepTime;
        System.err.print("Decoding file...");
        HDF5AllelicEncoder.createDecodedFile(tempFiles.encodedFile, tempFiles.lookupFile, tempFiles.decodedFile, alleleSeparator, elementSeparator);
        stepTime = System.currentTimeMillis();
        System.err.println("took " + ((stepTime - lastTime) / 1000.0) + "s");
//        assertFilesAreEqual(tempFiles.inputFile, tempFiles.decodedFile);
        stepTime = System.currentTimeMillis();
        System.err.println("Total time was " + ((stepTime - startTime) / 1000.0) + "s");
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
        r1.close();
        r2.close();
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


}
