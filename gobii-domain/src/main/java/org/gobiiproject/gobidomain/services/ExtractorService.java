package org.gobiiproject.gobidomain.services;

import java.io.IOException;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;

public interface ExtractorService {

	void sendProcedure(ExtractorInstructionFilesDTO procedure) throws IOException;

}
