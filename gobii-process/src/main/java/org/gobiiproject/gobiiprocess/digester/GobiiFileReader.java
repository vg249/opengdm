package org.gobiiproject.gobiiprocess.digester;

import org.apache.commons.cli.*;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.DtoRequestDataSet;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.impl.LoaderInstructionsDAOImpl;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.CropDbConfig;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiiprocess.digester.csv.CSVFileReader;
import org.gobiiproject.gobiiprocess.digester.vcf.VCFFileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.mv;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.parseInstructionFile;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;

/**
 * Base class for processing instruction files. Start of chain of control for Digester. Takes first argument as instruction file, or promts user.
 * @author jdl232 Josh L.S.
 */
public class GobiiFileReader {
	private static String rootDir="../";
	private static String loaderScriptPath,extractorScriptPath,pathToHDF5;
	private static final String VARIANT_CALL_TABNAME="matrix";
	private static final String	LINKAGE_GROUP_TABNAME="linkage_group";
	private static final String GERMPLASM_TABNAME="germplasm";
	private static String pathToHDF5Files;
	private static boolean verbose;
	private static String errorLogOverride;
	private static String propertiesFile;
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, InterruptedException{

		Options o = new Options()
         		.addOption("v", "verbose", false, "Verbose output")
         		.addOption("e", "errlog", true, "Error log override location")
         		.addOption("r", "rootDir", true, "Fully qualified path to gobii root directory")
         		.addOption("c","config",true,"Fully qualified path to gobii configuration file")
         		.addOption("h", "hdfFiles", true, "Fully qualified path to hdf files")
         		;
        
		 CommandLineParser parser = new DefaultParser();
         try{
               CommandLine cli = parser.parse( o, args );
               if(cli.hasOption("rootDir")) rootDir = cli.getOptionValue("rootDir");
               if(cli.hasOption("verbose")) verbose=true;
               if(cli.hasOption("errLog")) errorLogOverride = cli.getOptionValue("errLog");
               if(cli.hasOption("config")) propertiesFile = cli.getOptionValue("config");
               if(cli.hasOption("hdfFiles")) pathToHDF5Files = cli.getOptionValue("hdfFiles");
                args=cli.getArgs();//Remaining args passed through
                
         }catch(org.apache.commons.cli.ParseException exp ) {
             new HelpFormatter().printHelp("java -jar Digester.jar ","Also accepts input file directly after arguments\n"
                		+ "Example: java -jar Digester.jar -c /home/jdl232/customConfig.properties -v /home/jdl232/testLoad.json",o,null,true);
              
               System.exit(2);
         }
		
		
		
     	extractorScriptPath=rootDir+"extractors/";
     	loaderScriptPath=rootDir+"loaders/";
     	pathToHDF5=loaderScriptPath+"hdf5/bin/";
    	
    	if(propertiesFile==null)propertiesFile=rootDir+"config/gobii-web.properties";
		
		
		
		boolean success=true;
		Map<String,File> loaderInstructionMap = new HashMap<>();//Map of Key to filename
		List<String> loaderInstructionList=new ArrayList<String>(); //Ordered list of loader instructions to execute, Keys to loaderInstructionMap
		DataSetType dst=null;
		DataSetOrientationType dso=null;
		
		ConfigSettings configuration=null;
		try {
			configuration = new ConfigSettings(propertiesFile);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		String instructionFile=null;
		if(args.length==0 ||args[0]==""){
			Scanner s=new Scanner(System.in);
			System.out.println("Enter Loader Instruction File Location:");
			instructionFile=s.nextLine();
		    if(instructionFile.equals("")) instructionFile="../loaders/etc/jdl232_01_pretty.json";
		    s.close();
		}
		else{
			instructionFile=args[0];
		}
		
		//Error logs go to a file based on crop (for human readability) and 
		
		List<GobiiLoaderInstruction> list= parseInstructionFile(instructionFile);
		if(list==null || list.isEmpty()){
			System.err.println("No instruction for file "+instructionFile);
			success=false;
			return;
		}
		GobiiLoaderInstruction zero=list.iterator().next();
		Integer dataSetId=zero.getDataSetId();
		GobiiCropType crop=zero.getGobiiCropType();
		if(crop==null) crop=divineCrop(instructionFile);
		CropConfig cropConfig=configuration.getCropConfig(crop);	
		if(pathToHDF5Files==null)pathToHDF5Files=rootDir+"crops/"+crop.toString().toLowerCase()+"/hdf5/";

		
		String errorPath=getLogName(zero,cropConfig,crop);
		String jobFile=zero.getGobiiFile().getDestination();
		
		
		//TODO: HACK - Job's name is 
		String jobName = getJobName(crop,list);
		String jobUser=zero.getContactEmail();
		
		System.out.println("Beginning List Processing");
		success=true;
		for(GobiiLoaderInstruction inst:list){
			if(inst==null){
				System.err.println("Missing or malformed instruction in " + instructionFile );
				continue;
			}
			if(dataSetId==null){
				dataSetId=inst.getDataSetId();//Pick it up from relevant instruction
			}
			String destinationFile=HelperFunctions.getDestinationFile(inst);//Intermediate file
			//Bah, I don't care if this fails, not adding to 'success'
			HelperFunctions.tryExec("chmod -R 777 " +destinationFile.substring(0, destinationFile.lastIndexOf("/")));//TODO: This is here for permissions issues - also lastindexof is wrong
			GobiiFile file = inst.getGobiiFile();
			if(file==null){
				System.err.println("Instruction " + instructionFile + " Table " + inst.getTable() + " has bad 'file' column" );
				continue;
			}
			GobiiFileType instructionFileType = file.getGobiiFileType();
			if(instructionFileType==null){
				System.err.println("Instruction " + instructionFile + " Table " + inst.getTable() + " has missing filetype" );
				continue;
			}
			
			
			switch(inst.getGobiiFile().getGobiiFileType()){
			case VCF:
				success&=VCFFileReader.parseInstruction(inst);
				break;
			case GENERIC:
				String dstDir=destinationFile;
				if(!new File(destinationFile).isDirectory()){
					dstDir=destinationFile.substring(0, destinationFile.lastIndexOf("/"));
				}
				CSVFileReader reader = new CSVFileReader(dstDir,"/");
				success&=reader.processCSV(inst);
				break;
			case HAPMAP:
				String tmpFile=inst.getGobiiFile().getSource()+list.indexOf(inst);
				ArrayList<GobiiLoaderInstruction> justTheOne=new ArrayList<GobiiLoaderInstruction>();
				justTheOne.add(inst);
				try {
					new LoaderInstructionsDAOImpl().writeInstructions(tmpFile, justTheOne );
				} catch (GobiiDaoException e) {
					e.printStackTrace();
					success=false;
				}
				success&=HelperFunctions.tryExec(loaderScriptPath+"etc/parse_hmp.pl"+" "+tmpFile, null, errorPath);
				break;
			default:
				System.err.println("Unable to deal with file type " + inst.getGobiiFile().getGobiiFileType());
				break;
			}
			
			//Check if columns need to be translated
			for(GobiiFileColumn gfc:inst.getGobiiFileColumns()){
				if(gfc.getDataSetType()!=null){
					dst=gfc.getDataSetType();
					if(gfc.getDataSetOrientationType()!=null)dso=gfc.getDataSetOrientationType();
					break;
				}
			}
			if(dst!=null && inst.getTable().equals(VARIANT_CALL_TABNAME)){
				errorPath=getLogName(inst, cropConfig, crop, "Matrix_Processing");
				String function=null;
				boolean functionStripsHeader=false;
				String fromFile=HelperFunctions.getDestinationFile(inst);
				String toFile=HelperFunctions.getDestinationFile(inst)+".2";
				switch(dst){		
				case NUCLEOTIDE_2_LETTER:
						function="python "+loaderScriptPath+"etc/SNPSepRemoval.py";
						functionStripsHeader=true;
						break;
					case IUPAC:
						function=loaderScriptPath+"etc/IUPACmatrix_to_bi.pl tab";
						break;
					case SSR_ALLELE_SIZE:
						//No Translation Needed. Done before GOBII
						break;
					case DOMINANT_NON_NUCLEOTIDE:
						//No Translation Needed. Done before GOBII
						break;
					case CO_DOMINANT_NON_NUCLEOTIDE:
						//No Translation Needed. Done before GOBII
						break;
					default:System.err.println("Unknown type "+dst.toString());break;
				}
				
				if(function!=null){
					//Try running script (from -> to), then replace original file with new one.
					success&=HelperFunctions.tryExec(function+" "+fromFile+" "+toFile,null,errorPath);
					rm(fromFile);
					
				}
				else{
					mv(fromFile,toFile);
				}
				
				//toFile now contains data, we move it back to original position with second transformation (swap)
				
				if(!functionStripsHeader){	
					success&=HelperFunctions.tryExec("tail -n +2 ",fromFile,errorPath,toFile);
					rm(toFile);
				}else{
					success&=HelperFunctions.tryExec("mv "+toFile+" "+fromFile);
				}
				
				boolean isSampleFast=false;
				if(DataSetOrientationType.SAMPLE_FAST.equals(dso))isSampleFast=true;
				if(isSampleFast){
					//Rotate to marker fast before loading it
					HelperFunctions.tryExec("python "+loaderScriptPath+"TransposeMatrix.py -i " + fromFile);
				}
				
			}
			
			String instructionName=inst.getTable();
			loaderInstructionMap.put(instructionName, new File(HelperFunctions.getDestinationFile(inst)));
			loaderInstructionList.add(instructionName);//TODO Hack - for ordering
			if(LINKAGE_GROUP_TABNAME.equals(instructionName)||GERMPLASM_TABNAME.equals(instructionName)){
				success&=HelperFunctions.tryExec(loaderScriptPath+"LGduplicates.py -i "+HelperFunctions.getDestinationFile(inst));
			}
		}
		
		if(success){

			errorPath=getLogName(zero, cropConfig, crop, "IFLs");
			HelperFunctions.printDoneFile(instructionFile);
			String pathToIFL=loaderScriptPath+"postgres/gobii_ifl/gobii_ifl.py";
			String outputDir=" -o " + cropConfig.getIntermediateFilesDirectory();
			String connectionString=" -c "+HelperFunctions.getPostgresConnectionString(cropConfig);
			
			//Load PostgreSQL
			for(String key:loaderInstructionList){
				if(!VARIANT_CALL_TABNAME.equals(key)){
				String inputFile=" -i "+loaderInstructionMap.get(key);
				String integrityCheck="";//Kevin - what the heck, you removed a parameter on me
				String outputFile=outputDir;
				System.out.println("Running IFL: "+pathToIFL+connectionString+integrityCheck+inputFile+outputFile);				
				success&=HelperFunctions.tryExec(pathToIFL+connectionString+integrityCheck+inputFile+outputFile,null,errorPath );
				}
			}
			
			//Load Monet/HDF5
			errorPath=getLogName(zero, cropConfig, crop, "Matrix_Upload");
			String variantFilename="DS"+dataSetId;
			File variantFile=loaderInstructionMap.get(VARIANT_CALL_TABNAME);
			String markerFileLoc=pathToHDF5Files+"DS"+dataSetId+".marker_id";
			String sampleFileLoc=pathToHDF5Files+"DS"+dataSetId+".dnarun_id";		
			
		if(variantFile!=null && dataSetId==null){
				System.err.println("Data Set ID is null for variant call");
				success=false;
		}
		if((variantFile!=null)&&dataSetId!=null){	
				String loadVariantMatrix=loaderScriptPath+"monet/loadVariantMatrix.py";
				//python loadVariantMatrix.py <Dataset_Identifier.variant> <Dataset_Identifier.marker_id> <Dataset_Identifier.dnarun_id> <hostname> <port> <dbuser> <dbpass> <dbname>
				CropDbConfig monetConf=cropConfig.getCropDbConfig(GobiiDbType.MONETDB); 
				String loadVariantUserPort=monetConf.getHost()+" "+monetConf.getPort() + " " +monetConf.getUserName()+ " " + monetConf.getPassword() + " " + monetConf.getDbName();
				System.out.println("Not Running MonetDB (Testing Shutoff)");//TODO: Fix Monet DB
				generateIdLists(cropConfig, markerFileLoc, sampleFileLoc, dataSetId, errorPath);
				//success&=HelperFunctions.tryExec("python "+loadVariantMatrix+" "+variantFile.getPath()+" "+markerFileLoc+" "+sampleFileLoc+" "+loadVariantUserPort,null,errorPath);
			
				//HDF-5
				//Usage: %s <datasize> <input file> <output HDF5 file
				String loadHDF5=pathToHDF5+"loadHDF5";
				String HDF5File=pathToHDF5Files+"DS_"+dataSetId+".h5";
				int size=0;
				switch(dst){		
				case NUCLEOTIDE_2_LETTER: case IUPAC:
						size=2;break;
					case SSR_ALLELE_SIZE:size=8;break;
					case CO_DOMINANT_NON_NUCLEOTIDE:
					case DOMINANT_NON_NUCLEOTIDE:size=1;break;
					default:System.err.println("Unknown type "+dst.toString());break;
				}
				System.out.println("Running HDF5 Loader. HDF5 Generating at "+HDF5File);
				success&=HelperFunctions.tryExec(loadHDF5+" "+size+" "+variantFile.getPath()+" "+HDF5File,null,errorPath);
				updateValues(configuration, crop, dataSetId,variantFilename, HDF5File);
			}
			if(success){
				System.out.println("Successfully Uploaded files");
			}
			else{
				System.out.println("Unsuccessfully Uploaded files");
			}
			
			
		}//endif(success)
		else{
			System.out.println("Unsuccessfully Generated files");
		}
		HelperFunctions.sendEmail(jobName, null, success, errorPath, configuration, jobUser,HelperFunctions.getDoneFileAsArray(instructionFile));
	}

	private static String getJobName(GobiiCropType crop, List<GobiiLoaderInstruction> list) {
		String cropName=crop.name();
		cropName=cropName.charAt(0)+cropName.substring(1,cropName.length()).toLowerCase();
		String jobName=cropName + " digest of ";
		String source=list.get(0).getGobiiFile().getSource();
		File sourceFolder=new File(source);
		File[] f=sourceFolder.listFiles();
		if(f.length!=0) source=f[0].getName();
		jobName+=source;
		
		return jobName;
	}
	
	/**
	 * Generates a log file location given a crop name, crop type, and process ID. (Given by the process calling this method).
	 * 
	 * Currently works by placing logs in the intermediate file directory.
	 * @param config Crop configuration
	 * @param crop Crop type
	 * @return The logfile location for this process
	 */
	private static String getLogName(GobiiLoaderInstruction gli,CropConfig config,GobiiCropType crop){
		String cropName=crop.name();
		String destination=gli.getGobiiFile().getDestination();
		String table=gli.getTable();
		return destination +"/"+cropName+"_Table-"+table+".log";
	}
	/**
	 * Generates a log file location given a crop name, crop type, and process ID. (Given by the process calling this method).
	 * 
	 * Currently works by placing logs in the intermediate file directory.
	 * @param config Crop configuration
	 * @param crop Crop type
	 * @return The logfile location for this process
	 */
	private static String getLogName(GobiiLoaderInstruction gli,CropConfig config,GobiiCropType crop, String process){
		String cropName=crop.name();
		String destination=gli.getGobiiFile().getDestination();
		return destination +"/"+cropName+"_Process-"+process+".log";
	}
	/**
	 * Determine crop type by looking at the intruction file's location for the name of a crop.
	 * @param instructionFile
	 * @return GobiiCropType
	 */
	private static GobiiCropType divineCrop(String instructionFile) {
		String upper=instructionFile.toUpperCase();
		GobiiCropType crop = null;
		for(GobiiCropType c:GobiiCropType.values()){
				if(upper.contains(c.toString())){
					crop=c;
					break;
				}
		}
		return crop;
	}

	/**
	 * Generates appropriate monetDB files.
	 * Reason - Raza is weird.
	 * @param cropConfig Connection String
	 * @param markerFile No header
	 * @param dnaRunFile With header
	 * @param dsid Because
	 * @param errorFile We might blow up
	 * @return if we blew up
	 */
	private static boolean generateIdLists(CropConfig cropConfig,String markerFile,String dnaRunFile,int dsid,String errorFile){
		String gobiiIFL="python " + extractorScriptPath+"postgres/gobii_mde/gobii_mde.py"+" -c "+HelperFunctions.getPostgresConnectionString(cropConfig)+
			" -m "+markerFile+".tmp"+
			" -s "+dnaRunFile+".tmp"+
			" -d "+dsid;
		boolean success=true;
		success &=tryExec(gobiiIFL, null, errorFile);
		success &=tryExec("cut -f1 "+markerFile	+".tmp | tail -n +2 > "+markerFile, null, errorFile);
		rm(markerFile+".tmp");
		success &=tryExec("cut -f1 "+dnaRunFile+".tmp > "+dnaRunFile, null, errorFile);
		rm(dnaRunFile+".tmp");
		
		return success;
	}
	public static boolean updateValues(ConfigSettings config,GobiiCropType type, Integer dataSetId,String monetTableName,String hdfFileName) {
		try{
			// set up authentication and so forth
			// you'll need to get the current from the instruction file
			ClientContext context=ClientContext.getInstance(config);
			context.setCurrentClientCrop(type);
			SystemUsers systemUsers = new SystemUsers();
			SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());

			if(! context.login(userDetail.getUserName(), userDetail.getPassword())){
				return false;
			}



			DataSetDTO dataSetRequest = new DataSetDTO(DtoMetaData.ProcessType.READ);

			dataSetRequest.setDataSetId(dataSetId);
			DtoRequestDataSet dtoProcessor = new DtoRequestDataSet();
			DataSetDTO dataSetResponse = dtoProcessor.process(dataSetRequest);

			if (!dataSetResponse.getDtoHeaderResponse().isSucceeded()) {
				System.out.println();
				System.out.println("*** Header errors: ");
				for (HeaderStatusMessage currentStatusMesage : dataSetResponse.getDtoHeaderResponse().getStatusMessages()) {
					System.out.println(currentStatusMesage.getMessage());
				}
				return false;
			}

			dataSetResponse.setDataTable(monetTableName);
			dataSetResponse.setDataFile(hdfFileName);
			dataSetResponse.setProcessType(DtoMetaData.ProcessType.UPDATE);


			dataSetResponse = dtoProcessor.process(dataSetResponse);
			// if you didn't succeed, do not pass go, but do log errors to your log file
			if (!dataSetResponse.getDtoHeaderResponse().isSucceeded()) {
				System.out.println();
				System.out.println("*** Header errors: ");
				for (HeaderStatusMessage currentStatusMesage : dataSetResponse.getDtoHeaderResponse().getStatusMessages()) {
					System.out.println(currentStatusMesage.getMessage());
				}
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
