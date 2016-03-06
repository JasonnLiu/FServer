package com.jason.am.resource;

import java.io.File;
import java.io.IOException;

import com.jason.am.util.B64Code;


public class Resource {
	private File file;
	private String uri;

	public Resource(File file, String uri) {
		this.file = file;
		this.uri = uri;
	}

	public File getFile() {
		return this.file;
	}

	public boolean exists() {
		return file.exists();
	}

	public long lastModified() {
		return file.lastModified();
	}

	/**
	 * ETag hash
	 */
	public String getWeakETag() {
		try {
			StringBuilder b = new StringBuilder(32);
			b.append("M/\"");

			int length = uri.length();
			long lhash = 0;
			for (int i = 0; i < length; i++)
				lhash = 31 * lhash + uri.charAt(i);

			B64Code.encode(file.lastModified() ^ lhash, b);
			B64Code.encode(length ^ lhash, b);
			b.append('"');
			return b.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
