package com.example.form;

import javax.validation.constraints.NotBlank;

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
		@NotBlank(message = "メールアドレスを入力してください")
		private String email;
		
		/**	パスワード */
		@NotBlank(message = "パスワードを入力してください")
		private String password;
		
		/**	確認用パスワード */
		@NotBlank(message = "確認用パスワードを入力してください")
		private String confirmationPassword;
		
		/**	郵便番号 */
		@NotBlank(message = "郵便番号を入力してください")
		private String zipcode;
		
		/**	住所 */
		@NotBlank(message = "ご住所を入力してください")
		private String address;
		
		/**	電話番号 */
		@NotBlank(message = "お電話番号を入力してください")
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