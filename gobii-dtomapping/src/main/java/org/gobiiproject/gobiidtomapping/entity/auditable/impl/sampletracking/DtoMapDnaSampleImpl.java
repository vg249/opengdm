package org.gobiiproject.gobiidtomapping.entity.auditable.impl.sampletracking;

import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.sampletracking.DtoMapDnaSample;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.DnaSampleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

/**
 * Created by VCalaminos on 5/2/2019.
 */
public class DtoMapDnaSampleImpl implements DtoMapDnaSample{

    Logger LOGGER = LoggerFactory.getLogger(DtoMapDnaSampleImpl.class);

    //@Autowired
    //private RsDnaSampleDao rsSampleTrackingDnaSampleDao;

    @Autowired
    private DtoListQueryColl dtoListSampleTrackingQueryColl;

    @Override
    public List<DnaSampleDTO> createSamples(List<DnaSampleDTO> dnaSampleDTOList) throws GobiiDtoMappingException {

        List<DnaSampleDTO> returnVal = null;

        try {

            ResultSet resultSet = dtoListSampleTrackingQueryColl.getResultSet(ListSqlId.CREATE_ID_DNASAMPLES,
                    new HashMap<String, Object>(){{

                    }}, new HashMap<String, Object>(){{
                        put("sampleList", dnaSampleDTOList);
                    }});

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping Error", e);
            throw new GobiiDtoMappingException(e);
        }

        return  returnVal;

    }

    public DnaSampleDTO replace(Integer dnaSampleId, DnaSampleDTO dnaSampleDTO) throws GobiiDtoMappingException {

        DnaSampleDTO returnVal = null;

        return returnVal;

    }

    public DnaSampleDTO get(Integer dnaSampleId) throws GobiiDtoMappingException {

        DnaSampleDTO returnVal = null;

        return returnVal;

    }

    public List<DnaSampleDTO> getList() throws GobiiDtoMappingException {

        List<DnaSampleDTO> returnVal = null;

        return returnVal;

    }

    public DnaSampleDTO create(DnaSampleDTO dnaSampleDTO) throws GobiiDtoMappingException {

        return dnaSampleDTO;

    }


}
