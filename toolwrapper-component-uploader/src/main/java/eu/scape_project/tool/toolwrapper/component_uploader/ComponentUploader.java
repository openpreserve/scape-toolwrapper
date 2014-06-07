/**
 ################################################################################
 #                  Copyright 2012 The SCAPE Project Consortium
 #
 #   This software is copyrighted by the SCAPE Project Consortium. 
 #   The SCAPE project is co-funded by the European Union under
 #   FP7 ICT-2009.4.1 (Grant Agreement number 270137).
 #
 #   Licensed under the Apache License, Version 2.0 (the "License");
 #   you may not use this file except in compliance with the License.
 #   You may obtain a copy of the License at
 #
 #                   http://www.apache.org/licenses/LICENSE-2.0              
 #
 #   Unless required by applicable law or agreed to in writing, software
 #   distributed under the License is distributed on an "AS IS" BASIS,
 #   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   
 #   See the License for the specific language governing permissions and
 #   limitations under the License.
 ################################################################################
 */
package eu.scape_project.tool.toolwrapper.component_uploader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;
import org.xml.sax.SAXException;

import eu.scape_project.tool.toolwrapper.core.configuration.Constants;
import eu.scape_project.tool.toolwrapper.core.exceptions.ErrorParsingCmdArgsException;
import eu.scape_project.tool.toolwrapper.data.components_spec.Component;
import eu.scape_project.tool.toolwrapper.data.components_spec.Components;
import eu.scape_project.tool.toolwrapper.data.tool_spec.Operation;
import eu.scape_project.tool.toolwrapper.data.tool_spec.Tool;

/** Class that uploads a Component (Taverna workflow) to the myExperiment site */
public class ComponentUploader {

	private static Logger logger = Logger.getLogger(ComponentUploader.class);

	private static final String MY_EXPERIMENT_COMPONENT_URI = "http://www.myexperiment.org/component.xml";
	private static final String DEFAULT_DESCRIPTION = "TBA";
	private static final String DEFAULT_LICENSE = "by-sa";
	private static final String COMPONENT_UPLOAD_TEMPLATE = "<workflow>"
			+ "<title>%s</title>"
			+ "<description>%s</description>"
			+ "<component-family>http://www.myexperiment.org/packs/%s</component-family>"
			+ "<license-type>%s</license-type>"
			+ "<content-type>application/vnd.taverna.t2flow+xml</content-type>"
			+ "<content>%s</content>" + "</workflow>";

	private Client restClient;
	private Options options;

