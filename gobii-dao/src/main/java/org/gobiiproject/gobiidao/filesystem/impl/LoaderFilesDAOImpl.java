package org.gobiiproject.gobiidao.filesystem.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.LoaderFilesDAO;
import org.gobiiproject.gobiidao.filesystem.LoaderInstructionsDAO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public class LoaderFilesDAOImpl implements LoaderFilesDAO {

    private final String LOADER_FILE_EXT = ".json";


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
    public LoaderFilePreviewDTO makeDirectory(String directoryName) throws GobiiDaoException {
        LoaderFilePreviewDTO returnVal = new LoaderFilePreviewDTO();
        if (!doesPathExist(directoryName)) {

            File pathToCreate = new File(directoryName);

            if (!pathToCreate.mkdirs()) {
                throw new GobiiDaoException("Unable to create directory " + directoryName);
            }

            verifyDirectoryPermissions(directoryName);
            returnVal.setDirectoryName(pathToCreate.getName());

        } else {
            throw new GobiiDaoException("The specified path already exists: " + directoryName);
        }

        return returnVal;
    }

} // LoaderInstructionsDAOImpl
