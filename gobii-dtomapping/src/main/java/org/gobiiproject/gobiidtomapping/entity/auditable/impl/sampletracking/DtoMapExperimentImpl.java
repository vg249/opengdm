package org.gobiiproject.gobiidtomapping.entity.auditable.impl.sampletracking;

import org.gobiiproject.gobiidao.resultset.access.RsExperimentDao;
import org.gobiiproject.gobiidao.resultset.core.ParamExtractor;
import org.gobiiproject.gobiidao.resultset.core.listquery.DtoListQueryColl;
import org.gobiiproject.gobiidao.resultset.core.listquery.ListSqlId;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.sampletracking.DtoMapExperiment;
import org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIdListImpl;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ExperimentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 5/1/2019.
 */
public class DtoMapExperimentImpl implements DtoMapExperiment {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);

    @Autowired
    private DtoListQueryColl dtoListSampleTrackingQueryColl;

    @Autowired
    private RsExperimentDao rsExperimentDao;

    @Override
    public List<ExperimentDTO> getList() throws GobiiDtoMappingException {

        List<ExperimentDTO> returnVal;

        try {

            returnVal = (List<ExperimentDTO>) dtoListSampleTrackingQueryColl.getList(ListSqlId.QUERY_ID_EXPERIMENT_ALL);

            if (returnVal == null) {
                returnVal = new ArrayList<>();
            }

        } catch (Exception e) {
            LOGGER.error("Gobii Mapping error", e);
            throw new GobiiDtoMappingException(e);
        }

        return returnVal;
    }

    public ExperimentDTO get(Integer experimentId) throws GobiiDtoMappingException {

        ExperimentDTO returnVal = new ExperimentDTO();

        return returnVal;
    }

    @Override
    public ExperimentDTO create(ExperimentDTO experimentDTO) throws GobiiDtoMappingException {

        ExperimentDTO returnVal = experimentDTO;

        Map<String, Object> parameters = ParamExtractor.makeParamVals(returnVal);
        Integer experimentId = rsExperimentDao.createExperiment(parameters);
        returnVal.setId(experimentId);

        return returnVal;
    }

    @Override
    public ExperimentDTO replace(Integer experimentId, ExperimentDTO experimentDTO) throws GobiiDtoMappingException {

        ExperimentDTO returnVal= experimentDTO;

        return  returnVal;

    }

}
