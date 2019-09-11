package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
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
}
