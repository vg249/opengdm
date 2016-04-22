package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.dto.container.DataSetDTO;

/**
 * Created by Phil on 4/21/2016.
 */
public interface DtoMapDataSet {

    DataSetDTO getDataSetDetails(DataSetDTO dataSetDTO) throws GobiiDtoMappingException;
}
