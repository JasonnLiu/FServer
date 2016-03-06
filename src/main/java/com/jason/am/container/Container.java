package com.jason.am.container;

import javax.servlet.http.HttpServlet;

public interface Container {
	
	
	public HttpServlet getServlet();
	
	public HttpServlet registerServlet();
}
