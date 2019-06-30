package org.gobiiproject.gobidomain.services.impl;

import jdk.nashorn.internal.runtime.Context;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DnaRunService;
import org.gobiiproject.gobidomain.services.GenotypeCallsService;
import org.gobiiproject.gobiidao.hdf5.HDF5Interface;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapGenotypeCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsMarkerMetadataDTO;
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

    @Override
    public List<GenotypeCallsDTO> getGenotypeCallsByDnarunId(
            Integer dnarunId,
            Integer pageToken,
            Integer pageSize) {

        List<GenotypeCallsDTO> returnVal = new ArrayList<>();

        DnaRunDTO dnarun = null;

        try {

            dnarun = dnarunService.getDnaRunById(dnarunId);

            List<Integer> dnarunDatasets = dnarun.getVariantSetIds();

            Collections.sort(dnarunDatasets);

            Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

            Map<String, ArrayList<String>> sampleHdf5IndexMap = new HashMap<>();


            sampleHdf5IndexMap.put(dnarun.getVariantSetIds().get(0).toString(),
                    new ArrayList<>());

            sampleHdf5IndexMap.get(
                    dnarun.getVariantSetIds().get(0).toString()).add(
                    dnarun.getDatasetDnarunIndex().get(
                            dnarun.getVariantSetIds().get(0).toString()).toString());

            List<GenotypeCallsMarkerMetadataDTO> genotypeMarkerMetadata = new ArrayList<>();

            Integer lastDataset = null;

            for(Integer datasetId : dnarunDatasets) {
                genotypeMarkerMetadata = dtoMapGenotypeCalls.getMarkerMetaDataList(datasetId,
                        pageToken, pageSize);
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
                if(genotypeMarkerMetadata.size() == pageSize) {
                    lastDataset = datasetId;
                    break;
                }
            }



            HDF5Interface.setPathToHDF5Files("/storage/local_test/gobii_bundle/crops/arbitrary-id-0/hdf5/");

            HDF5Interface.getHDF5GenoFromSampleList(
                    true, "/storage/local_test/gobii_bundle/crops/arbitrary-id-0/files/error.txt",
                    "/storage/local_test/gobii_bundle/crops/arbitrary-id-0/files/", markerHdf5IndexMap,
                    sampleHdf5IndexMap);

            FileInputStream fstream = new FileInputStream(
                    "/storage/local_test/gobii_bundle/crops/arbitrary-id-0/files/markerList.genotype");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;
            int i = 0;

            while ((strLine = br.readLine()) != null)   {
                returnVal.get(i).setGenotype(new HashMap<>());
                returnVal.get(i).getGenotype().put("string_value", strLine);
                i++;
            }

            fstream.close();

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
}
