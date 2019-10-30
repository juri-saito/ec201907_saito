package com.example.domain;

/**
 * ユーザ情報を表すドメインクラス.
 * 
 * @author juri.saito
 *
 */
/**
 * @author juri.saito
 *
 */
public class User {

	/**	ユーザID */
	private Integer id;

	/**	名前 */
	private String name;
	
	/**	Eメール */
	private String email;
	
	/**	パスワード */
	private String password;
	
	/**	郵便番号 */
	private String zipcode;
	
	/**	住所 */
	private String address;
	
	/**	電話番号 */
	private String telephone;
	
	/** 入退会状況 */
	private Integer status;
	
	/** 権限 */
	private String role;
	
	/**
	 * 引数無しのコンストラクタ
	 */
	public User() {
	}
	
	
	/**
	 * 初期化用コンストラクタ
	 * @param id　ID
	 * @param name　名前
	 * @param email　メールアドレス
	 * @param password　パスワード
	 * @param zipcode　郵便番号
	 * @param address　住所
	 * @param telephone　電話番号
	 * @param status　入退会状況
	 * @param role　権限
	 */
	public User(Integer id, String name, String email, String password, String zipcode, String address,
			String telephone, Integer status, String role) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.zipcode = zipcode;
		this.address = address;
		this.telephone = telephone;
		this.status = status;
		this.role = role;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", zipcode="
				+ zipcode + ", address=" + address + ", telephone=" + telephone + ", status=" + status + ", role="
				+ role + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}