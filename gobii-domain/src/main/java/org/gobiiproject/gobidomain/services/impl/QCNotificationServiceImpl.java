package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.QCNotificationService;
import org.gobiiproject.gobiidtomapping.DtoMapQCData;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCDataDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class QCNotificationServiceImpl implements QCNotificationService {

    private Logger LOGGER = LoggerFactory.getLogger(QCNotificationServiceImpl.class);

    @Autowired
    private DtoMapQCData dtoMapQCData = null;

    @Override
    public QCDataDTO createQCData(String cropType, QCDataDTO qcDataDTO)
            throws GobiiException {
        QCDataDTO returnVal;

        returnVal = dtoMapQCData.createData(cropType, qcDataDTO);

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);

        return returnVal;
    }

}
