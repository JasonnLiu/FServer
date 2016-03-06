package com.jason.am.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class RequestDecoder {

	public static Request decode(Socket s) {
		BufferedReader br = null;
		String reqCmd = null;
		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			reqCmd = br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
	
		if (reqCmd == null) {
			return null;
		}
		String[] cmds = reqCmd.split("\\s");
		
		
		
		System.out.println(cmds[0]);
		System.out.println(cmds[1]);
		System.out.println(cmds[2]);

		// Invalid request command
		if (cmds.length != 3) {
			return null;
		}
		// Request method check
		if (!HttpMethod.isAccept(cmds[0])) {
			return null;
		}
		// URL check
		if (cmds[1].length() == 0) {
			return null;
		}
		// Http version ckeck
		if (cmds[2].length() == 0) {
			return null;
		}
		
		

		Request httpReq = new Request();
		httpReq.setMethod(cmds[0]);
		httpReq.setVersion(cmds[2]);

		// Read headers
		String line;
		int contentLength = 0;
		HashMap<String, String> headers = new HashMap<String, String>();
		try {
			while ((line = br.readLine()) != null && !line.equals("")) {

				int idx = line.indexOf(": ");
				if (idx == -1) {
					continue;
				}
				if (HttpHeaders.CONTENT_LENGTH.equals(line)) {
					try {
						contentLength = Integer.parseInt(line
								.substring(idx + 2).trim());
					} catch (RuntimeException e) {
						continue;
					}
				}
				headers.put(line.substring(0, idx), line.substring(idx + 2));
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		httpReq.setHeaders(headers);

		// Read url parameters
		String reqUrl = cmds[1];
		int idx = reqUrl.indexOf("?");
		if (idx > 0) { // valid
			HashMap<String, String> paramaterMap = new HashMap<String, String>();

			String paramater = reqUrl.substring(idx + 1).trim();
			String[] params = paramater.split("&");
			for (String entry : params) {
				String[] group = entry.split("=");

				if (group.length != 2) {// invalid param
					continue;
				}
				paramaterMap.put(group[0], group[1]);
			}
			httpReq.setParameters(paramaterMap);
			reqUrl = reqUrl.substring(0, idx);
		}
		httpReq.setRequestURI(reqUrl);

		/*
		 * POST request decoder
		 */
		if (HttpMethod.POST.name().equals(cmds[0])) {
			// Read post parameters
			if (contentLength > 0) {
				char[] buff = new char[contentLength];
				try {
					br.read(buff);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// decode paramater
				HashMap<String, String> paramaterMap = httpReq.getParameters();
				if (paramaterMap == null) {
					paramaterMap = new HashMap<String, String>();
				}

				String paramater = new String(buff);
				httpReq.setIs(new HttpInputStream(paramater.getBytes()));

				String[] params = paramater.split("&");
				for (String entry : params) {
					String[] group = entry.split("=");

					if (group.length != 2) {// invalid param
						continue;
					}
					paramaterMap.put(group[0], group[1]);
				}
				httpReq.setParameters(paramaterMap);
			}
		}

		try {
			s.shutdownInput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return httpReq;
	}

}
