package org.gobiiproject.gobiidtomapping.entity.noaudit.impl;

import org.gobiiproject.gobiidao.hdf5.HDF5Interface;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapGenotypeCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsMarkerMetadataDTO;
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


    @Override
    public List<GenotypeCallsMarkerMetadataDTO> getMarkerMetaDataList(
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


    /**
     * Gets the list of Genotypes Calls for given dnarun metadata.
     * @param dnarun
     * @param pageToken
     * @param pageSize
     * @return
     */
    public List<GenotypeCallsDTO> getGenotypeCallsList(
            DnaRunDTO dnarun, String pageToken,
            Integer pageSize, String outputDirPath) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();

        List<Integer> dnarunDatasets = dnarun.getVariantSetIds();

        Integer startDatasetId = null;

        Integer markerIdLimit = null;



        Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

        Map<String, ArrayList<String>> sampleHdf5IndexMap = new HashMap<>();
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

            sampleHdf5IndexMap.put(dnarun.getVariantSetIds().get(0).toString(),
                    new ArrayList<>());

            sampleHdf5IndexMap.get(
                    dnarun.getVariantSetIds().get(0).toString()).add(
                    dnarun.getDatasetDnarunIndex().get(
                            dnarun.getVariantSetIds().get(0).toString()).toString());

            List<GenotypeCallsMarkerMetadataDTO> genotypeMarkerMetadata = new ArrayList<>();

            Integer startIndex = 0;

            if(startDatasetId != null) {
                startIndex = dnarunDatasets.indexOf(startDatasetId);
            }

            for(int i = startIndex; i < dnarunDatasets.size(); i++) {

                Integer datasetId = dnarunDatasets.get(i);

                genotypeMarkerMetadata = this.getMarkerMetaDataList(datasetId,
                        markerIdLimit, pageSize);

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

            readHdf5GenotypesFromResult(returnVal, markerHdf5IndexMap, sampleHdf5IndexMap);

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

    private void readHdf5GenotypesFromResult (List<GenotypeCallsDTO> returnVal,
            Map<String, ArrayList<String>> markerHdf5IndexMap,
            Map<String, ArrayList<String>> sampleHdf5IndexMap)  throws Exception {

        String tempFolder = UUID.randomUUID().toString();

        File genotypCallsFile = new File(
               hdf5Interface.getHDF5Genotypes(true, markerHdf5IndexMap, sampleHdf5IndexMap, tempFolder));

        FileInputStream fstream = new FileInputStream(genotypCallsFile);

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        int i = 0;

        String strLine;

        while ((strLine = br.readLine()) != null) {
            returnVal.get(i).setGenotype(new HashMap<>());
            returnVal.get(i).getGenotype().put("string_value", strLine);
            i++;
        }

        fstream.close();

    }
}
