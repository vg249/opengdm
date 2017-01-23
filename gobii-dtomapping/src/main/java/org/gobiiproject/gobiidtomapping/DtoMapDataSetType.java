package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.DataSetTypeDTO;

import java.util.List;

/**
 * Created by VCalaminos on 2017-01-04.
 */
public interface DtoMapDataSetType {

    List<DataSetTypeDTO> getDataSetTypes() throws GobiiDtoMappingException;

}
