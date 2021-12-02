package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobiimodel.utils.error.Logger;
import org.gobiiproject.gobiiprocess.digester.csv.matrixValidation.NucleotideSeparatorSplitter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class designed to work with @HDF5Interface to encode and then decode insertions, deletions, and other non-standard or
 * non-allelic data, cleaning it to an allele and encoded-non-standard only HDF5 and a lookup table with a row for each
 * marker to the dataset
 */
public class HDF5AllelicEncoder {
    private static final String DEFAULT_SEPARATOR = "/";
    private static final String newLine = System.lineSeparator();
    private static final char newLineChar = newLine.charAt(0);
    private static final HashSet<String> unencodedAlleles = new HashSet<>(Arrays.asList(
            "A", "C", "G", "T", "N",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "+", "-", ".", "?", " "));

    /**
     * @param inputRow         entire input row as a string
     * @param alleleSeparator  Separator between alleles (usually /). If null, each element will get a separator based on NucleotideSeparatorSplitter's default logic
     * @param elementSeparator Separator between elements (usually tab)
     * @return A pair of values, the data entry, and the inner value of the lookup table entry
     */
    public static EncodedValues encodeRow(String inputRow, String alleleSeparator, String elementSeparator, byte[] elementSeparatorBytes) throws Exception {
        List<Byte> outBytes = new ArrayList<>();
        RowTranslator currentRowTranslator = new RowTranslator();
        for (String element : inputRow.split(elementSeparator, -1)) {
            if (!outBytes.isEmpty()) for (byte b : elementSeparatorBytes) outBytes.add(b);
            //In case first element is an unknown, or otherwise oddball, check for each element if we don't know beforehand (a usual case)
            if (alleleSeparator == null) {
                alleleSeparator = NucleotideSeparatorSplitter.findSeparator(element);
            }
            for (byte b : currentRowTranslator.getEncodedString(element, alleleSeparator)) outBytes.add(b);
            //outRow.append(currentRowTranslator.getEncodedString(element,alleleSeparator,false));
        }

        int outByteSize = outBytes.size();
        byte[] outByteArr = new byte[outByteSize];
        for (int i = 0; i < outByteSize; i++) {
            outByteArr[i] = outBytes.get(i);
        }
        return new EncodedValues(outByteArr, currentRowTranslator.encodeRowTransform());
    }

