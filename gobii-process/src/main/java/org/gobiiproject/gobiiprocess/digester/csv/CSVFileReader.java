package org.gobiiproject.gobiiprocess.digester.csv;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

//import org.gobiiproject.gobiimodel.ConfigurationSettings;
/*
 * Class containing the methods for loading data from CSV
 * 
 * @author v.calaminos
 * @date 4/14/2016
 * 
 */
public class CSVFileReader {
	private static final String NEWLINE = "\n";//jdl232 - Lets make this a constant, so if we do have to change it, we can.
	private List<String> column_keys;
	private Map<String, String> constant_values = new HashMap<String, String>();
	private String tmpFileLocation = "E:\\GOBII\\temp_files";
	private String tmpFileSeparator="\\";
	private int maxSize = 0;
	Map<String, String> file_column_constant;
	Map<String, String> file_column_autoincrement;
	int countMax;
	int startNo = 0; // starting number for auto increment; can be 1; waiting for confirmation
	
	public CSVFileReader(String tmpFileLocation, String tmpFileSeparator){
		this.tmpFileLocation=tmpFileLocation;
		this.tmpFileSeparator=tmpFileSeparator;
	}
	public CSVFileReader(){//Kept for backwards compatibility
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, InterruptedException{
		parseInstructionFile("E:\\GOBII\\instruction_files\\sample1.json");
	}
	
	/*
	 * Parses a given instruction file. Assign needed attributes to LoaderInstruction object.
	 */
	public static void parseInstructionFile(String filePath) throws FileNotFoundException, IOException, ParseException, InterruptedException{
			CSVFileReader reader = new CSVFileReader();
			for(GobiiLoaderInstruction loaderInstruction:HelperFunctions.parseInstructionFile(filePath)){
			reader.processCSV(loaderInstruction);
			}
			//'Done' file is an anacronism now
		}
	
	/*
	 *	Reads the CSV input file for each instruction in the instruction file
	 *	Returns a List of records.
	 * 
	 */
	public void processCSV(GobiiLoaderInstruction loaderInstruction) throws IOException, InterruptedException{
	List<String> tempFiles = new ArrayList<>();
		file_column_constant = new HashMap<String, String>();
		file_column_autoincrement= new HashMap<String, String>();
		try {

			List<GobiiFileColumn> columns = new ArrayList<>();
			column_keys = new ArrayList<>();
			columns = loaderInstruction.getGobiiFileColumns();
	        // Print the name from the list....
			String fileListString="",delimiterString="";
			
			boolean first=true;
			for(GobiiFileColumn column : columns) {
	        	String tmpFileName = tmpFileLocation + tmpFileSeparator + column.getName() + ".tmp";//jdl232 - lets call these what they are.
	        	FileWriter tmpFileWriter = new FileWriter(tmpFileName);
	        	tempFiles.add(tmpFileName);
	        	fileListString = fileListString + " " + tmpFileName;//jdls - Generate this up here. NOTE: fileListString now starts with a space character. 
	        	if(!first)delimiterString=delimiterString+(column.isSubcolumn()?"\\0":"\\t"); //delimiter string is between previous column and this column
	        	first=false;

	        	//add row for headers
	        	column_keys.add(column.getName());
	        	String columnName=column.getName();
	        	if(column.isSubcolumn())columnName="";//jdl232 - Lets give column/subcolumn groups 1 name.
	        	tmpFileWriter.write(columnName);
	        	tmpFileWriter.write(NEWLINE);

	        	File file = new File(loaderInstruction.getGobiiFile().getSource());
	        	countMax = 0;
	        	if(file.isDirectory()){
	        		listFilesFromFolder(file, tmpFileWriter, column, loaderInstruction, true, tmpFileName);
	        	}else{
	        		writeToTempFiles(file, tmpFileWriter, column, loaderInstruction, true, tmpFileName);
	        	}
	        	
	    		tmpFileWriter.close();
	        }
	        
			/*
			 *	Build temp file for columns with CONSTANT and AUTOINCREMENT type
			 *	@vcc 
			 */
	        FileWriter fw;
	        for (String fileName : tempFiles){
	        	
	        	if(file_column_constant.containsKey(fileName)){
	        		fw = new FileWriter(fileName, false);
	        		String column_key = file_column_constant.get(fileName);
	        		String columnName=column_key;
	        		for(GobiiFileColumn c:columns){
	        			if(c.getName().equals(column_key)){
	        				if(c.isSubcolumn())columnName="";
	        				break;//jdl232 - Lets not put names in subcolumns
	        			}
	        		}
	        		fw.write(columnName);
	        		for(int i=0; i<maxSize; i++){
	        			fw.write(NEWLINE);
	        			fw.write(constant_values.get(column_key));
	        		}
	        		fw.close();
	        	}
	        	else if(file_column_autoincrement.containsKey(fileName)){
	        		fw = new FileWriter(fileName, false);
	        		String column_key = file_column_autoincrement.get(fileName);
	        		String columnName = column_key;
	        		for(GobiiFileColumn c:columns){
	        			if(c.getName().equals(column_key)){
	        				if(c.isSubcolumn())columnName="";
	        				break;//jdl232 - Lets not put names in subcolumns
	        			}
	        		}
	        		fw.write(columnName);
	        		for(int i=startNo;i<maxSize;i++){
	        			fw.write(NEWLINE);
	        			fw.write(Integer.toString(i));
	        		}
	        		fw.close();
	        	}
	        }
	        
	        /*
	         * Merge all temporary files into one intermediate file using "paste"
	         */
	        delimiterString=delimiterString+"\\n";
	        String commandStr = "paste -d"+delimiterString+""+fileListString;//+" > "+loaderInstruction.getFile().getDestination()+" 2>>/home/jdl232/err.log";
	       HelperFunctions.tryExec(commandStr, HelperFunctions.getDestinationFile(loaderInstruction),"err.log" );
	        for(String filename:tempFiles){
	        	HelperFunctions.tryExec("rm " + filename);
	        }
		} catch (FileNotFoundException e) {
			ErrorLogger.logError("CSVReader","Unexpected Missing File",e);
		} catch (IOException e) {
			ErrorLogger.logError("CSVReader","Unexpected IO Error",e);
		}
	}
	
	/*
	 *	Recursive for reading all files from a directory;
	 *	This method also writes the files into the temporary files;
	 *	@vcc 
	 */
	public void listFilesFromFolder(File folder, FileWriter tmpFileWriter, GobiiFileColumn column, GobiiLoaderInstruction loaderInstruction, boolean first, String tmpFileName){
		if(folder==null){
			ErrorLogger.logWarning("CSVFileReader","Read from null folder");
			return;
		}
		for(File file : folder.listFiles()){
			if(file.isDirectory()){
				listFilesFromFolder(file, tmpFileWriter, column, loaderInstruction, first, tmpFileName);
			}else{
				try {
					writeToTempFiles(file, tmpFileWriter, column, loaderInstruction, first, tmpFileName);
				} catch (IOException e) {
					ErrorLogger.logError("CSVReader","Failure to write temp files",e);
				}
				first = false;
			}
		}
		return;
	}
	
	
	
	/*
	 *	Method for writing the values into temporary files.
	 *	Values are determined based on the column_type
	 *	@vcc
	 */
	public void writeToTempFiles(File file, FileWriter tmpFileWriter, GobiiFileColumn column, GobiiLoaderInstruction loaderInstruction, boolean first, String tmpFileName) throws IOException{
		Scanner lr = null;
		String line = "";
		int counter;
		lr = new Scanner(file);
		GobiiColumnType type = column.getGobiiColumnType();
		switch(type){
		case CONSTANT:
			constant_values.put(column.getName(), column.getConstantValue());
			file_column_constant.put(tmpFileName, column.getName());
			tmpFileWriter.write(column.getConstantValue());
			break;
		case CSV_COLUMN:
    		//set line number to the starting row number
			counter = 0;
			while(lr.hasNextLine()){
				line = lr.nextLine();
				if(counter >= column.getrCoord() && line != null){
					if(!first) tmpFileWriter.write(NEWLINE);
					first = false;
					String[] row = line.split(loaderInstruction.getGobiiFile().getDelimiter());
					if(row.length > column.getcCoord()){
						String newValue = HelperFunctions.filter(row[column.getcCoord()], column.getFilterFrom(), column.getFilterTo(),column.getFindText(),column.getReplaceText());
						tmpFileWriter.write(newValue);
					}else{
						tmpFileWriter.write("");
					}
					countMax++;
				}
				counter++;
			}
    		if(countMax > maxSize){maxSize = countMax;}
    	break;
    	
		case CSV_ROW:
    		counter = 0;
    		while(lr.hasNextLine()){
    			line = lr.nextLine();
    			if(counter == column.getrCoord() && line != null){
    				if(!first) tmpFileWriter.write(NEWLINE);
    				first = false;
    				String[] row = line.split(loaderInstruction.getGobiiFile().getDelimiter());
    				//loop through the columns
    				for (int i=column.getcCoord(); i<row.length; i++){
    					String newValue = HelperFunctions.filter(row[i], column.getFilterFrom(), column.getFilterTo(),column.getFindText(),column.getReplaceText());
    					tmpFileWriter.write(newValue);
    					tmpFileWriter.write(NEWLINE);
    					countMax++;
    				}
    			}
    			counter++;
    		}
    		if(countMax > maxSize){maxSize = countMax;}
    	break;
		case CSV_BOTH:
    		counter = 0;
    		while(lr.hasNextLine()){
    			line = lr.nextLine();
    			if(counter >= column.getrCoord() && line != null){
    				if(!first) tmpFileWriter.write(NEWLINE);
    				first = false;
    				String tempStr = "";
    				String[] row = line.split(loaderInstruction.getGobiiFile().getDelimiter());
    				for (int i=column.getcCoord(); i<row.length; i++){
    					String newValue = HelperFunctions.filter(row[i], column.getFilterFrom(), column.getFilterTo(),column.getFindText(),column.getReplaceText());
    					tempStr = tempStr + (tempStr.length()>0?"\t":"") +newValue;
    				}
    				tmpFileWriter.write(tempStr);
    				countMax++;
    			}
    			counter++;
    		}
    		if(countMax > maxSize){maxSize = countMax;}
    	break;
		case AUTOINCREMENT:
			file_column_autoincrement.put(tmpFileName, column.getName());
		break;
		default:
			ErrorLogger.logError("CSVReader","No method for " + type);
			break;
		}
		lr.close();
	}
}