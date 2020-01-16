package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DnaRunService;
import org.gobiiproject.gobidomain.services.GenotypeCallsService;
import org.gobiiproject.gobidomain.services.MarkerBrapiService;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapGenotypeCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;
import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class GenotypeCallsServiceImpl implements GenotypeCallsService {

    Logger LOGGER = LoggerFactory.getLogger(GenotypeCallsService.class);

    @Autowired
    private DtoMapGenotypeCalls dtoMapGenotypeCalls = null;

    @Autowired
    private DnaRunService dnarunService = null;

    @Autowired
    private MarkerBrapiService markerService = null;

    @Autowired
    private DnaRunDao dnaRunDao = null;

    ///**
    // * Gets the genotype calls from all datasets for given dnarunId.
    // * @param dnarunId - dnarunId given by user.
    // * @param pageToken - String token with datasetId and markerId combination of last page's last element.
    // *                  If unspecified, first page will be extracted.
    // * @param pageSize - Page size to extract. If not specified default page size.
    // * @return List of Genotype calls for given dnarunId.
    // */
    //@Override
    //public List<GenotypeCallsDTO> getGenotypeCallsByDnarunId(
    //        Integer dnarunId, String pageToken,
    //        Integer pageSize) {

    //    List<GenotypeCallsDTO> returnVal = new ArrayList<>();

    //    DnaRunDTO dnarun = null;

    //    try {

    //        dnarun = dnarunService.getDnaRunById(dnarunId);

    //        String outputDirPath = "";

    //        returnVal =  dtoMapGenotypeCalls.getGenotypeCallsList(
    //                dnarun, pageToken, pageSize);

    //    }
    //    catch (GobiiException gE) {

    //        LOGGER.error(gE.getMessage(), gE.getMessage());

    //        throw new GobiiDomainException(
    //                gE.getGobiiStatusLevel(),
    //                gE.getGobiiValidationStatusType(),
    //                gE.getMessage()
    //        );
    //    }
    //    catch (Exception e) {
    //        LOGGER.error("Gobii service error", e);
    //        throw new GobiiDomainException(e);
    //    }

    //    return returnVal;
    //}


    /**
     * Get Genotypes to callSetDbId.
     * BrAPI field callSetDbId corresponds to dnaRunId in GDM system
     * @param callSetDbId - Corresponds to dnaRunId. Get Genotypes calls belonging to the callSetId.
     * @param pageSize - Number of genotype calls per page.
     * @param pageToken - Cursor to indetify where the next page start. DnaRun can be ibn multiple dataset.
     *                  Assume, a given dnaRun is in multiple datasets {7,5,6} and each have set of markers of their own.
     *                  {datasetId-markerId} pageToken means, fetch Genotypes from datasetIds greater
     *                  than or equal to given datasetId and markerId ascending order cursors until the page fills.
     *                  nextPageToken will be where datasetId and markerId starts for next page.
     * @return List of Genotype calls for given page.
     */
    @Override
    public List<GenotypeCallsDTO> getGenotypeCallsByCallSetId(Integer callSetDbId,
                                                              Integer pageSize,
                                                              String pageToken) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();

        DnaRun dnaRun = dnaRunDao.getDnaRunById(callSetDbId);


        //List<Integer> dnarunDatasets = dnarun.getVariantSetIds();

        //Integer startDatasetId = null;

        //Integer markerIdLimit = null;



        //Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

        //Map<String, ArrayList<String>> dnarunHdf5IndexMap = new HashMap<>();
        //try {
        //    //Sort Dataset Ids in ascenrding order to aid page indexing.
        //    Collections.sort(dnarunDatasets);

        //    if(pageToken != null) {

        //        String[] pageTokenSplit = pageToken.split("-", 2);

        //        if (pageTokenSplit.length == 2) {
        //            try {
        //                startDatasetId = Integer.parseInt(Arrays.asList(pageTokenSplit).get(0));
        //                markerIdLimit = Integer.parseInt(Arrays.asList(pageTokenSplit).get(1));
        //            } catch (Exception e) {
        //                markerIdLimit = null;
        //                startDatasetId = null;
        //            }
        //        }
        //    }

        //    List<GenotypeCallsMarkerMetadataDTO> genotypeMarkerMetadata = new ArrayList<>();

        //    Integer startIndex = 0;

        //    if(startDatasetId != null) {
        //        startIndex = dnarunDatasets.indexOf(startDatasetId);
        //    }

        //    for(int i = startIndex; i < dnarunDatasets.size(); i++) {

        //        Integer datasetId = dnarunDatasets.get(i);

        //        dnarunHdf5IndexMap.put(datasetId.toString(),
        //                new ArrayList<>());

        //        dnarunHdf5IndexMap.get(datasetId.toString()).add(
        //                dnarun.getDatasetDnarunIndex().get(
        //                        datasetId.toString()).toString());


        //        genotypeMarkerMetadata = this.getMarkerMetaDataByMarkerIdLimit(
        //                datasetId, markerIdLimit, pageSize);

        //        for(GenotypeCallsMarkerMetadataDTO marker : genotypeMarkerMetadata) {

        //            GenotypeCallsDTO genotypeCall = new GenotypeCallsDTO();

        //            genotypeCall.setCallSetDbId(dnarun.getCallSetDbId());
        //            genotypeCall.setCallSetName(dnarun.getCallSetName());
        //            genotypeCall.setVariantDbId(marker.getMarkerId());
        //            genotypeCall.setVariantName(marker.getMarkerName());
        //            genotypeCall.setVariantSetDbId(datasetId);

        //            if(!markerHdf5IndexMap.containsKey(datasetId.toString())) {

        //                markerHdf5IndexMap.put(
        //                        datasetId.toString(),
        //                        new ArrayList<>());
        //            }
        //            markerHdf5IndexMap.get(
        //                    datasetId.toString()).add(
        //                    marker.getHdf5MarkerIdx(datasetId.toString()));
        //            returnVal.add(genotypeCall);
        //        }
        //        if(genotypeMarkerMetadata.size() >= pageSize) {
        //            break;
        //        }
        //        else {
        //            pageSize -= genotypeMarkerMetadata.size();
        //        }
        //    }

        //    String extractListPath = extractGenotypes(markerHdf5IndexMap, dnarunHdf5IndexMap);

        //    readHdf5GenotypesFromResult(returnVal, extractListPath);

        //}
        //catch(GobiiException ge) {
        //    throw ge;
        //}
        //catch (Exception e) {
        //    LOGGER.error(e.getMessage(), e);
        //    throw new GobiiException(
        //            GobiiStatusLevel.ERROR,
        //            GobiiValidationStatusType.UNKNOWN,
        //            "Failed to extract genotypes. " +
        //                    "Please try again. If error persists, try contacting the System administrator");
        //}

        return returnVal;
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
    public List<GenotypeCallsDTO> getGenotypeCallsByMarkerId(
            Integer markerId, String pageToken,
            Integer pageSize) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();


        MarkerBrapiDTO marker = null;

        try {

            marker = markerService.getMarkerById(markerId);

            String outputDirPath = "";

            returnVal =  dtoMapGenotypeCalls.getGenotypeCallsList(
                    marker, pageToken, pageSize);

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
     * @param datasetId - datasetId given by user.
     * @param pageToken - String token with datasetId and markerId combination of last page's last element.
     *                  If unspecified, first page will be extracted.
     * @param pageSize - Page size to extract. If not specified default page size.
     * @return List of Genotype calls for given dnarunId.
     */
    @Override
    public List<GenotypeCallsDTO> getGenotypeCallsByDatasetId(
            Integer datasetId, String pageToken,
            Integer pageSize) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();



        try {


            String outputDirPath = "";

            returnVal =  dtoMapGenotypeCalls.getGenotypeCallsList(datasetId, pageToken, pageSize);

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
            String extractQueryFilePath, String pageToken,
            Integer pageSize) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();



        try {


            String outputDirPath = "";

            returnVal =  dtoMapGenotypeCalls.getGenotypeCallsListByExtractQuery(
                    extractQueryFilePath, pageToken, pageSize);

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
    public Map<String, String> getGenotypeCallsAsString(Integer datasetId, String pageToken) {

        Map<String, String> returnVal = new HashMap<>();

        try {
            return dtoMapGenotypeCalls.getGenotypeCallsAsString(datasetId, pageToken);
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

    @Override
    public String getNextPageToken() {
        if(dtoMapGenotypeCalls.getNextPageOffset() == null || dtoMapGenotypeCalls.getNextColumnOffset() == null) {
            if(dtoMapGenotypeCalls.getNextColumnOffset() == null) {
                return dtoMapGenotypeCalls.getNextPageOffset();
            }
            return null;
        }
        else {
            return (dtoMapGenotypeCalls.getNextPageOffset() +
                    "-" + dtoMapGenotypeCalls.getNextColumnOffset());
        }
    }
}
