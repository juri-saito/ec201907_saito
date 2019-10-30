package com.example.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.domain.LoginUser;
import com.example.domain.User;
import com.example.repository.UserRepository;

/**
 * ログイン後のユーザ情報に権限情報を付与するサービスクラス.
 * 
 * @author juri.saito
 *
 */
@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService{

	/** DBから(メールアドレスの)情報を得るためのリポジトリ */
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * DBからメールアドレスが一致するユーザを検索しログイン情報を構成して返す.
	 */
	@Override
	public UserDetails loadUserByUsername(String email)
		
		throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(email);
		
		if(user == null) {
			throw new UsernameNotFoundException("そのメールアドレスは登録されていません");
		}else if(user != null && user.getStatus() == 0){
			throw new UsernameNotFoundException("そのメールアドレスは退会済みです");
		}
		//ユーザ権限付与
		Collection<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_USER")); // ユーザ権限付与
		
		return new LoginUser(user, authorityList);
	}
}
