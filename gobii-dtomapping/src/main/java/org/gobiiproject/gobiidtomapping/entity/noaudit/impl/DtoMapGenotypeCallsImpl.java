package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.hdf5.HDF5Interface;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapGenotypeCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.*;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class DtoMapGenotypeCallsImpl implements DtoMapGenotypeCalls {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapGenotypeCallsImpl.class);

    @Autowired
    private DtoListQueryColl dtoListQueryColl;

    @Autowired
    private HDF5Interface hdf5Interface;

    private String nextPageOffset;

    private String nextColumnOffset;

    public List<GenotypeCallsMarkerMetadataDTO> getMarkerMetaDataByMarkerIdLimit(
            Integer datasetId,
            Integer markerId,
            Integer pageSize) throws GobiiDtoMappingException {

        List<GenotypeCallsMarkerMetadataDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if(datasetId != null) {
                sqlParams.put("datasetId", datasetId);
            }
            else {

                LOGGER.error("datasetId is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Dataset Id");
            }

            if (markerId != null) {
                sqlParams.put("markerId", markerId);
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }


            returnVal = (List<GenotypeCallsMarkerMetadataDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_GENOTYPE_CALLS_MARKER_METADATA,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


    public List<GenotypeCallsMarkerMetadataDTO> getMarkerMetaDataByDatasetId(
            Integer datasetId,
            Integer pageOffset,
            Integer pageSize) throws GobiiDtoMappingException {

        List<GenotypeCallsMarkerMetadataDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if(datasetId != null) {
                sqlParams.put("datasetId", datasetId);
            }
            else {

                LOGGER.error("datasetId is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Dataset Id");
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }
            else {

                LOGGER.error("page size is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Page Size is required");
            }

            if(pageOffset != null) {
                sqlParams.put("pageOffset", pageOffset);
            }


            returnVal = (List<GenotypeCallsMarkerMetadataDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_MARKER_METADATA_BY_DATASET,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    public List<GenotypeCallsDnarunMetadataDTO> getDnarunMetaDataByDatasetId(
            Integer datasetId,
            Integer pageOffset,
            Integer pageSize) throws GobiiDtoMappingException {

        List<GenotypeCallsDnarunMetadataDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if(datasetId != null) {
                sqlParams.put("datasetId", datasetId);
            }
            else {

                LOGGER.error("datasetId is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Dataset Id");
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }
            else {

                LOGGER.error("page size is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Page Size is required");
            }

            if(pageOffset != null) {
                sqlParams.put("pageOffset", pageOffset);
            }


            returnVal = (List<GenotypeCallsDnarunMetadataDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_DNARUN_METADATA_BY_DATASET,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


    public List<GenotypeCallsDnarunMetadataDTO> getDnarunMetaDataByDnarunIdLimit(
            Integer datasetId,
            Integer dnarunId,
            Integer pageSize) throws GobiiDtoMappingException {

        List<GenotypeCallsDnarunMetadataDTO> returnVal;

        try {

            Map<String, Object> sqlParams = new HashMap<>();

            if(datasetId != null) {
                sqlParams.put("datasetId", datasetId);
            }
            else {

                LOGGER.error("datasetId is null");

                throw new GobiiDtoMappingException(
                        GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "Invalid Dataset Id");
            }

            if (dnarunId != null) {
                sqlParams.put("dnarunId", dnarunId);
            }

            if (pageSize != null) {
                sqlParams.put("pageSize", pageSize);
            }


            returnVal = (List<GenotypeCallsDnarunMetadataDTO>) dtoListQueryColl.getList(
                    ListSqlId.QUERY_ID_GENOTYPE_CALLS_DNARUN_METADATA,
                    null,
                    sqlParams
            );

            if (returnVal == null) {
                return new ArrayList<>();
            }

        }
        catch (GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE);

            throw new GobiiDtoMappingException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());
        }
        catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }


    /**
     * Gets the list of Genotypes Calls for given dnarun metadata.
     * @param dnarun
     * @param pageToken
     * @param pageSize
     * @return
     */
    @Override
    public List<GenotypeCallsDTO> getGenotypeCallsList(
            DnaRunDTO dnarun, String pageToken,
            Integer pageSize) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();

        List<Integer> dnarunDatasets = dnarun.getVariantSetIds();

        Integer startDatasetId = null;

        Integer markerIdLimit = null;



        Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

        Map<String, ArrayList<String>> dnarunHdf5IndexMap = new HashMap<>();
        try {
            //Sort Dataset Ids in ascenrding order to aid page indexing.
            Collections.sort(dnarunDatasets);

            if(pageToken != null) {

                String[] pageTokenSplit = pageToken.split("-", 2);

                if (pageTokenSplit.length == 2) {
                    try {
                        startDatasetId = Integer.parseInt(Arrays.asList(pageTokenSplit).get(0));
                        markerIdLimit = Integer.parseInt(Arrays.asList(pageTokenSplit).get(1));
                    } catch (Exception e) {
                        markerIdLimit = null;
                        startDatasetId = null;
                    }
                }
            }

            List<GenotypeCallsMarkerMetadataDTO> genotypeMarkerMetadata = new ArrayList<>();

            Integer startIndex = 0;

            if(startDatasetId != null) {
                startIndex = dnarunDatasets.indexOf(startDatasetId);
            }

            for(int i = startIndex; i < dnarunDatasets.size(); i++) {

                Integer datasetId = dnarunDatasets.get(i);

                dnarunHdf5IndexMap.put(datasetId.toString(),
                        new ArrayList<>());

                dnarunHdf5IndexMap.get(datasetId.toString()).add(
                        dnarun.getDatasetDnarunIndex().get(
                                datasetId.toString()).toString());


                genotypeMarkerMetadata = this.getMarkerMetaDataByMarkerIdLimit(
                        datasetId, markerIdLimit, pageSize);

                for(GenotypeCallsMarkerMetadataDTO marker : genotypeMarkerMetadata) {

                    GenotypeCallsDTO genotypeCall = new GenotypeCallsDTO();

                    genotypeCall.setCallSetDbId(dnarun.getCallSetDbId());
                    genotypeCall.setCallSetName(dnarun.getCallSetName());
                    genotypeCall.setVariantDbId(marker.getMarkerId());
                    genotypeCall.setVariantName(marker.getMarkerName());
                    genotypeCall.setVariantSetDbId(datasetId);

                    if(markerHdf5IndexMap.containsKey(
                            datasetId.toString())) {

                        markerHdf5IndexMap.get(
                                datasetId.toString()).add(
                                marker.getHdf5MarkerIdx());

                    }
                    else {
                        markerHdf5IndexMap.put(
                                datasetId.toString(),
                                new ArrayList<>());
                        markerHdf5IndexMap.get(
                                datasetId.toString()).add(
                                marker.getHdf5MarkerIdx());
                    }
                    returnVal.add(genotypeCall);
                }
                if(genotypeMarkerMetadata.size() >= pageSize) {
                    break;
                }
                else {
                    pageSize -= genotypeMarkerMetadata.size();
                }
            }

            String extractListPath = extractGenotypes(markerHdf5IndexMap, dnarunHdf5IndexMap);

            readHdf5GenotypesFromResult(returnVal, extractListPath);

        }
        catch(GobiiException ge) {
            throw ge;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Failed to extract genotypes. " +
                            "Please try again. If error persists, try contacting the System administrator");
        }

        return returnVal;
    }

    /**
     * Gets the list of Genotypes Calls for given marker metadata.
     * @param marker
     * @param pageToken
     * @param pageSize
     * @return
     */
    @Override
    public List<GenotypeCallsDTO> getGenotypeCallsList(
            MarkerBrapiDTO marker, String pageToken,
            Integer pageSize) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();

        List<Integer> markerDatasets = marker.getVariantSetDbId();

        Integer startDatasetId = null;

        Integer dnarunIdLimit = null;

        Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

        Map<String, ArrayList<String>> dnarunHdf5IndexMap = new HashMap<>();

        try {

            //Sort Dataset Ids in ascenrding order to aid page indexing.
            Collections.sort(markerDatasets);

            if(pageToken != null) {

                String[] pageTokenSplit = pageToken.split("-", 2);

                if (pageTokenSplit.length == 2) {
                    try {
                        startDatasetId = Integer.parseInt(Arrays.asList(pageTokenSplit).get(0));
                        dnarunIdLimit = Integer.parseInt(Arrays.asList(pageTokenSplit).get(1));
                    } catch (Exception e) {
                        dnarunIdLimit = null;
                        startDatasetId = null;
                    }
                }
            }

            List<GenotypeCallsDnarunMetadataDTO> genotypeDnarunMetadata = new ArrayList<>();

            Integer startIndex = 0;

            if(startDatasetId != null) {
                startIndex = markerDatasets.indexOf(startDatasetId);
            }

            for(int i = startIndex; i < markerDatasets.size(); i++) {

                Integer datasetId = markerDatasets.get(i);

                markerHdf5IndexMap.put(datasetId.toString(),
                        new ArrayList<>());

                markerHdf5IndexMap.get(datasetId.toString()).add(
                        marker.getDatasetMarkerIndex().get(datasetId.toString()).toString());


                genotypeDnarunMetadata = this.getDnarunMetaDataByDnarunIdLimit(
                        datasetId, dnarunIdLimit, pageSize);

                for(GenotypeCallsDnarunMetadataDTO dnarun : genotypeDnarunMetadata) {

                    GenotypeCallsDTO genotypeCall = new GenotypeCallsDTO();

                    genotypeCall.setCallSetDbId(dnarun.getDnarunId());
                    genotypeCall.setCallSetName(dnarun.getDnarunName());
                    genotypeCall.setVariantDbId(marker.getVariantDbId());
                    genotypeCall.setVariantName(marker.getVariantName());
                    genotypeCall.setVariantSetDbId(datasetId);

                    if(dnarunHdf5IndexMap.containsKey(
                            datasetId.toString())) {

                        dnarunHdf5IndexMap.get(
                                datasetId.toString()).add(
                                dnarun.getHdf5DnarunIdx());

                    }
                    else {
                        dnarunHdf5IndexMap.put(
                                datasetId.toString(),
                                new ArrayList<>());
                        dnarunHdf5IndexMap.get(
                                datasetId.toString()).add(
                                dnarun.getHdf5DnarunIdx());
                    }
                    returnVal.add(genotypeCall);
                }
                if(genotypeDnarunMetadata.size() >= pageSize) {
                    break;
                }
                else {
                    pageSize -= genotypeDnarunMetadata.size();
                }
            }

            String extractListPath = extractGenotypes(markerHdf5IndexMap, dnarunHdf5IndexMap);

            readHdf5GenotypesFromResult(returnVal, extractListPath);

        }
        catch(GobiiException ge) {
            throw ge;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Failed to extract genotypes. " +
                            "Please try again. If error persists, try contacting the System administrator");
        }

        return returnVal;
    }

    /**
     * Gets the list of Genotypes Calls for given datasetId.
     * @param datasetId
     * @param pageToken
     * @param pageSize
     * @return
     */
    @Override
    public List<GenotypeCallsDTO> getGenotypeCallsList(
            Integer datasetId, String pageToken,
            Integer pageSize) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();

        Integer pageOffset = null;
        Integer columnOffset = 0;

        Integer markerPageSize = 0;

        List<GenotypeCallsMarkerMetadataDTO> markerMetadataList = new ArrayList<>();

        List<GenotypeCallsDnarunMetadataDTO> dnarunMetadataList = new ArrayList<>();

        Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

        Map<String, ArrayList<String>> dnarunHdf5IndexMap = new HashMap<>();

        try {

            dnarunMetadataList = this.getDnarunMetaDataByDnarunIdLimit(datasetId, pageOffset, pageSize);

            if(pageToken != null) {
                String[] pageTokenSplit = pageToken.split("-", 2);

                if (pageTokenSplit.length == 2) {

                    try {

                        pageOffset = Integer.parseInt(Arrays.asList(pageTokenSplit).get(0));

                        columnOffset = Integer.parseInt(Arrays.asList(pageTokenSplit).get(1));

                        if(columnOffset > dnarunMetadataList.size()) {
                            pageOffset = null;
                            columnOffset = 0;
                        }
                    } catch (Exception e) {
                        pageOffset = null;
                        columnOffset = 0;
                    }
                }
            }


            Integer previousPageExcess = 0;

            if(columnOffset > 0) {
                previousPageExcess = dnarunMetadataList.size() - columnOffset;
            }

            markerPageSize = (int) Math.ceil(
                    ((double)pageSize - (double)previousPageExcess)
                            / (double)dnarunMetadataList.size());

            if (columnOffset > 0) {
                pageOffset -= 1;
                markerPageSize += 1;
            }


            markerMetadataList = this.getMarkerMetaDataByDatasetId(datasetId, pageOffset, markerPageSize);

            //HDF5 index map for markers
            for(GenotypeCallsMarkerMetadataDTO markerMetadata : markerMetadataList) {

                if(markerHdf5IndexMap.containsKey(datasetId.toString())) {

                    markerHdf5IndexMap.get(
                            datasetId.toString()).add(
                            markerMetadata.getHdf5MarkerIdx());

                }
                else {
                    markerHdf5IndexMap.put(
                            datasetId.toString(),
                            new ArrayList<>());
                    markerHdf5IndexMap.get(
                            datasetId.toString()).add(
                            markerMetadata.getHdf5MarkerIdx());
                }

            }

            //HDF5 index for dnaruns
            for(GenotypeCallsDnarunMetadataDTO dnarunMetadata : dnarunMetadataList) {
                if(dnarunHdf5IndexMap.containsKey(
                        datasetId.toString())) {

                    dnarunHdf5IndexMap.get(
                            datasetId.toString()).add(
                            dnarunMetadata.getHdf5DnarunIdx());

                }
                else {
                    dnarunHdf5IndexMap.put(
                            datasetId.toString(),
                            new ArrayList<>());
                    dnarunHdf5IndexMap.get(
                            datasetId.toString()).add(
                            dnarunMetadata.getHdf5DnarunIdx());
                }
            }


            Integer nextPageOffset = markerPageSize;

            if(pageOffset != null) {
                nextPageOffset += pageOffset;
            }

            this.setNextPageOffset(nextPageOffset.toString());

            String extractFilePath = this.extractGenotypes(markerHdf5IndexMap, dnarunHdf5IndexMap);

            this.readHdf5GenotypesFromMatrix(
                    returnVal, extractFilePath,
                    pageSize, datasetId,
                    columnOffset, markerMetadataList, dnarunMetadataList);

        }
        catch(GobiiException ge) {
            throw ge;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new GobiiException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    "Failed to extract genotypes. " +
                            "Please try again. If error persists, try contacting the System administrator");
        }

        return returnVal;
    }

    /**
     * Extracts genotypes from the hdf5.
     *
     */
    private String extractGenotypes(Map<String, ArrayList<String>> markerHdf5IndexMap,
                                    Map<String, ArrayList<String>> sampleHdf5IndexMap) throws Exception {

        String tempFolder = UUID.randomUUID().toString();

        String genotypCallsFilePath = hdf5Interface.getHDF5Genotypes(
                true, markerHdf5IndexMap,
                sampleHdf5IndexMap, tempFolder);

        return genotypCallsFilePath;

    }

    private void readHdf5GenotypesFromResult (List<GenotypeCallsDTO> returnVal,
                                              String extractListPath)  throws Exception {

        File genotypCallsFile = new File(extractListPath);

        FileInputStream fstream = new FileInputStream(genotypCallsFile);

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        int i = 0;

        int chrEach;

        StringBuilder genotype = new StringBuilder();

        while ((chrEach = br.read()) != -1) {
            char genotypesChar = (char) chrEach;
            if(genotypesChar == '\t' || genotypesChar == '\n') {
                returnVal.get(i).setGenotype(new HashMap<>());
                returnVal.get(i).getGenotype().put("string_value", genotype.toString());
                i++;
                genotype.setLength(0);
            }
            else {
               genotype.append(genotypesChar);
            }
        }

        fstream.close();

    }

    public void setNextColumnOffset(String columnOffset) {
        this.nextColumnOffset = columnOffset;
    }

    public void setNextPageOffset(String pageOffset) {
        this.nextPageOffset = pageOffset;
    }

    @Override
    public String getNextColumnOffset() {
        return this.nextColumnOffset;
    }

    @Override
    public String getNextPageOffset() {
        return this.nextPageOffset;
    }

    private void readHdf5GenotypesFromMatrix (
            List<GenotypeCallsDTO> returnVal,
            String genotypeMatrixFilePath,
            Integer pageSize,
            Integer datasetId,
            Integer columnOffset,
            List<GenotypeCallsMarkerMetadataDTO> markerMetadataList,
            List<GenotypeCallsDnarunMetadataDTO> dnarunMetadataList)  throws Exception {


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

                genotypeCall.setCallSetDbId(dnarunMetadataList.get(j).getDnarunId());
                genotypeCall.setCallSetName(dnarunMetadataList.get(j).getDnarunName());
                genotypeCall.setVariantDbId(markerMetadataList.get(i).getMarkerId());
                genotypeCall.setVariantName(markerMetadataList.get(i).getMarkerName());
                genotypeCall.setVariantSetDbId(datasetId);

                genotypeCall.setGenotype(new HashMap<>());
                genotypeCall.getGenotype().put("string_value", genotype.toString());

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
            }

        }

        this.setNextColumnOffset(j.toString());

        fstream.close();

    }
}
