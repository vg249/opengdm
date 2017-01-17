package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.container.DataSetTypeDTO;
import org.gobiiproject.gobiimodel.types.DataSetType;

import java.util.List;

/**
 * Created by VCalaminos on 2017-01-05.
 */
public interface DataSetTypeService {

    List<DataSetTypeDTO> getDataSetTypes() throws GobiiDomainException;

}
