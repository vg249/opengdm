package org.gobiiproject.gobiiprocess;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import org.gobiiproject.gobiiprocess.digester.csv.matrixValidation.NucleotideSeparatorSplitter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class HDF5AllelicEncoderV2 {
    private final String elementSeparator;
    private String alleleSeparator = null;
    private static final int MAX_LOOKUP = 126;
    private static final String lookupSeparator = ";";
    private static final Set<String> reservedAlleles = Set.of(
            "A", "C", "G", "T", "N",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "+", "-",
            ".", "?", " "
    );
    // Counting 20 reserved alleles and 32 reserved command characters, we can support up to 75 alternate alleles with this scheme.
    private final List<Integer> reservedIndices = reservedAlleles.stream()
            .map((s) -> (int) s.charAt(0))
            .sorted()
            .collect(Collectors.toList());
    {
        // Add control characters to reserved indices
        for (int i = 0; i <= 0x1f; i++) reservedIndices.add(i);
    }
    // Maps character values to lookup indices and vice-versa.
    private final BiMap<Integer, Integer> lookupIndexMap = HashBiMap.create();
    {
        int charVal = 0;
        int indexPos = 0;
        while (charVal <= MAX_LOOKUP && indexPos <= (MAX_LOOKUP - reservedAlleles.size())) {
            if (reservedIndices.contains(charVal)) charVal++;
            else lookupIndexMap.put(charVal++, indexPos++);
        }
    }

    public HDF5AllelicEncoderV2(String elementSeparator, String alleleSeparator) {
        this.elementSeparator = elementSeparator;
        this.alleleSeparator = alleleSeparator;
    }

    public HDF5AllelicEncoderV2(String elementSeparator) {
        this.elementSeparator = elementSeparator;
    }

    public void createEncodedFile(File inputFile, File encodedFile, File lookupFile) throws Exception {
        int rowIndex = 0;
        try (
            BufferedWriter encodedFileWriter = new BufferedWriter(new FileWriter(encodedFile));
            BufferedWriter lookupFileWriter = new BufferedWriter(new FileWriter(lookupFile));
            BufferedReader inputFileReader = new BufferedReader(new FileReader(inputFile))
        ) {
            String inputLine = inputFileReader.readLine();
            if (inputLine != null) {
                if (alleleSeparator == null) {
                    String firstElement = inputLine.split(elementSeparator)[0];
                    alleleSeparator = NucleotideSeparatorSplitter.findSeparator(firstElement);
                }
                do {
                    EncodedValues encodedValues = encodeRow(inputLine, rowIndex);
                    encodedFileWriter.write(encodedValues.encodedRow);
                    lookupFileWriter.write(encodedValues.lookupRow);
                    rowIndex++;
                } while ((inputLine = inputFileReader.readLine()) != null);
            }
        } catch (Exception e) {
            StringJoiner message = new StringJoiner("\n");
            message.add("Error encoding file.")
                    .add("Line:\t" + rowIndex)
                    .add("Input file:\t" + inputFile)
                    .add("Encoded file:\t" + encodedFile)
                    .add("Lookup filet:\t" + lookupFile);
            Logger.logError("HDF5AllelicEncoderV2", message.toString(), e);
            throw e;
        }

    }

    private EncodedValues encodeRow(String inputRow, int rowIndex) throws Exception {
        List<String> encodings = new ArrayList<>();
        StringJoiner joiner = new StringJoiner(elementSeparator);
        for (String a : inputRow.split(elementSeparator)) {
            String s = encodeGenotype(a, encodings);
            joiner.add(s);
        }
        String encodedRow = joiner + System.lineSeparator();
        String lookupRow = "";
        if (!encodings.isEmpty()) {
            StringJoiner result = new StringJoiner(lookupSeparator);
            for (String encoding : encodings) {
                String s = encodeLookup(encoding);
                result.add(s);
            }
            lookupRow = rowIndex + "\t" + result + System.lineSeparator();
        }
        return new EncodedValues(encodedRow, lookupRow);
    }

    private String encodeGenotype(String genotype, List<String> encodings) throws Exception {
        StringJoiner joiner = new StringJoiner("");
        for (String a : genotype.split(alleleSeparator)) {
            String s = encodeAllele(a, encodings);
            assert s.getBytes().length == 1;
            joiner.add(s);
        }
        return joiner.toString();
    }

    private String encodeAllele(String allele, List<String> encodings) throws Exception {
        String encoded = allele;
        if (!reservedAlleles.contains(allele)) {
            int encodingIndex;
            if (!encodings.contains(allele)) {
                encodings.add(allele);
                encodingIndex = encodings.size() - 1;
            } else {
                encodingIndex = encodings.indexOf(allele);
            }
            Integer encodedInteger = lookupIndexMap.inverse().get(encodingIndex);
            if (encodedInteger == null) {
                StringJoiner message = new StringJoiner("\n");
                message.add("Too many alternate alleles.");
                message.add("encodingIndex = " + encodingIndex);
                message.add("encodings = " + encodings);
                message.add("lookupIndexMap = " + lookupIndexMap);
                message.add("reservedIndices = " + reservedIndices);
                Logger.logError("HDF5AllelicEncoderV2", message.toString());
                throw new Exception();
            }
            encoded = String.valueOf((char) (int) encodedInteger);
        }
        return encoded;
    }

    private static String encodeLookup(String e) {
        if (e.isEmpty()) return "D";
        return "I" + e;
    }

    public void createDecodedFile(File encodedFile, File lookupFile, File decodedFile) {
        createDecodedFile(encodedFile, lookupFile, decodedFile, true);
    }

    public void createDecodedFile(File encodedFile, File lookupFile, File decodedFile, boolean markerFast) {
        HashMap<Integer, String[]> alleleEncodings = readLookupFile(lookupFile);
        int rowIndex = 0;
        try (
            BufferedReader encodedFileReader = new BufferedReader(new FileReader(encodedFile));
            BufferedWriter decodedFileWriter = new BufferedWriter(new FileWriter(decodedFile))
        ) {
            String inputLine = encodedFileReader.readLine();
            if (inputLine != null) {
                if (alleleSeparator == null) {
                    String firstElement = inputLine.split(elementSeparator)[0];
                    alleleSeparator = NucleotideSeparatorSplitter.findSeparator(firstElement);
                }
                do {
                    String decodedRow;
                    if (markerFast) {
                        decodedRow = decodeRowMarkerFast(inputLine, alleleEncodings, rowIndex);
                    } else {
                        decodedRow = decodeRowSampleFast(inputLine, alleleEncodings);
                    }
                    decodedFileWriter.append(decodedRow);
                    decodedFileWriter.append(System.lineSeparator());
                    rowIndex++;
                } while ((inputLine = encodedFileReader.readLine()) != null);
            }
        } catch (Exception e) {
            StringJoiner message = new StringJoiner("\n");
            message.add("Error decoding file.")
                    .add("Line:\t" + rowIndex)
                    .add("Encoded file:\t" + encodedFile)
                    .add("Lookup filet:\t" + lookupFile)
                    .add("Decoded file:\t" + decodedFile);
            Logger.logError("HDF5AllelicEncoderV2", message.toString(), e);
            System.err.println(message);
        }
    }

    public void createDecodedFileFromList(File encodedFile, File lookupFile, String posList, File decodedFile, boolean markerFast) {
        HashMap<Integer, String[]> alleleEncodings = readLookupFile(lookupFile);
        Iterator<Integer> positions = Arrays.stream(posList.split("\n"))
                .map(String::trim)
                .map(Integer::parseInt)
                .iterator();
        int nextRow = -1;
        int rowIndex = -1; // initialized here in case error is thrown before reading first line
        try (
            BufferedReader encodedFileReader = new BufferedReader(new FileReader(encodedFile));
            BufferedWriter decodedFileWriter = new BufferedWriter(new FileWriter(decodedFile))
        ) {
            String inputLine;
            while (positions.hasNext()) {
                nextRow = positions.next();
                while ((inputLine = encodedFileReader.readLine()) != null && ++rowIndex < nextRow); // skip lines until we encounter one in the list
                if (nextRow == rowIndex && inputLine != null) {
                    String decodedRow;
                    if (markerFast) {
                        decodedRow = decodeRowMarkerFast(inputLine, alleleEncodings, rowIndex);
                    } else {
                        decodedRow = decodeRowSampleFast(inputLine, alleleEncodings);
                    }
                    decodedFileWriter.append(decodedRow);
                    decodedFileWriter.append(System.lineSeparator());
                }
            }
        } catch (Exception e) {
            String message = "Error decoding encoded HDF5 file from marker list. Line " + rowIndex + "; Lookup " + nextRow;
            Logger.logError("HDF5AllelicEncoder", message, e);
            System.err.println(message);
        }
    }

    private String decodeRowMarkerFast(String inputLine, HashMap<Integer, String[]> alleleEncodings, int rowIndex) {
        StringJoiner joiner = new StringJoiner(elementSeparator);
        for (String genotype : inputLine.split(elementSeparator)) {
            String s;
            String[] encodings;
            if ((encodings = alleleEncodings.get(rowIndex)) != null) {
                s = decodeGenotype(genotype, encodings);
            } else {
                s = decodeGenotype(genotype);
            }
            joiner.add(s);
        }
        return joiner.toString();
    }

    private String decodeRowSampleFast(String inputLine, HashMap<Integer,String[]> alleleEncodings) {
        StringJoiner joiner = new StringJoiner(elementSeparator);
        String[] genotypes = inputLine.split(elementSeparator);
        for (int i = 0; i < genotypes.length; i++) {
            String s;
            String[] encodings;
            if ((encodings = alleleEncodings.get(i)) != null) {
                s = decodeGenotype(genotypes[i], encodings);
            } else {
                s = decodeGenotype(genotypes[i]);
            }
            joiner.add(s);
        }
        return joiner.toString();
    }

    private String decodeGenotype(String genotype, String[] encodings) {
        StringJoiner joiner = new StringJoiner(alleleSeparator);
        for (char c: genotype.toCharArray()) {
            if (!reservedAlleles.contains(String.valueOf(c))) {
                String lookup = encodings[lookupIndexMap.get((int) c)];
                joiner.add(lookup);
            } else {
                joiner.add(String.valueOf(c));
            }
        }
        return joiner.toString();
    }

    private String decodeGenotype(String genotype) {
        StringJoiner joiner = new StringJoiner(alleleSeparator);
        for (char c: genotype.toCharArray()) {
            joiner.add(String.valueOf(c));
        }
        return joiner.toString();
    }

    private HashMap<Integer, String[]> readLookupFile(File lookupFile) {
        HashMap<Integer, String[]> alleleEncodings = new HashMap<>();
        try (
            BufferedReader lookupFileReader = new BufferedReader(new FileReader(lookupFile))
        ) {
            String lookupLine;
            int rowIndex = 0;
            while ((lookupLine = lookupFileReader.readLine()) != null) {
                try {
                    String[] splitLine = lookupLine.split("\t");
                    Integer lookupRowIndex = Integer.parseInt(splitLine[0]);
                    String[] encodings = Arrays.stream(splitLine[1].split(lookupSeparator))
                            .map((e) -> e.substring(1))
                            .toArray(String[]::new);
                    alleleEncodings.put(lookupRowIndex, encodings);
                } catch (Exception e) {
                    StringJoiner message = new StringJoiner("\n");
                    message.add("Error parsing lookup table.")
                            .add("Line: " + rowIndex)
                            .add("File: " + lookupFile);
                    Logger.logError("HDF5AllelicEncoderV2", message.toString(), e);
                }
                rowIndex++;
            }
        } catch (Exception e) {
            String message = "Error reading lookup file " + lookupFile;
            Logger.logError("HDF5AllelicEncoder", message, e);
        }
        return alleleEncodings;
    }

    static class EncodedValues {
        String encodedRow;
        String lookupRow;
        public EncodedValues(String encodedRow, String lookupRow) {
            this.encodedRow = encodedRow;
            this.lookupRow = lookupRow;
        }
    }

}
