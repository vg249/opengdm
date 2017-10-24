package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMap;
import org.gobiiproject.gobiimodel.headerlesscontainer.DataSetDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.JobDTO;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapDataSet extends DtoMap<DataSetDTO>{


    DataSetDTO create(DataSetDTO dataSetDTO) throws GobiiDtoMappingException;
    DataSetDTO replace(Integer projectId, DataSetDTO dataSetDTO) throws GobiiDtoMappingException;
    DataSetDTO get(Integer projectId) throws GobiiDtoMappingException;
    List<DataSetDTO> getList() throws GobiiDtoMappingException;

    List<DataSetDTO> getByTypeId(Integer typeId) throws GobiiDtoMappingException;

}
