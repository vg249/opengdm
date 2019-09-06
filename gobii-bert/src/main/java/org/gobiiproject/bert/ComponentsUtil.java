package org.gobiiproject.bert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.Random;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ComponentsUtil {

	private static final String PAYLOAD_ENVELOPE_JSON_TEMPLATE = "{\"payload\": {\"data\": %s}}";

	public static <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> as, Object body, String ... params) {

		HttpEntity<Object> request = new HttpEntity<>(body, headers);

		return new RestTemplate().exchange(url, HttpMethod.GET, request, as, (Object[]) params);
	}

	public static <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> as, String ... params) {
		return get(url, headers, as, null, params);
	}

	public static <T> ResponseEntity<T> post(String url, HttpHeaders headers, Class<T> as, Object body, String ... params) {
		HttpEntity<Object> request = new HttpEntity<>(body, headers);

		return new RestTemplate().exchange(url, HttpMethod.POST, request, as, (Object[]) params);
	}

	public static <T> ResponseEntity<T> post(String url, HttpHeaders headers, Class<T> as, String ... params) {
		return post(url, headers, as, null, params);
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

		if (! sessions.containsKey(pair)) {
			Session session = jsch.getSession(user, host, 22);
			return session;
		}

		return sessions.get(pair);
	}

	public static void ssh(String host, String user, String command) throws JSchException {

		Channel channel = null;
		try {

			Session session = sessionOf(host, user);

			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			session.connect(30000);

			channel = session.openChannel("exec");

			((ChannelExec) channel).setCommand(command);

			channel.connect(30000);
		} catch (JSchException e) {
			throw e;
		} finally {
			if (channel != null) {
				channel.disconnect();
			}
		}
	}

	public static void scp(String host, String user, String from, String to, String ... params) throws JSchException, IOException {

		Session session = sessionOf(host, user);

		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);

		session.connect(30000);


		boolean ptimestamp = true;

		// exec 'scp -t rfile' remotely
		String command = "scp " + (ptimestamp ? "-p" : "") + " -t ";

		command += Arrays.stream(params).collect(Collectors.joining(" "));
		command += " " + to;

		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);

		// get I/O streams for remote scp
		OutputStream out = channel.getOutputStream();
		InputStream in = channel.getInputStream();

		channel.connect();

		if (checkAck(in) != 0) {
			System.exit(0);
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
				System.exit(0);
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
			System.exit(0);
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
			System.exit(0);
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
}
