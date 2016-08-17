package org.gobiiproject.gobiiprocess.digester.vcf;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import org.gobiiproject.gobiimodel.utils.ExternalFunctionCall;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.VcfParameters;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.gobiiproject.gobiimodel.utils.ExternalFunctionCall.extern;
import static org.gobiiproject.gobiimodel.utils.ExternalFunctionCall.sarg;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rm;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.*;
import static org.gobiiproject.gobiimodel.utils.HelperIterators.getConstantIterator;
import static org.gobiiproject.gobiimodel.utils.HelperIterators.getFilteredIterator;

/**
 * 
 * @author Josh L.S.
 *
 */
public class VCFFileReader {
	public static final String OUTPUT_DELIMITER="\t";
	private String fileLocation;
	private String outLocation;
	private List<String> tempFiles = new LinkedList<String>();
	private String tpedFile;
	private String tempVCF;
	public boolean hasError=false;
	private static ExternalFunctionCall mv=extern("mv(unix)","mv");
	private static ExternalFunctionCall cut=extern("cut(unix)","cut");
	private static ExternalFunctionCall rm=extern("rm(unix)","rm");
	
	public static void main(String[] args){
		System.out.println("Test harness " +/*LOL*/ "for VCF FileReader");
		String filename;
		if(args.length==0 ||args[0]==""){
			Scanner s=new Scanner(System.in);
			System.out.println("Enter Instruction File Location:");
			filename=s.nextLine();
			if(filename==null || filename=="") filename="DataLoader/scripts/sample_instruction_2.json";
		}
		else{
			filename=args[0];
		}
		List<GobiiLoaderInstruction> instructions=parseInstructionFile(filename);
		//TODO: Use real loader instructions
		for(GobiiLoaderInstruction instruction:instructions){
			VCFFileReader.parseInstruction(instruction);
		}
	}
	

