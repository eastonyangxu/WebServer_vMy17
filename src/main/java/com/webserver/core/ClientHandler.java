package com.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlets.HttpServlets;
import com.webserver.servlets.LoginServlet;
import com.webserver.servlets.RegServlet;

public class ClientHandler implements Runnable {
	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		
		try {
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			String url = request.getRequestUrl();
			
			String servletName = ServerContext.getServletMapping(url);
			if(servletName!=null) {
				Class cls = Class.forName(servletName);
				HttpServlets servlet = (HttpServlets)cls.newInstance();
				servlet.service(request, response);
			}else {
				File file = new File("webapps"+url);
				if(file.exists()) {
					System.out.println("该资源已找到");
					response.setFile(file);
				}else {
					System.out.println("该资源不存在");
					response.setFile(new File("webapps/root/404.html"));
				}
			}
			response.fulsh();
			
		}catch (EmptyRequestException e) {
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
