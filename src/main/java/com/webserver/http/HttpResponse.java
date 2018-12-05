package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.webserver.core.ServerContext;

public class HttpResponse {
	
	private Socket socket;
	private OutputStream out;
	private File file;
	private Map<String,String> headers = new HashMap<String,String>();
	private int statusCode = 400;
	private String statusReason = "OK";
	
	public HttpResponse(Socket socket) {
		try {
			this.socket = socket;
			out = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void fulsh() {
		sendStatusLine();
		sendHeaders();
		sendContent();
	}
	
	public void sendStatusLine() {
		System.out.println("正在响应状态行");
		try {
			String line = ServerContext.protocol+" "+statusCode+" "+statusReason;
			println(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("状态行响应完毕");
	}
	
	public void sendHeaders() {
		System.out.println("响应消息头");
		try {
			
			Set<Entry<String,String>> EntrySet = headers.entrySet();
			for(Entry<String,String> e: EntrySet) {
				String key = e.getKey();
				String value = e.getValue();
				String line = key+": "+value;
				println(line);
			}
			println("");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("消息头响应完毕");
	}

	private void println(String line) throws UnsupportedEncodingException, IOException {
		byte[] data = line.getBytes("ISO8859-1");
		out.write(data);
		out.write(HttpContext.CR);
		out.write(HttpContext.LF);
	}
	
	public void sendContent() {
		try (FileInputStream in = new FileInputStream(file);)
		{
			byte[] data = new byte[(int)file.length()];
			int len = -1;
			while((len=in.read(data))!=-1) {
				out.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		headers.put("Content-Length", file.length()+"");
		String ext = file.getName().substring(file.getName().lastIndexOf(".")+1);
		String ContentType = HttpContext.getMimeMapping(ext);
		headers.put("Content-Type", ContentType);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		statusReason = HttpContext.getStatusReason(statusCode);
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	
}
