package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; //pom.xmlにSprindSecurityの<dependency>記載する必要あり
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; //pom.xmlにSprindSecurityの<dependency>記載する必要あり
import org.springframework.security.crypto.password.PasswordEncoder; //pom.xmlにSprindSecurityの<dependency>記載する必要あり

/**
 * ログイン認証用設定.
 * @author juri.saito
 *
 */
@Configuration   //自分の設定してログインページを出力するのに必要
@EnableWebSecurity   //自分の設定してログインページを出力するのに必要
public class SecurityConfig  extends WebSecurityConfigurerAdapter{
	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception{
	        http.authorizeRequests().antMatchers("/").permitAll();
	    }

	
	/**
	 * bcryptアルゴリズムでハッシュ化で実装を返す
	 * パスワードハッシュ化やマッチ確認する際にPasswordEncoderクラスのDIを可能にする
	 * @return　bcryptアルゴリズムでハッシュ化する実装オブジェクト
	 */
	@Bean
	public PasswordEncoder passwordEncoder() { 
		return new BCryptPasswordEncoder();
	}
}
