package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.User;

/**
 * ユーザ情報の操作に使用するリポジトリ.
 * 
 * @author juri.saito
 *
 */
@Repository
public class UserRepository {
	
	/**
	 * ユーザ情報を格納するローマッパーの定義.
	 */
	private static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setZipcode(rs.getString("zipcode"));
		user.setAddress(rs.getString("address"));
		user.setTelephone(rs.getString("telephone"));
		return user;
	};
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * メールアドレスからユーザ情報を取得.
	 * @param email メールアドレス
	 * @return　ユーザ情報　メールアドレスが一致しない場合nullを返す
	 */
	public User findByEmail(String email) {
		String sql = "SELECT id, name, email, password, zipcode, address, telephone FROM users WHERE email= :email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
		List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
		if(userList.size() == 0) {
			return null;
		}
		return userList.get(0);
	}
	
	/**
	 * ユーザ情報を挿入.
	 * @param user ユーザ情報
	 */
	public void insert(User user) {
		String sql = "INSERT INTO users (name, email, password, zipcode, address, telephone) values (:name, :email, :password, :zipcode,:address, :telephone);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		template.update(sql, param);
	}
	/**
	 * ユーザ情報を変更.
	 * 
	 * @param user　ユーザ情報
	 */
	public void update(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "UPDATE users SET name=:name, email=:email, zipcode=:zipcode, address=:address,telephone=:telephone,role=:role WHERE id=:id;";
		template.update(sql, param);
	}
	
	/**
	 * ユーザ情報を削除.
	 * 
	 * @param user　ユーザ情報
	 */
	public void delete(int id) {
		String sql = "DELETE FROM users WHERE id=:id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}
	
}
