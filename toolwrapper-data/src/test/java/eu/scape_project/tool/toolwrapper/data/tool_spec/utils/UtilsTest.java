package eu.scape_project.tool.toolwrapper.data.tool_spec.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import eu.scape_project.tool.toolwrapper.data.tool_spec.Tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class UtilsTest {

    private static final String SPEC_FOLDER = "../developmentExamples/";
    private static final String TOOL_SPEC_XML = SPEC_FOLDER + "digital-preservation-migration-image-imagemagick-image2tiff.xml";

    @Test
    public void shouldLoadToolFromFile() throws JAXBException, IOException, SAXException {
        Tool image2Tiff = Utils.createTool(TOOL_SPEC_XML);
        assertToolHasValues(image2Tiff);
    }

    private void assertToolHasValues(Tool image2Tiff) {
        assertNotNull(image2Tiff);

        assertEquals("Apache-2.0", image2Tiff.getLicense().getName().value());

        assertEquals(1, image2Tiff.getOperations().getOperation().size());
    }

    @Test
    public void shouldLoadToolFromStream() throws JAXBException, IOException, SAXException {
        final InputStream toolSpecStream = new FileInputStream(TOOL_SPEC_XML);
        try {
            Tool image2Tiff = Utils.fromInputStream(toolSpecStream);
            assertToolHasValues(image2Tiff);

        } finally {
            toolSpecStream.close();
        }
    }

    @Test(expected = UnmarshalException.class)
    public void shouldFailOnWellFormedXmlNotMatchingSchema() throws JAXBException, IOException, SAXException {
        final InputStream toolSpecStream = getClass().getResourceAsStream("/missingToolName.xml");
        try {
            Utils.fromInputStream(toolSpecStream);
        } finally {
            toolSpecStream.close();
        }
    }
}
