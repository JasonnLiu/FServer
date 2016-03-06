package com.jason.am.handler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import com.jason.am.http.HttpHeaders;
import com.jason.am.http.HttpOutputStream;
import com.jason.am.http.HttpStatus;
import com.jason.am.http.MimeTypes;
import com.jason.am.http.Request;
import com.jason.am.http.RequestDecoder;
import com.jason.am.http.Response;
import com.jason.am.resource.Resource;
import com.jason.am.util.IOUtil;
import com.jason.am.util.StringUtil;

public class RequestHandler implements Runnable {

	private Socket s;

	private ServletHandler sh = new ServletHandler();

	private static String resourceBase = "webapps";

	private static final SimpleDateFormat GMT_SDF = new SimpleDateFormat(
			"EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);

	public RequestHandler(Socket socket) {
		this.s = socket;
	}

	public void run() {
		Request req;
		Response resp = null;

		req = RequestDecoder.decode(s);

		String methodName = req.getMethod();

		String requestURI = req.getRequestURI();
		System.out.println(requestURI);

		// default index.html page
		if (requestURI.equals("/")) {
			requestURI = "/index.html";
		}

		resp = sh.handle(req, methodName, requestURI);

		if (null == resp) {
			resp = new Response();
			Resource resource = new Resource(
					new File(resourceBase + requestURI), requestURI);

			if (!resource.exists()) {
				resp.setStatus(HttpStatus.NOT_FOUND_404);
				try {
					File file = new File(resourceBase + StringUtil.PAGE_404);
					FileInputStream in = new FileInputStream(file);
					resp.setBody(IOUtil.readBytes(in));
					in.close();
					resp.getHeaders().put(HttpHeaders.CONTENT_LENGTH, String.valueOf(resp.getBody().length));
					resp.getHeaders().put(HttpHeaders.CONTENT_TYPE, MimeTypes.TEXT_HTML.getType());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// Last-Modified
			HashMap<String, String> headers = resp.getHeaders();
			String lastModified = GMT_SDF.format(new Date(resource
					.lastModified()));
			headers.put(HttpHeaders.LAST_MODIFIED, lastModified);

			// ETag check
			String ifnm = req.getHeader(HttpHeaders.IF_NONE_MATCH);
			String etag = resource.getWeakETag();
			if (ifnm != null && ifnm.equals(etag)) {

				resp.setStatus(HttpStatus.NOT_MODIFIED_304);

			} else {
				headers.put(HttpHeaders.ETAG, etag);
			}
			// check resource type
			int postfixIdx = requestURI.lastIndexOf(".");
			if (postfixIdx > 0) {
				headers.put(HttpHeaders.CONTENT_TYPE,
						MimeTypes.getType(requestURI.substring(postfixIdx)));
			}

			byte[] outBytes = resp.getBody();

			
			headers.put(HttpHeaders.CONTENT_LENGTH,
					String.valueOf(outBytes.length));
		}else{
			HashMap<String, String> headers = resp.getHeaders();
			String contentType = resp.getContentType();
			if(contentType == null) {
				String encoding = resp.getCharacterEncoding();
				if(encoding != null) {
					headers.put(HttpHeaders.CONTENT_TYPE, MimeTypes.TEXT_HTML.getType() + "; " + encoding);
				}
			}
			HttpOutputStream hos = null;
			try {
				hos = (HttpOutputStream) resp.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] outbytes = hos.getOut().toByteArray();
			resp.setBody(outbytes);
		}

		try {
			OutputStream os = s.getOutputStream();
			// config status message
			String respStat = HttpStatus.getMessage(resp.getStatus());

			StringBuilder headers = new StringBuilder();
			headers.append(resp.getHttpVersion() + " " + resp.getStatus() + " "
					+ respStat + StringUtil.CRLF);

			// write headers
			for (Map.Entry<String, String> header : resp.getHeaders()
					.entrySet()) {
				headers.append(header.getKey() + ": " + header.getValue()
						+ StringUtil.CRLF);
			}

			headers.append(StringUtil.CRLF);
			os.write(headers.toString().getBytes());
			if (resp.getBody() != null) {
				String encoding = resp.getCharacterEncoding();

				if (encoding != null) {
					Charset charset = Charset.forName(encoding);
					OutputStreamWriter writer = new OutputStreamWriter(os,
							charset);
					writer.write(new String(resp.getBody(), charset));

					writer.close();
				} else {
					os.write(resp.getBody());
				}
			}

			os.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
