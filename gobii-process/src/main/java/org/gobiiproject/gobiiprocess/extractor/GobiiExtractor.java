package org.gobiiproject.gobiiprocess.extractor;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.extractor.flapjack.FlapjackTransformer;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;

import static org.gobiiproject.gobiimodel.types.GobiiExtractFilterType.WHOLE_DATASET;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.*;
import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.*;

public class GobiiExtractor {
	//Paths
	private static String  pathToHDF5, propertiesFile,pathToHDF5Files;
	
	private static String lastErrorFile=null;
	private static String errorLogOverride;
	private static boolean verbose;
	private static String rootDir="../";
    private static String markerListOverrideLocation=null;
	public static void main(String[] args) throws Exception {
		Options o = new Options()
         		.addOption("v", "verbose", false, "Verbose output")
         		.addOption("e", "errlog", true, "Error log override location")
         		.addOption("r", "rootDir", true, "Fully qualified path to gobii root directory")
         		.addOption("c","config",true,"Fully qualified path to gobii configuration file")
         		.addOption("h", "hdfFiles", true, "Fully qualified path to hdf files")
				.addOption("m", "markerList", true, "Fully qualified path to marker list files - (Debugging, forces marker list extract)");
        
        CommandLineParser parser = new DefaultParser();
        try{
            CommandLine cli = parser.parse( o, args );
            if(cli.hasOption("verbose")) verbose=true;
            if(cli.hasOption("errLog")) errorLogOverride = cli.getOptionValue("errLog");
            if(cli.hasOption("config")) propertiesFile = cli.getOptionValue("config");
            if(cli.hasOption("rootDir")){
                rootDir = cli.getOptionValue("rootDir");
            }
            if(cli.hasOption("hdfFiles")) pathToHDF5Files = cli.getOptionValue("hdfFiles");
            if(cli.hasOption("markerList")) markerListOverrideLocation=cli.getOptionValue("markerList");
            args=cli.getArgs();//Remaining args passed through

        }catch(org.apache.commons.cli.ParseException exp ) {
            new HelpFormatter().printHelp("java -jar Extractor.jar ","Also accepts input file directly after arguments\n"
                    + "Example: java -jar Extractor.jar -c /home/jdl232/customConfig.properties -v /home/jdl232/testLoad.json",o,null,true);

            System.exit(2);
        }
		
     	String extractorScriptPath=rootDir+"extractors/";
    	pathToHDF5=extractorScriptPath+"hdf5/bin/";
    	
    	if(propertiesFile==null)propertiesFile=rootDir+"config/gobii-web.properties";
		
		boolean success=true;
		ConfigSettings configuration=null;
		try {
			configuration = new ConfigSettings(propertiesFile);
		} catch (Exception e) {
			logError("Extractor","Failure to read Configurations",e);
			return;
		}
		String logDir=configuration.getFileSystemLog();
		ErrorLogger.setLogFilepath(logDir);
		String instructionFile=null;
		if(args.length==0 ||args[0]==""){
			Scanner s=new Scanner(System.in);
			System.out.println("Enter Extractor Instruction File Location:");
			instructionFile=s.nextLine();
		    if(instructionFile.equals("")) instructionFile="scripts//jdl232_01_pretty.json";
		    s.close();
		}
		else{
			instructionFile=args[0];
		}

		List<GobiiExtractorInstruction> list= parseExtractorInstructionFile(instructionFile);
		if(list==null){
			ErrorLogger.logError("Extractor","No instruction for file "+instructionFile);
			return;
		}
		for(GobiiExtractorInstruction inst:list){
			String crop = inst.getGobiiCropType();
			if(crop==null) crop=divineCrop(instructionFile);
			Path cropPath = Paths.get(rootDir+"crops/"+crop.toLowerCase());
			if (!(Files.exists(cropPath) &&
				  Files.isDirectory(cropPath))) {
				ErrorLogger.logError("Extractor","Unknown Crop Type: "+crop);
				return;
			}
			CropConfig cropConfig= null;
			try {
				cropConfig = configuration.getCropConfig(crop);
			} catch (Exception e) {
				logError("Extractor","Unknown exception getting crop",e);
				return;
			}
			if (cropConfig == null) {
				logError("Extractor","Unknown Crop Type: "+crop+" in the Configuration File");
				return;
			}
			if(pathToHDF5Files==null)pathToHDF5Files=cropPath.toString()+"/hdf5/";


			Integer mapId;
			List<Integer> mapIds=inst.getMapsetIds();
			if(mapIds.isEmpty() || mapIds.get(0).equals(null)){
				mapId=null;
			}else if(mapIds.size()>1){
				logError("Extraction Instruction","Too many map IDs for extractor. Expected one, recieved "+mapIds.size());
				mapId=null;
			}
			else{
				mapId=mapIds.get(0);
			}

			for(GobiiDataSetExtract extract:inst.getDataSetExtracts()){
				GobiiExtractFilterType filterType = extract.getGobiiExtractFilterType();
                if(filterType==null) filterType=WHOLE_DATASET;
                if(markerListOverrideLocation!=null)filterType=GobiiExtractFilterType.BY_MARKER;
				String extractDir=extract.getExtractDestinationDirectory();
				tryExec("rm -f "+extractDir+"*");
				//TODO: Fix underlying permissions issues
				//tryExec("chmod -R 777 " +extractDir.substring(0, extractDir.lastIndexOf('/')));
				String markerFile=extractDir+"marker.file";
				String extendedMarkerFile=markerFile+".ext";
				String mapsetFile=extractDir+"mapset.file";
				String markerPosFile=markerFile+".pos";
				String sampleFile=extractDir+"sample.file";
				String projectFile=extractDir+"summary.file";
				String chrLengthFile = markerFile+".chr";
				Path mdePath = FileSystems.getDefault().getPath(new StringBuilder(extractorScriptPath).append("postgres/gobii_mde/gobii_mde.py").toString());
				if (!(mdePath.toFile().exists() &&
					  mdePath.toFile().isFile())) {
					ErrorLogger.logDebug("Extractor", new StringBuilder(mdePath.toString()).append(" does not exist!").toString());
					return;
				}
				String gobiiMDE;
				switch(filterType){
					case WHOLE_DATASET:
						gobiiMDE = "python "+ mdePath+
								" -c " + HelperFunctions.getPostgresConnectionString(cropConfig) +
								" -m " + markerFile +
								" -b " + mapsetFile +
								" -s " + sampleFile +
								" -p " + projectFile +
								(mapId==null?"":(" -D "+mapId))+
								" -d " + extract.getDataSetId() +
								" -l -v ";
						break;
					case BY_MARKER:
					    String listLocation=extractDir+extract.getListFileName();
                        if(markerListOverrideLocation!=null) listLocation=markerListOverrideLocation;
						gobiiMDE = "python "+ mdePath+
								" -c " + HelperFunctions.getPostgresConnectionString(cropConfig) +
								" -m " + markerFile +
								" -b " + mapsetFile +
								" -s " + sampleFile +
								" -p " + projectFile +
                                " -x " + listLocation +
								(mapId==null?"":(" -D "+mapId))+
								" -l -v ";
						break;
					default:
						gobiiMDE="";
						ErrorLogger.logError("GobiiExtractor", "UnknownFilterType " + filterType);
				}

				String errorFile=getLogName(extract,cropConfig,extract.getDataSetId());
				ErrorLogger.logInfo("Extractor","Executing MDEs");
				ErrorLogger.logDebug("Extractor",gobiiMDE);
				tryExec(gobiiMDE, extractDir+"mdeOut", errorFile);
				Integer dataSetId=extract.getDataSetId();

				//HDF5
				String tempFolder=extractDir;
				GobiiFileType fileType = extract.getGobiiFileType();
				boolean markerFast=(fileType == GobiiFileType.HAPMAP);

				String genoFile;
				switch(filterType) {
					case WHOLE_DATASET:
						genoFile = getHDF5Genotype(markerFast, errorFile, dataSetId, tempFolder);
						break;
					case BY_MARKER:
						genoFile = getHDF5GenoFromMarkerList(markerFast, errorFile, tempFolder, markerPosFile);
						break;
                    default:
                        genoFile="";
                        ErrorLogger.logError("GobiiExtractor", "UnknownFilterType " + filterType);
                        break;
				}

				switch(extract.getGobiiFileType()){

				case FLAPJACK:
					String genoOutFile = flapjackExtract(success, configuration, inst, extract, extractDir, markerFile, extendedMarkerFile, sampleFile, chrLengthFile, errorFile, dataSetId, tempFolder, genoFile);
					HelperFunctions.sendEmail(extract.getDataSetName()+ " Genotype Extract", genoOutFile, success&&ErrorLogger.success(), errorFile, configuration, inst.getContactEmail());
					break;
				
				case HAPMAP:
					String hapmapOutFile = hapmapExtract(extractorScriptPath, extractDir, extendedMarkerFile, sampleFile, projectFile, chrLengthFile, errorFile, dataSetId, genoFile);
					HelperFunctions.sendEmail(extract.getDataSetName()+" Hapmap Extract",hapmapOutFile,success&&ErrorLogger.success(),errorFile,configuration,inst.getContactEmail());
					break;

					default:
						ErrorLogger.logError("Extractor","Unknown Extract Type "+extract.getGobiiFileType());
						HelperFunctions.sendEmail(extract.getDataSetName()+" "+extract.getGobiiFileType()+" Extract",null,false,errorFile,configuration,inst.getContactEmail());
				}
				rmIfExist(markerPosFile);
				rmIfExist(extendedMarkerFile);
				rmIfExist(mapsetFile);
				ErrorLogger.logDebug("Extractor","DataSet "+dataSetId+" Created");
			}
			HelperFunctions.completeInstruction(instructionFile,configuration.getProcessingPath(crop, GobiiFileProcessDir.EXTRACTOR_DONE));
		}
	}


