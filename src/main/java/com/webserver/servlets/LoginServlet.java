package com.webserver.servlets;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.webserver.core.ServerContext;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class LoginServlet extends HttpServlets{
	
	public void service(HttpRequest request, HttpResponse response) {
		System.out.println("开始处理登录");
		String username = request.getParameters("username");
		String password = request.getParameters("password");
		System.out.println("username:"+username+";password:"+password);
				
		boolean check = false;
		try (RandomAccessFile raf = new RandomAccessFile("user.dat","r");)
		{
			for(int i=0;i<raf.length()/100;i++) {
				raf.seek(i*100);
				byte[] data = new byte[32];
				raf.read(data);
				String name = new String(data,ServerContext.URIEcoding).trim();
				if(name.equals(username)) {
					data = new byte[32];
					raf.read(data);
					String pwd = new String(data,ServerContext.URIEcoding).trim();
					if(pwd.equals(password)) {
						check = true;
						break;
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(check) {
			response.setFile(new File("webapps/myweb/login_success.html"));
		}else {
			response.setFile(new File("webapps/myweb/login_fail.html"));
		}
		System.out.println("登录完毕");
	}
	
}
