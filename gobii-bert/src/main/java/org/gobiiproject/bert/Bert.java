package org.gobiiproject.bert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ernie.core.Ernie;
import org.gobiiproject.bert.components.Api;
import org.springframework.web.client.HttpServerErrorException;

public class Bert {

	public static void main(String[] args) {

		Ernie ernie = new Ernie();

		ernie.loadComponents(Api.class);

		for (String s : args) {
			ernie.runFile(args[0]);
		}

	}
}
