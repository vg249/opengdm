package org.gobiiproject.gobidomain.services;

import java.io.IOException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterProcedureDTO;

public interface DigesterService {

	void sendProcedure(DigesterProcedureDTO procedure) throws IOException;

}
