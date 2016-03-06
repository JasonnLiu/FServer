package com.jason.am.launcher;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import com.jason.am.exception.ServerException;

public class WebXMLContext {
	private static final String WEB_XML_PATH = "webapps/WEB-INF/web.xml";
	// private static final String WEB_XML_PATH = "H:\\ljx\\test.xml";

	private Map<String, String> servletUrlMappings = new HashMap<String, String>();

	public WebXMLContext() {
		Map<String, ServletDefinition> servletDefines = new HashMap<String, ServletDefinition>();
		try {
			readServletNodes(servletDefines);

			readServletMapping(servletDefines);

			patternUrls(servletDefines);

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private void readServletNodes(Map<String, ServletDefinition> servletDefines)
			throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new File(WEB_XML_PATH));
		Map<String, String> nsMap = new HashMap<String, String>();
		// add namaspace

		// nsMap.put("ns", "http://java.sun.com/xml/ns/javaee");
		XPath xsub = document.createXPath("//web-app/servlet");
		// xsub.setNamespaceURIs(nsMap);

		// nsMap.put("ns", "http://java.sun.com/xml/ns/javaee");
		// XPath xsub = document.createXPath("//ns:web-app/ns:servlet");
		// xsub.setNamespaceURIs(nsMap);

		@SuppressWarnings("unchecked")
		List<Element> servlets = xsub.selectNodes(document);

		for (Element element : servlets) {

			if (element.elements().size() != 2) {
				throw new ServerException(
						"Read web.xml error : invalid servlet configurtion !");
			}
			String name = element.element("servlet-name").getText();
			String clazz = element.element("servlet-class").getText();
			if (StringUtils.isEmpty(name) || StringUtils.isEmpty(clazz)) {
				throw new ServerException(
						"Read web.xml error : invalid servlet configurtion !");
			}
			ServletDefinition sd = new ServletDefinition(name, clazz);
			servletDefines.put(name, sd);
		}
	}

	private void readServletMapping(
			Map<String, ServletDefinition> servletDefines)
			throws DocumentException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(new File(WEB_XML_PATH));
		Map<String, String> nsMap = new HashMap<String, String>();
		// add namaspace

		// nsMap.put("ns", "http://java.sun.com/xml/ns/javaee");
		XPath xsub = document.createXPath("//web-app/servlet-mapping");
		// xsub.setNamespaceURIs(nsMap);

		// nsMap.put("ns", "http://java.sun.com/xml/ns/javaee");
		// XPath xsub = document.createXPath("//ns:web-app/ns:servlet");
		// xsub.setNamespaceURIs(nsMap);

		@SuppressWarnings("unchecked")
		List<Element> servletMaps = xsub.selectNodes(document);

		for (Element element : servletMaps) {

			if (element.elements().size() != 2) {
				throw new ServerException(
						"Read web.xml error : invalid servlet configurtion !");
			}
			String name = element.element("servlet-name").getText();
			String urlPattern = element.element("url-pattern").getText();
			if (StringUtils.isEmpty(name) || StringUtils.isEmpty(urlPattern)) {
				throw new ServerException(
						"Read web.xml error : invalid servlet configurtion !");
			}
			ServletDefinition sd = servletDefines.get(name);
			if (sd == null) {
				throw new ServerException("Read web.xml error : canot find "
						+ name + " servlet defined !");
			}
			sd.setUrlPattern(urlPattern);
		}
	}

	private void patternUrls(Map<String, ServletDefinition> servletDefines) {
		for (Map.Entry<String, ServletDefinition> entry : servletDefines
				.entrySet()) {
			ServletDefinition sd = entry.getValue();
			servletUrlMappings.put(sd.getUrlPattern(), sd.getClassName());
		}
	}

	public Map<String, String> getServletUrlMappings() {
		return servletUrlMappings;
	}
}
