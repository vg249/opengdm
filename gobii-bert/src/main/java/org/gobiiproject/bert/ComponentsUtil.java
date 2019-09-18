package org.gobiiproject.bert;

import java.io.*;
import java.util.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.Channel;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.UserAuthException;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class ComponentsUtil {

	private static final String PAYLOAD_ENVELOPE_JSON_TEMPLATE = "{\"payload\": {\"data\": %s}}";

	public static String buildParamString(String ... params) {

		if (params.length % 2 != 0) {
			throw new RuntimeException("Number of params must be even");
		}

		if (params.length == 0) {
			return "";
		}

		StringBuilder s = new StringBuilder();
		s.append("?");
		for (int i = 0 ; i < params.length ; i += 2) {
			s.append(params[i]);
			s.append("=");
			s.append(params[i+1]);
		}

		return s.toString();
	}

	public static <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> as, Object body, String ... params) {

		HttpEntity<Object> request = new HttpEntity<>(body, headers);

		return new RestTemplate().exchange(buildUri(url, params), HttpMethod.GET, request, as, (Object[]) params);
	}

	public static <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> as, String ... params) {
		return get(url, headers, as, null, params);
	}

	public static <T> ResponseEntity<T> post(String url, HttpHeaders headers, Class<T> as, Object body, String ... params) {
		HttpEntity<Object> request = new HttpEntity<>(body, headers);

		return new RestTemplate().exchange(buildUri(url, params), HttpMethod.POST, request, as, params);
	}

	public static <T> ResponseEntity<T> post(String url, HttpHeaders headers, Class<T> as, String ... params) {
		return post(url, headers, as, null, params);
	}

	public static String buildUri(String url, String ... params) {

		return url + buildParamString(params);
	}

	public static String makeEnvelopeJson(String template, Object ... params) {
		return String.format(PAYLOAD_ENVELOPE_JSON_TEMPLATE,
				String.format(template, params));
	}

	public static JsonNode getIn(JsonNode node, Object ... path) {

		for (Object s : path) {
			if (s instanceof Integer) {
				node = node.get((Integer) s);
			} else if (s instanceof String) {
				node = node.get((String) s);
			}
		}

		return node;
	}

	public static <T> T as(Object object, Class<T> c) {
		return new ObjectMapper().convertValue(object, c);
	}

	public static String slurp(InputStream inputStream) throws IOException {
		StringWriter sw = new StringWriter();
		IOUtils.copy(inputStream, sw);
		return sw.toString();
	}

	public static String slurp(String path) throws IOException {
		FileInputStream inputStream = new FileInputStream(path);
		return slurp(inputStream);
	}

	public static Stream<String> slurpLines(InputStream inputStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		return reader.lines();
	}

	public static void spit(String path, String content) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		writer.write(content);
		writer.flush();
		writer.close();
	}

	public static HttpHeaders headers(String ... headers) {

		if (! (headers.length % 2 == 0)) {
			throw new RuntimeException("Number of headers must be even");
		}

		HttpHeaders h = new HttpHeaders();
		for (int i = 0 ; i < headers.length ; i+=2) {
			h.add(headers[i], headers[i+1]);
		}
		return h;
	}

	public static <T> T result(ResponseEntity<JsonNode> response, Class<T> as) {
		return new ObjectMapper().convertValue(getIn(response.getBody(), "payload", "data", 0), as);
	}

	private static HashMap<String[], SSHClient> clients = new HashMap<>();


	public static SSHClient clientOf(String host, String user) throws IOException {
		final String[] pair = {host, user};

		if (! clients.containsKey(pair) || ! clients.get(pair).isConnected()) {

			SSHClient client = new SSHClient();
			client.addHostKeyVerifier(new PromiscuousVerifier());
			client.connect(host);
			client.loadKeys("/Users/ljc237-admin/.ssh/id_rsa");
			client.authPublickey(user);

			clients.put(pair, client);
		}

		return clients.get(pair);
	}

	public static Session sessionOf(String host, String user) throws IOException {
		return clientOf(host, user).startSession();
	}

	public static void go(Runnable ... runnable) {
		new Thread(() -> {
			for (Runnable r : runnable) {
				r.run();
			}
		}).start();
	}

	public static void ssh(String host, String user, String command) throws IOException {

		System.out.println(command);

		Session session = sessionOf(host, user);

		Session.Command cmd = session.exec(command);


		String results = slurp(cmd.getInputStream());
		String errors = slurp(cmd.getErrorStream());

		if (StringUtils.isNotEmpty(results)) {
			System.out.println("*** RESULTS ***\n" + results);
		}

		if (StringUtils.isNotEmpty(errors)) {
			System.out.println("*** ERRORS ***\n " + errors);
		}

		cmd.join(15, TimeUnit.SECONDS);
		System.out.println("\n** exit status: " + cmd.getExitStatus() + "\n");
	}

	public static void scp(String host, String user, String from, String to) throws IOException {

		SSHClient ssh = clientOf(host, user);

		ssh.newSCPFileTransfer().upload(new FileSystemFile(from), to);
	}

	public static void scpDirectory(String host, String user, String directoryPath, String to) throws IOException {

		File directory = new File(directoryPath);

		ssh(host, user, "mkdir -p " + to);

		for (File f : directory.listFiles()) {
			if (f.isDirectory()) {
				scpDirectory(host, user, f.getAbsolutePath(), to + "/" + f.getName());
			} else {
				scp(host, user, f.getAbsolutePath(), to + "/");
			}
		}
	}

	public static void scpContent(String host, String user, String content, String to) throws IOException {

		String tmpFileName = to.substring(to.lastIndexOf("/")) + randomString();

		File file = File.createTempFile(tmpFileName, "tmp");

		spit(file.getAbsolutePath(), content);

		scp(host, user, file.getAbsolutePath(), to);

		rm(file.getAbsolutePath());
	}

	public static void rm(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	public static int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		//          1 for error,
		//          2 for fatal error,
		//         -1
		if (b == 0) return b;
		if (b == -1) return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			}
			while (c != '\n');
			if (b == 1) { // error
				System.out.print(sb.toString());
			}
			if (b == 2) { // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}

	public static String randomString() {
		return new Date().getTime() + "" + new Random().nextInt();
	}

	public static void printHttpError(HttpStatusCodeException e) throws RuntimeException {
		try {
			System.out.println(e.getResponseBodyAsString().replace("\\n", "\n"));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
