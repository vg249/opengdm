package org.gobiiproject.gobiiprocess.digester.vcf;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

/**
 * Created by AVB on 1/5/2017.
 *
 * Bares all the matrix data by rejecting anything after the ":" character inclusive and
 * creates a file of bimatrix data in the "bimatrix" file by using
 * the following rules:
 * a) the (bare) matrix cell must be X/Y where X, Y can be 0 or 1 so there are 4 possibilities: 0/0, 0/1, 1/0, and 1/1.
 * b) 0 from the matrix cell -> the cell from the same row in the .mref matrix but in the first column "ref"
 * c) 1 from the matrix cell -> the cell from the same row in the .mref matrix but in the second column "alt"
 **/
public class VCFTransformer {
	/**
	 * Creates a new VCFTransformer using the path arguments of the ".mref" and "matrix" files to capture all their data
	 * as well as the path argument of the new "bimatrix" file to dump the transformed VCF data
	 */
	public VCFTransformer(String mrefFilePath, String matrixFilePath, String bimatrixFilePath) {
		try {
			BufferedReader mrefFileBufferedReader = new BufferedReader(new FileReader(new File(mrefFilePath)));
			// Omitting the .mref header
			String mrefLine = mrefFileBufferedReader.readLine();
			BufferedReader matrixFileBufferedReader = new BufferedReader(new FileReader(new File(matrixFilePath)));
			// Omitting the matrix header
			String matrixLine = matrixFileBufferedReader.readLine();
			BufferedWriter bimatrixFileBufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(bimatrixFilePath))));
			bimatrixFileBufferedWriter.write("bi matrix");
			bimatrixFileBufferedWriter.newLine();
			while (((mrefLine = mrefFileBufferedReader.readLine()) != null) &&
				   ((matrixLine = matrixFileBufferedReader.readLine()) != null)) {
				String[] mrefLineData = mrefLine.trim().toUpperCase().split("\\s+");
				String[] matrixLineData = matrixLine.trim().split("\\s+");
				int columnsNumber = matrixLineData.length;
				for (int i = 0; i < columnsNumber; i++) {
					matrixLineData[i] = matrixLineData[i].split(":")[0];
					String[] numbers = matrixLineData[i].split("/");
					if (numbers.length != 2) {
						mrefFileBufferedReader.close();
						matrixFileBufferedReader.close();
						bimatrixFileBufferedWriter.flush();
						bimatrixFileBufferedWriter.close();
						ErrorLogger.logError("VCFTransformer", "Incorrect data: " + matrixLineData[i], new Exception());
						return;
					}
					String bimatrixCell = "";
					for (int k = 0; k < 2; k++) {
						if (k == 1) {
							bimatrixCell = bimatrixCell + "/";
						}
						int number;
						try {
							number = Integer.parseInt(numbers[k]);
							if ((number == 0) || (number == 1)) {
								bimatrixCell = bimatrixCell + mrefLineData[number];
							} else {
								mrefFileBufferedReader.close();
								matrixFileBufferedReader.close();
								bimatrixFileBufferedWriter.flush();
								bimatrixFileBufferedWriter.close();
								ErrorLogger.logError("VCFTransformer", "Not covered number: " + number, new Exception());
								return;
							}
						} catch (NumberFormatException n) {
							mrefFileBufferedReader.close();
							matrixFileBufferedReader.close();
							bimatrixFileBufferedWriter.flush();
							bimatrixFileBufferedWriter.close();
							ErrorLogger.logError("VCFTransformer", "Not numerical: " + numbers[k], n);
							return;
						}
					}
					bimatrixFileBufferedWriter.write(bimatrixCell);
					bimatrixFileBufferedWriter.write("\t");
				}
				bimatrixFileBufferedWriter.newLine();
			}
			mrefFileBufferedReader.close();
			matrixFileBufferedReader.close();
			bimatrixFileBufferedWriter.flush();
			bimatrixFileBufferedWriter.close();
		} catch (IOException e) {
			ErrorLogger.logError("VCFTransformer", "IOException Error", e);
		}
	}
}
