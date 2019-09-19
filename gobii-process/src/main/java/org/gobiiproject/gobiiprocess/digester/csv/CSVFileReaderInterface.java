package org.gobiiproject.gobiiprocess.digester.csv;

import org.apache.commons.lang3.tuple.Pair;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by jdl232 on 3/28/2017.
 */
public abstract class CSVFileReaderInterface {
    public static RowColPair<Integer> lastMatrixSizeRowCol = null;
    /**
     * Reads the input file specified by the loader instruction and creates a digest file based on the instruction. For more detailed discussions on the resulting digest file's format
     * see either the documentation of the IFLs or {@link org.gobiiproject.gobiiprocess.digester.GobiiFileReader} documentation.
     * @throws IOException If an unexpected filesystem error occurs
     * @throws InterruptedException If interrupted (Signals, etc)
     */
    abstract void processCSV(GobiiLoaderProcedure procedure, GobiiLoaderInstruction instruction) throws IOException, InterruptedException;

    public static Integer getLastMatrixRowSize(){return lastMatrixSizeRowCol.row;}
    public static Integer getLastMatrixColSize(){return lastMatrixSizeRowCol.col;}
}
