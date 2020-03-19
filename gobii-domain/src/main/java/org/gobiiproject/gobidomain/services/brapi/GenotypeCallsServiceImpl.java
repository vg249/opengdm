package org.gobiiproject.gobidomain.services.brapi;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.PageToken;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.BrapiDefaults;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDao;
import org.gobiiproject.gobiisampletrackingdao.MarkerDao;
import org.gobiiproject.gobiisampletrackingdao.hdf5.HDF5Interface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class GenotypeCallsServiceImpl implements GenotypeCallsService {

    Logger LOGGER = LoggerFactory.getLogger(GenotypeCallsService.class);


    @Autowired
    private DnaRunDao dnaRunDao = null;

    @Autowired
    private MarkerDao markerDao = null;

    @Autowired
    private HDF5Interface hdf5Interface;


    /**
     * Get Genotypes to callSetDbId.
     * BrAPI field callSetDbId corresponds to dnaRunId in GDM system
     * @param callSetDbId - Corresponds to dnaRunId for which genotype calls need to fetched.
     * @param pageSize - Number of genotype calls per page to be fetched.
     * @param pageToken - Cursor to identify where the page starts. DnaRun can be in more than one dataset.
     *                  Assume, a given dnaRun is in multiple dataset {7,5,6} and each with set of markers of their own.
     *                  {datasetId-markerId} pageToken means, fetch Genotypes from datasetIds greater
     *                  than or equal to given datasetId and markerId ascending order cursors until the page fills.
     *                  nextPageToken will be where datasetId and markerId starts for next page.
     * @return List of Genotype calls for given page.
     */
    @Override
    public PagedResult<GenotypeCallsDTO> getGenotypeCallsByCallSetId(Integer callSetDbId,
                                                                     Integer pageSize,
                                                                     String pageToken) {

        PagedResult<GenotypeCallsDTO> returnVal = new PagedResult<>();

        List<GenotypeCallsDTO> genotypeCalls = new ArrayList<>();

        Integer markerIdCursor = null;

        Integer startDatasetId = null;

        String nextPageToken = null;

        Map<String, ArrayList<String>> markerHdf5IndexMap;

        Map<String, ArrayList<String>> dnarunHdf5IndexMap;


        try {

            Map<String, Integer> pageTokenParts = PageToken.decode(pageToken);

            if(pageTokenParts != null ) {
                startDatasetId = pageTokenParts.getOrDefault("datasetID", null);

                markerIdCursor = pageTokenParts.getOrDefault("markerId", null);
            }

            if(pageSize == null) {
                pageSize = Integer.parseInt(BrapiDefaults.pageSize);
            }

            Integer genotypesToBeRead = pageSize;

            //Get DNA run
            DnaRun dnaRun = dnaRunDao.getDnaRunById(callSetDbId);

            // Parse list of datasetIds the dnarun belongs to
            List<Integer> dnaRunDatasetIds = this.getDatasetIdsFromDatasetJsonIndex(
                    dnaRun.getDatasetDnaRunIdx());

            // Sort dataset ids
            Collections.sort(dnaRunDatasetIds);

            Integer datasetIdCursorStart = 0;

            if(startDatasetId != null) {
                datasetIdCursorStart = dnaRunDatasetIds.indexOf(startDatasetId);
            }

            // Read Genotypes for makers in dataset until page is filled
            for(int datasetIdCursor = datasetIdCursorStart;
                datasetIdCursor < dnaRunDatasetIds.size();
                datasetIdCursor++) {

                markerHdf5IndexMap = new HashMap<>();

                dnarunHdf5IndexMap = new HashMap<>();

                Integer datasetId = dnaRunDatasetIds.get(datasetIdCursor);

                dnarunHdf5IndexMap.put(datasetId.toString(),
                        new ArrayList<>());

                dnarunHdf5IndexMap.get(datasetId.toString()).add(
                        dnaRun.getDatasetDnaRunIdx().get(datasetId.toString()).textValue());


                List<Marker> markers = markerDao.getMarkersByMarkerIdCursor(
                        genotypesToBeRead,
                        markerIdCursor,
                        null,
                        datasetId);

                GenotypeCallsDTO genotypeCall;

                // Add Marker and DnaRun Metadata associated with genotype calls.
                // Extract HdF5 index for each marker and map it by dataset id.
                for(Marker marker : markers) {

                    genotypeCall = new GenotypeCallsDTO();

                    ModelMapper.mapEntityToDto(marker, genotypeCall);

                    ModelMapper.mapEntityToDto(dnaRun, genotypeCall);

                    genotypeCall.setVariantSetDbId(datasetId);

                    if(!markerHdf5IndexMap.containsKey(datasetId.toString())) {

                        markerHdf5IndexMap.put(
                                datasetId.toString(),
                                new ArrayList<>());
                    }

                    markerHdf5IndexMap.get(
                            datasetId.toString()).add(
                                    marker.getDatasetMarkerIdx().get(datasetId.toString()).textValue());


                    genotypeCalls.add(genotypeCall);

                }

                String extractListPath = extractGenotypes(markerHdf5IndexMap, dnarunHdf5IndexMap);

                readGenotypesFromFile(genotypeCalls, extractListPath);

                if(markers.size() >= genotypesToBeRead) {
                    break;
                }
                else {
                    genotypesToBeRead -= markers.size();
                }


            }

            if(genotypeCalls.size() >= pageSize) {

                Map<String ,Integer> nextPageCursorMap = new HashMap<>();

                nextPageCursorMap.put("datasetId",
                        genotypeCalls.get(genotypeCalls.size() - 1).getVariantSetDbId());

                nextPageCursorMap.put("markerId",
                        genotypeCalls.get(genotypeCalls.size() - 1).getVariantDbId());


                nextPageToken = PageToken.encode(nextPageCursorMap);

                returnVal.setNextPageToken(nextPageToken);

            }

            returnVal.setResult(genotypeCalls);

            return returnVal;

        }
        catch (GobiiException gE) {

            throw gE;

        }
        catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error. Please check the error log");

        }

    }


    private List<Integer> getDatasetIdsFromDatasetJsonIndex(JsonNode jsonNode) {

        try {
            List<Integer> datasetIds = new ArrayList<>();

            Iterator datasetIdsIter = jsonNode.fieldNames();

            while (datasetIdsIter.hasNext()) {
                datasetIds.add(Integer.parseInt(datasetIdsIter.next().toString()));
            }

            return datasetIds;
        }
        catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error. Please check the error log");
        }
    }

    /**
     * Gets the genotype calls from all datasets for given markerId.
     * @param markerId - markerId given by user.
     * @param pageToken - String token with datasetId and markerId combination of last page's last element.
     *                  If unspecified, first page will be extracted.
     * @param pageSize - Page size to extract. If not specified default page size.
     * @return List of Genotype calls for given dnarunId.
     */
    @Override
    public PagedResult<GenotypeCallsDTO> getGenotypeCallsByVariantDbId(
            Integer markerId, Integer pageSize, String pageToken) {

        PagedResult<GenotypeCallsDTO> returnVal = new PagedResult<>();

        List<GenotypeCallsDTO> genotypeCalls = new ArrayList<>();

        Integer dnaRunIdCursor = null;

        Integer startDatasetId = null;

        String nextPageToken = null;

        Map<String, ArrayList<String>> markerHdf5IndexMap;

        Map<String, ArrayList<String>> dnarunHdf5IndexMap;


        try {

            Map<String, Integer> pageTokenParts = PageToken.decode(pageToken);

            if(pageTokenParts != null ) {
                startDatasetId = pageTokenParts.getOrDefault("datasetId", null);

                dnaRunIdCursor = pageTokenParts.getOrDefault("dnaRunId", null);
            }

            if(pageSize == null) {
                pageSize = Integer.parseInt(BrapiDefaults.pageSize);
            }

            Integer genotypesToBeRead = pageSize;

            Marker marker = markerDao.getMarkerById(markerId);


            // Parse list of datasetIds the dnarun belongs to
            List<Integer> markerDatasetIds = this.getDatasetIdsFromDatasetJsonIndex(marker.getDatasetMarkerIdx());

            // Sort dataset ids
            Collections.sort(markerDatasetIds);

            Integer datasetIdCursorStart = 0;

            if(startDatasetId != null) {
                datasetIdCursorStart = markerDatasetIds.indexOf(startDatasetId);
            }

            // Read Genotypes for makers in dataset until page is filled
            for(int datasetIdCursor = datasetIdCursorStart;
                datasetIdCursor < markerDatasetIds.size();
                datasetIdCursor++) {

                markerHdf5IndexMap = new HashMap<>();

                dnarunHdf5IndexMap = new HashMap<>();

                Integer datasetId = markerDatasetIds.get(datasetIdCursor);

                markerHdf5IndexMap.put(datasetId.toString(),
                        new ArrayList<>());

                markerHdf5IndexMap.get(datasetId.toString()).add(
                        marker.getDatasetMarkerIdx().get(datasetId.toString()).textValue());


                List<DnaRun> dnaRuns = dnaRunDao.getDnaRunsByDnaRunIdCursor(genotypesToBeRead,
                        dnaRunIdCursor,
                        datasetId);

                // Add Marker and DnaRun Metadata associated with genotype calls.
                // Extract HdF5 index for each marker and map it by dataset id.
                for(DnaRun dnaRun : dnaRuns) {

                    GenotypeCallsDTO genotypeCall = new GenotypeCallsDTO();

                    ModelMapper.mapEntityToDto(marker, genotypeCall);

                    ModelMapper.mapEntityToDto(dnaRun, genotypeCall);

                    genotypeCall.setVariantSetDbId(datasetId);

                    if(!dnarunHdf5IndexMap.containsKey(datasetId.toString())) {

                        dnarunHdf5IndexMap.put(
                                datasetId.toString(),
                                new ArrayList<>());
                    }

                    dnarunHdf5IndexMap.get(
                            datasetId.toString()).add(
                            dnaRun.getDatasetDnaRunIdx().get(datasetId.toString()).textValue());

                    genotypeCalls.add(genotypeCall);

                }
                if(dnaRuns.size() > 0) {
                    String extractListPath = extractGenotypes(markerHdf5IndexMap, dnarunHdf5IndexMap);

                    readGenotypesFromFile(genotypeCalls, extractListPath);
                }
                else {
                    continue;
                }

                if(dnaRuns.size() >= genotypesToBeRead) {
                    break;
                }
                else {
                    genotypesToBeRead -= dnaRuns.size();
                }


            }

            if(genotypeCalls.size() >= pageSize) {

                Map<String ,Integer> nextPageCursorMap = new HashMap<>();

                nextPageCursorMap.put("datasetId",
                        genotypeCalls.get(genotypeCalls.size() - 1).getVariantSetDbId());

                nextPageCursorMap.put("dnaRunId",
                        genotypeCalls.get(genotypeCalls.size() - 1).getCallSetDbId());


                nextPageToken = PageToken.encode(nextPageCursorMap);

                returnVal.setNextPageToken(nextPageToken);

            }

            returnVal.setResult(genotypeCalls);

            return returnVal;

        }
        catch (GobiiException gE) {

            throw gE;

        }
        catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Internal Server Error. Please check the error log");

        }
    }

    /**
     * Gets the genotype calls in given datasets.
     * @param datasetId - datasetId given by user.
     * @param pageToken - String token with datasetId and markerId combination of last page's last element.
     *                  If unspecified, first page will be extracted.
     * @param pageSize - Page size to extract. If not specified default page size.
     * @return List of Genotype calls for given dnarunId.
     */
    @Override
    public PagedResult<GenotypeCallsDTO> getGenotypeCallsByVariantSetDbId(
            Integer datasetId,
            Integer pageSize,
            String pageToken) {

        PagedResult<GenotypeCallsDTO> returnVal = new PagedResult<>();

        //Result
        List<GenotypeCallsDTO> genotypeCalls = new ArrayList<>();

        //next page token for the next page
        String nextPageToken;

        Integer pageOffset = 0;

        Integer columnOffset = 0;
        Integer dnaRunOffset = 0;

        Integer markerPageSize = 0;

        Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

        Map<String, ArrayList<String>> dnarunHdf5IndexMap = new HashMap<>();

        SortedMap<Integer, Integer> dnarunHdf5OrderMap = new TreeMap<Integer, Integer>();

        try {


            List<DnaRun> dnaRuns = dnaRunDao.getDnaRunsByDatasetId(datasetId, pageSize, dnaRunOffset);


            if(pageToken != null) {

                Map<String, Integer> pageTokenParts = PageToken.decode(pageToken);

                pageOffset = pageTokenParts.get("pageOffset");

                columnOffset = pageTokenParts.get("columnOffset");
            }

            if(columnOffset > dnaRuns.size()) {
                pageOffset = null;
                columnOffset = 0;
            }

            Integer previousPageExcess = 0;

            if(columnOffset > 0) {
                previousPageExcess = dnaRuns.size() - columnOffset;
            }

            markerPageSize = (int) Math.ceil(
                    ((double)pageSize - (double)previousPageExcess)
                            / (double)dnaRuns.size());

            if (columnOffset > 0) {
                pageOffset -= 1;
                markerPageSize += 1;
            }


            List<Marker> markers = markerDao.getMarkersByDatasetId(datasetId, markerPageSize, pageOffset);

            //HDF5 index map for markers
            for(Marker marker : markers) {

                if(!markerHdf5IndexMap.containsKey(datasetId.toString())) {
                    markerHdf5IndexMap.put(datasetId.toString(), new ArrayList<>());
                }
                markerHdf5IndexMap.get(
                        datasetId.toString()).add(
                        marker.getDatasetMarkerIdx().get(datasetId.toString()).textValue());

            }

            Integer orderIndex = 0;

            //HDF5 index for dnaruns
            for(DnaRun dnaRun : dnaRuns) {

                if(!dnarunHdf5IndexMap.containsKey(datasetId.toString())) {
                    dnarunHdf5IndexMap.put(
                            datasetId.toString(),
                            new ArrayList<>());
                }

                dnarunHdf5IndexMap.get(
                        datasetId.toString()).add(
                        dnaRun.getDatasetDnaRunIdx().get(datasetId.toString()).textValue());

                dnarunHdf5OrderMap.put(
                        Integer.parseInt(dnaRun.getDatasetDnaRunIdx().get(datasetId.toString()).asText()),
                        orderIndex);

                orderIndex++;

            }

            Integer nextPageOffset = markerPageSize;

            if(pageOffset != null) {
                nextPageOffset += pageOffset;
            }

            String extractFilePath = this.extractGenotypes(markerHdf5IndexMap, dnarunHdf5IndexMap);


            Integer nextColumnOffset = this.readGenotypesFromFile(genotypeCalls, extractFilePath,
                    pageSize, datasetId,
                    columnOffset, markers,
                    dnaRuns, new ArrayList<>(dnarunHdf5OrderMap.values()));

            //result values
            returnVal.setResult(genotypeCalls);

            //Set page token only if there are genotypes
            if(genotypeCalls.size() > 0) {

                Map<String, Integer> nextPageCursorMap = new HashMap<>();

                //set next page offset and column offset as page token parts
                nextPageCursorMap.put("pageOffset", nextPageOffset);
                nextPageCursorMap.put("columnOffset", nextColumnOffset);


                nextPageToken = PageToken.encode(nextPageCursorMap);

                returnVal.setNextPageToken(nextPageToken);

            }

            if(extractFilePath != null && extractFilePath.endsWith("result.genotypes")) {
                File extractFile = new File(extractFilePath);
                String parent = extractFile.getParent();
                File extractFolder = new File(parent);
                extractFolder.delete();
            }



        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE.getMessage());

            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;
    }

    /**
     * Gets the genotype calls in given datasets.
     * @param extractQueryFilePath - datasetId given by user.
     * @param pageToken - String token with datasetId and markerId combination of last page's last element.
     *                  If unspecified, first page will be extracted.
     * @param pageSize - Page size to extract. If not specified default page size.
     * @return List of Genotype calls for given dnarunId.
     */
    @Override
    public List<GenotypeCallsDTO> getGenotypeCallsByExtractQuery(
            String extractQueryFilePath, Integer pageSize,
            String pageToken) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();

        try {


            String outputDirPath = "";


        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE.getMessage());

            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

        return returnVal;
    }

    @Override
    public String getGenotypeCallsAsString(Integer datasetId, Integer pageNum) {

        String returnVal = "";

        Objects.requireNonNull(pageNum, "pageNum : Non Null parameter");
        Objects.requireNonNull(datasetId, "markerRowOffset : Non Null parameter");

        //TODO: Add properties in gobii-web.xml to configure maximum page sizes
        Integer pageSize = 10000;

        Integer dnaRunRowOffset = 0;
        Integer markerRowOffset = pageNum*pageSize;

        List<String> headerValues = new ArrayList<>();

        Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

        Map<String, ArrayList<String>> dnarunHdf5IndexMap = new HashMap<>();

        try {

            List<Marker> markers = markerDao.getMarkersByDatasetId(datasetId, pageSize, markerRowOffset);

            if(markers.size() == 0) {
                return returnVal;
            }

            //Add header for first page
            if(pageNum == 0) {
                headerValues.add("MarkerName");
            }

            List<DnaRun> dnaRuns = dnaRunDao.getDnaRunsByDatasetId(datasetId, pageSize,
                    dnaRunRowOffset, false);

            while(dnaRuns.size() >= pageSize) {
                dnaRunRowOffset += pageSize;
                dnaRuns.addAll(dnaRunDao.getDnaRunsByDatasetId(datasetId, pageSize,
                        dnaRunRowOffset, false));
            }

            for(Marker marker : markers) {
                if(!markerHdf5IndexMap.containsKey(datasetId.toString())) {
                    markerHdf5IndexMap.put(datasetId.toString(), new ArrayList<>());
                }
                markerHdf5IndexMap.get(datasetId.toString()).add(
                        marker.getDatasetMarkerIdx().get(datasetId.toString()).textValue());
            }

            for(DnaRun dnaRun : dnaRuns) {
                if(!dnarunHdf5IndexMap.containsKey(datasetId.toString())) {
                    dnarunHdf5IndexMap.put(datasetId.toString(), new ArrayList<>());
                }
                dnarunHdf5IndexMap.get(datasetId.toString()).add(
                        dnaRun.getDatasetDnaRunIdx().get(datasetId.toString()).textValue());
                if(pageNum == 0) {
                    headerValues.add(dnaRun.getDnaRunName());
                }
            }

            //Append header for first page
            String headerString = null;
            if(pageNum == 0 && headerValues.size() > 0) {
                headerString = String.join(",", headerValues);
            }

            String extractFilePath = this.extractGenotypes(markerHdf5IndexMap, dnarunHdf5IndexMap);
            returnVal = this.readGenotypesFromFile(extractFilePath, markers, dnaRuns, headerString);

            return returnVal;
        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE.getMessage());

            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage()
            );
        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
        }

    }

    /**
     * Extracts genotypes from the hdf5.
     *
     */
    private String extractGenotypes(Map<String, ArrayList<String>> markerHdf5IndexMap,
                                    Map<String, ArrayList<String>> sampleHdf5IndexMap) throws Exception {

        String tempFolder = UUID.randomUUID().toString();

        String genotypCallsFilePath = hdf5Interface.getHDF5Genotypes(
                true,
                markerHdf5IndexMap,
                sampleHdf5IndexMap, tempFolder);

        return genotypCallsFilePath;

    }


    private void readGenotypesFromFile (List<GenotypeCallsDTO> returnVal,
                                              String extractListPath) {

        try {

            File genotypCallsFile = new File(extractListPath);

            FileInputStream fstream = new FileInputStream(genotypCallsFile);

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            int i = 0;

            int chrEach;

            StringBuilder genotype = new StringBuilder();

            while ((chrEach = br.read()) != -1) {

                char genotypesChar = (char) chrEach;

                if (genotypesChar == '\t' || genotypesChar == '\n') {

                    returnVal.get(i).setGenotype(new HashMap<>());
                    String[] genotypeValues = new String[]{genotype.toString()};
                    returnVal.get(i).getGenotype().put("values", genotypeValues);
                    i++;
                    genotype.setLength(0);

                } else {
                    genotype.append(genotypesChar);
                }
            }

            fstream.close();
        }
        catch (Exception e) {

            LOGGER.error( "Gobii Extraction service failed to read from result file",e);
            throw new GobiiDomainException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.NONE,
                    "Genotypes Extraction failed. System Error.");
        }


    }



    private Integer readGenotypesFromFile (
            List<GenotypeCallsDTO> returnVal,
            String genotypeMatrixFilePath,
            Integer pageSize,
            Integer datasetId,
            Integer columnOffset,
            List<Marker> markers,
            List<DnaRun> dnaruns,
            List<Integer> dnarunOrder)  throws Exception {


        File genotypCallsFile = new File(genotypeMatrixFilePath);

        FileInputStream fstream = new FileInputStream(genotypCallsFile);

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        Integer i = 0; // row index
        Integer j = 0; // column index
        int k = 0; // genotypes count



        int chrEach;

        StringBuilder genotype = new StringBuilder();

        while ((chrEach = br.read()) != -1 && k < pageSize) {


            char genotypesChar = (char) chrEach;

            if(genotypesChar == '\t' || genotypesChar == '\n') {

                if(j < columnOffset) {
                    j += 1;
                    genotype.setLength(0);
                    continue;
                }

                columnOffset = 0;

                GenotypeCallsDTO genotypeCall = new GenotypeCallsDTO();

                genotypeCall.setCallSetDbId(dnaruns.get(dnarunOrder.get(j)).getDnaRunId());
                genotypeCall.setCallSetName(dnaruns.get(dnarunOrder.get(j)).getDnaRunName());
                genotypeCall.setVariantDbId(markers.get(i).getMarkerId());
                genotypeCall.setVariantName(markers.get(i).getMarkerName());
                genotypeCall.setVariantSetDbId(datasetId);

                genotypeCall.setGenotype(new HashMap<>());
                String[] genotypeValues = new String[] {genotype.toString()};
                genotypeCall.getGenotype().put("values", genotypeValues);

                returnVal.add(genotypeCall);


                if(genotypesChar == '\t') {
                    j++;
                }
                else {
                    i++;
                    j = 0;
                }

                k++;

                genotype.setLength(0);

            }
            else {
                genotype.append(genotypesChar);
                if(genotype.length() == 2) {
                    genotype.insert(1, '/');
                }
            }

        }


        fstream.close();

        return j;
    }

    private String readGenotypesFromFile (
            String genotypeMatrixFilePath,
            List<Marker> markerMetadataList,
            List<DnaRun> dnarunMetadataList,
            String header
    )  throws Exception {


        File genotypCallsFile = new File(genotypeMatrixFilePath);

        FileInputStream fstream = new FileInputStream(genotypCallsFile);

        int i = 0;

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        int chrEach;

        StringBuilder genotypes = new StringBuilder();

        if(header != null && !header.isEmpty()) {
            genotypes.append(header);
            genotypes.append('\n');
        }

        genotypes.append(markerMetadataList.get(i).getMarkerName());
        genotypes.append(',');

        while ((chrEach = br.read()) != -1) {


            char genotypesChar = (char) chrEach;

            if(genotypesChar == '\t') {
                genotypes.append(',');
            }
            else if(genotypesChar == '\n') {
                i++;
                genotypes.append('\n');
                if(i < markerMetadataList.size()) {
                    genotypes.append(markerMetadataList.get(i).getMarkerName());
                    genotypes.append(',');
                }
            }
            else {
                genotypes.append(genotypesChar);
            }

        }


        fstream.close();

        return genotypes.toString();

    }
}

