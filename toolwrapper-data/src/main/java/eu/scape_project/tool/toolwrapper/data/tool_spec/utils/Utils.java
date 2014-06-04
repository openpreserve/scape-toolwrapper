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
package eu.scape_project.tool.toolwrapper.data.tool_spec.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

import eu.scape_project.tool.toolwrapper.data.tool_spec.Tool;

/**
 * Utility class with static methods useful to interact with toolspec info
 */
public final class Utils {
	private static final String TOOLSPEC_FILENAME_IN_RESOURCES = "/tool-1.1_draft.xsd";

    private static JAXBContext jc;
    
    static {
        try {
            jc  = createContext();
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static JAXBContext createContext() throws JAXBException {
        return JAXBContext.newInstance(Tool.class);
    }
    
	private Utils() {
	}
	
	/**
	 * Method that creates a {@link Tool} instance from the provided toolspec
	 * file, validating it against toolspec XML Schema
	 * 
	 * @param toolspecFilePath
	 *            path to the toolspec file
	 */
	public static Tool createTool(String toolspecFilePath)
			throws JAXBException, IOException, SAXException {
	    return fromInputStream(new FileInputStream(new File(toolspecFilePath)));
	}

    /**
     * Unmarshals an input stream of xml data to a Tool.
     */
	public static Tool fromInputStream(InputStream input) throws JAXBException, IOException, SAXException {
        Unmarshaller unmarshaller = Utils.createUnmarshaller();
        JAXBElement<Tool> unmarshalled = unmarshaller.unmarshal(new StreamSource(input), Tool.class);
        return unmarshalled.getValue();
    }
	
	private static Unmarshaller createUnmarshaller() throws JAXBException, IOException, SAXException {
        Unmarshaller unmarshaller = jc.createUnmarshaller();
		setSchemaTo(unmarshaller);
        return unmarshaller;
    }

    private static void setSchemaTo(Unmarshaller unmarshaller) throws IOException, SAXException {
        // copy XML Schema from resources to a temporary location
		File schemaFile = File.createTempFile("schema", null);
		FileUtils.copyInputStreamToFile(Utils.class
						.getResourceAsStream(TOOLSPEC_FILENAME_IN_RESOURCES),
						schemaFile);
		Schema schema = SchemaFactory.newInstance(
				XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaFile);

		// validate provided toolspec against XML Schema
		unmarshaller.setSchema(schema);
    }
}
