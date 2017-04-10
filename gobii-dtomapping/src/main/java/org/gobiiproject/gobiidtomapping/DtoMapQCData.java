package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCDataDTO;

import java.util.List;

public interface DtoMapQCData {

    void createData(List<QCDataDTO> qcDataDTOsList, Long qcJobID, String newQCDataDirectory)  throws GobiiException;

}
