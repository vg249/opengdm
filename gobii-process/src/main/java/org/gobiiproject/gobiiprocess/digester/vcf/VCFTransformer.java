package org.gobiiproject.gobiiprocess.digester.vcf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

/**
 * Created by AVB on 1/5/2017.
 *
 * Bares all the matrix data by rejecting anything after the ":" character inclusive and
 * creates a file of bimatrix data in the "bimatrix" file by using
 * the following rules:
 * a) the (bare) matrix cell must be X/Y where X, Y can be 0 or 1 or . so there are 9 possibilities: 0/0, 0/1, 0/., 1/0, 1/1, 1/., ./0, ./1, and ./.
 * b) 0 from the matrix cell -> the cell from the same row in the .mref matrix but in the first column "ref"
 * c) 1 from the matrix cell -> the cell from the same row in the .mref matrix but in the second column "alt"
 * d) . from the matrix cell -> N
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
					String[] terms = matrixLineData[i].split("/");
					if (terms.length != 2) {
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
						switch (terms[k]) {
							case "0":
								bimatrixCell = bimatrixCell + mrefLineData[0];
								break;
							case "1": 
								bimatrixCell = bimatrixCell + mrefLineData[1];
								break;
							case ".":
								bimatrixCell = bimatrixCell + "N";
								break;
							default: 
								mrefFileBufferedReader.close();
								matrixFileBufferedReader.close();
								bimatrixFileBufferedWriter.flush();
								bimatrixFileBufferedWriter.close();
								ErrorLogger.logError("VCFTransformer", "Unknown term: " + terms[k], new Exception());
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
