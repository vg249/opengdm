package org.gobiiproject.bert;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ApiUtil {

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

	public static String slurpInputStream(InputStream inputStream) throws IOException {
		StringWriter sw = new StringWriter();
		IOUtils.copy(inputStream, sw);
		return sw.toString();
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
}
