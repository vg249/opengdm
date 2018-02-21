package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedList;

import java.util.List;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapDataSet extends DtoMap<DataSetDTO>{


    DataSetDTO create(DataSetDTO dataSetDTO) throws GobiiDtoMappingException;
    DataSetDTO replace(Integer projectId, DataSetDTO dataSetDTO) throws GobiiDtoMappingException;
    DataSetDTO get(Integer projectId) throws GobiiDtoMappingException;
    List<DataSetDTO> getList() throws GobiiDtoMappingException;
    PagedList<DataSetDTO> getListPaged(Integer pageSize, Integer pageNo, String pgQueryId) throws GobiiDtoMappingException;

    List<DataSetDTO> getByTypeId(Integer typeId) throws GobiiDtoMappingException;

}
