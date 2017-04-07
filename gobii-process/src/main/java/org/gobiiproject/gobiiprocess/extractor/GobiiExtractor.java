package org.gobiiproject.gobiiprocess.extractor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.cli.*;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.ClientContext;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.common.RestResourceUtils;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCInstructionsDTO;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.email.MailInterface;
import org.gobiiproject.gobiimodel.utils.email.QCMessage;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiiprocess.extractor.flapjack.FlapjackTransformer;
import org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapTransformer;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.mv;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.*;
import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.*;

public class GobiiExtractor {
	//Paths
	private static String  pathToHDF5, propertiesFile,pathToHDF5Files;
	private static UriFactory uriFactory;
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
                if(filterType==null) filterType=GobiiExtractFilterType.WHOLE_DATASET;
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

						//Kevin's example
						/*
						python gobii_mde.py
						-c postgresql://loaderusr:loaderusr@localhost:5432/extraction_test
						--extractByDataset -m /Users/KevinPalis/Work/Datafiles/MDE_Output/marker_meta7.txt -s /Users/KevinPalis/Work/Datafiles/MDE_Output/sample_meta7.txt -p /Users/KevinPalis/Work/Datafiles/MDE_Output/project_meta7.txt -b /Users/KevinPalis/Work/Datafiles/MDE_Output/mapset_meta7.txt -v -l -D 3 -d 5
						 */
						gobiiMDE = "python "+ mdePath+
								" -c " + HelperFunctions.getPostgresConnectionString(cropConfig) +
								" --extractByDataset"+
								" -m " + markerFile +
								" -b " + mapsetFile +
								" -s " + sampleFile +
								" -p " + projectFile +
								(mapId==null?"":(" -D "+mapId))+
								" -d " + extract.getDataSetId() +
								" -l -v ";
						break;
					case BY_MARKER:
						/**
						 * Kevin's example
						 * python gobii_mde.py -c postgresql://loaderusr:loaderusr@localhost:5432/extraction_test --extractByMarkers -m /Users/KevinPalis/Work/Datafiles/MDE_Output/marker_meta8.txt -s /Users/KevinPalis/Work/Datafiles/MDE_Output/sample_meta8.txt -b /Users/KevinPalis/Work/Datafiles/MDE_Output/mapset_meta8.txt  --platformList 5,7 --datasetType 165 -v -l -D 2
						 */

						//Build optional parameter strings
						String platformTerm,mapIdTerm,markerListLocation;
						platformTerm=mapIdTerm=markerListLocation="";
						if(mapId!=null) {
							mapIdTerm=" -D "+mapId;
						}
						List<Integer> platforms=extract.getPlatformIds();
						if(platforms!=null && !platforms.isEmpty()){
							platformTerm=" --platformList " + commaFormat(platforms);
						}
						//List takes extra work, as it might be a <List> or a <File>
						//Create a file out of the List if non-null, else use the <File>
						List<String> markerList=extract.getMarkerList();
						if(markerList!=null && !markerList.isEmpty()){
							markerListLocation=" -x "+createTempFileForMarkerList(extractDir,markerList);
						}
						else if(extract.getListFileName()!=null){
							markerListLocation=" -x "+extractDir+extract.getListFileName();
						}
						//else if file is null and list is empty or null - > no term

						if(markerListOverrideLocation!=null) markerListLocation=" -x "+ markerListOverrideLocation;

						//Actually call the thing
						gobiiMDE = "python "+ mdePath+
								" -c " + HelperFunctions.getPostgresConnectionString(cropConfig) +
								" --extractByMarkers"+
								" -m " + markerFile +
								" -b " + mapsetFile +
								" -s " + sampleFile +
								" -p " + projectFile +
                                markerListLocation +
								" --datasetType " + extract.getGobiiDatasetType().getName() +
								mapIdTerm +
								platformTerm +
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

