package eu.scape_project.tool.toolwrapper.toolwrapper_bash_debian_generator.utils;

import java.io.File;
import java.io.FileFilter;

/** Class that implements the {@link FileFilter} to Debian packages files */
public class DebianFileFilter implements FileFilter {
	@Override
	public boolean accept(File file) {
		return file.getName().endsWith("deb");
	}
}
