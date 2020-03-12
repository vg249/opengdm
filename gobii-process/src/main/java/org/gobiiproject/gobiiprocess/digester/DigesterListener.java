package org.gobiiproject.gobiiprocess.digester;

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
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterConfig;
import org.gobiiproject.gobiimodel.dto.instructions.loader.DigesterProcedureDTO;

public class DigesterListener {

	private ServerSocket socket;

	private Digester digester;
	private int threadCount;
	private ThreadPoolExecutor executor;

	public DigesterListener(Digester digester, int threadCount) {
		this.digester = digester;
		this.threadCount = threadCount;
	}

	public void start(int port) throws IOException {
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);

		socket = new ServerSocket(port);

		new Thread(() -> {
			for(;;) {
				try {
					Socket client = socket.accept();

					BufferedReader clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

					String input = clientReader.readLine();

					DigesterProcedureDTO procedure = new ObjectMapper().readValue(input, DigesterProcedureDTO.class);

					executor.execute(() -> {
						try {
							digester.run(procedure);
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
		});
	}


	/**
	 * Main class of Digester Jar file. Uses command line parameters to determine instruction file, and runs whole program.
	 *
	 * @param args See Digester.jar -? to get a list of arguments
	 * throws FileNotFoundException, IOException, ParseException, InterruptedException
	 */
	public static void main(String[] args) throws Exception {

		//Section - Setup
		Options o = new Options()
				.addOption("v", "verbose", false, "Verbose output")
				.addOption("e", "errlog", true, "Error log override location")
				.addOption("r", "rootDir", true, "Fully qualified path to gobii root directory")
				.addOption("c", "config", true, "Fully qualified path to gobii configuration file")
				.addOption("h", "hdfFiles", true, "Fully qualified path to hdf files");
		LoaderGlobalConfigs.addOptions(o);

		DigesterConfig config = new DigesterConfig();

		config.setRootDir("../");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cli = parser.parse(o, args);
			if (cli.hasOption("rootDir")) config.setRootDir(cli.getOptionValue("rootDir"));
			if (cli.hasOption("verbose")) config.setVerbose(true);
			if (cli.hasOption("errLog")) config.setErrorLogOverride(cli.getOptionValue("errLog"));
			if (cli.hasOption("config")) config.setPropertiesFile(cli.getOptionValue("config"));
			if (cli.hasOption("hdfFiles")) config.setPathToHDF5Files(cli.getOptionValue("hdfFiles"));
			LoaderGlobalConfigs.setFromFlags(cli);
			args = cli.getArgs();//Remaining args passed through

		} catch (org.apache.commons.cli.ParseException exp) {
			new HelpFormatter().printHelp("java -jar Digester.jar ", "Also accepts input file directly after arguments\n" +
					"Example: java -jar Digester.jar -c /home/jdl232/customConfig.properties -v /home/jdl232/testLoad.json", o, null, true);
			System.exit(2);
		}

		if (config.getPropertiesFile() == null)
			config.setPropertiesFile(config.getRootDir() + "config/gobii-web.xml");


		Digester digester = new Digester(config);

		new DigesterListener(digester, 4).start(1234);
	}


}
