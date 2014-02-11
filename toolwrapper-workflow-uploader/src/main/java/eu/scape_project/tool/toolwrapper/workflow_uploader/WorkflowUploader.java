package eu.scape_project.tool.toolwrapper.workflow_uploader;

import java.io.FileInputStream;
import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;

import eu.scape_project.tool.toolwrapper.core.exceptions.ErrorParsingCmdArgsException;
import eu.scape_project.tool.toolwrapper.data.components_spec.Component;
import eu.scape_project.tool.toolwrapper.data.components_spec.Components;

public class WorkflowUploader {
	private static Logger logger = Logger.getLogger(WorkflowUploader.class);

	private Client restClient;
	private Options options;

	public WorkflowUploader() {

		// REST client instantiation and configuration
		restClient = ClientBuilder.newClient();

		Option opt;
		setOptions(new Options());

		opt = new Option("s", "componentspec", true,
				"component spec file location");
		opt.setRequired(true);
		getOptions().addOption(opt);

		opt = new Option("c", "component", true, "component file location");
		opt.setRequired(true);
		getOptions().addOption(opt);

		opt = new Option("u", "username", true, "username");
		opt.setRequired(true);
		getOptions().addOption(opt);

		opt = new Option("p", "password", true, "password");
		opt.setRequired(true);
		getOptions().addOption(opt);

		opt = new Option("i", "family", true, "component family id");
		opt.setRequired(true);
		getOptions().addOption(opt);

	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public String uploadComponentToMyExperiment(String username,
			String password, String familyId, String componentSpecFilePath,
			String componentFilePath) throws IOException {
		String res = null;

		Components components = eu.scape_project.tool.toolwrapper.data.components_spec.utils.Utils
				.createComponents(componentSpecFilePath);

		if (components != null) {

			for (Component component : components.getComponent()) {

				String title = component.getName();
				String description = "TBA";
				String license = component.getLicense().getName().toString();

				byte[] encodedBytes = Base64.encodeBase64(IOUtils
						.toByteArray(new FileInputStream(componentFilePath)));
				String base64 = new String(encodedBytes);

				String postData = "<workflow>"
						+ "<title>%s</title>"
						+ "<description>%s</description>"
						+ "<component-family>http://www.myexperiment.org/packs/%s.html</component-family>"
						+ "<license-type>%s</license-type>"
						+ "<content-type>application/vnd.taverna.t2flow+xml</content-type>"
						+ "<content>%s</content>" + "</workflow>";

				postData = String.format(postData, title, description,
						familyId, license, base64);

				logger.debug(postData);

				// set basic authentication
				HttpBasicAuthFilter httpBasicAuthFilter = new HttpBasicAuthFilter(
						username, password);
				restClient.register(httpBasicAuthFilter);

				// HTTP POST request
				WebTarget uploadRequest = restClient
						.target("http://www.myexperiment.org/component.xml");

				Response response = uploadRequest.request().post(
						Entity.entity(postData, MediaType.APPLICATION_XML));

				logger.debug("status:" + response.getStatus());
				if (response.getStatus() == Status.OK.getStatusCode()) {
					logger.info("Component uploaded with success!");
				} else {
					logger.error("Error uploading component!\n"
							+ response.readEntity(String.class));
				}
			}
		}
		return res;
	}

	private CommandLine parseArguments(String[] args)
			throws ErrorParsingCmdArgsException {
		CommandLineParser parser = new PosixParser();
		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(options, args);
		} catch (org.apache.commons.cli.ParseException e) {
			throw new ErrorParsingCmdArgsException(e);
		}
		return commandLine;
	}

	public static void main(String[] args) throws IOException,
			ErrorParsingCmdArgsException {

		WorkflowUploader workflowUploader = new WorkflowUploader();

		CommandLine parseArguments = workflowUploader.parseArguments(args);

		logger.debug("<" + parseArguments.getOptionValue("u") + ">\n" + "<"
				+ parseArguments.getOptionValue("p") + ">\n" + "<"
				+ parseArguments.getOptionValue("i") + ">\n" + "<"
				+ parseArguments.getOptionValue("s") + ">\n" + "<"
				+ parseArguments.getOptionValue("c") + ">\n");

		workflowUploader.uploadComponentToMyExperiment(
				parseArguments.getOptionValue("u"),
				parseArguments.getOptionValue("p"),
				parseArguments.getOptionValue("i"),
				parseArguments.getOptionValue("s"),
				parseArguments.getOptionValue("c"));

	}

}
