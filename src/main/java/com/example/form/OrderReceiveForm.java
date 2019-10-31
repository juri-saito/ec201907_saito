package com.example.form;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 注文商品をショッピングカートに追加するときに使用するフォーム
 * @author juri.saito
 *
 */
public class OrderReceiveForm {
	
	 /**
	  * クレジットカード払いのグループをインタフェースで定義
	 * @author juri.saito
	 *
	 */
	public static interface PayByCreditCard {
	    };
	
	/** ユーザID */
	private Integer userId;

	/** 注文日 */
	private Date orderDate;
	
	/** 注文者名 */
	@NotBlank(message="お名前を入力してください。")
	private String destinationName;
	
	/** 注文Email */
	@NotBlank(message="メールアドレスを入力してください。")
	@Email(message="メールアドレスの形式が不正です。")
	private String destinationEmail;
	
	/** 注文郵便番号 */
	@Pattern(message="入力形式が不正です" ,  regexp = "^[0-9]{7}$")
	private String destinationZipcode;
	
	/** 注文住所 */
	@NotBlank(message="住所を入力してください。")
	private String destinationAddress;
	
	/** 注文電話番号 */
	@NotBlank(message="電話番号を入力してください。")
	@Pattern(message="入力形式が不正です" ,  regexp = "^[0-9]{11}$")
	private String destinationTel;
	
	/** 配達日 */
	@Pattern(message = "入力形式が不正です", regexp = "[1-2][0-9]{3}-[0-1][0-9]-[0-3][1-9]")
	@NotBlank(message = "配達日を入力してください")
	private String deliveryDate;
	
	/** 配達時間 */
	@NotBlank(message = "配達時間を入力してください")
	private String deliveryTime;
	
	/** 支払い方法 */
	@NotBlank(message = "支払い方法を入力してください")
	private String paymentMethod;

	/** ユーザID */
	public Integer getUserId() {
		return userId;
	}
	
	/** 利用者ID */
//	private String user_id;
	
	/** 注文NO */
	private String order_number;
	
	/** 決済金額 */
	private String amount;
	
	/** クレジットカード番号 */
	@NotBlank(message = "クレジットカード番号を入力してください", groups= PayByCreditCard.class)
	@Pattern(message="入力形式が不正です" ,  regexp = "^[0-9]{14,16}$", groups= PayByCreditCard.class)
	private String card_number;
	
	/** カード有効期限（年） */
	@NotBlank(message = "カード有効期限（年）を入力してください", groups= PayByCreditCard.class)
	private String card_exp_year;
	
	/** カード有効期限（月） */
	@NotBlank(message = "カード有効期限（月）を入力してください", groups= PayByCreditCard.class)
	private String card_exp_month;
	
	/** カード名義人 */
	@NotBlank(message = "カード名義人を入力してください", groups= PayByCreditCard.class)
	@Pattern(message="入力形式が不正です" ,  regexp = "^[a-z]{1,50}$", groups= PayByCreditCard.class)
	private String card_name;
	
	/** セキュリティコード */
	@Pattern(message="入力形式が不正です" ,  regexp = "^[0-9]{2,3}$", groups= PayByCreditCard.class)
	@NotBlank(message = "セキュリティコードを入力してください", groups= PayByCreditCard.class)
	private String card_cvv;


	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getDestinationEmail() {
		return destinationEmail;
	}

	public void setDestinationEmail(String destinationEmail) {
		this.destinationEmail = destinationEmail;
	}

	public String getDestinationZipcode() {
		return destinationZipcode;
	}

	public void setDestinationZipcode(String destinationZipcode) {
		this.destinationZipcode = destinationZipcode;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String getDestinationTel() {
		return destinationTel;
	}

	public void setDestinationTel(String destinationTel) {
		this.destinationTel = destinationTel;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getCard_exp_year() {
		return card_exp_year;
	}

	public void setCard_exp_year(String card_exp_year) {
		this.card_exp_year = card_exp_year;
	}

	public String getCard_exp_month() {
		return card_exp_month;
	}

	public void setCard_exp_month(String card_exp_month) {
		this.card_exp_month = card_exp_month;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public String getCard_cvv() {
		return card_cvv;
	}

	public void setCard_cvv(String card_cvv) {
		this.card_cvv = card_cvv;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public long getlongAmount() {
		return Long.parseLong(amount);
	}
	
	public long getLongOrder_number() {
		return Long.parseLong(order_number);
	}
	
	public long getLongAmount() {
		return Long.parseLong(amount);
	}
	
	public long getLongCard_number() {
		return Long.parseLong(card_number);
	}
	
	public int getIntCard_exp_year() {
		return Integer.parseInt(card_exp_year);
	}
	
	public int getIntCard_exp_month() {
		return Integer.parseInt(card_exp_month);
	}
	
	public int getIntCard_cvv() {
		return Integer.parseInt(card_cvv);
	}
}
