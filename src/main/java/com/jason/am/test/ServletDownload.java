package com.jason.am.test;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletDownload extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String path = "/обть.png";
		String realPath = this.getServletContext().getRealPath(path);
		System.out.println(realPath);
		//InputStream is = new FileInputStream(realPath);
		InputStream is = this.getServletContext().getResourceAsStream(path);
		String filename = path.substring(path.lastIndexOf("/") + 1);
		resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf8"));
		OutputStream os = resp.getOutputStream();
		byte[] b = new byte[1024];
		int len ;
		while ((len = is.read(b)) > 0) {
			os.write(b, 0, len);;
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

}
