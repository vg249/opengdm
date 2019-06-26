package org.gobiiproject.gobiiprocess.digester.csv;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderMetadata;
import org.gobiiproject.gobiiprocess.digester.GobiiLoader;

import java.io.IOException;

/**
 * Created by jdl232 on 3/28/2017.
 */
public abstract class CSVFileReaderInterface {
    public static RowColPair<Integer> lastMatrixSizeRowCol = null;
    /**
     * Reads the input file specified by the loader instruction and creates a digest file based on the instruction. For more detailed discussions on the resulting digest file's format
     * see either the documentation of the IFLs or {@link GobiiLoader} documentation.
     * @param loaderInstruction Singular instruction, specifying input and output directories
     * @throws IOException If an unexpected filesystem error occurs
     * @throws InterruptedException If interrupted (Signals, etc)
     */
    abstract void processCSV(GobiiLoaderMetadata metadata, GobiiLoaderInstruction loaderInstruction) throws IOException, InterruptedException;

    public static Integer getLastMatrixRowSize(){return lastMatrixSizeRowCol.row;}
    public static Integer getLastMatrixColSize(){return lastMatrixSizeRowCol.col;}
}
