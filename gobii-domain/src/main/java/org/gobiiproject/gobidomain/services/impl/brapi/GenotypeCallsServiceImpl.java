package org.gobiiproject.gobidomain.services.impl.brapi;

import com.fasterxml.jackson.databind.JsonNode;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.PageToken;
import org.gobiiproject.gobidomain.services.GenotypeCallsService;
import org.gobiiproject.gobidomain.services.MarkerBrapiService;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapGenotypeCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedListByCursor;
import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
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
    private DtoMapGenotypeCalls dtoMapGenotypeCalls = null;

    @Autowired
    private MarkerBrapiService markerService = null;

    @Autowired
    private DnaRunDao dnaRunDao = null;

    @Autowired
    private MarkerDao markerDao = null;

    @Autowired
    private HDF5Interface hdf5Interface;


    ///**


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
    public PagedListByCursor<GenotypeCallsDTO> getGenotypeCallsByCallSetId(Integer callSetDbId,
                                                                           Integer pageSize,
                                                                           String pageToken) {

        PagedListByCursor<GenotypeCallsDTO>  returnVal = new PagedListByCursor<>();

        List<GenotypeCallsDTO> genotypeCalls = new ArrayList<>();

        Integer markerIdCursor = null;

        Integer startDatasetId = null;

        String nextPageToken = null;

        Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

        Map<String, ArrayList<String>> dnarunHdf5IndexMap = new HashMap<>();


        try {

            Map<String, Integer> pageTokenParts = PageToken.decode(pageToken);

            if(pageTokenParts != null ) {
                startDatasetId = pageTokenParts.getOrDefault("datasetID", null);

                markerIdCursor = pageTokenParts.getOrDefault("markerId", null);
            }

            DnaRun dnaRun = dnaRunDao.getDnaRunById(callSetDbId);

            List<Integer> dnaRunDatasetIds = this.getDatasetIdsFromDatasetJsonIndex(
                    dnaRun.getDatasetDnaRunIdx());

            Collections.sort(dnaRunDatasetIds);

            Integer datasetIdCursorStart = 0;

            if(startDatasetId != null) {
                datasetIdCursorStart = dnaRunDatasetIds.indexOf(startDatasetId);
            }

            // Get Genotypes for makers in dataset until page is filled
            for(int datasetIdCursor = datasetIdCursorStart;
                datasetIdCursor < dnaRunDatasetIds.size();
                datasetIdCursor++) {

                Integer datasetId = dnaRunDatasetIds.get(datasetIdCursor);

                dnarunHdf5IndexMap.put(datasetId.toString(),
                        new ArrayList<>());

                dnarunHdf5IndexMap.get(datasetId.toString()).add(
                        dnaRun.getDatasetDnaRunIdx().get(datasetId.toString()).textValue());


                List<Marker> markers = markerDao.getMarkersByMarkerIdCursor(markerIdCursor, datasetId, pageSize);

                // Add Marker and DnaRun Metadata associated with genotype calls.
                // Extract HdF5 index for each marker and map it by dataset id.
                for(Marker marker : markers) {

                    GenotypeCallsDTO genotypeCall = new GenotypeCallsDTO();

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

                readHdf5GenotypesFromResult(genotypeCalls, extractListPath);

                if(pageSize >= markers.size()) {
                    break;
                }
                else {
                    pageSize -= markers.size();
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

            returnVal.setListData(genotypeCalls);

            return returnVal;

        }
        catch (GobiiException gE) {

            throw gE;

        }
        catch (Exception e) {
            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);
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
            throw new GobiiDomainException(e);
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


    private void readHdf5GenotypesFromResult (List<GenotypeCallsDTO> returnVal,
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



}

