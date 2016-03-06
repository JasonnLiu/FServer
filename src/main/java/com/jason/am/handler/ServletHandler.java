package com.jason.am.handler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jason.am.app.WebInfConfiguration;
import com.jason.am.container.Container;
import com.jason.am.http.HttpOutputStream;
import com.jason.am.http.Request;
import com.jason.am.http.Response;
import com.jason.am.launcher.WebXMLContext;

public class ServletHandler {

	private ClassLoader webappClassLoader = WebInfConfiguration
			.getWebAppClassLoader();

	private Map<String, String> servletUrlMappings;

	public ServletHandler() {
		WebXMLContext xmlCtx = new WebXMLContext();
		servletUrlMappings = xmlCtx.getServletUrlMappings();
	}

	public Response handle(Request req, String methodName, String requestURI) {

		Response resp = new Response();
		String className = null;
		className = servletUrlMappings.get(requestURI);
		if (null == className) {
			return null;
		}
		if (!methodName.equals("GET") && !methodName.equals("POST")) {
			return null;
		}
		try {
			System.out.println(className);
			Class<?> clazz = Thread.currentThread().getContextClassLoader()
					.loadClass(className);
			Method method;
			String servletMethodName = null;
			if (methodName.equals("GET")) {
				servletMethodName = "doGet";
			}
			if (methodName.equals("POST")) {
				servletMethodName = "doPost";
			}

			method = clazz.getDeclaredMethod(servletMethodName, new Class[] {
					HttpServletRequest.class, HttpServletResponse.class });
			method.setAccessible(true);
			method.invoke(clazz.newInstance(), new Object[] { req, resp });

		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		}
		return resp;

	}

}