	/** Empty constructor */
	public ComponentUploader() {

		// REST client instantiation and configuration
		restClient = ClientBuilder.newClient();

		Option opt;
		this.options = new Options();

		opt = new Option("u", "username", true, "myExperiment username");
		opt.setRequired(true);
		getOptions().addOption(opt);

		opt = new Option("p", "password", true, "myExperiment password");
		opt.setRequired(true);
		getOptions().addOption(opt);

		opt = new Option("i", "family", true, "component family id");
		opt.setRequired(true);
		getOptions().addOption(opt);

		opt = new Option("t", "toolspec", true, "toolspec file location");
		opt.setRequired(true);
		options.addOption(opt);

		opt = new Option("s", "componentspec", true,
				"component spec file location");
		opt.setRequired(true);
		getOptions().addOption(opt);

		opt = new Option("c", "component", true,
				"component file location (Taverna workflow)");
		opt.setRequired(true);
		getOptions().addOption(opt);

		opt = new Option(
				"l",
				"license",
				true,
				"component license (as myExperiment license set doesn't match the Toolwrapper one)");
		opt.setRequired(false);
		getOptions().addOption(opt);

		opt = new Option("d", "description", true, "component description");
		opt.setRequired(false);
		getOptions().addOption(opt);

	}

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	/**
	 * Uploads the component file to the myExperiment site
	 * 
	 * @param username
	 *            myExperiment username
	 * @param password
	 *            myExperiment password
	 * @param familyId
	 *            component family ID (the number part of the URI)
	 * @param componentSpecFilePath
	 *            the path to the component spec file
	 * @param componentFilePath
	 *            the path to the component file (Taverna workflow)
	 * @param license
	 *            myExperiment license (myExperiment accepted license set
	 *            doesn't match with Component license set). if no license is
	 *            provided, by default this will be set to
	 *            {@link ComponentUploader#DEFAULT_LICENSE}
	 * @param description
	 *            if no description is provided, by default this will be set to
	 *            {@link ComponentUploader#DEFAULT_DESCRIPTION}
	 * 
	 * @return true if the upload was successful, false otherwise
	 * 
	 * */
	public boolean uploadComponentToMyExperiment(String username,
			String password, String familyId, String toolSpecFilePath,
			String componentSpecFilePath, String componentFilePath,
			String license, String description) throws IOException,
			JAXBException, SAXException {
		boolean res = true;
		String descriptionValue = null;

		Tool tool = eu.scape_project.tool.toolwrapper.data.tool_spec.utils.Utils
				.createTool(toolSpecFilePath);

		Components components = eu.scape_project.tool.toolwrapper.data.components_spec.utils.Utils
				.createComponents(componentSpecFilePath);

		if (components != null) {

			for (Component component : components.getComponent()) {

				File componentFile = new File(componentFilePath);

				// we only want the Component object of the workflow provided
				// (workflows generated by the toolwrapper are automatically
				// named OPERATION_NAME.t2flow (see
				// /tool/operations/operation/@name), where OPERATION NAME must
				// match the Component name (see /components/component/@name))
				if (componentFile.getName().startsWith(component.getName())) {

					for (Operation operation : tool.getOperations()
							.getOperation()) {
						if (operation.getName().equals(component.getName())) {
							descriptionValue = operation.getDescription();
							break;
						}
					}

					String title = component.getName();
					if (descriptionValue == null
							|| descriptionValue.trim().length() == 0) {
						descriptionValue = (description == null ? DEFAULT_DESCRIPTION
								: description);
					}
					// NOTE: myExperiment accepted license set doesn't match
					// with Component license set
					String licenseValue = license == null ? DEFAULT_LICENSE
							: license;

					final FileInputStream input = new FileInputStream(componentFilePath);
					byte[] encodedBytes = null;
					try {
					    encodedBytes = Base64
							.encodeBase64(IOUtils
									.toByteArray(input));
					} finally {
					    input.close();
					}
					String base64 = new String(encodedBytes,
							Charset.defaultCharset());

					String postData = String.format(COMPONENT_UPLOAD_TEMPLATE,
							title, descriptionValue, familyId, licenseValue,
							base64);

					logger.debug(postData);

					// set basic authentication
					HttpBasicAuthFilter httpBasicAuthFilter = new HttpBasicAuthFilter(
							username, password);
					restClient.register(httpBasicAuthFilter);

					// HTTP POST request
					WebTarget uploadRequest = restClient
							.target(MY_EXPERIMENT_COMPONENT_URI);

					Response uploadResponse = uploadRequest.request().post(
							Entity.entity(postData, MediaType.APPLICATION_XML));

					logger.debug("status:" + uploadResponse.getStatus());

					if (uploadResponse.getStatus() == Status.OK.getStatusCode()) {
						logger.info("Component \"" + component.getName()
								+ "\" uploaded with success!");

					} else {
						logger.error("Error uploading component \""
								+ component.getName() + "\"!\n"
								+ uploadResponse.readEntity(String.class));
					}
				}
			}
		}
		return res;
	}

	/**
	 * Method used to print command-line syntax (usage) using
	 * {@link HelpFormatter}
	 */
	private void printUsage() {
		HelpFormatter helpFormatter = new HelpFormatter();

		PrintWriter systemErrPrintWriter = new PrintWriter(
				new OutputStreamWriter(System.err, Charset.defaultCharset()),
				true);
		helpFormatter.printHelp(systemErrPrintWriter,
				HelpFormatter.DEFAULT_WIDTH, "\"" + getClass().getSimpleName()
						+ ".jar\"", null, options,
				HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD,
				Constants.SCAPE_COPYRIGHT_STATEMENT, true);
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
			JAXBException, SAXException {

		int exitCode = 0;

		ComponentUploader componentUploader = new ComponentUploader();

		try {

			CommandLine parseArguments = componentUploader.parseArguments(args);

			logger.debug("<" + parseArguments.getOptionValue("u") + ">\n" + "<"
					+ parseArguments.getOptionValue("p") + ">\n" + "<"
					+ parseArguments.getOptionValue("i") + ">\n" + "<"
					+ parseArguments.getOptionValue("s") + ">\n" + "<"
					+ parseArguments.getOptionValue("t") + ">\n" + "<"
					+ parseArguments.getOptionValue("c") + ">\n" + "<"
					+ parseArguments.getOptionValue("l") + ">\n" + "<"
					+ parseArguments.getOptionValue("d") + ">\n");

			if (!componentUploader.uploadComponentToMyExperiment(
					parseArguments.getOptionValue("u"),
					parseArguments.getOptionValue("p"),
					parseArguments.getOptionValue("i"),
					parseArguments.getOptionValue("t"),
					parseArguments.getOptionValue("s"),
					parseArguments.getOptionValue("c"),
					parseArguments.getOptionValue("l"),
					parseArguments.getOptionValue("d"))) {
				exitCode = 1;
			}
		} catch (ErrorParsingCmdArgsException e) {
			logger.error("[ERROR] " + e.getMessage());
			componentUploader.printUsage();
			exitCode = 2;
		}

		System.exit(exitCode);
	}
}
