package com.webserver.servlets;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

public abstract class HttpServlets {
	public abstract void service(HttpRequest request, HttpResponse response);
}
