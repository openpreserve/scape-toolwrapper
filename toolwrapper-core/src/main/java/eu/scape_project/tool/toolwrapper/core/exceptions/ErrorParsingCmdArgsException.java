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
package eu.scape_project.tool.toolwrapper.core.exceptions;

/**
 * Exception that can be used when an expected set of options of a command-line
 * execution wasn't properly provided
 */
public class ErrorParsingCmdArgsException extends Exception {
	private static final long serialVersionUID = 3680269489361878011L;

	/** Empty constructor (calls {@link Throwable} empty constructor) */
	public ErrorParsingCmdArgsException() {
		super();
	}

	/**
	 * Constructor which receives the error message (calls {@link Throwable}
	 * with the provided error message)
	 * 
	 * @param message
	 *            error message
	 */
	public ErrorParsingCmdArgsException(String message) {
		super(message);
	}

	/**
	 * Constructor which receives the error cause (calls {@link Throwable} with
	 * the provided error cause)
	 * 
	 * @param cause
	 *            error cause
	 */
	public ErrorParsingCmdArgsException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor which receives the error message & cause (calls
	 * {@link Throwable} with the provided error message & cause)
	 * 
	 * @param message
	 *            error message
	 * @param cause
	 *            error cause
	 */
	public ErrorParsingCmdArgsException(String message, Throwable cause) {
		super(message, cause);
	}
}
