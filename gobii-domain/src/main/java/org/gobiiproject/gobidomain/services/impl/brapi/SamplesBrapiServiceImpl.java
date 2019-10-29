package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.services.SamplesBrapiService;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SamplesBrapiDTO;
import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiisampletrackingdao.DnaSampleDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class SamplesBrapiServiceImpl implements SamplesBrapiService {


    @Autowired
    private DnaSampleDao dnaSampleDao;

    @Override
    public List<SamplesBrapiDTO> getSamples(Integer pageNum, Integer pageSize) {

        List<SamplesBrapiDTO> returnVal = new ArrayList<>();

        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(pageNum, pageSize);


        for(DnaSample dnaSample : dnaSamples) {

            SamplesBrapiDTO samplesBrapiDTO = new SamplesBrapiDTO();

            ModelMapper.mapEntityToDto(dnaSample, samplesBrapiDTO);

            returnVal.add(samplesBrapiDTO);
        }

        return returnVal;
    }


}
