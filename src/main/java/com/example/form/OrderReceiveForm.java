package com.example.form;

import java.util.Date;

/**
 * 注文商品をショッピングカートに追加するときに使用するフォーム
 * @author juri.saito
 *
 */
public class OrderReceiveForm {
	
	/** ユーザID */
	private Integer userId;

	/** 注文日 */
	private Date orderDate;
	
	/** 注文者名 */
	private String destinationName;
	
	/** 注文Email */
	private String destinationEmail;
	
	/** 注文郵便番号 */
	private String destinationZipcode;
	
	/** 注文住所 */
	private String destinationAddress;
	
	/** 注文電話番号 */
	private String destinationTel;
	
	/** 配達日 */
	private String deliveryDate;
	
	/** 配達時間 */
	private String deliveryTime;
	
	/** 支払い方法 */
	private Integer paymentMethod;

	/** ユーザID */
	public Integer getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return "OrderReceiveForm [userId=" + userId + ", orderDate=" + orderDate + ", destinationName="
				+ destinationName + ", destinationEmail=" + destinationEmail + ", destinationZipcode="
				+ destinationZipcode + ", destinationAddress=" + destinationAddress + ", destinationTel="
				+ destinationTel + ", deliveryDate=" + deliveryDate + ", deliveryTime=" + deliveryTime
				+ ", paymentMethod=" + paymentMethod + "]";
	}

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

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
