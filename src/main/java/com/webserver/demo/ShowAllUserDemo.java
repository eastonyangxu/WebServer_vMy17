package com.webserver.demo;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ShowAllUserDemo {
	public static void main(String[] args) throws IOException {
		RandomAccessFile raf = new RandomAccessFile("user.dat","r");
		for(int i=0;i<raf.length()/100;i++){
			//读取用户名
			//连续读取32字节，将其转换为字符串
			byte[] data = new byte[32];
			raf.read(data);
			String username = new String(data,"UTF-8").trim();
			System.out.print("用户名："+username);
			
			//读密码
			raf.read(data);
			String passworld = new String(data,"UTF-8").trim();
			System.out.print(";密码："+passworld);
			
			//读昵称
			raf.read(data);
			String nickname = new String(data,"UTF-8").trim();
			System.out.print(";昵称："+nickname);
			
			//读密码
			int age = raf.readInt();
			System.out.println(";年龄："+age);
			
			
		}
		raf.close();
	}
}
