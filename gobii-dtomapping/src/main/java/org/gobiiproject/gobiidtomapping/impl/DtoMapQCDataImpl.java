package org.gobiiproject.gobiidtomapping.impl;

import java.util.List;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.QCDataDAO;
import org.gobiiproject.gobiidtomapping.DtoMapContact;
import org.gobiiproject.gobiidtomapping.DtoMapQCData;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DtoMapQCDataImpl implements DtoMapQCData {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapQCDataImpl.class);

    @Autowired
    private QCDataDAO qcDataDAO;

    @Autowired
    private DtoMapContact dtoMapContact;

    private void createDirectories(String qcDataDirectory) throws GobiiDaoException {
        if (qcDataDirectory != null) {
            if (!(qcDataDAO.doesPathExist(qcDataDirectory))) {
                qcDataDAO.makeDirectory(qcDataDirectory);
            } else {
                qcDataDAO.verifyDirectoryPermissions(qcDataDirectory);
            }
        }
    }

    @Override
    public void createData(List<QCDataDTO> qcDataDTOsList, String newQCDataDirectory) throws GobiiException {

        try {
            createDirectories(newQCDataDirectory);
            for (int index = 0; index < qcDataDTOsList.size(); index++) {
                // Each QCDataDTO contains a QC output file to be copied from KDCompute to Gobii
                QCDataDTO qcDataDTO = qcDataDTOsList.get(index);
                qcDataDAO.writeData(qcDataDTO, newQCDataDirectory);
            }
        } catch (GobiiException e) {
            LOGGER.error("Gobii Maping Error", e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Gobii Maping Error", e);
            throw new GobiiException(e);
        }
    }
}