    /**
     * Encodes an entire file using encodeRow on each row of the input file, generating a lookup file
     * @param originalFilePath The Bare input to convert to an encoded file
     * @param encodedFilePath  Where the encoded file of fixed width characters (the one to HDF5) should go
     * @param lookupFilePath   Where the lookup file should go
     * @param alleleSeparator  If null, uses the builtins to determine separator for each element. See NucleotideSeparatorSplitter.getSeparator()
     * @param elementSeparator Usually tab, but put in for strange formats, what the between-value separator is
     */
    public static void createEncodedFile(File originalFilePath, File encodedFilePath, File lookupFilePath, String alleleSeparator, String elementSeparator) {
        try (
                BufferedReader input = new BufferedReader(new FileReader(originalFilePath));
                //In theory, this should prevent any encoding issues when sent to the raw HDF5 interface
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(encodedFilePath));
                BufferedWriter lookupWriter = new BufferedWriter(new FileWriter(lookupFilePath));
        )
        {
//            OutputStreamWriter lookupFile = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(lookupFilePath)));
            byte[] sepBytes = elementSeparator.getBytes();
            //Note - line by line parse doesn't seem to have too much of a performance impact, oddly enough. See: https://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-java/40597140#40597140
            String inputLine;
            int i = 0;
            while ((inputLine = input.readLine()) != null) {
                EncodedValues ret = encodeRow(inputLine, alleleSeparator, elementSeparator, sepBytes);
                bos.write(ret.encodedRow);
                bos.write(newLineChar);
                //Lookup file is sparse, and may not exist if there are no entries at all
                if (ret.lookupRow.length() > 0) { //we have a row in the lookup table
                    lookupWriter.write(i + "\t" + ret.lookupRow);
                    lookupWriter.newLine();
                }
                lookupWriter.flush();
                i++;
            }
        } catch (Exception e) {
            Logger.logError("HDF5AllelicEncoder", "Error encoding allele data in HDF5 file", e);
        }
    }

    /**
     * Decodes and adds separators to the output of an HDF5 matrix
     * @param encodedFilePath
     * @param lookupFilePath
     * @param decodedFilePath
     * @param alleleSeparator
     * @param elementSeparator
     */

    public static void createDecodedFile(File encodedFilePath, File lookupFilePath, File decodedFilePath, String alleleSeparator, String elementSeparator, boolean markerFast) {
        HashMap<Integer, List<String>> lookupLines = null;
        try {
            lookupLines = readLookupFile(lookupFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (
            FileInputStream encodedFile = new FileInputStream(encodedFilePath);
            BufferedWriter decodedFile = new BufferedWriter(new FileWriter(decodedFilePath));
        ) {
            byte[] inputLine;
            if (markerFast) {
                int rowIndex = 0;
                List<String> lookupLine;
                while ((inputLine = getBytesTillNewline(encodedFile)) != null) {
                    lookupLine = (lookupLines == null) ? null : lookupLines.get(rowIndex);
                    String decodedRow = decodeRow(inputLine, lookupLine, alleleSeparator, elementSeparator);
                    decodedFile.write(decodedRow);
                    decodedFile.newLine();
                    rowIndex++;
                }
            } else {
                while ((inputLine = getBytesTillNewline(encodedFile)) != null) {
                    String decodedRow = decodeRowSampleFast(inputLine, lookupLines, alleleSeparator, elementSeparator);
                    decodedFile.write(decodedRow);
                    decodedFile.newLine();
                }
            }
        } catch (Exception e) {
            Logger.logError("HDF5AllelicEncoder", "Error decoding encoded HDF5 file", e);
        }
    }

    public static void createDecodedFile(File encodedFilePath, File lookupFilePath, File decodedFilePath, String alleleSeparator, String elementSeparator) {
        createDecodedFile(encodedFilePath, lookupFilePath, decodedFilePath, alleleSeparator, elementSeparator, true);
    }

    private static HashMap<Integer, List<String>> readLookupFile(File lookupFile) throws IOException {
        HashMap<Integer, List<String>> result = new HashMap<>(127);
        String line;
        BufferedReader lookupFileReader = new BufferedReader(new FileReader(lookupFile));
        while ((line = lookupFileReader.readLine()) != null) {
            int splitAt = line.indexOf('\t');
            int key = Integer.parseInt(line.substring(0, splitAt));
            List<String> vals = Arrays.stream(line.substring(splitAt + 1).split(";", -1))
                    .map(a -> a.substring(1))
                    .collect(Collectors.toList());
            result.put(key, vals);
        }
        lookupFileReader.close();
        return result;
    }

    //ReadLine for FIS. Eww, but I need the raw bytes.
    private static byte[] getBytesTillNewline(FileInputStream fis) throws IOException {
        List<Byte> assemblyList = new ArrayList<>();
        int nextByteI;
        while (true) {
            nextByteI = fis.read();
            if (nextByteI == -1) {
                //EOF
                if (assemblyList.size() == 0) return null;
            }
            byte nextByte = (byte) nextByteI;
            if (nextByteI == -1 || nextByte == newLineChar) {
                byte[] outArr = new byte[assemblyList.size()];
                for (int i = 0; i < assemblyList.size(); i++) {
                    outArr[i] = assemblyList.get(i);
                }
                return outArr;
            }
            assemblyList.add(nextByte);
        }
    }

    //This one's slightly different, as we need to align the output with the input marker list. As each line is currently a marker, we
    // can assume that the lines correspond to the nth entries in the 'marker list', which should be a zero based list of increasing numbers
    //corresponding to rows in the translation table.
    public static void createDecodedFileFromList(File encodedFilePath, File lookupFilePath, String markers, File decodedFilePath, String alleleSeparator, String elementSeparator, boolean markerFast) {

        Set<Integer> markerList = Arrays.stream(markers.split(newLine))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        HashMap<Integer, List<String>> lookupLines = null;
        try {
            lookupLines = readLookupFile(lookupFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                FileInputStream encodedFile = new FileInputStream(encodedFilePath);
                BufferedWriter decodedFile = new BufferedWriter( new FileWriter(decodedFilePath));
        ) {
            byte[] inputLine;
            if (markerFast) {
                int rowIndex = 0;
                Iterator<Integer> markerIterator = markerList.iterator();
                if (!markerIterator.hasNext()) {
                    return;
                }
                int nextRow = markerIterator.next();
                while ((inputLine = getBytesTillNewline(encodedFile)) != null && markerIterator.hasNext()) {
                    if (rowIndex == nextRow) {
                        nextRow = markerIterator.next();
                        List<String> lookupLine = lookupLines == null ? null : lookupLines.get(rowIndex);
                        String decodedRow = decodeRow(inputLine, lookupLine, alleleSeparator, elementSeparator);
                        decodedFile.write(decodedRow);
                        decodedFile.write(newLine);
                    }
                    rowIndex++;
                }
            } else {
                while ((inputLine = getBytesTillNewline(encodedFile)) != null) {
                    String decodedRow = decodeRowSampleFast(inputLine, lookupLines, alleleSeparator, elementSeparator, markerList);
                    decodedFile.write(decodedRow);
                    decodedFile.write(newLine);
                }
            }
        } catch (Exception e) {
            Logger.logError("HDF5AllelicEncoder", "Error decoding encoded HDF5 file", e);
        }
    }

    public static String decodeRow(byte[] inputRow, List<String> lookupRow, String alleleSeparator, String elementSeparator) throws Exception {
        StringBuilder outRow = new StringBuilder();
        RowTranslator currentRowTranslator = new RowTranslator(lookupRow);
        byte elementSeparatorAsByte = elementSeparator.getBytes()[0];
        int offset = 0;
        for (int k = 0; k <= inputRow.length; k++) {
            if (k >= inputRow.length || inputRow[k] == elementSeparatorAsByte) {
                if (offset > 0) outRow.append(elementSeparator);
                byte[] ba = new byte[k - offset];
                System.arraycopy(inputRow, offset, ba, 0, ba.length);
                outRow.append(currentRowTranslator.getDecodedString(ba, ba.length, alleleSeparator));
                offset = k + 1;
            }
        }
        return outRow.toString();
    }

    public static String decodeRowSampleFast(byte[] inputRow, HashMap<Integer, List<String>> lookupLines, String alleleSeparator, String elementSeparator, Set<Integer> markerList) throws Exception {
        StringBuilder outRow = new StringBuilder();
        byte elementSeparatorAsByte = elementSeparator.getBytes()[0];
        int segmentNum = 0;
        int offset = 0;
        boolean noFilter = markerList == null;
        for (int k = 0; k <= inputRow.length; k++) {
            if (k >= inputRow.length || inputRow[k] == elementSeparatorAsByte) {
                if (noFilter || markerList.contains(segmentNum)) {
                    if (offset > 0) outRow.append(elementSeparator);
                    RowTranslator currentRowTranslator = new RowTranslator(lookupLines.get(segmentNum));
                    byte[] ba = new byte[k - offset];
                    System.arraycopy(inputRow, offset, ba, 0, ba.length);
                    outRow.append(currentRowTranslator.getDecodedString(ba, ba.length, alleleSeparator));
                }
                segmentNum++;
                offset = k + 1;
            }
        }
        return outRow.toString();
    }

    public static String decodeRowSampleFast(byte[] inputRow, HashMap<Integer, List<String>> lookupLineList, String alleleSeparator, String elementSeparator) throws Exception {
        return decodeRowSampleFast(inputRow, lookupLineList, alleleSeparator, elementSeparator, null);
    }

    static int decodeChar(char value) throws Exception {
        int intVal = ((int) value) & 0xFF; //Cut it off at a byte, making it a ubyte, effectively
        if (intVal < 129) throw new Exception("Decoded character from h5 does not map (under 128)");
        return intVal - 129;
    }

    static class EncodedValues {
        public byte[] encodedRow;
        public String lookupRow;

        public EncodedValues(byte[] encodedRow, String lookupRow) {
            this.encodedRow = encodedRow;
            this.lookupRow = lookupRow;
        }
    }

    static class RowTranslator {

        List<String> nonstandardAlleles = new ArrayList<>(127);;

        //An empty one
        RowTranslator() {
        }

        RowTranslator(List<String> nonstandardAlleles) {
            if (nonstandardAlleles != null) {
                this.nonstandardAlleles = nonstandardAlleles;
            }
        }

        //Encodes the current state of the rowreader into a 'marker row' for data encoded in this marker
        String encodeRowTransform() {
            return nonstandardAlleles.stream().map(this::encodeRowEntry).collect(Collectors.joining(";"));
        }


        //Every nonstandard allele is prepended with an "I" for futureproofing
        private String encodeRowEntry(String nonstandardAlleleValue) {
            if (nonstandardAlleleValue.equals("")) return "D";
            return "I" + nonstandardAlleleValue;
        }

        /**
         * Encodes a 'single' allele into an HDF5 friendly format.
         * For example, if a 2 letter type is ACCAT/A, getEncodedValue(ACCAT) ->  ‚ (131)
         * getEncodedValue(A) -> A (41)
         * @param allele 'allele', insertion or deletion to encode
         * @return
         */
        private byte getEncodedAllele(String allele) {
            if (unencodedAlleles.contains(allele)) {
                return (byte) allele.charAt(0);
            }
            int encoding = nonstandardAlleles.indexOf(allele);
            if (encoding == -1) {
                encoding = nonstandardAlleles.size();
                nonstandardAlleles.add(allele);
            }
            return (byte) (encoding + 129);
        }

        /**
         * Encodes a genotype string with a separator. Separator specified as second parameter.
         * All encoded values are squashed to one character, and that character
         *  For example, if a 2 letter type is ACCAT/A, with a separator specified of / ->  ‚ (131) A (41)
         *  ,A becomes the encoded string
         * @param input
         * @param separator
         * @return
         */
        public byte[] getEncodedString(String input, String separator) throws Exception {
            String[] segments = input.split(separator, -1);
            byte[] outputs = new byte[segments.length];
            for (int i = 0; i < segments.length; i++) {
                outputs[i] = getEncodedAllele(segments[i]);
            }
            return outputs;
        }

        /**
         * "Unpacks" the encoded single-character to allele stream, adding separators in the middle if there is more than one element
         *  For example, if   ‚ (131) A (41) is the input, (131) goes into the lookup table of ACCAT, and (41) A
         *  is in the unencoded alleles, it comes out A. Given a / separator, it becomes ACCAT/A
         *
         * @param separator
         * @return
         * @throws Exception
         */
        String getDecodedString(byte[] encodedBytes, int length, String separator) throws Exception {
            //Special override for SSR datatype
            String ssr = checkSSR(encodedBytes, separator);
            if (ssr != null) return ssr;
            StringBuilder out = new StringBuilder();
            boolean first = true;
            for (int i = 0; i < length; i++) {
                char encodedAllele = (char) encodedBytes[i];
                if ((encodedAllele == '\t') || (encodedAllele == ' ') || encodedAllele == 0) { //Yeah, sometimes it's 0?
                    continue;
                }
                if (!first) {
                    out.append(separator);
                } else {
                    first = false;
                }
                out.append(getDecodedAllele(encodedAllele));
            }
            return out.toString();
        }

        String checkSSR(byte[] encodedBytes, String separator) {
            if (encodedBytes.length != 8) return null;
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                if (i == 4) result.append(separator);
                int b = encodedBytes[i];
                if (b < 48 || b > 58) return null;
                result.append(b);
            }
            return result.toString();
        }

        String getDecodedAllele(char internalChar) throws Exception {
            String internalCharAsString = internalChar + "";
            if (unencodedAlleles.contains(internalCharAsString)) {
                return internalCharAsString;
            }
            int index;
            try {
                index = decodeChar(internalChar);
            } catch (Exception e) {
                //Throw a warning, but pass through the character on catastrophic decode failure. (Probably an unexpected unencoded value, not an encoded value at all.)
                Logger.logWarning("HDF5AlleleEncoder", "No character to decode to- '" + internalChar + "'" + " (" + (int) internalChar + ")");
                return internalChar + "";
            }
            if (index >= nonstandardAlleles.size()) {
                Logger.logError("HDF5AllelicEncoder", new Exception("Decoded character from h5 does not map (over translator length) " + index + " " + internalCharAsString));
            }
            return nonstandardAlleles.get(index);
        }
    }
}
