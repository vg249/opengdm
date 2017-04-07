package org.gobiiproject.gobiidao.filesystem;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCDataDTO;

public interface QCDataDAO {

    boolean writeData(QCDataDTO qcDataDTO,
                      String QCDataDirectory) throws GobiiDaoException;

    boolean doesPathExist(String pathName) throws GobiiDaoException;

    void verifyDirectoryPermissions(String pathName) throws GobiiDaoException;

    void makeDirectory(String pathName) throws GobiiDaoException;

}
