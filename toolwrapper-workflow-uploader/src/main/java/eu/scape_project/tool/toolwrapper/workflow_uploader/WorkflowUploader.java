package eu.scape_project.tool.toolwrapper.workflow_uploader;

import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

public class WorkflowUploader {
	private static Logger logger = Logger.getLogger(WorkflowUploader.class);

	private Client restClient;
	private Options options;
	private static final String DEFAULT_MY_EXPERIMENT_BASE_URL = "http://www.myexperiment.org";
	private static final String DEFAULT_MY_EXPERIMENT_SESSION_COOKIE_NAME = "myexperiment_session";

	private String myExperimentBaseURL;

	public WorkflowUploader() {
		myExperimentBaseURL = DEFAULT_MY_EXPERIMENT_BASE_URL;

		// REST client instantiation and configuration
		restClient = ClientBuilder.newClient();

		Option opt;
		options = new Options();
		opt = new Option("w", "workflow", true, "workflow file location");
		opt.setRequired(true);
		options.addOption(opt);
		opt = new Option("o", "outDir", true,
				"directory where to put the generated artifacts");
		opt.setRequired(true);
		options.addOption(opt);
	}

	public WebTarget getWebTarget(String resourceLocation) {
		return restClient.target(resourceLocation);
	}

	public String getMyExperimentSessionID(String username, String password) {
		String res = null;
		String cookieRequestData = "<session><username>" + username
				+ "</username><password>" + password + "</password></session>";

		// HTTP POST request
		WebTarget loginRequest = restClient
				.target(DEFAULT_MY_EXPERIMENT_BASE_URL + "/session");
		Response response = loginRequest.request().post(
				Entity.entity(cookieRequestData, MediaType.APPLICATION_XML));
		logger.debug("<<<<<<<\n" + response + "\n<<<<<<<");
		MultivaluedMap<String, Object> headers = response.getHeaders();
		for (String headerName : headers.keySet()) {
			logger.debug("header:" + headerName + " " + headers.get(headerName));
		}

		logger.debug("status:" + response.getStatus());
		if (response.getStatus() == Status.OK.getStatusCode()) {
			for (Entry<String, NewCookie> cookieEntry : response.getCookies()
					.entrySet()) {
				logger.debug("Cookie:" + cookieEntry.getKey());
				if (DEFAULT_MY_EXPERIMENT_SESSION_COOKIE_NAME
						.equals(cookieEntry.getKey())) {
					res = cookieEntry.getValue().getValue();
					break;
				}
			}
		}
		return res;
	}

	public static void main(String[] args) {
		// obtain object instance
		WorkflowUploader workflowUploader = new WorkflowUploader();
		// authenticate user on myExperiment
		String myExperimentSessionID = workflowUploader
				.getMyExperimentSessionID(args[0], args[1]);
		logger.debug("myExperimentSessionID>" + myExperimentSessionID);
		// myExperimentSessionID = "3f6ac15e6a525ed0fe3d308f6192a6ab";

		if (myExperimentSessionID != null) {
			// if user was successfully authenticated on myExperiment

			Cookie authenticationCookie = new Cookie(
					DEFAULT_MY_EXPERIMENT_SESSION_COOKIE_NAME,
					myExperimentSessionID);
			// NewCookie authenticationCookie = new NewCookie(
			// DEFAULT_MY_EXPERIMENT_SESSION_COOKIE_NAME,
			// myExperimentSessionID);
			WebTarget webTarget = workflowUploader
					.getWebTarget("http://www.myexperiment.org/workflow.xml?id=3923");
			Response response = webTarget.request()
					.cookie(authenticationCookie).get();

			// Response response = webTarget.request().get();
			logger.debug("<<<<" + response.toString());
		}
	}
}
