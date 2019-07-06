package org.gobiiproject.gobiidao.hdf5;

import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.email.ProcessMessage;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.checkFileExistence;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;
import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.logDebug;

/**
 * Interface to extract genotypes from HDF5.
 */
public class HDF5Interface {


    private String cropType;

    private static String pathToHDF5;


    private static Map<String, String> cropsHdf5FilesMap;


    public HDF5Interface(String cropType) {
        this.cropType = cropType;
    }

    public static String getPathToHDF5() {
        return pathToHDF5;
    }

    public static void setPathToHDF5(String pathToHDF5) {
        HDF5Interface.pathToHDF5 = pathToHDF5;
    }

    public static String getPathToHDF5Files(String cropType) {
        return HDF5Interface.cropsHdf5FilesMap.getOrDefault(cropType, null);
    }

    public static void setPathToHDF5Files(String cropType, String pathToHDF5Files) {
        HDF5Interface.cropsHdf5FilesMap.put(cropType, pathToHDF5Files);
    }


    /**
     * Gets a pared down list of markers and samples based on position file and sample position file
     * @param markerFast if the output is 'markerFast' or sample fast
     * @param errorFile Location of temporary error file (mostly but not entirely ignored
     * @param tempFolder Location of folder to store temporary files
     * @return String location of the output file on the filesystem.
     * @throws FileNotFoundException if the datasets provided contain an invalid dataset, or the temporary file folder is badly chmodded
     */
    public String getHDF5Genotypes(
            boolean markerFast, String errorFile,
            String tempFolder, Map<String, ArrayList<String>> datasetMarkerMap,
            Map<String, ArrayList<String>> datasetSampleMap) throws FileNotFoundException{

        StringBuilder genoFileString=new StringBuilder();

        //Hashset does not maintain order. So, sort them before you process.
        List<String> datasetList = new ArrayList<String>(datasetMarkerMap.keySet());

        Collections.sort(datasetList);

        try{
            for(String datasetId : datasetList) {

                int dsID=Integer.parseInt(datasetId);


                String positionList = String.join("\n", datasetMarkerMap.get(datasetId));

                String sampleList = String.join(",", datasetSampleMap.get(datasetId));

                String positionListFileLoc = tempFolder+"position.list";

                FileSystemInterface.rmIfExist(positionListFileLoc);

                FileWriter w = new FileWriter(positionListFileLoc);

                w.write(positionList);

                w.close();

                String genoFile = null;

                if(datasetSampleMap.get(datasetId).size() > 0) {

                    genoFile = getHDF5GenotypeByDatatset(
                            markerFast, errorFile, dsID,
                            tempFolder, positionListFileLoc, sampleList);

                    if(genoFile==null) return null;
                }

                if(genoFile != null) {

                    genoFileString.append(" "+genoFile);

                }
            }
        }
        catch(IOException e) {

            ErrorLogger.logError("GobiiExtractor", "MarkerList reading failed", e);

        }

        //Coallate genotype files
        String genoFile=tempFolder+"genotypes";

        logDebug("MarkerList", "Accumulating markers into final genotype file");

        if(genoFileString.length() == 0){
            ErrorLogger.logError("HDF5Interface","No genotype data to extract");
            return null;
        }

        String genotypePartFileIdentifier=genoFileString.toString();

        if(markerFast) {
            tryExec("paste" + genotypePartFileIdentifier, genoFile, errorFile);
        }
        else{
            tryExec("cat" + genotypePartFileIdentifier, genoFile, errorFile);
        }

        for(String tempGenoFile:genotypePartFileIdentifier.split(" ")) {
            rmIfExist(tempGenoFile);
        }

        return genoFile;
    }


