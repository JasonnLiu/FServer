package com.jason.am.launcher;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.jason.am.http.server.WebServer;

public class Main {

	public static void main(String[] args) {
		ClassLoader cl = createClassLoader();
		Thread.currentThread().setContextClassLoader(cl);
		Class<?> startupClass;
		try {
			startupClass = Thread.currentThread().getContextClassLoader()
					.loadClass("com.jason.am.launcher.Bootstrap");
			Object instance = startupClass.newInstance();
			startupClass.getMethod("launch").invoke(instance);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

	}

	public static ClassLoader createClassLoader() {

		File commonLibDir = new File("lib");

		File[] repositories = commonLibDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar") ? true : false;
			}
		});
		for (File f : repositories) {
			System.out.println(f.getName());
			System.out.println(f.toURI().toString());
		}

		URL[] urls = new URL[repositories.length];
		for (int i = 0, size = repositories.length; i < size; i++) {
			try {
				urls[i] = new URL(repositories[i].toURI().toString());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = Main.class.getClassLoader();
			if (parent == null) {
				parent = ClassLoader.getSystemClassLoader();
			}
		}
		return new URLClassLoader(urls, parent);
	}

}
