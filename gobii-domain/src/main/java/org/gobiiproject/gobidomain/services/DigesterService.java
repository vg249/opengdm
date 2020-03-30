package org.gobiiproject.gobidomain.services;

import java.io.IOException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;

public interface DigesterService {

	void sendProcedure(LoaderInstructionFilesDTO procedure) throws IOException;

}
