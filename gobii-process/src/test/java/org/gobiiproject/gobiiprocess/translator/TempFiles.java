package org.gobiiproject.gobiiprocess.translator;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Stream;

class TempFiles {
    public File inputFile;
    public File lookupFile;
    public File encodedFile;
    public File decodedFile;
    public File sampleFastEncodedFile;
    public File sampleFastInputFile;
    private final String timestamp;

    TempFiles() { this(false); }
    TempFiles(boolean verbose) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd-HHmmssnnnnnn"));
        inputFile = createTempFile("input-mf");
        lookupFile = createTempFile("lookup");
        encodedFile = createTempFile("encoded-mf");
        decodedFile = createTempFile("decoded");
        sampleFastEncodedFile = createTempFile("encoded-sf");
        sampleFastInputFile = createTempFile("input-sf");
        if (verbose) {
            System.err.println(
                    "Input file: " + inputFile + "\n" +
                    "Input file (sf): " + sampleFastInputFile + "\n" +
                    "Lookup file: " + lookupFile + "\n" +
                    "Encoded file: " + encodedFile + "\n" +
                    "Encoded file (sf): " + sampleFastEncodedFile + "\n" +
                    "Decoded file: " + decodedFile
            );
        }
    }

    private File createTempFile(String prefix) {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String path = tmpDir + "/" + timestamp + "_" + prefix + ".tmp";
        return new File(path);
    }

    void createLargeInputFile(int rows, int cols) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile));
        int j = 0;
        int i = 0;
        String sameLine = null;
        while (i <= rows) {
            if (sameLine == null) {
                StringJoiner lineJoiner = new StringJoiner("\t");
                while (j <= cols) {
                    lineJoiner.add("A/A");
                    lineJoiner.add("ACGT/ACGT");
                    lineJoiner.add("ACGTA/ACG");
                    lineJoiner.add("TTTT/T");
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

    void createRandomInputFile(int rows, int cols) throws IOException {
        String[] alleles = {"A", "C", "AC", "ACG"};
        createRandomInputFile(rows, cols, 2, alleles);
    }

    void createRandomInputFile(int rows, int cols, int ploidy) throws IOException {
        String[] alleles = {
                "A", "C", "G", "T", "N",
                "A", "C", "G", "T", "N",
                "A", "C", "G", "T", "N",
                "A", "C", "G", "T", "N",
                "", "", "",
                "AC", "ACG", "ACGT", "ACGTA", "ACGTAC", "ACGTACG", "ACGTACGT"
        };
        createRandomInputFile(rows, cols, ploidy, alleles);
    }

    void createRandomInputFile(int rows, int cols, int ploidy, String[] alleles) throws IOException {
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

    void createSampleFastInputFile() throws Exception {
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

    void createSampleFastEncodedFile() throws Exception {
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