	private static String getHDF5GenoFromMarkerList(boolean markerFast, String errorFile, String tempFolder,String posFile) throws FileNotFoundException{
		BufferedReader br=new BufferedReader(new FileReader(posFile));
        StringBuilder genoFileString=new StringBuilder();
		try{
		br.readLine();//header
		while(br.ready()) {
			String[] line = br.readLine().split("\t");
			if(line.length < 2){
				ErrorLogger.logDebug("MarkerList","Skipping line " + Arrays.deepToString(line));
				continue;
			}
			int dsID=Integer.parseInt(line[0]);
			String positionList=line[1].replace(',','\n');
			String positionListFileLoc=tempFolder+"position.list";
			FileSystemInterface.rmIfExist(positionListFileLoc);
			FileWriter w = new FileWriter(positionListFileLoc);
			w.write(positionList);
			w.close();
            String genoFile=getHDF5Genotype(markerFast, errorFile,dsID,tempFolder,positionListFileLoc);
            genoFileString.append(" "+genoFile);
		}
		}catch(IOException e) {
			ErrorLogger.logError("GobiiExtractor", "MarkerList reading failed", e);
		}

		//Coallate genotype files
		String genoFile=tempFolder+"markerList.genotype";
		logDebug("MarkerList", "Accumulating markers into final genotype file");
		String genotypePartFileIdentifier=genoFileString.toString();
		if(markerFast) {
			tryExec("paste" + genotypePartFileIdentifier, genoFile, errorFile);
		}
		else{
			tryExec("cat" + genotypePartFileIdentifier, genoFile, errorFile);
		}
		return genoFile;
	}

