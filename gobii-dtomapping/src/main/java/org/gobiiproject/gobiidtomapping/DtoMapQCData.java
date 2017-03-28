package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCDataDTO;

public interface DtoMapQCData {

    QCDataDTO createData(String cropType, QCDataDTO qcDataDTO)  throws GobiiException;

}
