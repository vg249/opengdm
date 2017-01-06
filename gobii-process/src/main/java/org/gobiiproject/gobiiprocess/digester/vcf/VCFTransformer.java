package org.gobiiproject.gobiiprocess.digester.vcf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

/**
 * Created by AVB on 1/5/2017.
 */
public class VCFTransformer {

	private String[][] mrefMatrix;
	private String[][] bareMatrix;
	private String parentDirectory;

	/**
	 * Creates a new VCFTransformer using both path arguments of the ".mref" and "matrix" files to capture all their data in the memory
	 * as well as the parent directory of the "matrix" file
	 *
	 * @param mrefFile
	 * @param matrixFile
	 */
    public VCFTransformer(String mrefFile, String matrixFile) {
        try {
            Path mrefFilePath = Paths.get(mrefFile);
            Stream<String> mrefStream = Files.lines(mrefFilePath);
            this.mrefMatrix = mrefStream.map(line->line.trim().toUpperCase().split("\\s+"))
            		                    .toArray(String[][]::new);
    
            //Stream.of(this.mrefMatrix).map(Arrays::toString).forEach(System.out::println);
		} catch (IOException e) {
			ErrorLogger.logError("VCFTransformer","Something wrong with the .mref file", e);
		}
		try {
			Path matrixFilePath = Paths.get(matrixFile);
            Stream<String> matrixStream = Files.lines(matrixFilePath);
            this.bareMatrix = matrixStream.map(line->line.trim().split("\\s+"))
		                                  .toArray(String[][]::new);

            //System.out.println();
            //Stream.of(this.bareMatrix).map(Arrays::toString).forEach(System.out::println);
            
            this.parentDirectory = matrixFilePath.getParent().toString();
        } catch (IOException e) {
			ErrorLogger.logError("VCFTransformer","Something wrong with the matrix file", e);
        }
    }

	/**
	 * Bares all the matrix data by rejecting anything after the ":" character inclusive and
	 * creates a file of bimatrix data in the same directory of the matrix data file by using
	 * the following rules:
	 * a) the (bare) matrix cell must be X/Y where X, Y can be 0 or 1 so there are 4 possibilities: 0/0, 0/1, 1/0, and 1/1.
	 * b) 0 from the matrix cell -> the cell from the same row in the .mref matrix but in the first column "ref"
	 * c) 1 from the matrix cell -> the cell from the same row in the .mref matrix but in the second column "alt"
	 */
    public boolean execute() {
    	
    	if ((this.mrefMatrix == null) || (this.mrefMatrix.length < 1)) {
			ErrorLogger.logError("VCFTransformer","No .mref data", new Exception());
    		return false;
    	}
    	
    	if ((this.bareMatrix == null) || (this.bareMatrix.length < 1)) {
			ErrorLogger.logError("VCFTransformer","No matrix data", new Exception());
    		return false;
    	}
        
    	if (this.mrefMatrix.length != this.bareMatrix.length) {
			ErrorLogger.logError("VCFTransformer","Different matrix row numbers", new Exception());
    		return false;
    	}
    	
        try {
        	// Writing the .barematrix data in the same directory
        	File bareMatrixFile = new File(Paths.get(this.parentDirectory, ".barematrix").toString());
        	// Always overwriting the .barematrix file
        	bareMatrixFile.createNewFile();
        	BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(bareMatrixFile)));
        	// Writing the bare matrix header
        	bufferedWriter.write("bare matrix");
    		bufferedWriter.newLine();
    		// Writing the bare matrix data
        	int rowsNumber = this.bareMatrix.length;
        	for (int i = 1; i < rowsNumber; i++) {
        		int columnsNumber = this.bareMatrix[i].length;
        		for (int j = 0; j < columnsNumber; j++) {
        			this.bareMatrix[i][j] = this.bareMatrix[i][j].split(":")[0]; 
        			bufferedWriter.write(this.bareMatrix[i][j]);
        			bufferedWriter.write("\t");
        		}
        		bufferedWriter.newLine();
        	}
        	bufferedWriter.flush();
        	bufferedWriter.close();

        	//Stream.of(this.bareMatrix).map(Arrays::toString).forEach(System.out::println);
        
        	// Writing the .bimatrix data in the same directory
        	File biMatrixFile = new File(Paths.get(this.parentDirectory, ".bimatrix").toString());
        	// Always overwriting the .bimatrix file
        	biMatrixFile.createNewFile();
        	bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(biMatrixFile)));
        	// Writing the bimatrix header
        	bufferedWriter.write("bi matrix");
    		bufferedWriter.newLine();
    		
    		rowsNumber = this.bareMatrix.length;
        	for (int i = 1; i < rowsNumber; i++) {
        		int columnsNumber = this.bareMatrix[i].length;
        		for (int j = 0; j < columnsNumber; j++) {
        			String[] numbers = this.bareMatrix[i][j].split("/");
        			if (numbers.length != 2) {
        				bufferedWriter.flush();
        	        	bufferedWriter.close();
        	        	ErrorLogger.logError("VCFTransformer","Incorrect data: " + this.bareMatrix[i][j], new Exception());
        				return false;
        			}
        			String bimatrixCell = "";
        			for (int k = 0; k < 2; k++)  {
        				if (k == 1) {
        					bimatrixCell = bimatrixCell + "/"; 
        				}
        				int number;
        				try {
        					number = Integer.parseInt(numbers[k]);
        				}
        				catch (NumberFormatException n) {
							ErrorLogger.logError("VCFTransformer","Not numerical: " + numbers[k], n);
            				bufferedWriter.flush();
                	        bufferedWriter.close();
            				return false;
        				}
            			if ((number == 0) || (number == 1))  {
            				bimatrixCell = bimatrixCell + this.mrefMatrix[i][number];
            			}
            			else {
							ErrorLogger.logError("VCFTransformer","Not covered number: " + number, new Exception());
            				bufferedWriter.flush();
                	        bufferedWriter.close();
            				return false;
            			}
        			}
        			bufferedWriter.write(bimatrixCell);
        			bufferedWriter.write("\t");
        		}
        		bufferedWriter.newLine();
        	}
    		bufferedWriter.flush();
        	bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return true;
    }
}
