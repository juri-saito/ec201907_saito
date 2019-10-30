package com.example.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UserRepository;

/**
 * ユーザ関連機能の業務処理を行うサービス.
 * @author juri.saito
 *
 */
@Service
@Transactional
public class UserService { 
	
	@Autowired
	private PasswordEncoder passwordEncoder; //SecurityConfigに記述
	
	@Autowired
	private UserRepository userRepository;
	

	/**
	 * ユーザ情報を挿入する.
	 * パスワードのハッシュ化.
	 * 
	 * @param user ユーザ情報
	 */
	public void insert(User user) {
		String encodePassword =passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
		userRepository.insert(user);
	}
	
	/**
	 * メールアドレスからユーザ情報を取得.
	 * @param email メールアドレス
	 * @return　ユーザ情報
	 */
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	/**
	 * ユーザ情報を変更.
	 * 
	 * @param user　ユーザ情報
	 */
	public void update(User user) {
		userRepository.update(user);
	}
	
	/**
	 * ユーザ情報を削除.
	 * 
	 * @param user　ユーザ情報
	 */
	public void delete(int currentId) {
		userRepository.delete(currentId);
	}
	
	/**
	 * ユーザ情報として登録されているパスワードと、リクエストパラメータとして受け取ったパスワードを比較.
	 * 
	 * @param user　ユーザ情報
	 * @param rawPassword　リクエストパラメータとして受け取ったパスワード
	 * @return
	 */
	public boolean matches(User user, String rawPassword) {
	    return passwordEncoder.matches(rawPassword, user.getPassword()); 
	}
	
	/**
	 * SpringSecurityから取り出したユーザーID(メールアドレス)を元に現在ログインしているユーザーの情報を取得するメソッド.
	 * 
	 * @return 現在ログインしているユーザーの情報
	 */
	public User inputCurrentUser(Principal principal) {
		//SpringSecurityのユーザーID(メールアドレス)を元にユーザー情報を検索
		Authentication authentication = (Authentication) principal;
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String email = userDetails.getUsername();
		User currentUser = findByEmail(email);
	
		return currentUser;
		
	}
}
