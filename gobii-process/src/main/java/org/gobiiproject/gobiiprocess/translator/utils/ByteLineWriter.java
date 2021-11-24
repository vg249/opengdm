package org.gobiiproject.gobiiprocess.translator.utils;

import java.io.*;

public class ByteLineWriter implements AutoCloseable {
    public final File file;
    public final BufferedOutputStream bos;

    public ByteLineWriter(File file) throws FileNotFoundException {
        this.file = file;
        this.bos = new BufferedOutputStream(new FileOutputStream(file));
    }

    @Override
    public void close() throws Exception {
        bos.close();
    }
}
