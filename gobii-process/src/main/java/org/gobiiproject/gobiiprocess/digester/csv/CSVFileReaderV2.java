package org.gobiiproject.gobiiprocess.digester.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

/**
 * CSV-Specific File Loader class, used by
 * {@link org.gobiiproject.gobiiprocess.digester.GobiiFileReader} Contains
 * methods specific to reading of single-character separated text files, such as
 * .csv, tab-delimited, and pipe-separated values. This class is used, when none
 * of GobiiColumnType in GobiiLoaderInstruction
 * {@link org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction}
 * is CSV_COL.
 * 
 * @author CSarma
 * @date 3/23/2017
 */

public class CSVFileReaderV2 {

	private static final String NEWLINE = "\n";
	private static final String TAB = "\t";
	private String tmpFileLocation = "E:\\GOBII\\temp_files";
	private String tmpFileSeparator = "\\";
	static final String CONSTANT_ENTRY = GobiiColumnType.CONSTANT.name();
	static final String AUTO_INCREMENT_ENTRY = GobiiColumnType.AUTOINCREMENT.name();
	static final String CSV_ROW_ENTRY = GobiiColumnType.CSV_ROW.name();
	static final String CSV_BOTH_ENTRY = GobiiColumnType.CSV_BOTH.name();
	final String AUTO_INCREMENT_START_VALUE = "0"; // TODO: Need to finalize
	List<GobiiFileColumn> columnList;
	List<String> fileLine;
	List<ArrayList<String>> requiredRows;
	int maxLines = 0;
	boolean isFirstLine = true;

	/**
	 * Creates a new CSVFileReader, specifying a location to store 'temporary
	 * files', and a folder separator used to delineate a level in the FS. For
	 * example, a temporary folder of ~/tmpFiles on a Unix would have a location
	 * of "~/tmpFiles" and a separator of "/"
	 *
	 * @param tmpFileLocation
	 * @param tmpFileSeparator
	 */
	public CSVFileReaderV2(String tmpFileLocation, String tmpFileSeparator) {
		this.tmpFileLocation = tmpFileLocation;
		this.tmpFileSeparator = tmpFileSeparator;
	}

