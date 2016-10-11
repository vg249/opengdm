package org.gobiiproject.gobiidtomapping;


import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;

import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */


public interface DtoMapLoaderInstructions {

    LoaderInstructionFilesDTO createInstruction(LoaderInstructionFilesDTO loaderInstructionFilesDTO);
    List<LoaderInstructionFilesDTO> getInstructions(LoaderInstructionFilesDTO loaderInstructionFilesDTO);
}
