package eu.scape_project.tool.toolwrapper.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {
	private File temporaryDirectory = null, temporaryDirectory2 = null,
			temporaryDirectory3 = null;

	@Test
	public void createTemporaryDirectory() {
		String tempDirFileName = "tempDir1";
		temporaryDirectory = Utils.createTemporaryDirectory(tempDirFileName);
		Assert.assertTrue(temporaryDirectory != null);
	}

	@Test(expected = NullPointerException.class)
	public void createTemporaryDirectory2() {
		temporaryDirectory = Utils.createTemporaryDirectory(null);
		Assert.assertTrue(temporaryDirectory == null);
	}

	@Test
	public void copyResourceToTemporaryDirectory() {
		String tempDirFileName = "tempDir2";
		String tempFileName = "testFile.txt";
		try {
			temporaryDirectory2 = File.createTempFile(tempDirFileName, "");
			Assert.assertTrue(temporaryDirectory2.delete());
			Assert.assertTrue(Utils.copyResourceToTemporaryDirectory(
					temporaryDirectory2, "/", tempFileName, null, true,
					this.getClass()));
			File tempResourceFile = new File(temporaryDirectory2, tempFileName);
			Assert.assertTrue(tempResourceFile != null
					&& tempResourceFile.length() > 0);
		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void writeTemplateContent() {
		String tempDirFileName = "tempDir3";
		String testFileName = "oneTestFile.txt";
		StringWriter w = new StringWriter();
		try {
			w.append('a');
			temporaryDirectory3 = File.createTempFile(tempDirFileName, "");
			Assert.assertTrue(temporaryDirectory3.delete()
					&& temporaryDirectory3.mkdir());
			Assert.assertTrue(Utils.writeTemplateContent(temporaryDirectory3,
					null, testFileName, w, false));
			File f = new File(temporaryDirectory3, testFileName);
			Assert.assertTrue(f.exists() && f.length() > 0);
		} catch (IOException e) {
			Assert.fail();
		}
	}

	@Test
	public void wrapWithDoubleQuotes() {
		String original = "lala";
		String wrappedVersion = Utils.wrapWithDoubleQuotes(original);
		Assert.assertEquals("\"" + original + "\"", wrappedVersion);
	}

	@After
	public void cleanUp() {
		if (temporaryDirectory != null) {
			FileUtils.deleteQuietly(temporaryDirectory);
		}
		if (temporaryDirectory2 != null) {
			FileUtils.deleteQuietly(temporaryDirectory2);
		}
		if (temporaryDirectory3 != null) {
			FileUtils.deleteQuietly(temporaryDirectory3);
		}
	}
}
