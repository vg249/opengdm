package org.gobiiproject.bert.components;

import ernie.core.Action;
import org.gobiiproject.bert.ApiUtil;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ProjectDTO;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Api {

	private static final String URL_PROJECT = "%s/projects";

	private String url;

	@Action("host")
	public void host(String url) {
		this.url = url;
	}

	@Action("project")
	public void createProject(String name) {

	}
}
