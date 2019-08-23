package org.gobiiproject.bert.components;

import com.fasterxml.jackson.databind.JsonNode;
import ernie.core.Action;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ProtocolDTO;
import org.springframework.http.ResponseEntity;

import static org.gobiiproject.bert.ApiUtil.*;

public class Api {

	private static final String URL_PING = "%s/ping";
	private static final String URL_CONTACT_SEARCH = "%s/contact-search";
	private static final String URL_PROJECT = "%s/projects";
	private static final String URL_EXPERIMENTS = "%s/experiments";
	private static final String URL_PROTOCOLS = "%s/protocols";
	private static final String URL_PLATFORMS = "%s/platforms";

	private static final String PROJECT_JSON_TEMPLATE = "{\"projectName\": %s}";
	private static final String PROTOCOL_JSON_TEMPLATE = "{\"protocolName\": %s}";
	private static final String PLATFORM_JSON_TEMPLATE = "{\"platformName\": %s}";
	private static final String EXPERIMENT_JSON_TEMPLATE = "{\"projectId\": %s, \"experimentName\": %s}";

	private String url;

	private String url(String endpoint) {
		return String.format(endpoint, url);
	}

	@Action("host")
	public void host(String url) {

		post(url(URL_PING), "", JsonNode.class);

		this.url = url;
	}

	@Action("principalInvestigator")
	public Integer principalInvestigator(String name) throws Exception {

		String[] tokens = name.split(" ");

		ResponseEntity<JsonNode> response;

		if (tokens.length == 1) {
			response = get(url(URL_CONTACT_SEARCH), JsonNode.class,
					"firstName", tokens[0]);
		} else if (tokens.length == 2) {
			response = get(url(URL_CONTACT_SEARCH), JsonNode.class,
					"firstName", tokens[0], "lastName", tokens[1]);
		} else {
			throw new Exception("Principal Investigator should be provide a first name, or a first name and a last name");
		}

		return response.getBody().get("payload").get("data").get(0).get("contactId").asInt();
	}

	@Action("project")
	public ProjectDTO createProject(Integer principalInvestigatorId, String name) {

		String json = makeEnvelopeJson(PROJECT_JSON_TEMPLATE, name);

		ResponseEntity<JsonNode> response = post(url(URL_PROJECT), json, JsonNode.class);

		return result(response, ProjectDTO.class);
	}

	@Action("protocol")
	public ProtocolDTO createProtocol(String name) {
		String json = makeEnvelopeJson(PROTOCOL_JSON_TEMPLATE, name);

		ResponseEntity<JsonNode> response = post(url(URL_PROTOCOLS), json, JsonNode.class);

		return result(response, ProtocolDTO.class);
	}

	@Action("platform")
	public PlatformDTO createPlatform(String name) {
		String json = makeEnvelopeJson(PLATFORM_JSON_TEMPLATE, name);

		ResponseEntity<JsonNode> response = post(url(URL_PLATFORMS), json, JsonNode.class);

		return result(response, PlatformDTO.class);
	}

	@Action("experiment")
	public ExperimentDTO createExperiment(ProjectDTO project, String name) {

		String json = makeEnvelopeJson(EXPERIMENT_JSON_TEMPLATE, project.getId(), name);

		ResponseEntity<JsonNode> response = post(url(URL_EXPERIMENTS), json, JsonNode.class);

		return result(response, ExperimentDTO.class);
	}

}
