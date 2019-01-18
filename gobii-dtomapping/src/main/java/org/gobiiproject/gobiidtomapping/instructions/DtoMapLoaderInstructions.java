package org.gobiiproject.gobiidtomapping.instructions;


import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;

/**
 * Created by Phil on 4/12/2016.
 */


public interface DtoMapLoaderInstructions {

    LoaderInstructionFilesDTO createInstruction(String cropType, LoaderInstructionFilesDTO loaderInstructionFilesDTO) throws GobiiDtoMappingException, GobiiDaoException;
    LoaderInstructionFilesDTO getStatus(String cropType, String getInstructions) throws GobiiDtoMappingException;
}
