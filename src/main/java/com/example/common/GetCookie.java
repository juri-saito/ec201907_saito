package com.example.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetCookie extends HttpServlet {
	  public void service(HttpServletRequest request, HttpServletResponse response)
	                     throws ServletException, IOException {

	  	//クッキーの取得
		Cookie cooks[] = request.getCookies();
		String value = null;
		for (int i = 0; cooks != null && i < cooks.length; i++) {
			//クッキーの名前で判別
			if (cooks[i].getName().equals("recent_item")) {
				value = cooks[i].getValue();
				System.out.println("ああああああああああああああ" + value);
				break;
			}
		}
	  }
}