package com.jason.am.launcher;

import com.jason.am.http.server.WebServer;



public class Bootstrap {
	
	/**
	 * configuration setting and start server
	 */
	public void launch() {
		WebServer svr = new WebServer();
		
		
		svr.start();
	}
	
}
