package com.webserver.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ServerContext {
	public static String URIEcoding = "UTF-8";
	public static int port = 8088;
	public static String protocol = "HTTP/1.1";
	public static int maxThreads = 150;
	private static Map<String,String> servletMapping = new HashMap<String,String>();
	
	static {
		initServer();
		initServletMapping();
	}
	
	public static void initServer() {
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read("conf/server.xml");
			Element root = doc.getRootElement();
			Element e = root.element("Connector");
			
			URIEcoding = e.attributeValue("URIEncoding");
			port = Integer.parseInt(e.attributeValue("port"));
			protocol = e.attributeValue("protocol");
			maxThreads = Integer.parseInt(e.attributeValue("maxThreads"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	public static void initServletMapping() {
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read("conf/servlets.xml");
			Element root = doc.getRootElement();
			List<Element> elements = root.elements("Servlet");
			for(Element e: elements) {
				String key = e.attributeValue("url");
				String value = e.attributeValue("className");
				servletMapping.put(key, value);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}

	public static String getServletMapping(String url) {
		return servletMapping.get(url);
	}
	
	public static void main(String[] args) {
		System.out.println(URIEcoding);
		System.out.println(servletMapping);
	}
	
}
