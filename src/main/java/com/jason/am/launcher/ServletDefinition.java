package com.jason.am.launcher;

/**
 * Servlet Definition in web.xml <servlet> and <servlet-mapping> nodes
 */
public class ServletDefinition {
	
	private String name;
	private String className;
	private String urlPattern;
	
	public ServletDefinition(String name, String className) {
		this.name = name;
		this.className = className;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getUrlPattern() {
		return urlPattern;
	}
	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}
	
}
