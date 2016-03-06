package com.jason.am.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class HttpOutputStream extends ServletOutputStream {
	
	protected final ByteArrayOutputStream out = new ByteArrayOutputStream();
	
	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	public ByteArrayOutputStream getOut() {
		return out;
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		// TODO Auto-generated method stub
		
	}
	
}
