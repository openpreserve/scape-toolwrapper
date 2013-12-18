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
package eu.scape_project.tool.toolwrapper.core;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.xml.sax.SAXException;

import eu.scape_project.tool.toolwrapper.core.configuration.Constants;
import eu.scape_project.tool.toolwrapper.core.exceptions.ErrorParsingCmdArgsException;
import eu.scape_project.tool.toolwrapper.core.exceptions.SpecParsingException;
import eu.scape_project.tool.toolwrapper.data.tool_spec.Tool;
import eu.scape_project.tool.toolwrapper.data.tool_spec.utils.Utils;

/**
 * Abstract class that should be extended if one wants to implement a toolspec
 * related component for being used/invoked through the command-line. This way,
 * common command-line parameters are already defined (toolspec and output
 * directory), even if others can the added this are considered as the default
 * ones.
 * */
public abstract class ToolWrapperCommandline {
	private Options options;

	/** Public empty constructor */
	public ToolWrapperCommandline() {
		Option opt;
		options = new Options();
		opt = new Option("t", "toolspec", true, "toolspec file location");
		opt.setRequired(true);
		options.addOption(opt);
		opt = new Option("o", "outDir", true,
				"directory where to put the generated artifacts");
		opt.setRequired(true);
		options.addOption(opt);
	}

	/**
	 * Method used to print command-line syntax (usage) using
	 * {@link HelpFormatter}
	 */
	public void printUsage() {
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

	/**
	 * Method that parses the program arguments {@code args} and returns an
	 * object that represents that same arguments ({@link CommandLine})
	 * 
	 * @param args
	 *            command line arguments array
	 * @return a {@link CommandLine} object that reflects those same parameters
	 * @throws ErrorParsingCmdArgsException
	 */
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

	/**
	 * Method that assesses if the minimum command-line parameters were
	 * provided, as well as if the {@link Tool} object can be created
	 * 
	 * @param args
	 *            command line arguments array
	 * @return an {@link ImmutablePair} object containing both the
	 *         {@link CommandLine} object and the associated {@link Tool} object
	 */
	public ImmutablePair<CommandLine, Tool> processToolWrapperGenerationRequest(
			String[] args) throws SpecParsingException,
			ErrorParsingCmdArgsException {
		ImmutablePair<CommandLine, Tool> pair = null;
		Tool tool = null;
		CommandLine cmd = parseArguments(args);
		try {
			if (isCommandLineParamentersValid(cmd)) {
				tool = Utils.createTool(cmd.getOptionValue("t"));
				pair = ImmutablePair.of(cmd, tool);
			} else {
				throw new SpecParsingException(
						"Unable to process the toolspec provided! Please check it and also if all mandatory parameters were provided properly!");
			}
		} catch (JAXBException e) {
			throw new SpecParsingException(
					"Unable to process the toolspec provided! Please check it and also if all mandatory parameters were provided properly!",
					e);
		} catch (SAXException e) {
			throw new SpecParsingException("The XML Schema is not valid!", e);
		} catch (IOException e) {
			throw new SpecParsingException(
					"An error occured while copying the XML Schema from the resources to a temporary location!",
					e);
		}
		return pair;
	}
	
	private boolean isCommandLineParamentersValid(CommandLine cmd){
		return cmd != null && cmd.hasOption("t") && cmd.hasOption("o");
	}

	/** Getter for the options instance variable */
	public Options getOptions() {
		return options;
	}
}
