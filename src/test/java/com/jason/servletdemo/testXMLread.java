package com.jason.servletdemo;

import java.util.Map;

import com.jason.am.launcher.WebXMLContext;

public class testXMLread {

	public static void main(String[] args) {
		WebXMLContext w = new WebXMLContext();
		Map<String, String> m = w.getServletUrlMappings();

		for (Map.Entry<String, String> e : m.entrySet()) {
			System.out.println(e.getKey());
			System.out.println(e.getValue());
		}

	}
}
