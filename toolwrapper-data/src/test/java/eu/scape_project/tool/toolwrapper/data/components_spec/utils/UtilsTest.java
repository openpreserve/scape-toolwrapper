package eu.scape_project.tool.toolwrapper.data.components_spec.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import eu.scape_project.tool.toolwrapper.data.components_spec.Components;

import org.junit.Test;

public class UtilsTest {

    private static final String SPEC_FOLDER = "../developmentExamples/";
    private static final String COMPONENT_SPEC_XML = SPEC_FOLDER + "digital-preservation-migration-image-imagemagick-image2tiff.component";

    @Test
    public void shouldLoadComponentsFromFile() {
        Components components = Utils.createComponents(COMPONENT_SPEC_XML);

        assertNotNull(components);
        assertEquals(1, components.getComponent().size());
    }

}
