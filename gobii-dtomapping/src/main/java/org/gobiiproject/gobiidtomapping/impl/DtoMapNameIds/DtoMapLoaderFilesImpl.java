package org.gobiiproject.gobiidtomapping.impl.DtoMapNameIds;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.LoaderFilesDAO;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderFiles;
import org.gobiiproject.gobiidtomapping.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapLoaderFilesImpl implements DtoMapLoaderFiles {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapLoaderFilesImpl.class);

    private final String INSTRUCTION_FILE_EXT = ".json";

    @Autowired
    private LoaderFilesDAO loaderFilesDAO;

    public LoaderFilePreviewDTO makeDirectory(String cropType, String directoryName) throws GobiiDaoException {
        LoaderFilePreviewDTO returnVal = new LoaderFilePreviewDTO();
        String fileCropDirectory = null;
        ConfigSettings configSettings = new ConfigSettings();
        try {
            fileCropDirectory = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.RAW_USER_FILES);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != fileCropDirectory) {
            if (!loaderFilesDAO.doesPathExist(fileCropDirectory)) {
                loaderFilesDAO.makeDirectory(fileCropDirectory);
            } else {
                loaderFilesDAO.verifyDirectoryPermissions(fileCropDirectory);
            }

            System.out.println("fileCropDirectory: "+ fileCropDirectory);
        }

        if (null != directoryName) {
            String directoryPath = fileCropDirectory+ File.separator + directoryName;
            if (!loaderFilesDAO.doesPathExist(directoryName)) {
                returnVal = loaderFilesDAO.makeDirectory(directoryName);
            } else {
                loaderFilesDAO.verifyDirectoryPermissions(directoryName);
            }
        }

        returnVal.setId(0); //this is arbitrary for now
        return returnVal;

    } // createDirectories()

    public LoaderFilePreviewDTO getPreview(String cropType, String directoryName, String fileFormat) throws GobiiDaoException {
        LoaderFilePreviewDTO returnVal = null;

        ConfigSettings configSettings = new ConfigSettings();
        String fileCropDirectory = null;
        try {
            fileCropDirectory = configSettings.getProcessingPath(cropType, GobiiFileProcessDir.RAW_USER_FILES);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String directoryPath = fileCropDirectory+ File.separator + directoryName;
        if (!loaderFilesDAO.doesPathExist(directoryPath)) {
                throw new GobiiDaoException("The specified directory does not exist: " + directoryName);
            }else{
                returnVal = loaderFilesDAO.getPreview(directoryPath, fileFormat);
            }
        returnVal.setId(0); //this is arbitrary for now
        return returnVal;

    } // createDirectories()





}
