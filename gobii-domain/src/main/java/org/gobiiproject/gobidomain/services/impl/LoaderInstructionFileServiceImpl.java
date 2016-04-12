package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.LoaderInstructionFilesService;
import org.gobiiproject.gobiidtomapping.DtoMapLoaderInstructions;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Phil on 4/12/2016.
 */
public class LoaderInstructionFileServiceImpl implements LoaderInstructionFilesService {

    @Autowired
    private DtoMapLoaderInstructions dtoMapLoaderInstructions = null;

    @Override
    public LoaderInstructionFilesDTO getSampleLoaderFileInstructions() {
        return dtoMapLoaderInstructions.getSampleLoaderFileInstructions();
    }
}
