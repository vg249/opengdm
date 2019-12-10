package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.CvIdCvTermMapper;
import org.gobiiproject.gobidomain.services.SamplesBrapiService;
import org.gobiiproject.gobidomain.services.VariantSetsService;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.auditable.VariantSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SamplesBrapiDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;
import org.gobiiproject.gobiisampletrackingdao.DnaSampleDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class VariantSetsServiceImpl implements VariantSetsService {


    @Autowired
    private DatasetDao datasetDao;

    @Autowired
    private CvDao cvDao;

    @Override
    public List<VariantSetDTO> listVariantSets(Integer pageNum, Integer pageSize,
                                               Integer varianSetDbID) {

        List<VariantSetDTO> returnVal = new ArrayList<>();

        List<Dataset> datasets = datasetDao.listDatasetsByPageNum(
                pageNum, pageSize, varianSetDbID);


        for(Dataset dataset : datasets) {

            if(dataset != null) {


                VariantSetDTO variantSetDTO = new VariantSetDTO();

                ModelMapper.mapEntityToDto(dataset, variantSetDTO);

                returnVal.add(variantSetDTO);
            }
        }

        return returnVal;
    }


}
