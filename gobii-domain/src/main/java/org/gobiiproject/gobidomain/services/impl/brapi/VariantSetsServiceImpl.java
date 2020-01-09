package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.VariantSetsService;
import org.gobiiproject.gobiimodel.dto.entity.auditable.AnalysisBrapiDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.VariantSetDTO;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class VariantSetsServiceImpl implements VariantSetsService {


    @Autowired
    private DatasetDao datasetDao;

    private String fileUrlFormat = "/gobii-{0}/variantsets/{1, number}/calls/download";

    private String cropType = "";

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    @Override
    public List<VariantSetDTO> listVariantSets(Integer pageNum, Integer pageSize,
                                               Integer varianSetDbID) {

        List<VariantSetDTO> returnVal = new ArrayList<>();

        HashMap<Integer, AnalysisBrapiDTO> analysisBrapiDTOMap = new HashMap<>();

        try {

            List<Dataset> datasets = datasetDao.listDatasets(
                   pageNum,
                   pageSize,
                   varianSetDbID);

            for (Dataset dataset : datasets) {

                VariantSetDTO variantSetDTO = new VariantSetDTO();

                mapDatasetEntityToVariantSetDto(dataset, variantSetDTO, analysisBrapiDTOMap);

                returnVal.add(variantSetDTO);
            }

       }
       catch (Exception e) {

            throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Bad Request");

       }

       return returnVal;

    }

    public VariantSetDTO getVariantSetById(Integer variantSetDbId) {

        VariantSetDTO variantSetDTO = new VariantSetDTO();

        try {

            Dataset dataset = datasetDao.getDatasetById(variantSetDbId);


            mapDatasetEntityToVariantSetDto(dataset, variantSetDTO);


        }
        catch (Exception e) {

            throw new GobiiDomainException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Bad Request");

        }

        return variantSetDTO;

    }

    /**
     * Maps Dataset entity to variantSetDTO.
     *
     * @param dataset - Dataset Entity
     * @param variantSetDTO - VariantSetDTO
     * @param analysisBrapiDTOMap - HashMap of Analysis Entity with analysisId as their key.
     *                            The map is used to keep track of analysis which are already model mapped.
     *                            If the parameter is null, then all the analysis are mapped irrsespective of
     *                            whether they are already mapped or not
     */
    public void mapDatasetEntityToVariantSetDto(Dataset dataset,
                                                VariantSetDTO variantSetDTO,
                                                HashMap<Integer, AnalysisBrapiDTO> analysisBrapiDTOMap) {

        ModelMapper.mapEntityToDto(dataset, variantSetDTO);

        variantSetDTO.setFileUrl(
                MessageFormat.format(this.fileUrlFormat,
                        this.getCropType(),
                        dataset.getDatasetId()));

        for(Analysis analysis : dataset.getMappedAnalyses()) {

            if(analysisBrapiDTOMap == null || analysisBrapiDTOMap.containsKey(analysis.getAnalysisId()) == false) {

                AnalysisBrapiDTO analysisBrapiDTO = new AnalysisBrapiDTO();

                ModelMapper.mapEntityToDto(analysis, analysisBrapiDTO);

                variantSetDTO.getAnalyses().add(analysisBrapiDTO);

                if(analysisBrapiDTOMap != null) {
                    analysisBrapiDTOMap.put(analysis.getAnalysisId(), analysisBrapiDTO);
                }
            }
            else {

                variantSetDTO.getAnalyses().add(
                        analysisBrapiDTOMap.get(analysis.getAnalysisId()));


            }

        }


    }

    /**
     * Override for function with same name where the last argument has to be null
     * @param dataset
     * @param variantSetDTO
     */
    public void mapDatasetEntityToVariantSetDto(Dataset dataset,
                                                VariantSetDTO variantSetDTO) {
        mapDatasetEntityToVariantSetDto(dataset, variantSetDTO, null);

    }
}
