package com.webserver.servlets;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.server.ServerCloneException;
import java.util.Arrays;

import com.webserver.core.ServerContext;
import com.webserver.http.HttpContext;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public class RegServlet extends HttpServlets{
	
	public void service(HttpRequest request, HttpResponse response) {
		System.out.println("开始处理注册");
		String username = request.getParameters("username");
		String password = request.getParameters("password");
		String nickname = request.getParameters("nickname");
		int age = Integer.parseInt(request.getParameters("age"));
		System.out.println("uername:"+username+";password:"+password+";nickname:"+nickname+";age"+age);
		
		try (RandomAccessFile raf = new RandomAccessFile("user.dat","rw");)
		{
			raf.seek(raf.length());
			byte[] data = username.getBytes(ServerContext.URIEcoding);
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			data = password.getBytes(ServerContext.URIEcoding);
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			data = nickname.getBytes(ServerContext.URIEcoding);
			data = Arrays.copyOf(data, 32);
			raf.write(data);
			
			raf.writeInt(age);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		response.setFile(new File("webapps/myweb/reg_success.html"));
		System.out.println("注册成功");
	}
}