    /**
     * Performs the basic genotype extraction on a dataset given by dataSetId, filtered by the string entry from the marker list
     * and sample list files.
     * If marker list is null, do a dataset extract. Else, do a marker list extract on the dataset. If sampleList is also set, filter by samples afterwards
     * @param markerFast if the output is 'markerFast' or sample fast
     * @param errorFile where error logs can be stored temporarily
     * @param dataSetId Dataset ID to be pulled from
     * @param tempFolder folder to store intermediate results
     * @param markerList nullable - determines what markers to extract. File containing a list of marker positions, comma separated
     * @param sampleList nullable - list of comma delimited samples to cut out
     * @return file location of the dataset output.
     */
    public String getHDF5GenotypeByDatatset( boolean markerFast, String errorFile,
                                           Integer dataSetId, String tempFolder,
                                           String markerList, String sampleList) {

        String genoFile=tempFolder+"DS-"+dataSetId+".genotype";

        String HDF5File= getFileLoc(dataSetId);
        // %s <orientation> <HDF5 file> <output file>
        String ordering="samples-fast";
        if(markerFast)ordering="markers-fast";

        logDebug("Extractor","HDF5 Ordering is "+ordering);

        if(markerList!=null) {
            String hdf5Extractor=pathToHDF5+"fetchmarkerlist";
            ErrorLogger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+ ordering +" "+HDF5File+" "+markerList+" "+genoFile);
            boolean success=HelperFunctions.tryExec(hdf5Extractor + " " + ordering+" " + HDF5File+" "+markerList+" "+genoFile, null, errorFile);
            if(!success){
                rmIfExist(genoFile);
                return null;
            }
        }
        else {
            String hdf5Extractor=pathToHDF5+"dumpdataset";
            ErrorLogger.logInfo("Extractor","Executing: " + hdf5Extractor+" "+ordering+" "+HDF5File+" "+genoFile);
            boolean success=HelperFunctions.tryExec(hdf5Extractor + " " + ordering + " " + HDF5File + " " + genoFile, null, errorFile);
            if(!success){
                rmIfExist(genoFile);
                return null;
            }

        }
        if(sampleList!=null){
            filterBySampleList(genoFile,sampleList,markerFast, errorFile);
        }
        ErrorLogger.logDebug("Extractor",(ErrorLogger.success()?"Success ":"Failure " +"Extracting with "+ordering+" "+HDF5File+" "+genoFile));
        return genoFile;
    }

    private String getFileLoc(Integer dataSetId) {
        return HDF5Interface.cropsHdf5FilesMap.get(this.cropType) + "DS_" + dataSetId + ".h5";
    }

    /**
     * Filters a matrix passed back by the HDF5 extractor by a sample list
     * @param filename path to extract naked matrix
     * @param sampleList Comma separated list of sample positions
     */
    private void filterBySampleList(String filename, String sampleList, boolean markerFast, String errorFile){
        String tmpFile=filename+".tmp";
        FileSystemInterface.mv(filename,tmpFile);
        String cutString=getCutString(sampleList);
        if(!markerFast) {
            String sedString=cutString.replaceAll(",","p;");//1,2,3 => 1p;2p;3   (p added later)
            tryExec("sed -n "+sedString+"p",filename,errorFile,tmpFile); //Sed parameters need double quotes to be a single parameter
        }
        else{
            tryExec("cut -f"+getCutString(sampleList),filename,errorFile,tmpFile);
        }
        rmIfExist(tmpFile);
    }

    /**
     * Converts a string of 1,2,-1,4,5,6,-1,2 (Arbitrary -1's and NOT -1's into a comma delimited set
     * excluding positions where a -1 esists of one higher than the input value.
     *
     * Note: Since input is zero-based list, and the output to SED/CUT is one based, all numbers are incremented here.
     *
     * Examples:
     * 0,1,2,-1,4,5 -> 1,2,3,5,6
     * 7,-1,7,-1,7,-1 -> 8,8,8
     * @param sampleList Input string
     * @return Output string (see above)
     */
    private String getCutString(String sampleList){
        String[] entries=sampleList.split(",");
        StringBuilder cutString=new StringBuilder();//Cutstring -> 1,2,4,5,6
        int i=1;
        for(String entry:entries){
            int val=-1;
            try {
                //For some reason, spaces are everywhere, and Integer.parseInt is not very lenient
                String entryWithoutSpaces=entry.trim().replaceAll(" ","");
                val=Integer.parseInt(entryWithoutSpaces);
            }catch(Exception e){
                ErrorLogger.logDebug("GobiiExtractor NFE",e.toString());
            }
            if( val != -1){
                cutString.append((val+1)+",");
            }
            i++;
        }
        cutString.deleteCharAt(cutString.length()-1);
        return cutString.toString();
    }
}
