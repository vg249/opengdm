package org.gobiiproject.bert;

import java.io.*;
import java.util.*;
import java.util.Random;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
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

	private static HashMap<String[], Session> sessions = new HashMap<>();

	private static JSch jsch = new JSch();
	static {
		try {
			jsch.addIdentity("/Users/ljc237-admin/.ssh/id_rsa");
		} catch (JSchException e) {
			e.printStackTrace();
		}
	}

	public static Session sessionOf(String host, String user) throws JSchException {
		final String[] pair = {host, user};

		if (! sessions.containsKey(pair) || ! sessions.get(pair).isConnected()) {
			Session session = jsch.getSession(user, host, 22);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect(30000);
			sessions.put(pair, session);
		}

		return sessions.get(pair);
	}

	public static void printChannel(Channel channel) throws IOException {
		byte[] buffer = new byte[1024];
		InputStream in = channel.getInputStream();
		String line = "";
		while (true){
			while (in.available() > 0) {
				int i = in.read(buffer, 0, 1024);
				if (i < 0) {
					break;
				}
				line = new String(buffer, 0, i);
				System.out.println(line);
			}

			if(line.contains("logout")){
				break;
			}

			if (channel.isClosed()){
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee){}
		}
	}

	public static void go(Runnable ... runnable) {
		new Thread(() -> {
			for (Runnable r : runnable) {
				r.run();
			}
		}).start();
	}

	public static void ssh(String host, String user, String command) throws JSchException, IOException {

		Channel channel = null;
		try {

			Session session = sessionOf(host, user);

			channel = session.openChannel("exec");

			((ChannelExec) channel).setCommand(command);

			channel.connect(30000);
			printChannel(channel);

		} catch (JSchException e ) {
			throw e;
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
		}
	}

	public static void scp(String host, String user, String from, String to, String ... params) throws JSchException, IOException {

		Session session = sessionOf(host, user);

		boolean ptimestamp = true;

		// exec 'scp -t rfile' remotely
		String argString = String.join(" ", params) + " ";

		String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + argString + to;
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);

		// get I/O streams for remote scp
		OutputStream out = channel.getOutputStream();
		InputStream in = channel.getInputStream();

		channel.connect();

		if (checkAck(in) != 0) {
			throw new RuntimeException("scp failure");
		}

		File _lfile = new File(from);

		if (ptimestamp) {
			command = "T" + (_lfile.lastModified() / 1000) + " 0";
			// The access time should be sent here,
			// but it is not accessible with JavaAPI ;-<
			command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				throw new RuntimeException("scp failure");
			}
		}

		// send "C0644 filesize filename", where filename should not include '/'
		long filesize = _lfile.length();
		command = "C0644 " + filesize + " ";
		if (from.lastIndexOf('/') > 0) {
			command += from.substring(from.lastIndexOf('/') + 1);
		} else {
			command += from;
		}

		command += "\n";
		out.write(command.getBytes());
		out.flush();

		if (checkAck(in) != 0) {
			throw new RuntimeException("scp failure");
		}

		// send a content of lfile
		FileInputStream fis = new FileInputStream(from);
		byte[] buf = new byte[1024];
		while (true) {
			int len = fis.read(buf, 0, buf.length);
			if (len <= 0) break;
			out.write(buf, 0, len); //out.flush();
		}

		// send '\0'
		buf[0] = 0;
		out.write(buf, 0, 1);
		out.flush();

		if (checkAck(in) != 0) {
			throw new RuntimeException("scp failure");
		}
		out.close();

		try {
			if (fis != null) fis.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}

		channel.disconnect();
		session.disconnect();
	}

	public static void scpDirectory(String host, String user, String directoryPath, String to, String ... params) throws JSchException, IOException {

		File directory = new File(directoryPath);

		ssh(host, user, "mkdir -p " + to);

		for (File f : directory.listFiles()) {
			if (f.isDirectory()) {
				scpDirectory(host, user, f.getAbsolutePath(), to + "/" + f.getName(), params);
			} else {
				scp(host, user, f.getAbsolutePath(), to + "/", params);
			}
		}
	}

	public static void scpContent(String host, String user, String content, String to, String ... params) throws IOException, JSchException {

		String tmpFileName = to.substring(to.lastIndexOf("/")) + randomString();

		File file = File.createTempFile(tmpFileName, "tmp");

		spit(file.getAbsolutePath(), content);

		scp(host, user, file.getAbsolutePath(), to, params);

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
