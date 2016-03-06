package com.jason.am.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.jason.am.handler.RequestHandler;

public class WebServer extends Thread {

	private ServerSocket serverSocket;

	private Executor threadpool;

	public WebServer() {
		try {
			serverSocket = new ServerSocket(80, 10);
			threadpool = Executors.newFixedThreadPool(10);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		while (true) {
			try {
				threadpool.execute(new RequestHandler((serverSocket.accept())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