	/**
	 * Same as {@link CSVFileReaderV2#CSVFileReaderV2(String, String)}, but with
	 * default parameters of "D:\\GOBII\\temp_files" and "\\". Primarily for
	 * ease of use.
	 */
	public CSVFileReaderV2() {// Kept for backwards compatibility
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	// Testing purpose
	public static void main(String[] args)
			throws FileNotFoundException, IOException, ParseException, InterruptedException {
		parseInstructionFile("E:\\GOBII\\instruction_files\\sample1.json");
	}

	/**
	 * Parses a given instruction file, and executes the loader on every
	 * instruction found within, by passing the objects to
	 * {@link CSVFileReader#processCSV(GobiiLoaderInstruction)}. This method can
	 * be called directly to simulate an instruction file being parsed by the
	 * reader.
	 * 
	 * @param filePath
	 *            location in the file system of the instruction file
	 */
	// Testing purpose
	public static void parseInstructionFile(String filePath)
			throws FileNotFoundException, IOException, ParseException, InterruptedException {
		CSVFileReader reader = new CSVFileReader();
		for (GobiiLoaderInstruction loaderInstruction : HelperFunctions.parseInstructionFile(filePath)) {
			reader.processCSV(loaderInstruction);
		}
	}

	/**
	 * Reads the input file specified by the loader instruction and creates a
	 * digest file based on the instruction.
	 * 
	 * @param loaderInstruction
	 *            Singular instruction, specifying input and output directories
	 * @throws IOException
	 *             If an unexpected file system error occurs
	 * @throws InterruptedException
	 *             If interrupted (Signals, etc)
	 */
	public void processCSV(GobiiLoaderInstruction loaderInstruction) throws IOException, InterruptedException {

		getInfoFromInstruction(loaderInstruction);

		String outputFileName = HelperFunctions.getDestinationFile(loaderInstruction);

		try (BufferedWriter tempFileBufferedWriter = new BufferedWriter(new FileWriter(outputFileName))) {

			File file = new File(loaderInstruction.getGobiiFile().getSource());
			if (file.isDirectory()) {
				listFilesFromFolder(file, tempFileBufferedWriter, loaderInstruction);
			} else {
				writeToOutputFile(file, tempFileBufferedWriter, loaderInstruction);
			}
		} catch (FileNotFoundException e) {
			ErrorLogger.logError("CSVReader", "Unexpected Missing File", e);
		} catch (IOException e) {
			ErrorLogger.logError("CSVReader", "Unexpected IO Error", e);
		}
	}

	/**
	 * Reads data from a single input file, to be written to output file
	 * (referenced by {@code }tmpFileWriter}. This method is primarily called by
	 * {@link CSVFileReader#listFilesFromFolder(File, FileWriter, GobiiLoaderInstruction, boolean)}
	 * No more than one CSV_BOTH can exist in the instruction.
	 * 
	 * @param file
	 *            File to read from
	 * @param tempFileBufferedWriter
	 * @param loaderInstruction
	 * @throws IOException
	 *             when the requisite file is missing or cannot be read
	 */
	private void writeToOutputFile(File file, BufferedWriter tempFileBufferedWriter,
			GobiiLoaderInstruction loaderInstruction) throws IOException {
		// Read all the CSV_ROW first then go for CSV_BOTH
		// Only reading a file with CSV_BOTH starts we read the rest of the
		// file.
		readCSV_ROWS(file, loaderInstruction);
		int CSV_BOTH_ColumnNo = -1;
		for (int columnNo = 0; columnNo < fileLine.size(); columnNo = columnNo + 3) {
			if (fileLine.get(columnNo).equals(CSV_BOTH_ENTRY)) {
				CSV_BOTH_ColumnNo = columnNo;
				break;
			}
		}

		// There is a CSV_BOTH.
		if (CSV_BOTH_ColumnNo > -1) {
			GobiiFileColumn csv_BothColumn = columnList.get(CSV_BOTH_ColumnNo);

			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
				int rowNo = 0;
				String fileRow;
				List<String> rowList;
				while ((fileRow = bufferedReader.readLine()) != null) {
					if (rowNo >= csv_BothColumn.getrCoord()) {
						String[] row = fileRow.split(loaderInstruction.getGobiiFile().getDelimiter());
						rowList = new ArrayList<String>(Arrays.asList(row));
						getRow(row, rowList, csv_BothColumn);
						// Write Line

						if (isFirstLine) {
							writeFirstLine(tempFileBufferedWriter, rowList.size());
							isFirstLine = false;
						}
						writeOutputLine(tempFileBufferedWriter, rowList);
					}
					rowNo++;
				}
			}
		} else {
			List<String> rowList = new ArrayList<String>();
			for (int rowNo = 0; rowNo < maxLines; rowNo++) {
				if (isFirstLine) {
					writeFirstLine(tempFileBufferedWriter, rowList.size());
					isFirstLine = false;
				}
				writeOutputLine(tempFileBufferedWriter, rowList);
				
			}
			System.out.println("DONE");
		}
	}

