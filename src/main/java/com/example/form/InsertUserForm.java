package com.example.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * ユーザ情報登録時に使用するフォーム.
 * 
 * @author juri.saito
 *
 */
public class InsertUserForm {

		/**	名前 */
		@NotBlank(message = "お名前を入力してください")
		private String name;
		
		/**	Eメール */
		@Email( message = "メールアドレスが不正です" )
		@NotBlank(message = "メールアドレスを入力してください")
		private String email;
		
		/**	パスワード */
		@NotBlank(message = "パスワードを入力してください")
		
		
//			@Pattern(message = "パスワードが不正です", regexp = "^(?-i)(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,32}\b"),
			@Pattern(message = "半角小文字を1字以上含む必要があります", regexp = "^(?-i)(?=.*[a-z])[a-zA-Z\\d]\\b")
			@Pattern(message = "半角大文字を1字以上含む必要があります", regexp = "^(?-i)(?=.*[A-Z])[a-zA-Z\\d]\\b")
			@Pattern(message = "半角数字を1字以上含む必要があります", regexp = "^(?-i)(?=.*\\d)[a-zA-Z\\d]\\b")
			@Pattern(message = "8文字以上32文字以内にしてください", regexp = "^\\w{8,32}\\b")
		
		private String password;
		
		/**	確認用パスワード */
		@NotBlank(message = "確認用パスワードを入力してください")
		private String confirmationPassword;
		
		/**	郵便番号 */
		@NotBlank(message = "郵便番号を入力してください")
		@Pattern(message = "入力形式が不正です", regexp = "^[0-9]{7}$")
		private String zipcode;
		
		/**	住所 */
		@NotBlank(message = "ご住所を入力してください")
		private String address;
		
		/**	電話番号 */
		@NotBlank(message = "お電話番号を入力してください")
		@Pattern(message = "入力形式が不正です", regexp = "^[0-9]{10,11}$")
		private String telephone;
		
		@Override
		public String toString() {
			return "InsertUserForm [name=" + name + ", email=" + email + ", password=" + password
					+ ", confirmationPassword=" + confirmationPassword + ", zipcode=" + zipcode + ", address=" + address
					+ ", telephone=" + telephone + "]";
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

		public String getConfirmationPassword() {
			return confirmationPassword;
		}

		public void setConfirmationPassword(String confirmationPassword) {
			this.confirmationPassword = confirmationPassword;
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
		

}