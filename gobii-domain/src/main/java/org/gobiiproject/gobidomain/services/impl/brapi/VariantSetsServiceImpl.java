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
import java.util.HashSet;
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

            List<Object[]> datasetsWithMarkerAndSampleCounts = datasetDao.listDatasetsWithMarkersAndSamplesCounts(
                   pageNum,
                   pageSize,
                   varianSetDbID);

            for (Object[] resultTuple : datasetsWithMarkerAndSampleCounts) {

                VariantSetDTO variantSetDTO = new VariantSetDTO();

                Dataset dataset = (Dataset) resultTuple[0];

                ModelMapper.mapEntityToDto(dataset, variantSetDTO);

                variantSetDTO.setFileUrl(
                        MessageFormat.format(this.fileUrlFormat,
                                this.getCropType(),
                                dataset.getDatasetId()));

                for(Analysis analysis : dataset.getMappedAnalyses()) {

                    if(analysisBrapiDTOMap.containsKey(analysis.getAnalysisId())) {

                        variantSetDTO.getAnalyses().add(analysisBrapiDTOMap.get(analysis.getAnalysisId()));


                    }
                    else {

                        AnalysisBrapiDTO analysisBrapiDTO = new AnalysisBrapiDTO();

                        if(variantSetDTO.getAnalyses() == null) {
                            variantSetDTO.setAnalyses(new HashSet<>());
                        }

                        ModelMapper.mapEntityToDto(analysis, analysisBrapiDTO);

                        variantSetDTO.getAnalyses().add(analysisBrapiDTO);

                        analysisBrapiDTOMap.put(analysis.getAnalysisId(), analysisBrapiDTO);

                    }

                }

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


}
