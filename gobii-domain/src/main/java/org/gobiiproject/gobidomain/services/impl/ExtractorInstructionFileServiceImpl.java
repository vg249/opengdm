package org.gobiiproject.gobidomain.services.impl;

import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobiidtomapping.DtoMapExtractorInstructions;
import org.gobiiproject.gobiimodel.dto.container.ExtractorInstructionFilesDTO;
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
    public ExtractorInstructionFilesDTO processExtractorFileInstructions(ExtractorInstructionFilesDTO extractorInstructionFilesDTO) {

        ExtractorInstructionFilesDTO returnVal = extractorInstructionFilesDTO;

        try {

            switch (returnVal.getProcessType()) {

                case CREATE:
                    returnVal = dtoMapExtractorInstructions.writeInstructions(extractorInstructionFilesDTO);
                    break;

                case READ:
                    returnVal = dtoMapExtractorInstructions.readInstructions(extractorInstructionFilesDTO);
                    break;

                default:
                    returnVal.getStatus().addStatusMessage(GobiiStatusLevel.ERROR,
                            GobiiValidationStatusType.BAD_REQUEST,
                            "Unsupported proces type " + returnVal.getProcessType().toString());

            } // switch

        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error("Gobii service error", e);
        }

        return returnVal;

    } // processExtractorFileInstructions

} // ExtractorInstructionFileServiceImpl
