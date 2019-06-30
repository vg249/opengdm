package org.gobiiproject.gobidomain.services.impl;

import jdk.nashorn.internal.runtime.Context;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.GenotypeCallsService;
import org.gobiiproject.gobiidao.hdf5.HDF5Interface;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapGenotypeCalls;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.GenotypeCallsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenotypeCallsServiceImpl implements GenotypeCallsService {

    Logger LOGGER = LoggerFactory.getLogger(GenotypeCallsService.class);

    @Autowired
    private DtoMapGenotypeCalls dtoMapGenotypeCalls = null;


    @Override
    public List<GenotypeCallsDTO> getGenotypeCallsByDnarunId(
            Integer dnarunId,
            Integer pageToken,
            Integer pageSize) {

        List<GenotypeCallsDTO> returnVal = null;

        try {

            returnVal = dtoMapGenotypeCalls.getListByDnarunId(
                    dnarunId, pageToken, pageSize);

            Map<String, ArrayList<String>> markerHdf5IndexMap= new HashMap<>();

            Map<String, ArrayList<String>> sampleHdf5IndexMap = new HashMap<>();

            sampleHdf5IndexMap.put("112", new ArrayList<>());

            sampleHdf5IndexMap.get("112").add("7");

            for(GenotypeCallsDTO genotypeCall : returnVal) {
                if(markerHdf5IndexMap.containsKey(
                        genotypeCall.getVariantSetDbId().toString())) {
                    markerHdf5IndexMap.get(
                            genotypeCall.getVariantSetDbId().toString()).add(
                            genotypeCall.getHdf5MarkerIdx());
                }
                else {
                    markerHdf5IndexMap.put(
                            genotypeCall.getVariantSetDbId().toString(),
                            new ArrayList<>());
                    markerHdf5IndexMap.get(
                            genotypeCall.getVariantSetDbId().toString()).add(
                                    genotypeCall.getHdf5MarkerIdx());
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
