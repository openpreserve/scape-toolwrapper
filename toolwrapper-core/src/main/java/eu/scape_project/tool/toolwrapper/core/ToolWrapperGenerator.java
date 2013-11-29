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

import eu.scape_project.tool.toolwrapper.data.tool_spec.Operation;
import eu.scape_project.tool.toolwrapper.data.tool_spec.Tool;

/** Interface to follow when implementing a toolwrapper generator */
public interface ToolWrapperGenerator {

	/**
	 * Method that does the heavy work, i.e., generates the desired artifact(s)
	 * (bash wrapper, Debian package, etc...)
	 * 
	 * @param tool
	 *            tool from which the artifact(s) is being created
	 * @param operation
	 *            if the artifact(s) is related to a particular
	 *            {@link Operation}
	 * @param outputDirectory
	 *            directory where to place the created artifact(s)
	 */
	boolean generateWrapper(Tool tool, Operation operation,
			String outputDirectory);
}
