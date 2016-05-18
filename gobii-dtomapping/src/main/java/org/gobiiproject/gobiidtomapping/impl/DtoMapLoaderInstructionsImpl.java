package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.LoaderFileDAO;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderInstructions;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapLoaderInstructionsImpl implements DtoMapLoaderInstructions {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapLoaderInstructionsImpl.class);


    @Autowired
    private LoaderFileDAO loaderFileDAO;

    @Override
    public LoaderInstructionFilesDTO writeInstructions(LoaderInstructionFilesDTO loaderInstructionFilesDTO) {

        LoaderInstructionFilesDTO returnVal = new LoaderInstructionFilesDTO();

        try {

            String outputFileName = loaderFileDAO.writeInstructions(loaderInstructionFilesDTO.getUserName(),
                    loaderInstructionFilesDTO.getGobiiLoaderInstructions());

            returnVal = loaderInstructionFilesDTO;
            returnVal.setOutputFileId(outputFileName);

        } catch (GobiiDaoException e) {
            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return returnVal;

    } // writeInstructions


    @Override
    public LoaderInstructionFilesDTO getSampleLoaderFileInstructions() {

        LoaderInstructionFilesDTO returnVal = new LoaderInstructionFilesDTO();

        List<GobiiLoaderInstruction> instructions = loaderFileDAO.getSampleInstructions();

        returnVal.setGobiiLoaderInstructions(instructions);

        return returnVal;
    }
}
