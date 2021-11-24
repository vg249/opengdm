package org.gobiiproject.gobiiprocess.translator.utils;

import java.io.*;
import java.util.StringJoiner;

public class ByteLineReader implements AutoCloseable {
    public final File file;
    private final BufferedInputStream bufferedInputStream;
    public final int bytesPerLine; // including newline
    public final int newLine = System.lineSeparator().getBytes()[0];
    public final int elementSeparator;
    private final int bytesPerElement; // excluding separator
    private final int elementsPerLine;

    /**
     * Instantiates a new ByteLineReader.
     *
     * @param file             the file
     * @param elementSeparator the element separator
     * @throws Exception the exception
     */
    public ByteLineReader(File file, String elementSeparator) throws Exception {
        if (elementSeparator.getBytes().length > 1) {
            throw new Exception("Element separator '" + elementSeparator + "' is more than one byte.");
        }
        this.elementSeparator = elementSeparator.charAt(0);
        this.file = file;

        FileInputStream tempFis = new FileInputStream(file);
        int currentByte;
        int byteCount = 0;
        int tempSegmentLength = -1;
        int currentSegmentLength = 0;
        int segmentCount = 0;
        while ((currentByte = tempFis.read()) != -1 && currentByte != newLine) {
            byteCount++;
            if (currentByte == this.elementSeparator) {
                if (tempSegmentLength == -1) tempSegmentLength = currentSegmentLength;
                else if (currentSegmentLength != tempSegmentLength) {
                    throw new Exception("Inconsistent number of bytes per element.");
                }
                currentSegmentLength = 0;
                segmentCount++;
            } else {
                currentSegmentLength++;
            }
        }
        tempFis.close();

        if (currentSegmentLength != tempSegmentLength) {
            throw new Exception("Inconsistent number of bytes in last element.");
        }

        this.bytesPerElement = tempSegmentLength;
        this.elementsPerLine = segmentCount + 1;
        this.bytesPerLine = byteCount;
        this.bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * Read line byte [ ] [ ].
     *
     * @return byte[][] or null if end of file is reached.
     * @throws Exception the exception
     */
    public byte[][] readLine() throws Exception {
        byte[][] lineBytes = new byte[elementsPerLine][bytesPerElement];
        int elementIndex = 0;
        int currentElementLength = 0;
        int currentByte;
        while ((currentByte = bufferedInputStream.read()) != -1) {
            if (currentByte == newLine) {
                return lineBytes;
            }
            if (currentByte == elementSeparator) {
                if (currentElementLength != bytesPerElement) {
                    StringJoiner message = new StringJoiner("\n");
                    message.add("Inconsistent number of bytes in element.");
                    message.add("elementIndex: " + elementIndex);
                    message.add("currentElementLength: " + currentElementLength);
                    throw new Exception(message.toString());
                }
                elementIndex++;
                currentElementLength = 0;
                continue;
            }
            lineBytes[elementIndex][currentElementLength++] = (byte) currentByte;
        }
        return null;
    }

    public int[][] readLineInts(int mask) throws Exception {
        int[][] lineBytes = new int[elementsPerLine][bytesPerElement];
        int elementIndex = 0;
        int currentElementLength = 0;
        int currentByte;
        while ((currentByte = bufferedInputStream.read()) != -1) {
            if (currentByte == newLine) return lineBytes;
            if (currentByte == elementSeparator) {
                if (currentElementLength != bytesPerElement) {
                    throw new Exception(
                        "Inconsistent number of bytes in element " + elementIndex +
                        " (expected " + bytesPerElement + ", found " + currentElementLength + ")"
                    );
                }
                elementIndex++;
                currentElementLength = 0;
                continue;
            }
            lineBytes[elementIndex][currentElementLength++] = currentByte & mask;
        }
        return null;
    }

    public int[][] readLineInts() throws Exception {
        return readLineInts(Integer.MAX_VALUE);
    }

    public int[][] readLineUBytes() throws Exception {
        return readLineInts(0xff);
    }

    public void close() throws IOException {
        bufferedInputStream.close();
    }
}
