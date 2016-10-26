package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.container.ExtractorInstructionFilesDTO;


/**
 * Created by Phil on 4/12/2016.
 */
public interface ExtractorInstructionFilesService {
    ExtractorInstructionFilesDTO processExtractorFileInstructions(ExtractorInstructionFilesDTO extractorInstructionFilesDTO);

    ExtractorInstructionFilesDTO createInstruction(String cropType, ExtractorInstructionFilesDTO extractorInstructionFilesDTO) throws GobiiException;

    ExtractorInstructionFilesDTO getInstruction(String cropType, String instructionFileName) throws GobiiException;

}
