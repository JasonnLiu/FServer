package com.jason.am.http;

public enum HttpMethod {
	GET, POST;

	public static boolean isAccept(String method) {
		for (HttpMethod m : HttpMethod.values()) {
			if (m.name().equals(method)) {
				return true;
			}
		}
		return false;
	}

	public static HttpMethod getMethod(String method) {
		for (HttpMethod m : HttpMethod.values()) {
			if (m.name().equals(method)) {
				return m;
			}
		}
		return null;
	}
}
