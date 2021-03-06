package org.gobiiproject.gobiiprocess.digester.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.digester.LoaderGlobalConfigs;
import org.gobiiproject.gobiiprocess.digester.csv.matrixValidation.MatrixValidation;
import org.gobiiproject.gobiiprocess.digester.csv.matrixValidation.ValidationResult;

/**
 * CSV-Specific File Loader class, used by
 * {@link org.gobiiproject.gobiiprocess.digester.GobiiFileReader} Contains
 * methods specific to reading of single-character separated text files, such as
 * .csv, tab-delimited, and pipe-separated values. This class is used, when none
 * of GobiiColumnType in GobiiLoaderInstruction
 * {@link org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction}
 * is CSV_COL.
 * Assumptions:
 * No more than one CSV_BOTH can exist in the instruction.
 * No other column in CSV_BOTH
 * Only one of CSV_ROW, CSV_COL, CSV_BOTH can exist in the file.
 *
 * @author CSarma
 * @date 3/23/2017
 */

public class CSVFileReaderV2 extends CSVFileReaderInterface {

    private static final String NEWLINE = "\n";
    private static final String TAB = "\t";
    private final String loaderScriptPath;
    private GobiiProcessedInstruction processedInstruction;
    private int maxLines = 0;

    CSVFileReaderV2(String loaderScriptPath) {
        this.loaderScriptPath = loaderScriptPath;
    }

    /**
     * Parses a given instruction file, and executes the loader on every instruction found within, by passing the objects to {@link CSVFileReaderV2#processCSV(GobiiLoaderInstruction)}.
     * This method can be called directly to simulate an instruction file being parsed by the reader.
     */
    public static void parseInstructionFile(List<GobiiLoaderInstruction> instructions, String loaderScriptPath) {
        CSVFileReaderInterface reader;
        if (LoaderGlobalConfigs.getSingleThreadFileRead()) {
            for (GobiiLoaderInstruction i : instructions) {
                try {
                    reader = new CSVFileReaderV2(loaderScriptPath);
                    reader.processCSV(i);
                } catch (InterruptedException e) {
                    ErrorLogger.logError("CSVFileReader", "Interrupted reading instruction", e);
                } catch (Exception e) {
                    ErrorLogger.logError("CSVFileReader", "Unexpected Exception in reader", e);
                }
            }
            return;
        }

        List<Thread> threads = new LinkedList<>();
        if (instructions == null) {
            ErrorLogger.logError("CSVFileReader", "No instructions passed in");
        } else {
            GobiiLoaderInstruction matrixInstruction = null;

            //Create threads
            for (GobiiLoaderInstruction loaderInstruction : instructions) {
                if (matrixInstruction == null && isMatrixInstruction(loaderInstruction)) {
                    matrixInstruction = loaderInstruction;
                    continue;//Skip processing until after all intermediate files
                }
                reader = new CSVFileReaderV2(loaderScriptPath);
                Thread processingThread = new Thread(new ReaderThread(reader, loaderInstruction));
                threads.add(processingThread);
                processingThread.start();
            }
            //Wait for all threads to complete, lets just wait for all of them in order (obviously, many will be done
            // before hand, in which case 'join' does nothing. Brilliant.
            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    ErrorLogger.logError("CSVFileReader", "Interrupt", e);
                }
            }

