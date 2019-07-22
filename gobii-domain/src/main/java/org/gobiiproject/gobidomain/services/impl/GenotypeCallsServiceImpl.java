package org.gobiiproject.gobidomain.services.impl;

import jdk.nashorn.internal.runtime.Context;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DnaRunService;
import org.gobiiproject.gobidomain.services.GenotypeCallsService;
import org.gobiiproject.gobidomain.services.MarkerBrapiService;
import org.gobiiproject.gobiidao.hdf5.HDF5Interface;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapGenotypeCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsMarkerMetadataDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;

public class GenotypeCallsServiceImpl implements GenotypeCallsService {

    Logger LOGGER = LoggerFactory.getLogger(GenotypeCallsService.class);

    @Autowired
    private DtoMapGenotypeCalls dtoMapGenotypeCalls = null;

    @Autowired
    private DnaRunService dnarunService = null;

    @Autowired
    private MarkerBrapiService markerService = null;

    /**
     * Gets the genotype calls from all datasets for given dnarunId.
     * @param dnarunId - dnarunId given by user.
     * @param pageToken - String token with datasetId and markerId combination of last page's last element.
     *                  If unspecified, first page will be extracted.
     * @param pageSize - Page size to extract. If not specified default page size.
     * @return List of Genotype calls for given dnarunId.
     */
    @Override
    public List<GenotypeCallsDTO> getGenotypeCallsByDnarunId(
            Integer dnarunId, String pageToken,
            Integer pageSize) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();

        DnaRunDTO dnarun = null;

        try {

            dnarun = dnarunService.getDnaRunById(dnarunId);

            String outputDirPath = "";

            returnVal =  dtoMapGenotypeCalls.getGenotypeCallsList(
                    dnarun, pageToken, pageSize);

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
    public String getNextPageToken() {
        if(dtoMapGenotypeCalls.getNextPageOffset() == null || dtoMapGenotypeCalls.getNextColumnOffset() == null) {
            return null;
        }
        else {
            return (dtoMapGenotypeCalls.getNextPageOffset() +
                    "-" + dtoMapGenotypeCalls.getNextColumnOffset());
        }
    }
}
