package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMap;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExperimentDTO;

import java.util.List;
import java.util.Optional;

/**
 * Created by Angel on 4/19/2016.
 */
public interface DtoMapExperiment extends DtoMap<ExperimentDTO> {

    ExperimentDTO create(ExperimentDTO experimentDTO) throws GobiiDtoMappingException;
    ExperimentDTO replace(Integer experimentId, ExperimentDTO experimentDTO) throws GobiiDtoMappingException;
    ExperimentDTO get(Integer experimentId) throws GobiiDtoMappingException;
    List<ExperimentDTO> getList() throws GobiiDtoMappingException;
    List<ExperimentDTO> getAlleleMatrices(Integer projectId) throws GobiiDtoMappingException;

}