            //Process matrix in main processing thread.
            if (matrixInstruction != null) {
                reader = new CSVFileReaderV2(loaderScriptPath);
                new ReaderThread(reader, matrixInstruction).run();//Calls this thread from our thread
            }
        }
    }

    //Gnarly logic - if the first column asked for is a both type, this likely is the matrix file
    private static boolean isMatrixInstruction(GobiiLoaderInstruction inst){
        return inst.getGobiiFileColumns().get(0).getGobiiColumnType().equals(GobiiColumnType.CSV_BOTH);
    }

    /**
     * Reads the input file specified by the loader instruction and creates a
     * digest file based on the instruction.
     *
     * @param loaderInstruction Singular instruction, specifying input and output directories
     */
    public void processCSV(GobiiLoaderInstruction loaderInstruction) {

        processedInstruction = new GobiiProcessedInstruction(loaderInstruction);
        processedInstruction.parseInstruction();

        String outputFileName = HelperFunctions.getDestinationFile(loaderInstruction);
        try (BufferedWriter tempFileBufferedWriter = new BufferedWriter(new FileWriter(outputFileName))) {

            File file = new File(loaderInstruction.getGobiiFile().getSource());
            File outputFile = new File(HelperFunctions.getDestinationFile(loaderInstruction));
            if (file.isDirectory()) {
                listFilesFromFolder(file, tempFileBufferedWriter, loaderInstruction, outputFile);
            } else {
                writeToOutputFile(file, tempFileBufferedWriter, loaderInstruction, outputFile);
            }
        } catch (FileNotFoundException e) {
            ErrorLogger.logError("CSVReader", "Unexpected Missing File", e);
        } catch (IOException e) {
            ErrorLogger.logError("CSVReader", "Unexpected IO Error", e);
        }
    }

    /**
     * Finds all files in DIR{@code folder}, reading each file for the
     * data needed, and writing to {@code tmpFileBufferedWriter}. Sub
     * folders(Nested folders) are ignored.
     *
     * @param folder                 Folder in the file-system to start from (input folder)
     * @param tempFileBufferedWriter Output File writer
     * @param loaderInstruction      Loader instruction
     */
    private void listFilesFromFolder(File folder, BufferedWriter tempFileBufferedWriter,
                                     GobiiLoaderInstruction loaderInstruction, File outputFile) {
        if (folder == null) {
            ErrorLogger.logWarning("CSVFileReader", "Read from null folder");
            return;
        }
        for (File file : folder.listFiles()) {
            // Sub folders are ignored
            if (file.isFile() & !file.getName().contains("digest")) {
                try {
                    writeToOutputFile(file, tempFileBufferedWriter, loaderInstruction, outputFile);
                } catch (IOException e) {
                    ErrorLogger.logError("CSVReader", "Failure to write digest files", e);
                }
            }
        }
    }

    /**
     * Reads data from a single input file, and writes to digest file
     * (referenced by {@code }tmpFileBufferedsWriter}. This method is primarily called by
     * {@link CSVFileReaderV2 listFilesFromFolder(File, BufferedWriter, GobiiLoaderInstruction, boolean)}
     *
     * @param file                   File to read from
     * @param tempFileBufferedWriter output file writer
     * @param loaderInstruction      Loader Instruction.
     * @throws IOException when the requisite file is missing or cannot be read
     */
    private void writeToOutputFile(File file, BufferedWriter tempFileBufferedWriter,
                                   GobiiLoaderInstruction loaderInstruction, File outputFile) throws IOException {

        if (processedInstruction.hasCSV_ROW()) {
            processCSV_ROW(file, tempFileBufferedWriter, loaderInstruction);
        } else if (processedInstruction.hasCSV_COL()) {
            processCSV_COL(file, tempFileBufferedWriter, loaderInstruction);
        } else if (processedInstruction.hasCSV_BOTH()) {
            RowColPair<Integer> matrixSize=processCSV_BOTH(file, tempFileBufferedWriter, loaderInstruction, outputFile);
            CSVFileReaderInterface.lastMatrixSizeRowCol=matrixSize;//Terrible hack to pass back to main thread the size of the file if it's a matrix file. There should be at most
            //One of these. It sucks, but passing it up the object chain doesn't make sense, as it goes through several layers of indirection.
        }
    }

    /**
     * Creates digest table for CSV_ROW.
     *
     * @param file                   Input file to read from.
     * @param tempFileBufferedWriter Output file writer.
     * @param loaderInstruction      Loader instruction.
     * @throws IOException Exception in I/O operations
     */
    private void processCSV_ROW(File file, BufferedWriter tempFileBufferedWriter,
                                GobiiLoaderInstruction loaderInstruction) throws IOException {
        readCSV_ROWS(file, loaderInstruction);
        // Added for consistency in flow. For CSV_ROW this variable is not used. So empty list is passed
        List<String> rowList = new ArrayList<>();
        if (processedInstruction.isFirstLine()) {
            writeFirstLine(tempFileBufferedWriter);
            processedInstruction.setFirstLine(false);
        }
        for (int rowNo = 0; rowNo < maxLines; rowNo++) {
            writeOutputLine(tempFileBufferedWriter, rowList);
        }
    }

    /**
     * Creates digest table for CSV_COL.
     *
     * @param file                   Input file to read from.
     * @param tempFileBufferedWriter Output file writer.
     * @param loaderInstruction      Loader instruction.
     */

    private void processCSV_COL(File file, BufferedWriter tempFileBufferedWriter,
                                GobiiLoaderInstruction loaderInstruction) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            int rowNo = 0;
            String fileRow;
            if (processedInstruction.isFirstLine()) {
                writeFirstLine(tempFileBufferedWriter);
                processedInstruction.setFirstLine(false);
            }
            // All the columns should start on the same row. Else there will be mismatch in the length of col's.
            int rowNoInGobiiColumn = 0;
            for (GobiiFileColumn gobiiFileColumn : processedInstruction.getColumnList()) {
                if (gobiiFileColumn.getGobiiColumnType().equals(GobiiColumnType.CSV_COLUMN)) {
                    rowNoInGobiiColumn = gobiiFileColumn.getrCoord();
                    break;
                }
            }
            List<Integer> reqCols = getRequiredColNo();
            while ((fileRow = bufferedReader.readLine()) != null) {

                String[] row = fileRow.split(loaderInstruction.getGobiiFile().getDelimiter(), -1);//Need to capture blank trailing values

                if (rowNo >= rowNoInGobiiColumn) {
                    List<String> rowList = new ArrayList<>();
                    for (Integer colNo : reqCols) {
                        rowList.add(row[colNo]);
                    }
                    getCol(rowList);
                    writeOutputLine(tempFileBufferedWriter, rowList);
                }
                rowNo++;
            }
        } catch (FileNotFoundException e) {
            ErrorLogger.logError("CSVReader", "Unexpected Missing File", e);
        } catch (IOException e) {
            ErrorLogger.logError("CSVReader", "Unexpected IO Error", e);
        }
    }

    /**
     * Creates digest table for CSV_BOTH.
     *
     * @param file                   Input file to read from.
     * @param tempFileBufferedWriter Output file writer.
     * @param loaderInstruction      Loader instruction.
     * #returns Pair of row, column counts as integers
     *
     * @throws IOException Exception in I/O operations
     */

    private RowColPair<Integer> processCSV_BOTH(File file, BufferedWriter tempFileBufferedWriter,
                                 GobiiLoaderInstruction loaderInstruction, File outputFile) throws IOException {
        Integer totalCols=null;
        Integer totalRows=null;
        boolean skipValidation = !LoaderGlobalConfigs.getValidation();

        GobiiFileColumn csv_BothColumn = null;
        for (GobiiFileColumn gobiiFileColumn : processedInstruction.getColumnList()) {
            if (gobiiFileColumn.getGobiiColumnType().equals(GobiiColumnType.CSV_BOTH)) {
                csv_BothColumn = gobiiFileColumn;
                break;
            }
        }
        // For matrix file there is no need of writing first line. Setting it to false for consistency
        processedInstruction.setFirstLine(false);
        String missingFile = loaderScriptPath + "/etc/missingIndicators.txt";
        String parentDirectory = outputFile.getParentFile().getAbsolutePath();
        String markerFile = parentDirectory + "/digest.marker";
        MatrixValidation matrixValidation = new MatrixValidation(loaderInstruction.getDatasetType().getName(), missingFile, markerFile);
        if (matrixValidation.setUp()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                int rowNo = 0;
                String fileRow;
                List<String> inputRowList, outputRowList;
                String delimiter = loaderInstruction.getGobiiFile().getDelimiter();
                boolean isVCF = loaderInstruction.getGobiiFile().getGobiiFileType().equals(GobiiFileType.VCF);
                while ((fileRow = bufferedReader.readLine()) != null) {
                    if (rowNo >= csv_BothColumn.getrCoord()) {
                        inputRowList = new ArrayList<>(Arrays.asList(fileRow.split(delimiter)));
                        outputRowList = new ArrayList<>();
                        getRow(inputRowList, csv_BothColumn);
                        ValidationResult validationResult=matrixValidation.validate(rowNo, csv_BothColumn.getrCoord(), inputRowList, outputRowList, isVCF, skipValidation);
                        if (validationResult.success) {
                            writeOutputLine(tempFileBufferedWriter, outputRowList);
                            totalCols=validationResult.numRows;
                        }
                        else {
                            if (matrixValidation.stopProcessing()) {
                                tempFileBufferedWriter.flush();
                                tempFileBufferedWriter.close();
                                FileSystemInterface.rmIfExist(HelperFunctions.getDestinationFile(loaderInstruction));
                                return new RowColPair<Integer>(totalCols,rowNo);
                            }
                        }
                    }
                    rowNo++;
                    totalRows=rowNo-csv_BothColumn.getrCoord();
                }
            }
        }
        if (matrixValidation.getErrorCount() != 0) {
            tempFileBufferedWriter.flush();
            tempFileBufferedWriter.close();
            FileSystemInterface.rmIfExist(HelperFunctions.getDestinationFile(loaderInstruction));
        }
        return new RowColPair<Integer>(totalRows,totalCols);
    }

    /**
     * Writes the first line to the file. Contains column names.
     *
     * @param tempFileBufferedWriter Output file writer.
     * @throws IOException Exception in I/O operations.
     */
    private void writeFirstLine(BufferedWriter tempFileBufferedWriter) throws IOException {
        StringBuilder outputLine = new StringBuilder();
        for (GobiiFileColumn column : processedInstruction.getColumnList()) {
            appendTabToOutput(outputLine, column);
            appendColumnName(outputLine, column);
        }
        tempFileBufferedWriter.write(outputLine.toString());
        tempFileBufferedWriter.write(NEWLINE);
    }

    /**
     * Adds column name if it is not a sub-column
     *
     * @param outputLine Line that is be written to file.
     * @param column     Column whose name is appended.
     */
    private void appendColumnName(StringBuilder outputLine, GobiiFileColumn column) {
        if (!column.isSubcolumn())
            outputLine.append(column.getName());
    }

    /**
     * Adds tab to output if it is not at beginning of line or not a sub-column
     *
     * @param outputLine Line that is be written to file.
     * @param column     Column whose name is appended.
     */
    private void appendTabToOutput(StringBuilder outputLine, GobiiFileColumn column) {
        if (outputLine.length() > 0) {
            if (!column.isSubcolumn())
                outputLine.append(TAB);
        }
    }

    /**
     * Writes a line to output file.
     *
     * @param tempFileBufferedWriter Output file writer.
     * @param rowList                Contains the columns that needs to be appended.
     * @throws IOException I/O Exception
     */
    private void writeOutputLine(BufferedWriter tempFileBufferedWriter, List<String> rowList) throws IOException {
        StringBuilder outputLine = new StringBuilder();
        // Used in traversing requiredRows
        int rowNo = 0;
        for (FileLineEntry entry : processedInstruction.getFileLine()) {
            GobiiFileColumn column = processedInstruction.getColumnList().get(entry.getColumnNo());
            switch (entry.getColumnType()) {
                case CONSTANT:
                    appendTabToOutput(outputLine, column);
                    outputLine.append(entry.getValue());
                    break;
                case AUTOINCREMENT:
                    appendTabToOutput(outputLine, column);
                    outputLine.append(entry.getValue());
                    String increment = (Integer.parseInt(entry.getValue()) + 1) + "";
                    entry.setValue(increment);
                    break;
                case CSV_ROW:
                    appendTabToOutput(outputLine, column);
                    outputLine.append(processedInstruction.getRequiredRows().get(rowNo).remove(0));
                    rowNo++;
                    break;
                case CSV_COLUMN:
                    appendTabToOutput(outputLine, column);
                    outputLine.append(rowList.remove(0));
                    break;
                case CSV_BOTH:
                    while (!rowList.isEmpty()) {
                        appendTabToOutput(outputLine, column);
                        outputLine.append(rowList.remove(0));
                    }
                    break;
                default:
                    break;
            }
        }

        tempFileBufferedWriter.write(outputLine.toString());
        tempFileBufferedWriter.write(NEWLINE);
    }

    /**
     * Reads all the required csv_rows and stores in requiredRows list. Stops
     * processing once all the required rows are read.
     *
     * @param file              Input file.
     * @param loaderInstruction Loader Instruction.
     */
    private void readCSV_ROWS(File file, GobiiLoaderInstruction loaderInstruction) {
        int maxRequiredRowNo = maxRequiredRow();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            int rowNo = 0;
            String fileRow;
            while ((fileRow = bufferedReader.readLine()) != null) {
                if (rowNo > maxRequiredRowNo) {
                    // All required rows read.
                    break;
                }
                String[] row = fileRow.split(loaderInstruction.getGobiiFile().getDelimiter());
                // Can't initialize here as it creates issue when same row is
                // used multiple gobiiFileColumn's.
                ArrayList<String> rowList;
                // Check for which all columns this row is required.
                int currentCol = 0;
                for (GobiiFileColumn column : processedInstruction.getColumnList()) {
                    if (column.getGobiiColumnType().equals(GobiiColumnType.CSV_ROW)) {
                        if (column.getrCoord() == rowNo) {
                            rowList = new ArrayList<>(Arrays.asList(row));
                            getRow(rowList, column);
                            processedInstruction.addRow(currentCol, rowList);

                            if (maxLines < rowList.size())
                                maxLines = rowList.size();
                        }
                        currentCol++;
                    }
                }
                rowNo++;
            }
        } catch (FileNotFoundException e) {
            ErrorLogger.logError("CSVReader", "Unexpected Missing File", e);
        } catch (IOException e) {
            ErrorLogger.logError("CSVReader", "Unexpected IO Error", e);
        }

    }

    /**
     * Applying filter on the column data.
     * Parses all the columns to identify CSV_COLUMN to identify the required columns.
     *
     * @param rowList List of the required columns required.
     */
    private void getCol(List<String> rowList) {
        int colNo = 0;
        for (GobiiFileColumn column : processedInstruction.getColumnList()) {
            if (column.getGobiiColumnType().equals(GobiiColumnType.CSV_COLUMN)) {
                rowList.set(colNo, HelperFunctions.filter(rowList.get(colNo), column.getFilterFrom(), column.getFilterTo(),
                        column.getFindText(), column.getReplaceText()));
                colNo++;
            }
        }
    }

    /**
     * Returns a list of required column No's
     */
    private List<Integer> getRequiredColNo() {
        List<Integer> reqCols = new ArrayList<>();
        for (GobiiFileColumn column : processedInstruction.getColumnList()) {
            if (column.getGobiiColumnType().equals(GobiiColumnType.CSV_COLUMN)) {
                reqCols.add(column.getcCoord());
            }
        }
        return reqCols;
    }

    /**
     * Removing columns till cCoord and applying filter.
     *
     * @param rowList row from the file
     * @param column  gobii file column
     */
    private void getRow(List<String> rowList, GobiiFileColumn column) {
        int colNo = 0;
        for (String element : rowList) {
            rowList.set(colNo, HelperFunctions.filter(element, column.getFilterFrom(), column.getFilterTo(),
                    column.getFindText(), column.getReplaceText()));
            colNo++;
        }
        for (int cCoord = 0; cCoord < column.getcCoord(); cCoord++) {
            rowList.remove(0);
        }
    }

    /**
     * Max row no required.
     *
     * @return max required row no.
     */
    private int maxRequiredRow() {
        int maxRowNo = -1;
        for (GobiiFileColumn gobiiFileColumn : processedInstruction.getColumnList()) {
            if ((gobiiFileColumn.getGobiiColumnType().equals(GobiiColumnType.CSV_ROW))
                    && (maxRowNo < gobiiFileColumn.getrCoord()))
                maxRowNo = gobiiFileColumn.getrCoord();
        }
        return maxRowNo;
    }
}


class ReaderThread implements Runnable {
    private CSVFileReaderInterface reader;
    private GobiiLoaderInstruction instruction;

    ReaderThread(CSVFileReaderInterface reader, GobiiLoaderInstruction instruction) {
        this.reader = reader;
        this.instruction = instruction;
    }

    @Override
    public void run() {
        try {
            reader.processCSV(instruction);
        } catch (Exception e) {
            ErrorLogger.logError("ReaderThread", "Error processing file read", e);
        }
    }
}

class RowColPair<I>{
    public I row;
    public I col;
    RowColPair(I row, I col){
        this.row=row;
        this.col=col;
    }
}