	/**
	 * Writes the first line to the file. Contains column names.
	 * 
	 * @param tempFileBufferedWriter
	 * @param sizeofCSV_BOTH
	 * @throws IOException
	 */
	private void writeFirstLine(BufferedWriter tempFileBufferedWriter, int sizeOfCSV_BOTH) throws IOException {
		StringBuilder outputLine = new StringBuilder();
		for (GobiiFileColumn column : columnList) {
			if (column.getGobiiColumnType().name().equals(CSV_BOTH_ENTRY)) {
				if (outputLine.length() > 0) {
					outputLine.append(column.isSubcolumn() ? "\\0" : TAB);
				}
				// Insert Name followed by tabs.
				outputLine.append(column.isSubcolumn() ? "\\0" : column.getName());

				sizeOfCSV_BOTH = sizeOfCSV_BOTH - 1; // Added name so one less
				for (int i = 0; i < sizeOfCSV_BOTH; i++) {
					outputLine.append(TAB);
					outputLine.append(TAB);
				}
			} else {
				if (outputLine.length() > 0) {
					outputLine.append(column.isSubcolumn() ? "\\0" : TAB);
				}
				outputLine.append(column.isSubcolumn() ? "\\0" : column.getName());
			}
		}
		tempFileBufferedWriter.write(outputLine.toString());
		tempFileBufferedWriter.write(NEWLINE);
	}

	/**
	 * Writes a line to output file.
	 * 
	 * @param tempFileBufferedWriter
	 * @param outputLineNo
	 * @param rowList
	 * @throws IOException
	 */
	private void writeOutputLine(BufferedWriter tempFileBufferedWriter, List<String> rowList)
			throws IOException {
		StringBuilder outputLine = new StringBuilder();
		// Used in traversing requiredRows
		int rowNo = 0;
		for (int columnNo = 0; columnNo < fileLine.size(); columnNo = columnNo + 3) {
			String columnValue = fileLine.get(columnNo + 1);
			GobiiFileColumn column = columnList.get(Integer.parseInt(fileLine.get(columnNo + 2)));
			if (fileLine.get(columnNo).equals(CONSTANT_ENTRY)) {
				if(outputLine.length() > 0){
					outputLine.append(column.isSubcolumn() ? "\\0" : TAB);
				}
				outputLine.append(columnValue);
				
			} else if (fileLine.get(columnNo).equals(AUTO_INCREMENT_ENTRY)) {
				if(outputLine.length() > 0){
					outputLine.append(column.isSubcolumn() ? "\\0" : TAB);
				}
				outputLine.append(columnValue);
				fileLine.set(columnNo + 1, Integer.toString(Integer.parseInt(columnValue) + 1));
			} else if (fileLine.get(columnNo).equals(CSV_ROW_ENTRY)) {
				// All the row lines are in requiredRows.
				if(outputLine.length() > 0){
					outputLine.append(column.isSubcolumn() ? "\\0" : TAB);
				}
				outputLine.append(requiredRows.get(rowNo).remove(0));
				rowNo++;
			} else if (fileLine.get(columnNo).equals(CSV_BOTH_ENTRY)) {
				while(!rowList.isEmpty()) {
					if(outputLine.length() > 0){
						outputLine.append(column.isSubcolumn() ? "\\0" : TAB);
					}
					outputLine.append(rowList.remove(0));
					
				}
			}
		}
		tempFileBufferedWriter.write(outputLine.toString());
		tempFileBufferedWriter.write(NEWLINE);
	}

