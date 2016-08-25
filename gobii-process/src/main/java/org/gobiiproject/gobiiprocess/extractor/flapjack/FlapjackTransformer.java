package org.gobiiproject.gobiiprocess.extractor.flapjack;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;
public class FlapjackTransformer {

	public static boolean generateMapFile(String markerFile, String sampleFile, int datasetId, String tempDir,String outFile,String errorFile){
/*		
	2) cut marker, chromosome, position
	3) remove headers
	4) cat with header file
	cut -f1,2,4 derp.txt | tail -n +2 > derp2.txt
		
*/		
		HelperFunctions.tryExec("echo # fjFile = MAP",tempDir+"map.header",errorFile);
		
		HelperFunctions.tryExec("cut -f1,3,4 "+markerFile,tempDir+"tmp",errorFile);
		HelperFunctions.tryExec("tail -n +2 "+tempDir+"tmp",tempDir+"map.body",errorFile);
		rm(tempDir+"tmp");
		
		HelperFunctions.tryExec("cat "+tempDir+"map.header "+tempDir+"map.body",outFile,errorFile);
		rm(tempDir+"map.header");
		rm(tempDir+"map.body");
		return true;
	}
	public static boolean generateGenotypeFile(String markerFile, String sampleFile, String genotypeFile, int datasetId, String tempDir, String outFile,String errorFile){
		/**
		 * Genotype file - 
1) create header file
  # fjFile = GENOTYPE
2)
cut marker names
transpose marker names
3) attach to top of genotype matrix
4) get sample names
5) add blank in front of first name
6) paste sample names to genotype file
7) add header file to top
		 */
		//TODO: run slash adder on genotype file
		String tempFile = tempDir + "tmp";
		String markerList = tempDir + "genotype.markerList";
		String inverseMarkerList = tempDir + "genotype.markerIList";
		
		HelperFunctions.tryExec("echo # fjFile = GENOTYPE",tempDir+"map.header",errorFile);
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
		
		//Sample list is all samples, no header
		HelperFunctions.tryExec("cut -f1 "+sampleFile, tempFile,errorFile);
		HelperFunctions.tryExec("tail -n +2 "+tempFile,tempDir+"genotype.sampleList",errorFile);
		rm(tempFile);
		
		
		
		HelperFunctions.tryExec("paste "+ tempDir+"genotype.sampleList "+genotypeFile,tempDir+"sample.matrix",errorFile);//And now we have a matrix with the samples attached
		HelperFunctions.tryExec("cat "+tempDir+"map.header "+ inverseMarkerList+" " + tempDir+"blank.file "+ tempDir+"sample.matrix",outFile,errorFile);

		rm(tempDir+"map.header");
		rm(markerList);
		rm(inverseMarkerList);
		rm(tempDir+"genotype.sampleList");
		rm(tempDir+"sample.matrix");
		rm(tempDir+"blank.file");
		
		
		return true;
	}
}