	public void parseInstructions(List<GobiiLoaderInstruction> instructions){
		for(GobiiLoaderInstruction instruction:instructions){
			parseInstruction(instruction);
		}
	}
	public static boolean parseInstruction(GobiiLoaderInstruction instruction){
		VcfParameters params=instruction.getVcfParameters();
		boolean isSuccess = true;
		String inFile=instruction.getGobiiFile().getSource();
		String outFile=instruction.getGobiiFile().getDestination();
		String errorFile=instruction.getGobiiFile().getDestination()+"/ERRORS";//TODO: Better error file name
		VCFFileReader vfr=new VCFFileReader();
		vfr.setFile(inFile);
		vfr.setOutput(outFile);
		List<Iterator<String>> columnIterators = new LinkedList<>();
		List<String> tempFiles = new LinkedList<>();
		for(GobiiFileColumn c:instruction.getGobiiFileColumns()){
			GobiiColumnType type = c.getGobiiColumnType();
			Iterator<String> iter=null;
			switch(type){
			case VCF_SAMPLE:
				String sampleTmp=outFile+".samples.tmp";
				vfr.saveSampleFile(sampleTmp,null);//Not implementing regular expressions
				tempFiles.add(sampleTmp);
				iter = getFilteredIterator(sampleTmp, c.getFilterFrom(), c.getFilterTo());
				if(iter==null)isSuccess=false;
				columnIterators.add(iter);
				break;
			case VCF_MARKER:
				String markerTmp=outFile+".markers.tmp";
				vfr.saveMarkerFile(markerTmp, null,errorFile);
				tempFiles.add(markerTmp);
				iter = getFilteredIterator(markerTmp, c.getFilterFrom(), c.getFilterTo());
				if(iter==null)isSuccess=false;
				columnIterators.add(iter);
				break;
			case VCF_VARIANT:
				boolean convertToIUPAC=params.isToIupac();
				convertToIUPAC=true;
				String IUPACTemp=outFile+".IUPAC.tmp";

				if(convertToIUPAC){
					vfr.saveIUPACFile(IUPACTemp,errorFile);
					tempFiles.add(IUPACTemp);
					iter = getFilteredIterator(IUPACTemp, c.getFilterFrom(), c.getFilterTo());
					if(iter==null)isSuccess=false;
					columnIterators.add(iter);
				}
				else{
					new Exception("Not implemented: convertToIUPAC=false VCF").printStackTrace();
					isSuccess=false;
				}
				break;
			case VCF_METADATA: //This 'Column' is really a lot of tab-separated columns
				String metadataId=c.getName();
				
				String oFile=vfr.saveMetadataFile(metadataId,"GT",params,errorFile);
				if(oFile==null){
					isSuccess=false;
					break;
				}
				tempFiles.add(oFile);
				iter = getFilteredIterator(oFile, c.getFilterFrom(), c.getFilterTo());
				if(iter==null)isSuccess=false;
				columnIterators.add(iter);
				break;
			//case VCF_INFO:
				//TODO - this
				//break;
			case CONSTANT:
				columnIterators.add(getConstantIterator(c.getConstantValue()));
				break;
			case VCF_MARKER_POS:
				columnIterators.add(getConstantIterator("0"));//TODO: Fix
				break;
			default:
				System.err.println("Cannot process type " + type.toString());
				isSuccess=false;
				break;
			}
			if(!isSuccess)break;
		}
		//Begin processing
		
		boolean done=false;
		try {
			PrintWriter pw=new PrintWriter(new File(vfr.outLocation));
			boolean first=true;
			for(GobiiFileColumn c:instruction.getGobiiFileColumns()){
				pw.write((first?"":OUTPUT_DELIMITER)+c.getName());
				first=false;
				
			}
			while(!done){
				for(Iterator<String> iter:columnIterators){
					if(!iter.hasNext())done=true;break; //If any iterator is empty, we're done
				}
				first=true;
				for(Iterator<String> iter:columnIterators){
					if(done)break;
					pw.write((first?"":OUTPUT_DELIMITER)+iter.next());
					first=false;
				}
				pw.write("\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			isSuccess=false;
		}
	
		for(String file:tempFiles){
			rm(file);
		}
		return ErrorLogger.success();
	}
	

	
	private void setOutput(String out) {
		outLocation=out;
	}

	public boolean setFile(String fileLocation) {
		this.fileLocation=fileLocation;
		File inFile=new File(fileLocation);//Check that the file is real
		if(!inFile.exists()){
			return false;
		}
		if(!inFile.canRead()){
			return false;
		}
		return true;
	}


	
	//////
	// File Digester methods
	//
	//////
	
	


	
	private boolean setTpedFile(String tpedFileStart, String errorFile){
		boolean result = true;
		tryFunc(extern("removePlus","awk/removePlus.awk",fileLocation),tpedFileStart+".noPlus",errorFile);
		tryFunc(extern("plink","plink","--vcf " +tpedFileStart+".noPlus -out " + tpedFileStart + " --recode --transpose --vcf-half-call m"),errorFile);//Creates outFilename.tped
		rm(tpedFileStart+".noPlus");
		
		this.tpedFile=tpedFileStart+".tped";
		this.tempFiles.add(tpedFile);
		return result;
	}

	public void saveMarkerFile(String outFilename, String markerRegex,String errorFile){
		boolean result=true;
		if(tpedFile==null)result = result | setTpedFile(outFilename.substring(0,outFilename.lastIndexOf('.')) + ".tped",errorFile);
		
		tryFunc(sarg(cut,"-f2 -d \" \" "+tpedFile),errorFile); //List of markers		
	}
	
	private void saveTempVCF(String tempVCF,VcfParameters params,String errorFile){
		this.tempVCF=tempVCF;
			
		String filters = getFilterArguments(params);//Note, space padded on both sides
		tryFunc(extern("BCFTools","bcftools"," view"+filters+"-Ov -o "+tempVCF+" "+fileLocation),errorFile);
		tempFiles.add(tempVCF);
		return;
	}
	
	public String getFilterArguments(VcfParameters params) {
		Float minDepth=params.getMinDp();
		Float minQuality=params.getMinQ();
		Float maf=params.getMaf();
		
		String filter=" ";
		String mafTerm=" ";
		if(maf !=null){
			mafTerm = " -q "+maf+" ";
		}
		if(minDepth!=null)filter="MIN(DP)>"+minDepth;
		if(minQuality!=null){
			if(filter.equals(""))filter="QUAL>"+minQuality;
			else filter=filter+" & "+"QUAL>"+minQuality;
		}
		String filterTerm="";
		if(filter !="")filterTerm=" -f \""+filter+"\" ";
		String filters=mafTerm+filterTerm;
		return filters;
	}
	
	private String saveMetadataFile(String outFilename, String metadata, VcfParameters params, String errorFile){
		boolean result=true;
		boolean success=true;
		String metadataFile=outFilename+".metadata."+metadata;
		if(tempVCF==null){
			saveTempVCF(outFilename+".tmp.vcf",params,errorFile);
		}
		tryFunc(extern("grep(unix)","grep","-v \"#\" "+tempVCF),outFilename+".noHeader",errorFile);
		tryFunc(cut.setArgs("-f9 + "+ outFilename+".noHeader"),outFilename+".genoList",errorFile); //List of markers
		tryFunc(cut.setArgs("--complement -f1,2,3,4,5,6,7,8,9 " + tempVCF),outFilename + ".noInfo",errorFile);
		BufferedReader genoList,vcfFile;
		PrintWriter writer;
		try{
			StringBuilder sb=new StringBuilder();
			genoList = new BufferedReader(new FileReader(outFilename+".genoList"));
			vcfFile = new BufferedReader(new FileReader(outFilename+".noHeader"));
			writer=new PrintWriter(metadataFile);
			
			String genoLine=null,vcfLine=null;
			try{
			genoLine=genoList.readLine();
			vcfLine=vcfFile.readLine();
			}catch(Exception e){
			}
			while(genoLine!=null&&vcfLine!=null){	
				int position=0;
				int index=genoLine.indexOf(metadata);
				if(index!=0){
					position=genoLine.substring(0, index-1).split(":").length;//0 to colon
				}
				String[] vcfElements=vcfLine.split("\t");
				for(String s:vcfElements){
					String geno=s.split(":")[position+1];//+1 for the 'genotype' in the beginning. 
					sb.append(s+"\t");
				}
				sb.deleteCharAt(sb.length()-1);//Remove final tab
				writer.println(sb.toString());
				//Get a new line from both files
				genoLine=vcfLine=null;
				try{
					genoLine=genoList.readLine();
					vcfLine=vcfFile.readLine();
				}catch(Exception e){}
			}
			writer.close();
			genoList.close();
			vcfFile.close();
		}catch(Exception e){
			e.printStackTrace();
			result = false;
		}

		rm(outFilename+".noHeader");
		rm(outFilename+".genoList");
		return metadataFile;
	}
	private String saveInfoFile(String outFilename, String metadata, VcfParameters params, String errorFile){
		//grep -v "#" test100.vcf | cut -f8 
		//grep -v "#" test100.vcf | cut -f8 | sed 's/^.*CNV=\([^;]*\).*/\1/g'
		
		//grep -v "#" test100.vcf | cut -f8 | sed '/^.*TGN=\([^;]*\).*/!d;s//\1/'

		 //grep -v "#" test100.vcf | cut -f8 | sed 's/.*TGN=\([^;]*\).*/\1/g' | sed 's/.*=.*//g'

		boolean success=true;
		boolean result=true;
		String metadataFile=outFilename+".metadata."+metadata;
		if(tempVCF==null){
			saveTempVCF(outFilename+".tmp.vcf",params,errorFile);
		}
		success&=tryExec("grep -v \"#\" "+tempVCF,outFilename+".noHeader",errorFile);
		tryFunc(sarg(cut,"-f9 " + outFilename+".noHeader"),outFilename+".genoList",errorFile); //List of markers
		tryFunc(sarg(cut,"--complement -f1,2,3,4,5,6,7,8,9 "+outFilename+".noHeader"),outFilename + ".noInfo",errorFile); //Data with no info columns
		BufferedReader genoList,vcfFile;
		PrintWriter writer;
		try{
			StringBuilder sb=new StringBuilder();
			genoList = new BufferedReader(new FileReader(outFilename+".genoList"));
			vcfFile = new BufferedReader(new FileReader(outFilename+".noInfo"));
			writer=new PrintWriter(metadataFile);
			
			String genoLine=null,vcfLine=null;
			try{
			genoLine=genoList.readLine();
			vcfLine=vcfFile.readLine();
			}catch(Exception e){
			}
			while(genoLine!=null&&vcfLine!=null){	
				int position=0;
				int index=genoLine.indexOf(metadata);
				if(index!=0){
					position=genoLine.substring(0, index-1).split(":").length;//0 to colon
				}
				String[] vcfElements=vcfLine.split("\t");
				for(String s:vcfElements){
					String geno=s.split(":")[position+1];//+1 for the 'genotype' in the beginning. 
					sb.append(s+"\t");
				}
				sb.deleteCharAt(sb.length()-1);//Remove final tab
				writer.println(sb.toString());
				//Get a new line from both files
				genoLine=vcfLine=null;
				try{
					genoLine=genoList.readLine();
					vcfLine=vcfFile.readLine();
				}catch(Exception e){}
			}
			writer.close();
			genoList.close();
			vcfFile.close();
		}catch(Exception e){
			e.printStackTrace();
			result = false;
		}

		rm(outFilename+".noHeader");
		rm(outFilename+".genoList");
		return metadataFile;
	}
	
	public void saveIUPACFile(String outFilename,String errorFile){
		boolean result=true;
		if(tpedFile==null)result = result | setTpedFile(outFilename.substring(0,outFilename.lastIndexOf('.')) + ".tped",errorFile);
		
		result = result & tryExec("cut --complement -f1,2,3,4 -d \" \" " + tpedFile,outFilename+".cut",errorFile );
		result = result & tryExec("awk/tpedToBi.awk",outFilename+".bi", errorFile,outFilename+".cut");
		
		result = result & tryExec("awk/BiToIUPAC.awk", outFilename,errorFile,outFilename+".bi");
		rm(outFilename + ".bi");
		if(!result)hasError=true;
		return;
	}
	
	


	//Filters and saves sample files based on the SampleRegex parameter
	public boolean saveSampleFile(String outFilename,String sampleRegex) {
		boolean result=true;
		result = result & tryExec("bcftools query -l " + fileLocation + " > " + outFilename);
		
		//Create a new 'filtered' .samples. Note: changes the base 'filename' to the new, filtered VCF file
		if(sampleRegex!=null){
			result = result & tryExec("mv " + outFilename+" "+outFilename+".slist");
			result = result & tryExec("grep " + sampleRegex + " "+outFilename+".slist" + " > "+outFilename);
			rm(outFilename+".slist");//TODO: fix this to move the original file or move the new file into the 'temp files'
			result = result & tryExec("bcftools convert -S " + outFilename + " -o "+fileLocation+".vcf "+fileLocation);
			fileLocation=fileLocation+".vcf";
		}
		return result;
	}
}


