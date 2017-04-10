package org.gobiiproject.gobiidao.filesystem.impl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.QCDataDAO;
import org.gobiiproject.gobiimodel.headerlesscontainer.QCDataDTO;

public class QCDataDAOImpl implements QCDataDAO {

    @Override
    public boolean writeData(QCDataDTO qcDataDTO,
                             String qcDataDirectoryIn) throws GobiiDaoException {

        boolean returnVal = false;
        try {
            Path qcDataFilePath = Paths.get(qcDataDTO.getDirectory(), qcDataDTO.getDataFile());
            File qcDataFile = new File(qcDataFilePath.toString());
            if (qcDataFile.exists()) {
                if (qcDataFile.isFile()) {
                    Path qcDataDirectoryPath = Paths.get(qcDataDirectoryIn);
                    File qcDataDirectory = new File(qcDataDirectoryPath.toString());
                    if (qcDataDirectory.exists()) {
                        if (qcDataDirectory.isDirectory()) {
                            try {
                                Files.copy(qcDataFilePath, qcDataDirectoryPath);
                                returnVal = true;
                            }
                            catch (IOException e) {
                                String message = e.getMessage() +
                                        "; copying " + qcDataFilePath.toString() +
                                        " to " + qcDataDirectoryPath.toString();
                                throw new GobiiDaoException(message);
                            }
                        }
                        else {
                            throw new GobiiDaoException(qcDataDirectoryPath.toString() + " is not a directory");
                        }
                    }
                    else {
                        throw new GobiiDaoException("The QC data directory does not exist: " + qcDataDirectoryPath.toString());
                    }
                }
                else {
                    throw new GobiiDaoException("The specified QC data file does not exist: " + qcDataFilePath.toString());
                }
            }
            else {
                throw new GobiiDaoException("The specified QC data file already exists: " + qcDataFilePath.toString());
            }
        }
        catch (Exception e) {
            String message = e.getMessage() + "; fqpn: ";
            throw new GobiiDaoException(message);
        }

        return returnVal;
    }

    @Override
    public boolean doesPathExist(String pathName) throws GobiiDaoException {
        return new File(pathName).exists();
    }

    @Override
    public void verifyDirectoryPermissions(String pathName) throws GobiiDaoException {

        File pathToCreate = new File(pathName);
        if (!pathToCreate.canRead() && !pathToCreate.setReadable(true, false)) {
            throw new GobiiDaoException("Unable to set read permissions on directory " + pathName);
        }

        if (!pathToCreate.canWrite() && !pathToCreate.setWritable(true, false)) {
            throw new GobiiDaoException("Unable to set write permissions on directory " + pathName);
        }
    }

    @Override
    public void makeDirectory(String pathName) throws GobiiDaoException {

        if (!doesPathExist(pathName)) {

            File pathToCreate = new File(pathName);

            if (!(pathToCreate.mkdirs())) {
                throw new GobiiDaoException("Unable to create directory " + pathName);
            }

            if ((!(pathToCreate.canRead())) && !(pathToCreate.setReadable(true, false))) {
                throw new GobiiDaoException("Unable to set read on directory " + pathName);
            }

            if ((!(pathToCreate.canWrite())) && (!(pathToCreate.setWritable(true, false)))) {
                throw new GobiiDaoException("Unable to set write on directory " + pathName);
            }

        } else {
            throw new GobiiDaoException("The specified path already exists: " + pathName);
        }
    }
}
