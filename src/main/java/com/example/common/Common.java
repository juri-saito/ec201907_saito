package com.example.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.domain.User;
import com.example.service.UserService;

@Configuration
public class Common {

	@Autowired
	private UserService userService;
	
	public Integer GetUserId() {
		//ユーザの認証情報を取得
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//認証情報が取得できた場合
		if(principal != "anonymousUser") {
			String email = ((UserDetails) principal).getUsername();
			User user = userService.findByEmail(email);
			return user.getId();
		}
		
		//認証情報が取得できなかった場合
		return null;
	}
	
}
