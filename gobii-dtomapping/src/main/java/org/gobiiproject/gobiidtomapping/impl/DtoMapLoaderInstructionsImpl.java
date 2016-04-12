package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.filesystem.LoaderFileDAO;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderInstructions;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstruction;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public class DtoMapLoaderInstructionsImpl implements DtoMapLoaderInstructions {

    @Autowired
    private LoaderFileDAO loaderFileDAO;


    @Override
    public LoaderInstructionFilesDTO getSampleLoaderFileInstructions() {
        LoaderInstructionFilesDTO returnVal = new LoaderInstructionFilesDTO();

        List<LoaderInstruction> instructions = loaderFileDAO.getSampleInstructions();

        returnVal.setLoaderInstructions(instructions);

        return returnVal;
    }
}
