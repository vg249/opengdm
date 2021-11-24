package org.gobiiproject.gobiiprocess.translator.utils;

public class ByteJoiner {
    public final byte delimiter;
//    private final List<Byte> eltsl = new ArrayList<>();
    public int size; // bytes added to joiner
    public int len;  // bytes added to joiner + delimiters
    public final int eltSize;
    public final int bufferSize;
    private byte[] elts;

    /**
     * Instantiates a new Byte joiner.
     *
     * @param delimiter Delimiter to add between elements.
     * @param eltSize   The number of bytes between delimiters.
     */
    public ByteJoiner(byte delimiter, int eltSize, int bufferSize) {
        this.delimiter = delimiter;
        this.eltSize = eltSize;
        this.bufferSize = bufferSize;
        this.elts = new byte[bufferSize];
    }

    /**
     * Instantiates a new Byte joiner with element size of 1.
     *
     * @param delimiter the delimiter.
     */
    public ByteJoiner(byte delimiter) { this(delimiter, 1, 16); }

//    /**
//     * Add byte to ByteJoiner.
//     *
//     * @param b byte to add.
//     * @return this ByteJoiner.
//     */
//    public ByteJoiner add(byte b) {
//        if (size % eltSize == 0) {
//            eltsl.add(delimiter);
//            len++;
//        }
//        eltsl.add(b);
//        len++;
//        size++;
//        return this;
//    }


    public ByteJoiner add(byte b) {
        if (size % eltSize == 0 && size > 0) {
            elts[len++] = delimiter;
        }
        elts[len++] = b;
        size++;
        return this;
    }

    public byte[] toByteArray() {
        return elts;
//        byte[] ba = new byte[len];
//        System.arraycopy(elts, 0, ba, 0, len);
//        return ba;
    }

    public byte[] toByteArray(byte[] ba) {
        assert elts.length == ba.length;
        System.arraycopy(elts, 0, ba, 0, len);
        return ba;
    }

//    public Byte[] toByteArray() {
//        return eltsl.toArray(new Byte[0]);
//    }

//    public byte[] join() throws Exception {
//        int delimCount = elts.length / eltSize;
//        byte[] result = new byte[elts.length + delimCount];
//        int i = 0;
//        for (byte b : elts) {
//            if (i % (eltSize + 1) == 0) {
//                result[i++] = delimiter;
//            }
//            result[i++] = b;
//        }
//        if (i != result.length) throw new Exception("idk");
//        return result;
//    }

}