				String genoFile=tempFolder+"DS-"+dataSetId+".genotype";
				String hdf5Extractor=pathToHDF5+"dumpdataset";
				String HDF5File=pathToHDF5Files+"DS_"+dataSetId+".h5";
				// %s <orientation> <HDF5 file> <output file>
				boolean markerFast=false;
				if(extract.getGobiiFileType()==GobiiFileType.HAPMAP)markerFast=true;
				String ordering="samples-fast";
				if(markerFast)ordering="markers-fast";
				ErrorLogger.logDebug("Extractor","HDF5 Ordering is "+ordering);
				ErrorLogger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile);
				HelperFunctions.tryExec(hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile,null,errorFile);
				success&=ErrorLogger.success();
				ErrorLogger.logDebug("Extractor",(success?"Success ":"Failure " + hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile));
				// Adding "/" back to the bi-allelic data made from HDF5
				if (extract.getGobiiDatasetType() != null) {
					if (extract.getGobiiDatasetType().getName().toLowerCase().equals("ssr_allele_size")) {
						ErrorLogger.logInfo("Extractor","Adding slashes to bi allelic data in " + genoFile);
						if (addSlashesToBiAllelicData(genoFile, extractDir, extract)) {
							ErrorLogger.logInfo("Extractor","Added slashes to all the bi-allelic data in " + genoFile);
						} else {
							ErrorLogger.logError("Extractor","Not added slashes to all the bi-allelic data in " + genoFile);
						}
					}
				}

				switch(extract.getGobiiFileType()){

					case FLAPJACK:
						String genoOutFile=extractDir+"DS"+dataSetId+".genotype";
						String mapOutFile=extractDir+"DS"+dataSetId+".map";
						lastErrorFile=errorFile;
						//Always regenerate requests - may have different parameters
						boolean extended=new File(extendedMarkerFile).exists();
						FlapjackTransformer.generateMapFile(extended?extendedMarkerFile:markerFile, sampleFile, chrLengthFile, dataSetId, tempFolder, mapOutFile, errorFile,extended);
						HelperFunctions.sendEmail(extract.getDataSetName()+ " Map Extract", mapOutFile, success&&ErrorLogger.success(), errorFile, configuration, inst.getContactEmail());
						FlapjackTransformer.generateGenotypeFile(markerFile, sampleFile, genoFile, dataSetId, tempFolder, genoOutFile,errorFile);
						HelperFunctions.sendEmail(extract.getDataSetName()+ " Genotype Extract", genoOutFile, success&&ErrorLogger.success(), errorFile, configuration, inst.getContactEmail());
						HelperFunctions.completeInstruction(instructionFile,configuration.getProcessingPath(crop, GobiiFileProcessDir.EXTRACTOR_DONE));
						break;
					case HAPMAP:
						String hapmapOutFile = extractDir+"DS"+dataSetId+".hmp.txt";
						HapmapTransformer hapmapTransformer = new HapmapTransformer();
						ErrorLogger.logDebug("GobiiExtractor","Executing Hapmap Generation");
						success &= hapmapTransformer.generateFile(markerFile, sampleFile, extendedMarkerFile, genoFile, hapmapOutFile, errorFile);
						HelperFunctions.sendEmail(extract.getDataSetName()+" Hapmap Extract",hapmapOutFile,success&&ErrorLogger.success(),errorFile,configuration,inst.getContactEmail());
						HelperFunctions.completeInstruction(instructionFile,configuration.getProcessingPath(crop, GobiiFileProcessDir.EXTRACTOR_DONE));
						break;
                    case META_DATA:
                        HelperFunctions.sendEmail(extract.getDataSetName()+" Metadata Extract",extractDir,success&&ErrorLogger.success(),errorFile,configuration,inst.getContactEmail());
                        HelperFunctions.completeInstruction(instructionFile,configuration.getProcessingPath(crop, GobiiFileProcessDir.EXTRACTOR_DONE));
                        break;
                    default:
						ErrorLogger.logError("Extractor","Unknown Extract Type "+extract.getGobiiFileType());
						HelperFunctions.sendEmail(extract.getDataSetName()+" "+extract.getGobiiFileType()+" Extract",null,false,errorFile,configuration,inst.getContactEmail());
				}
				rmIfExist(genoFile);
				rmIfExist(chrLengthFile);
				rmIfExist(markerPosFile);
				rmIfExist(extendedMarkerFile);
				ErrorLogger.logDebug("Extractor","DataSet "+dataSetId+" Created");

				//QC - Subsection #1 of 1
				if (inst.isQcCheck()) {
					ErrorLogger.logInfo("Extractor", "qcCheck detected");
					ErrorLogger.logInfo("Extractor", "Entering into the QC Subsection #1 of 1...");
					QCInstructionsDTO qcInstructionsDTOToSend = new QCInstructionsDTO();
					qcInstructionsDTOToSend.setContactId(inst.getContactId());
					qcInstructionsDTOToSend.setDataFileDirectory(configuration.getProcessingPath(crop, GobiiFileProcessDir.QC_INSTRUCTIONS));
					qcInstructionsDTOToSend.setDataFileName(new StringBuilder("qc_")
							.append(DateUtils.makeDateIdString()).toString());
					qcInstructionsDTOToSend.setDatasetId(dataSetId);
					qcInstructionsDTOToSend.setGobiiJobStatus(GobiiJobStatus.STARTED);
					qcInstructionsDTOToSend.setQualityFileName("");
					PayloadEnvelope<QCInstructionsDTO> payloadEnvelope = new PayloadEnvelope<>(qcInstructionsDTOToSend, GobiiProcessType.CREATE);
					ClientContext clientContext = ClientContext.getInstance(configuration, crop);
					SystemUserDetail SystemUserDetail = (new SystemUsers()).getDetail(SystemUserNames.USER_READER.toString());
					if (!(clientContext.login(SystemUserDetail.getUserName(), SystemUserDetail.getPassword()))) {
						ErrorLogger.logError("Digester", "Login Error:" + SystemUserDetail.getUserName() + "-" + SystemUserDetail.getPassword());
						return;
					} else {
						String currentCropContextRoot = clientContext.getInstance(null, false).getCurrentCropContextRoot();
						uriFactory = new UriFactory(currentCropContextRoot);
						GobiiEnvelopeRestResource<QCInstructionsDTO> restResourceForPost = new GobiiEnvelopeRestResource<QCInstructionsDTO>(uriFactory.resourceColl(ServiceRequestId.URL_FILE_QC_INSTRUCTIONS));
						PayloadEnvelope<QCInstructionsDTO> qcInstructionFileDTOResponseEnvelope = restResourceForPost.post(QCInstructionsDTO.class,
								payloadEnvelope);
						if (qcInstructionFileDTOResponseEnvelope != null) {
							ErrorLogger.logInfo("Extractor", "QC Instructions Request Sent");
						} else {
							ErrorLogger.logError("Extractor", "Error Sending QC Instructions Request");
						}

						String currentQCContextRoot = clientContext.getInstance(null, false).getCurrentQCContextRoot();
						String userToken = clientContext.getInstance(null, false).getUserToken();
						Long qcJobId = getQCStart(currentQCContextRoot, userToken, dataSetId, extractDir);
						if (qcJobId != null) {
							ErrorLogger.logInfo("Extractor", "Launched qcJobId: " + qcJobId);
							MailInterface mailInterface = new MailInterface(configuration);
							QCMessage qcMessage = new QCMessage();
							qcMessage.qcStart(inst.getContactEmail(), dataSetId, qcJobId);
							mailInterface.send(qcMessage);

							String currentCropDomain = clientContext.getInstance(null, false).getCurrentCropDomain();
							Integer currentCropPort = clientContext.getInstance(null, false).getCurrentCropPort();
							StringBuilder kDCComputeUrl = new StringBuilder("http://")
									.append(currentCropDomain)
									.append(":")
									.append(currentCropPort)
									.append(currentQCContextRoot);

							String qcNotificationsSubdirectory = configuration.getProcessingPath(crop, GobiiFileProcessDir.QC_NOTIFICATIONS);
							String newQCDataDirectory = new StringBuilder("ds_")
									.append(dataSetId)
									.append("_")
									.append(DateUtils.makeDateIdString()).toString();
							Path downloadDirectoryPath = Paths.get(qcNotificationsSubdirectory, newQCDataDirectory);
							File downloadDirectory = new File(downloadDirectoryPath.toString());
							downloadDirectory.mkdir();

							String qcStatus = getQCStatus(currentQCContextRoot, clientContext.getUserToken(), qcJobId, kDCComputeUrl.toString(), downloadDirectoryPath.toString());
							if (qcStatus != null) {
								// Liz wants to have a copy of all the extraction data in the QC output data directory
								String[] extractDirectoryFiles = new File(extractDir).list();
								for (String file : extractDirectoryFiles) {
									InputStream fileInputStream = new FileInputStream(new File(extractDir, file));
									OutputStream fileOutputStream = new FileOutputStream(new File(downloadDirectoryPath.toString(), file));
									byte[] buffer = new byte[1024];
									int length;
									while ((length = fileInputStream.read(buffer)) > 0){
										fileOutputStream.write(buffer, 0, length);
									}
									fileInputStream.close();
									fileOutputStream.close();
									ErrorLogger.logInfo("Extractor", new StringBuilder("File copied ")
											.append(file)
											.append(" from ")
											.append(extractDir)
											.append(" to ")
											.append(downloadDirectoryPath.toString()).toString());
								}
								ErrorLogger.logInfo("Extractor", new StringBuilder("The status of the QC job #")
										.append(qcJobId)
										.append(" is ")
										.append(qcStatus).toString());
								boolean qcSuccess = qcStatus.equals("COMPLETED");
								qcMessage.qcStatus(inst.getContactEmail(), qcJobId, qcSuccess, downloadDirectoryPath.toString());
								mailInterface.send(qcMessage);
							} else {
								ErrorLogger.logError("Extractor", "Error in getting the status of a QC job");
							}
						} else {
							ErrorLogger.logError("Extractor", "Error in starting a new QC job");
						}

					}
					ErrorLogger.logInfo("Extractor","Done with the QC Subsection #1 of 1!");
				}
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

	/**
	 * If marker list is null, do a dataset extract. Else, do a marker list extract on the dataset
	 * @param markerFast
	 * @param errorFile
	 * @param dataSetId Dataset ID to be pulled from
	 * @param tempFolder
	 * @param markerList nullable - determines what markers to extract. List of marker positions, comma separated
	 * @return
	 */
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

	/**
	 * Determine crop type by looking at the intruction file's location for the name of a crop.
	 * @param instructionFile
	 * @return GobiiCropType
	 */
	private static String divineCrop(String instructionFile) {
		String upper=instructionFile.toUpperCase();
		String from="/CROPS/";
		int fromIndex=upper.indexOf(from)+from.length();
		String crop=upper.substring(fromIndex,upper.indexOf('/',fromIndex));
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

	/**
	 * Brute force method to create a list of object,object,object,object
	 * @param inputList Nonempty list of elements
	 * @return the string value of elements comma separated
	 */
	//Dear next guy - yeah, doing a 'one step unroll' then placing in 'comma - object; comma - object' makes more sense.
	//Just be happy I used a StringBuilder
	private static String commaFormat(List inputList){
		StringBuilder sb=new StringBuilder();
		for(Object o:inputList){
			sb.append(o.toString());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length()-1);//Remove final comma
		return sb.toString();
	}

	/**
	 * Turns a list into a newline delimited file.
	 * @param tmpDir File path - will append 'markerList.tmp' and return
	 * @param markerList List to go into file, newline delimited
	 * @return location of new file.
	 */
	private static String createTempFileForMarkerList(String tmpDir,List<String> markerList){
		String tempFileLocation=tmpDir+"markerList.tmp";
		try {
			FileWriter f = new FileWriter(tempFileLocation);
			for(String marker:markerList){
				f.write(marker);
				f.write("\n");
			}
			f.close();
		}
		catch(Exception e){
			ErrorLogger.logError("Extractor","Could not create temp file "+tempFileLocation,e);
		}
		return tempFileLocation;
	}

	private static boolean addSlashesToBiAllelicData(String genoFile, String extractDir, GobiiDataSetExtract extract) throws Exception {
		Path SSRFilePath = Paths.get(genoFile);
		File SSRFile = new File(SSRFilePath.toString());
		if (SSRFile.exists()) {
			Path AddedSSRFilePath = Paths.get(extractDir, (new StringBuilder ("Added")).append(SSRFilePath.getFileName()).toString());
			// Deleting any temporal file existent
			rmIfExist(AddedSSRFilePath.toString());
			File AddedSSRFile = new File(AddedSSRFilePath.toString());
			if (AddedSSRFile.createNewFile()) {
				Scanner scanner = new Scanner(new FileReader(SSRFile));
				FileWriter fileWriter = new FileWriter(AddedSSRFile);
				// Copying the header
				if (scanner.hasNextLine()) {
					fileWriter.write((new StringBuilder (scanner.nextLine())).append(System.lineSeparator()).toString());
				}
				else {
					ErrorLogger.logError("Extractor", "Genotype file emtpy");
					return false;
				}
				if (!(scanner.hasNextLine())) {
					ErrorLogger.logError("Extractor", "No genotype data");
					return false;
				}
				Pattern pattern = Pattern.compile("^[0-9]{1,8}$");
				while (scanner.hasNextLine()) {
					String[] lineParts = scanner.nextLine().split("\\t");
					// The first column does not need to be converted
					StringBuilder addedLineStringBuilder = new StringBuilder(lineParts[0]);
					// Converting each column from the next ones
					for (int index = 1; index < lineParts.length; index++) {
						addedLineStringBuilder.append("\t");
						if (!(pattern.matcher(lineParts[index]).find())) {
							ErrorLogger.logError("Extractor","Incorrect SSR allele size format (1): " + lineParts[index]);
							addedLineStringBuilder.append(lineParts[index]);
						}
						else {
							if ((5 <= lineParts[index].length()) && (lineParts[index].length() <= 8)) {
								int leftNumber = Integer.parseInt(lineParts[index].substring(0, lineParts[index].length() - 4));
								int rightNumber = Integer.parseInt(lineParts[index].substring(lineParts[index].length() - 4));
								if ((leftNumber == 0) && (rightNumber == 0)) {
									addedLineStringBuilder.append("N/N");
								}
								else {
									addedLineStringBuilder.append(leftNumber);
									addedLineStringBuilder.append("/");
									addedLineStringBuilder.append(rightNumber);
								}
							}
							else {
								if ((1 <= lineParts[index].length()) && (lineParts[index].length() <= 4)) {
									int number = Integer.parseInt(lineParts[index]);
									if (number == 0) {
										addedLineStringBuilder.append("N/N");
									}
									else {
										addedLineStringBuilder.append("0/");
										addedLineStringBuilder.append(number);
									}
								}
								else {
										ErrorLogger.logError("Extractor","Incorrect SSR allele size format (2): " + lineParts[index]);
										addedLineStringBuilder.append(lineParts[index]);
								}
							}
						}
					}
					addedLineStringBuilder.append(System.lineSeparator());
					fileWriter.write(addedLineStringBuilder.toString());
				}
				scanner.close();
				fileWriter.close();
				// Deleting any original data backup file existent
				rmIfExist(Paths.get(SSRFilePath.toString() + ".bak").toString());
				// Backing up the original data file
				mv(SSRFilePath.toString(), SSRFilePath.toString() + ".bak");
				// Renaming the converted data file to the original data file name
				mv(AddedSSRFilePath.toString(), SSRFilePath.toString());
			}
			else {
				ErrorLogger.logError("Extractor","Unable to create the added SSR file: "+AddedSSRFilePath.toString());
				return false;
			}
		} else {
			ErrorLogger.logError("Extractor", "No genotype file: " + SSRFilePath.toString());
			return false;
		}

		return true;
	}

	private static Long getQCStart(String currentQCContextRoot, String userToken, int datasetId, String directory) {
		Long jobId = null;
		try {
			UriFactory uriFactory = new UriFactory(currentQCContextRoot);
			RestUri restUri = uriFactory.qcStart();
			restUri.setParamValue("datasetId", String.valueOf(datasetId));
			restUri.setParamValue("directory", directory);
			RestResourceUtils restResourceUtils = new RestResourceUtils();
			HttpMethodResult httpMethodResult = restResourceUtils.getHttp().get(restUri, userToken);
			JsonObject jsonObject = httpMethodResult.getPayLoad();
			jobId = jsonObject.get("jobId").getAsLong();
		}
		catch (Exception e) {
			ErrorLogger.logError("Extractor","qcStart: " + e.getMessage());
		}

		return jobId;
	}

	private static String getQCStatus(String currentQCContextRoot, String userToken, Long qcJobId, String kDComputeUrlIn, String downloadDirectory) {
		String status = null;
		try {
			UriFactory uriFactory = new UriFactory(currentQCContextRoot);
			RestUri restUri = uriFactory.qcStatus();
			restUri.setParamValue("jobid", String.valueOf(qcJobId));
			RestResourceUtils restResourceUtils = new RestResourceUtils();
			JsonObject payLoad = null;
			do {
				// Let's give a bit processing time to the KDCompute before (re-)sending a qcStatus request
				try {
					Thread.sleep(4000);
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
					ErrorLogger.logError("Extractor","qcStatus: " + ex.getMessage());
				}
				HttpMethodResult httpMethodResult = restResourceUtils.getHttp().get(restUri, userToken);
				payLoad = httpMethodResult.getPayLoad();
				status = payLoad.get("status").getAsString();
			} while ((status.equals("NEW")) || (status.equals("RUNNING")));
			if ((status.equals("COMPLETED")) || (status.equals("FAILED"))) {
				JsonObject resultsUrls = payLoad.get("resultsUrls").getAsJsonObject();
				Set<Map.Entry<String, JsonElement>> entrySet = resultsUrls.entrySet();
				for (Map.Entry<String,JsonElement> entry : entrySet) {
					StringBuilder kDComputeUrl = new StringBuilder(kDComputeUrlIn);
					String downloadLink = entry.getValue().getAsString().substring(1);
					kDComputeUrl.append(downloadLink);
					if (!(getQCDownload(kDComputeUrl.toString(), downloadDirectory))) {
						throw (new Exception("Error in QC file download"));
					}
				}
			}
			else {
				// The status has three possible values here: "CANCELLED", "UNKNOWN", and null
				throw (new Exception(status));
			}
		}
		catch (Exception e) {
			ErrorLogger.logError("Extractor","qcStatus: " + e.getMessage());
		}

		return status;
	}

	private static boolean getQCDownload(String fileURL, String downloadDirectory) throws Exception {
		boolean success = false;
		try {
			URL url = new URL(fileURL);
			HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
			int responseCode = httpConn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String fileName = "";
				String disposition = httpConn.getHeaderField("Content-Disposition");
				String contentType = httpConn.getContentType();
				int contentLength = httpConn.getContentLength();
				if (disposition != null) {
					int index = disposition.indexOf("filename=");
					if (index > 0) {
						fileName = disposition.substring(index + 10,
								disposition.length() - 1);
					}
				}
				else {
					fileName = fileURL.substring(fileURL.lastIndexOf(File.separator) + 1,
							fileURL.length());
				}
				ErrorLogger.logInfo("Extractor","Content-Type = " + contentType);
				ErrorLogger.logInfo("Extractor","Content-Disposition = " + disposition);
				ErrorLogger.logInfo("Extractor","Content-Length = " + contentLength);
				ErrorLogger.logInfo("Extractor","fileName = " + fileName);
				InputStream inputStream = httpConn.getInputStream();
				String downloadFile = downloadDirectory + File.separator + fileName;
				FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);
				int bytesRead = -1;
				byte[] buffer = new byte[4096];
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					fileOutputStream.write(buffer, 0, bytesRead); }
				fileOutputStream.close();
				inputStream.close();
				success = true;
				ErrorLogger.logInfo("Extractor", new StringBuilder("File downloaded: ").append(downloadFile).toString());
			}
			else {
				ErrorLogger.logError("Extractor","No file to download. Server replied HTTP code: " + responseCode);
			}
			httpConn.disconnect();
		}
		catch (Exception e) {
			ErrorLogger.logError("Extractor","qcDownload: " + e.getMessage());
		}

		return success;
	}

	private static void sendQCJobEmail(ConfigSettings configSettings, String contactEmail, int datasetId, Long qcJobID) {
		StringBuilder qcJobName = new StringBuilder("The QC job #")
				.append(qcJobID)
				.append(" targeting the data set #")
				.append(datasetId);
		HelperFunctions.sendEmail(qcJobName.toString(), "", true, "errorLogLoc", configSettings, contactEmail);
	}

	private static void sendQCDataEmail(ConfigSettings configSettings, Long qcJobID, boolean qcSuccess, String qcDataDirectory) {
		QCMessage qcMessage = new QCMessage();
		MailInterface mailInterface = new MailInterface(configSettings);
		StringBuilder body = new StringBuilder("The QC job #")
				 .append(qcJobID)
				 .append(" was ");
		if (qcSuccess) {
			body.append("successful.<br/>");
		}
		else {
			body.append("failed.<br/>");
		}
		body.append("The data of the QC job #")
				.append(qcJobID)
				.append(" are in ")
				.append(qcDataDirectory);
		qcMessage.setBody(body.toString());
		try {
			mailInterface.send(qcMessage);
		} catch (Exception e) {
			ErrorLogger.logError("Extractor", "Error in sending a QC data email");
		}
	}
}
