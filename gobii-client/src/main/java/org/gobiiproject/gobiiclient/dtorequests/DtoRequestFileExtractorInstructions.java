// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.restmethods.dtopost.DtoRequestProcessor;
import org.gobiiproject.gobiimodel.dto.container.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiiapimodel.types.ControllerType;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;

public class DtoRequestFileExtractorInstructions {


    public ExtractorInstructionFilesDTO process(ExtractorInstructionFilesDTO extractorInstructionFilesDTO) throws Exception {

        return new DtoRequestProcessor<ExtractorInstructionFilesDTO>().process(extractorInstructionFilesDTO,
                ExtractorInstructionFilesDTO.class,
                ControllerType.EXTRACTOR,
                ServiceRequestId.URL_FILE_EXTRACTOR_INSTRUCTIONS);
    }
}
