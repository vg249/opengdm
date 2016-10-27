package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobiidtomapping.DtoMapExtractorInstructions;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Angel on 6/8/2016.
 */
public class ExtractorInstructionFileServiceImpl implements ExtractorInstructionFilesService {

    private Logger LOGGER = LoggerFactory.getLogger(ExtractorInstructionFileServiceImpl.class);

    @Autowired
    private DtoMapExtractorInstructions dtoMapExtractorInstructions = null;


    @Override
    public ExtractorInstructionFilesDTO getInstruction(String cropType, String instructionFileName)  throws GobiiException {

        ExtractorInstructionFilesDTO returnVal;

        try {
            returnVal = dtoMapExtractorInstructions.readInstructions(cropType,instructionFileName);

            returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);

        } catch (Exception e) {

            LOGGER.error("Gobii service error", e);
            throw new GobiiDomainException(e);

        }

        return returnVal;
    }

    @Override
    public ExtractorInstructionFilesDTO createInstruction(String cropType, ExtractorInstructionFilesDTO ExtractorInstructionFilesDTO)
            throws GobiiException {
        ExtractorInstructionFilesDTO returnVal;

        returnVal = dtoMapExtractorInstructions.writeInstructions(cropType,ExtractorInstructionFilesDTO);

        returnVal.getAllowedProcessTypes().add(GobiiProcessType.READ);

        return returnVal;
    }    

} // ExtractorInstructionFileServiceImpl
