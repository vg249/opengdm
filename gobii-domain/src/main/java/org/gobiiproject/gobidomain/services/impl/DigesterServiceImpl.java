package org.gobiiproject.gobidomain.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.DigesterService;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;

public class DigesterServiceImpl implements DigesterService {


	@Override
	public void sendProcedure(LoaderInstructionFilesDTO procedure) throws IOException {
		// TODO add configurations

		if (procedure == null) {
			throw new GobiiDomainException("Procedure passed to Digester must not be null");
		}

		String ip = "gobii-compute-node";
		int port = 1234;

		Socket socket = new Socket(ip, port);

		try (OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)) {
			out.write(new ObjectMapper().writeValueAsString(procedure));
		}
	}
}
