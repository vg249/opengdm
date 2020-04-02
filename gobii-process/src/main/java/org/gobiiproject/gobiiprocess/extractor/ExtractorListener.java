package org.gobiiproject.gobiiprocess.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterConfig;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import org.gobiiproject.gobiiprocess.digester.Digester;
import org.gobiiproject.gobiiprocess.digester.DigesterListener;
import org.gobiiproject.gobiiprocess.digester.LoaderGlobalConfigs;
import static org.gobiiproject.gobiimodel.utils.error.Logger.logError;

public class ExtractorListener {
	private ServerSocket socket;

	private GobiiExtractor extractor;
	private int threadCount;
	private ThreadPoolExecutor executor;

	public ExtractorListener(GobiiExtractor extractor, int threadCount) {
		this.extractor = extractor;
		this.threadCount = threadCount;
	}

	public void start(int port) throws IOException {
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);

		socket = new ServerSocket(port);

		for(;;) {
			try {
				Socket client = socket.accept();

				BufferedReader clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

				String input = clientReader.readLine();

				ExtractorInstructionFilesDTO instructionFilesDTO = new ObjectMapper().readValue(input, ExtractorInstructionFilesDTO.class);

				executor.execute(() -> {
					try {
						extractor.run(instructionFilesDTO);
					} catch (Exception e) {
						// TODO something useful
						e.printStackTrace();
					}
				});

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Main class of Digester Jar file. Uses command line parameters to determine instruction file, and runs whole program.
	 *
	 * @param args See Digester.jar -? to get a list of arguments
	 * throws FileNotFoundException, IOException, ParseException, InterruptedException
	 */
	public static void main(String[] args) throws Exception {
		new ExtractorListener(new GobiiExtractor(args), 4);
	}
}
