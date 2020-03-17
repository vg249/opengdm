package org.gobiiproject.gobidomain.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.Socket;
import org.gobiiproject.gobidomain.services.DigesterService;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterProcedureDTO;

public class DigesterServiceImpl implements DigesterService {


	@Override
	public void sendProcedure(DigesterProcedureDTO procedure) throws IOException {
		// TODO add configurations

		String ip = "gobii-compute-node";
		int port = 1234;

		Socket socket = new Socket(ip, port);

		new ObjectMapper().writeValueAsBytes(socket.getOutputStream());
	}
}
