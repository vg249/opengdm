package org.gobiiproject.gobiidao.filesystem;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.instructions.GobiiQCComplete;

public interface QCInstructionsDAO {

    boolean writeInstructions(String instructionFileFqpn,
                              GobiiQCComplete instructions) throws GobiiDaoException;

    boolean doesPathExist(String pathName) throws GobiiDaoException;

    void verifyDirectoryPermissions(String pathName) throws GobiiDaoException;

    void makeDirectory(String pathName) throws GobiiDaoException;

    GobiiQCComplete getInstructions(String instructionFileFqpn);
}