	/**
	 * Reads all the required csv_rows and stores in requiredRows list. Stops
	 * processing once all the required rows are read.
	 * 
	 * @param file
	 * @param loaderInstruction
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void readCSV_ROWS(File file, GobiiLoaderInstruction loaderInstruction)
			throws IOException, FileNotFoundException {
		int maxRequiredRowNo = maxRequiredRow();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			int rowNo = 0;
			String fileRow;
			while ((fileRow = bufferedReader.readLine()) != null) {

				if (rowNo > maxRequiredRowNo) {
					// All required lines read.
					break;
				}
				String[] row = fileRow.split(loaderInstruction.getGobiiFile().getDelimiter());
				ArrayList<String> rowList = new ArrayList<String>(Arrays.asList(row));
				// Check for which all columns this row is required. Varies from
				// zero to any integer.
				int currentCol = 0;
				for (GobiiFileColumn column : columnList) {
					if (column.getGobiiColumnType().name().equals(CSV_ROW_ENTRY)) {
						if (column.getrCoord() == rowNo) {
							getRow(row, rowList, column);
							requiredRows.add(currentCol, rowList);
							
							if (maxLines < rowList.size())
								maxLines = rowList.size();
						}
						currentCol++;
					}
				}
				rowNo++;
			}
		}
	}

	private void getRow(String[] row, List<String> rowList, GobiiFileColumn column) {
		for (int cCoord = column.getcCoord(); cCoord < row.length; cCoord++) {
			row[cCoord] = HelperFunctions.filter(row[cCoord], column.getFilterFrom(), column.getFilterTo(),
					column.getFindText(), column.getReplaceText());
		}
		for (int cCoord = 0; cCoord < column.getcCoord(); cCoord++) {
			rowList.remove(0);
		}
	}

	/**
	 * Max row no required.
	 * 
	 * @return
	 */
	private int maxRequiredRow() {
		int maxRowNo = -1;
		for (GobiiFileColumn gobiiFileColumn : columnList) {
			if ((gobiiFileColumn.getGobiiColumnType().name().equals(CSV_ROW_ENTRY))
					&& (maxRowNo < gobiiFileColumn.getrCoord()))
				maxRowNo = gobiiFileColumn.getrCoord();
		}
		return maxRowNo;
	}

	/**
	 * Recursively finds all files in {@code folder}, reading each file for the
	 * data needed, and writing to {@code tmpFileBufferedWriter}. Sub
	 * folders(Nested folders) are ignored.
	 * 
	 * @param folder
	 *            Folder in the file-system to start from (input folder)
	 * @param tempFileBufferedWriter
	 * @param loaderInstruction
	 */
	private void listFilesFromFolder(File folder, BufferedWriter tempFileBufferedWriter,
			GobiiLoaderInstruction loaderInstruction) {
		if (folder == null) {
			ErrorLogger.logWarning("CSVFileReader", "Read from null folder");
			return;
		}
		for (File file : folder.listFiles()) {
			// Sub folders are ignored
			if (file.isFile()) {
				try {
					writeToOutputFile(file, tempFileBufferedWriter, loaderInstruction);
				} catch (IOException e) {
					ErrorLogger.logError("CSVReader", "Failure to write temp files", e);
				}
			}
		}
		return;
	}

	/**
	 * Get all the gobiiFileColums info from the instruction into columnList.
	 * Line of data to be written into temp file, is represented by fileLine.
	 * Each column is represented by three consecutive elements in fileLine
	 * gobiiFileColumnType, Value, Index in column list.
	 *  
	 * @param loaderInstruction
	 */
	private void getInfoFromInstruction(GobiiLoaderInstruction loaderInstruction) {

		int columnNo = 0;
		columnList = loaderInstruction.getGobiiFileColumns();
		fileLine = new ArrayList<String>();
		requiredRows = new ArrayList<ArrayList<String>>();
		for (GobiiFileColumn gobiiFileColumn : columnList) {
			switch (gobiiFileColumn.getGobiiColumnType()) {
			case CONSTANT:
				fileLine.add(CONSTANT_ENTRY);
				fileLine.add(gobiiFileColumn.getConstantValue());
				fileLine.add(Integer.toString(columnNo));
				break;
			case AUTOINCREMENT:
				fileLine.add(AUTO_INCREMENT_ENTRY);
				fileLine.add(AUTO_INCREMENT_START_VALUE);
				fileLine.add(Integer.toString(columnNo));
				break;
			case CSV_ROW:
				fileLine.add(CSV_ROW_ENTRY);
				fileLine.add(Integer.toString(columnNo));
				fileLine.add(Integer.toString(columnNo));
				requiredRows.add(new ArrayList<String>());
				break;
			case CSV_BOTH:
				fileLine.add(CSV_BOTH_ENTRY);
				fileLine.add(Integer.toString(columnNo));
				fileLine.add(Integer.toString(columnNo));
				break;
			default:
				System.err.println("Unable to deal with file type " + gobiiFileColumn.getGobiiColumnType().name());
				break;
			}
			columnNo++;
		}
	}
}
