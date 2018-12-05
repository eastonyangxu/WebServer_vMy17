package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.webserver.core.ServerContext;

public class HttpRequest {
	private Socket socket;
	private InputStream in;
	
	private String method;
	private String url;
	private String protocol;
	private Map<String,String> headers = new HashMap<String,String>();
	private String requestUrl;
	private String queryString;
	private Map<String,String> parameters = new HashMap<String,String>();
	
	public HttpRequest(Socket socket) throws EmptyRequestException {
		try {
			this.socket = socket;
			in = socket.getInputStream();
			
			parseReqeustLine();
			parseHeaders();
			parseContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void parseReqeustLine() throws EmptyRequestException {
		System.out.println("开始解析请求行");
		try {
			String line = readLine();
			String[] data = line.split("\\s");
			if(data.length<1) {
				throw new EmptyRequestException();
			}
			method = data[0];
			url = data[1];
			parseUrl();
			protocol = data[2];
			
			System.out.println("method:"+method);
			System.out.println("url:"+url);
			System.out.println("protocol:"+protocol);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("请求行解析完毕");
	}
	
	public void parseUrl() {
		try {
			url = URLDecoder.decode(url, ServerContext.URIEcoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(url.indexOf("?")!=-1) {
			String[] data = url.split("\\?");
			requestUrl = data[0];
			if(data.length>1) {
				queryString = data[1];
				parseParameters(queryString);
			}
			System.out.println("requestUrl:"+requestUrl);
			System.out.println("queryString:"+queryString);
			System.out.println("parameters:"+parameters);
		}else{
			requestUrl = url;
		}
		
	}

	private void parseParameters(String line) {
		String[] paras = line.split("&");
		for(String para: paras) {
			String[] arr = para.split("=");
			if(arr.length>1) {
				parameters.put(arr[0], arr[1]);
			}else {
				parameters.put(arr[0], null);
			}
		}
	}
	
	public void parseHeaders() {
		System.out.println("开始解析消息头");
		while(true) {
			try {
				String line = readLine();
				if("".equals(line)) {
					break;
				}
				String[] data = line.split(": ");
				headers.put(data[0], data[1]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("headers:"+headers);
		System.out.println("消息头解析完毕");
	}
	
	public void parseContent() {
		System.out.println("开始解析正文");
		
		if(headers.containsKey("Content-Length")) {
			int length = Integer.parseInt(headers.get("Content-Length"));
			try {
				byte[] data = new byte[length];
				in.read(data);
				String ContentType = headers.get("Content-Type");
				if("application/x-www-form-urlencoded".equals(ContentType)) {
					String line = new String(data,"ISO8859-1");
					line = URLDecoder.decode(line, ServerContext.URIEcoding);
					parseParameters(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("正文解析完毕");
	}
	
	public String readLine() throws IOException {
		StringBuilder builder = new StringBuilder();
		
		char c1='a',c2='a';
		int d = -1;
		while((d=in.read())!=-1) {
			c2 = (char)d;
			if(c1==HttpContext.CR && c2==HttpContext.LF) {
				break;
			}
			builder.append(c2);
			c1 = c2;
		}
		return builder.toString().trim();
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public String getQueryString() {
		return queryString;
	}

	public String getParameters(String name) {
		return parameters.get(name);
	}
	
	
}
