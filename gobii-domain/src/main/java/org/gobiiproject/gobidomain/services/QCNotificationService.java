package org.gobiiproject.gobidomain.services;

import java.util.List;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCDataDTO;

public interface QCNotificationService {
    void createQCData(List<QCDataDTO> qcDataDTOsList, ConfigSettings configSetting, String cropType)  throws GobiiDomainException;
}
