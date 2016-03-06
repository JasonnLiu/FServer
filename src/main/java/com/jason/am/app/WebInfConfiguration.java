package com.jason.am.app;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class WebInfConfiguration {

	private static final String WEB_INF_LIB_PATH = "webapps/WEB-INF/lib";

	private static final String WEB_INF_CLASSES_PATH = "webapps/WEB-INF/classes";

	private static ClassLoader WEB_APP_CLASS_LOADER;

	static {
		try {
			new WebInfConfiguration().scanWebappClassesAndLibs();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private void scanWebappClassesAndLibs() throws MalformedURLException {
		List<URL> resourceUrl = new ArrayList<URL>();
		scanWebInfLib(resourceUrl);
		scanWebappClasses(resourceUrl);

		WEB_APP_CLASS_LOADER = new URLClassLoader(
				resourceUrl.toArray(new URL[resourceUrl.size()]));
	}

	private void scanWebInfLib(List<URL> resourceUrl)
			throws MalformedURLException {
		File webappLib = new File(WEB_INF_LIB_PATH);

		File[] libs = webappLib.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar") ? true : false;
			}
		});

		if (libs == null || libs.length == 0) {
			return;
		}

		for (int i = 0, size = libs.length; i < size; i++) {
			resourceUrl.add(new URL(libs[i].toURI().toString()));
		}
	}

	private void scanWebappClasses(List<URL> resourceUrl)
			throws MalformedURLException {
		File webappClasses = new File(WEB_INF_CLASSES_PATH);

		File[] classes = webappClasses.listFiles();

		if (classes == null || classes.length == 0) {
			return;
		}

		for (int i = 0, size = classes.length; i < size; i++) {

			getClassesFile(classes[i], resourceUrl);
		}
	}

	/**
	 * Depth-first scanning
	 */
	private void getClassesFile(File source, List<URL> resourceUrl)
			throws MalformedURLException {
		if (source.exists() && source.isDirectory()) {

			for (File item : source.listFiles()) {
				getClassesFile(item, resourceUrl);
			}
		} else {
			resourceUrl.add(new URL(source.toURI().toString()));
		}
	}

	public static ClassLoader getWebAppClassLoader() {
		return WEB_APP_CLASS_LOADER;
	}

}
