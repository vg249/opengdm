package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCDataDTO;

public interface QCNotificationService {
    QCDataDTO createQCData(String cropType, QCDataDTO qcDataDTO) throws GobiiException;
}