	private static String getHDF5Genotype( boolean markerFast, String errorFile, Integer dataSetId, String tempFolder) {
		return getHDF5Genotype( markerFast, errorFile,dataSetId,tempFolder,null);
	}

	private static String getHDF5Genotype( boolean markerFast, String errorFile, Integer dataSetId, String tempFolder, String markerList) {
		String genoFile=tempFolder+"DS-"+dataSetId+".genotype";

		String HDF5File=pathToHDF5Files+"DS_"+dataSetId+".h5";
		// %s <orientation> <HDF5 file> <output file>
		String ordering="samples-fast";
		if(markerFast)ordering="markers-fast";

		logDebug("Extractor","HDF5 Ordering is "+ordering);

		if(markerList!=null) {
			String hdf5Extractor=pathToHDF5+"fetchmarkerlist";
			ErrorLogger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+HDF5File+" "+markerList+" "+genoFile);
			HelperFunctions.tryExec(hdf5Extractor + " " +HDF5File+" "+markerList+" "+genoFile, null, errorFile);
		}
		else {
			String hdf5Extractor=pathToHDF5+"dumpdataset";
			ErrorLogger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile);
			HelperFunctions.tryExec(hdf5Extractor + " " + ordering + " " + HDF5File + " " + genoFile, null, errorFile);
		}
		ErrorLogger.logDebug("Extractor",(ErrorLogger.success()?"Success ":"Failure " +"Extracting with "+ordering+" "+HDF5File+" "+genoFile));
		return genoFile;
	}

	private static String flapjackExtract(boolean success, ConfigSettings configuration, GobiiExtractorInstruction inst, GobiiDataSetExtract extract, String extractDir, String markerFile, String extendedMarkerFile, String sampleFile, String chrLengthFile, String errorFile, Integer dataSetId, String tempFolder, String genoFile) {
		String genoOutFile=extractDir+"DS"+dataSetId+".genotype";
		String mapOutFile=extractDir+"DS"+dataSetId+".map";
		lastErrorFile=errorFile;
		//Always regenerate requests - may have different parameters
		FlapjackTransformer.generateMapFile(extendedMarkerFile, sampleFile, chrLengthFile, dataSetId, tempFolder, mapOutFile, errorFile);
		HelperFunctions.sendEmail(extract.getDataSetName()+ " Map Extract", mapOutFile, success&& ErrorLogger.success(), errorFile, configuration, inst.getContactEmail());
		FlapjackTransformer.generateGenotypeFile(markerFile, sampleFile, genoFile, dataSetId, tempFolder, genoOutFile,errorFile);
		return genoOutFile;
	}

	private static String hapmapExtract(String extractorScriptPath, String extractDir, String extendedMarkerFile, String sampleFile, String projectFile, String chrLengthFile, String errorFile, Integer dataSetId, String genoFile) {
		String hapmapOutFile = extractDir+"DS"+dataSetId+".hmp.txt";
		try{
            System.out.println("Executing HapMap creation");
            String hapmapTransform="python "+extractorScriptPath+"HapmapExtractor.py"+
                    " -k "+extendedMarkerFile+
                    " -s "+sampleFile+
                    " -p "+projectFile+
                    " -m "+genoFile+
                    " -o "+hapmapOutFile;
            //HapmapTransformer.generateFile(markerFile,sampleFile,projectFile,tempFolder,hapmapOutFile,errorFile);
            HelperFunctions.tryExec(hapmapTransform, null, errorFile);
            rm(genoFile);
            rmIfExist(chrLengthFile);
        }catch(Exception e){
            ErrorLogger.logError("Extractor","Exception in HapMap creation",e);
        }
		return hapmapOutFile;
	}

	public static List<GobiiExtractorInstruction> parseExtractorInstructionFile(String filename){
		ObjectMapper objectMapper = new ObjectMapper();
		GobiiExtractorInstruction[] file = null;
		 
		try {
			file = objectMapper.readValue(new FileInputStream(filename), GobiiExtractorInstruction[].class);
		} catch (Exception e) {
			ErrorLogger.logError("Extractor","ObjectMapper could not read instructions",e);
		}
		if(file==null)return null;
		return Arrays.asList(file);
	}
	
	private static String divineCrop(String instructionFile) {
		String upper=instructionFile.toUpperCase();
		String crop = null;
		for(GobiiCropType c:GobiiCropType.values()){
				if(upper.contains(c.toString())){
					crop=c.name();
					break;
				}
		}
		return crop;
	}
	
	private static String getLogName(GobiiExtractorInstruction gli, CropConfig config, Integer dsid) {
		return getLogName(gli.getDataSetExtracts().get(0),config,dsid);
	 }

	private static String getLogName(GobiiDataSetExtract gli, CropConfig config, Integer dsid) {
		String cropName=config.getGobiiCropType();
		String destination=gli.getExtractDestinationDirectory();
		return destination +"/"+cropName+"_DS-"+dsid+".log";
	}
}
