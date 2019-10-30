package com.example.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UpdateUserForm {
	
	/**	ID */
	private Integer id;


	/** ユーザ名 */
	@NotBlank( message = "お名前を入力してください")
	private String name;

	/** メールアドレス */
	@Email( message = "アドレスが不正です" )
	@NotBlank( message = "メールアドレスを入力してください")
	private String email;

	/** パスワード */
	private String password;

	/** 郵便番号 */
	@NotBlank( message = "郵便番号を入力してください")
	@Pattern(message = "入力形式が不正です", regexp = "^[0-9]{7}$")
	private String zipcode;

	/** 住所 */
	@NotBlank( message = "住所を入力してください")
	private String address;
	
	/** 電話番号 */
	@NotBlank( message = "電話番号を入力してください")
	@Pattern(message = "入力形式が不正です", regexp = "^[0-9]{10,11}$")
	private String telephone;
	
	/** 権限 */
	private String Role;
	

	@Override
	public String toString() {
		return "UpdateUserForm [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", zipcode=" + zipcode + ", address=" + address + ", telephone=" + telephone + ", Role=" + Role + "]";
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}




	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}
	
	
}
