package org.gobiiproject.gobiidtomapping;


import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;

/**
 * Created by Phil on 4/12/2016.
 */


public interface DtoMapExtractorInstructions {

    ExtractorInstructionFilesDTO writeInstructions(String cropType, ExtractorInstructionFilesDTO extractorInstructionFilesDTO)  throws GobiiException;
    ExtractorInstructionFilesDTO readInstructions(String cropType, String instructionFileName)  throws GobiiException;
}
