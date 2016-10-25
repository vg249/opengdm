package org.gobiiproject.gobiiprocess.extractor.flapjack;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;

/**
 * Generates the output files for Flapjack import.
 * This class is split into generating the .map and .genotype files.
 */
public class FlapjackTransformer {

	/**
	 * Given a marker MDE file, generates a map file to {@code outFile}
	 * @param markerFile MDE output of marker file
	 * @param tempDir Place to store temporary files
	 * @param outFile The resulting map file
	 * @param errorFile The temporary file to write errors to
	 * @return true if success
	 */
	public static boolean generateMapFile(String markerFile, String tempDir,String outFile,String errorFile){
/*		
	2) cut marker, chromosome, position
	3) remove headers
	4) cat with response file
	cut -f1,2,4 derp.txt | tail -n +2 > derp2.txt
		
*/		
		HelperFunctions.tryExec("echo # fjFile = MAP",tempDir+"map.response",errorFile);
		
		HelperFunctions.tryExec("cut -f1,3,4 "+markerFile,tempDir+"tmp",errorFile);
		HelperFunctions.tryExec("tail -n +2 "+tempDir+"tmp",tempDir+"map.body",errorFile);
		rm(tempDir+"tmp");
		
		HelperFunctions.tryExec("cat "+tempDir+"map.response "+tempDir+"map.body",outFile,errorFile);
		rm(tempDir+"map.response");
		rm(tempDir+"map.body");
		return true;
	}

	/**
	 * Generates a genotype file from the MDE outputs
	 * @param markerFile MDE output of markers
	 * @param sampleFile Sample MDE output file
	 * @param genotypeFile Genotype MDE output file
	 * @param datasetId Unused
	 * @param tempDir Directory to write temporary files
	 * @param outFile Output genotype file
	 * @param errorFile Temporary file to write error logs to
	 * @return true on success
	 */
	public static boolean generateGenotypeFile(String markerFile, String sampleFile, String genotypeFile, int datasetId, String tempDir, String outFile,String errorFile){
		/**
		 * Genotype file - 
1) create response file
  # fjFile = GENOTYPE
2)
cut marker names
transpose marker names
3) attach to top of genotype matrix
4) get sample names
5) add blank in front of first name
6) paste sample names to genotype file
7) add response file to top
		 */
		//TODO: run slash adder on genotype file
		String tempFile = tempDir + "tmp";
		String markerList = tempDir + "genotype.markerList";
		String inverseMarkerList = tempDir + "genotype.markerIList";
		
		HelperFunctions.tryExec("echo # fjFile = GENOTYPE",tempDir+"map.response",errorFile);
		HelperFunctions.tryExec("echo ",tempDir+"blank.file",errorFile);
		
		//Markerlist contains all marker names, sequentially

		HelperFunctions.tryExec("cut -f1 "+markerFile, tempFile,errorFile);
		HelperFunctions.tryExec("tail -n +2 "+tempFile, markerList,errorFile);
		rm(tempFile);
		//Adds one line, then rotates 90 degrees, so each marker is a tab
		HelperFunctions.tryExec("cat "+tempDir+"blank.file "+markerList, tempFile,errorFile);
		HelperFunctions.tryExec("tr '\\n' '\\t'",inverseMarkerList,errorFile, tempFile);//Input redirection wheeee
		rm(tempFile);
		//Note, this file has no line ending.
		
		//Sample list is all samples, no response
		HelperFunctions.tryExec("cut -f1 "+sampleFile, tempFile,errorFile);
		HelperFunctions.tryExec("tail -n +2 "+tempFile,tempDir+"genotype.sampleList",errorFile);
		rm(tempFile);
		
		
		
		HelperFunctions.tryExec("paste "+ tempDir+"genotype.sampleList "+genotypeFile,tempDir+"sample.matrix",errorFile);//And now we have a matrix with the samples attached
		HelperFunctions.tryExec("cat "+tempDir+"map.response "+ inverseMarkerList+" " + tempDir+"blank.file "+ tempDir+"sample.matrix",outFile,errorFile);

		rm(tempDir+"map.response");
		rm(markerList);
		rm(inverseMarkerList);
		rm(tempDir+"genotype.sampleList");
		rm(tempDir+"sample.matrix");
		rm(tempDir+"blank.file");
		
		
		return true;
	}
}
