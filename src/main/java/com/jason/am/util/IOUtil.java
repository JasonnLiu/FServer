package com.jason.am.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {
	public static int bufferSize = 64 * 1024;

	public static byte[] readBytes(InputStream in) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		copy(in, bout);
		return bout.toByteArray();
	}

	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		copy(in, out, -1);
	}

	public static void copy(InputStream in, OutputStream out, long byteCount)
			throws IOException {

		byte buffer[] = new byte[bufferSize];
		int len = bufferSize;

		if (byteCount >= 0) {
			while (byteCount > 0) {
				int max = byteCount < bufferSize ? (int) byteCount : bufferSize;
				len = in.read(buffer, 0, max);

				if (len == -1)
					break;

				byteCount -= len;
				out.write(buffer, 0, len);
			}
		} else {
			while (true) {
				len = in.read(buffer, 0, bufferSize);
				if (len < 0)
					break;
				out.write(buffer, 0, len);
			}
		}
	}

}
