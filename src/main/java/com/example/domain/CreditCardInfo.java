package com.example.domain;

/**
 * クレジットカード情報を表すドメインクラス
 * @author juri.saito
 *
 */
public class CreditCardInfo {

	/** 利用者ID */
	private int user_id;
	
	/** 注文NO */
	private long order_number;
	
	/** 決済金額 */
	private long amount;
	
	/** クレジットカード番号 */
	private long card_number;
	
	/** カード有効期限（年） */
	private int card_exp_year;
	
	/** カード有効期限（月） */
	private int card_exp_month;
	
	/** カード名義人 */
	private String card_name;
	
	/** セキュリティコード */
	private int card_cvv;

	@Override
	public String toString() {
		return "CreditCardInfo [user_id=" + user_id + ", order_number=" + order_number + ", amount=" + amount
				+ ", card_number=" + card_number + ", card_exp_year=" + card_exp_year + ", card_exp_month="
				+ card_exp_month + ", card_name=" + card_name + ", card_cvv=" + card_cvv + "]";
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public long getOrder_number() {
		return order_number;
	}

	public void setOrder_number(long order_number) {
		this.order_number = order_number;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public long getCard_number() {
		return card_number;
	}

	public void setCard_number(long card_number) {
		this.card_number = card_number;
	}

	public int getCard_exp_year() {
		return card_exp_year;
	}

	public void setCard_exp_year(int card_exp_year) {
		this.card_exp_year = card_exp_year;
	}

	public int getCard_exp_month() {
		return card_exp_month;
	}

	public void setCard_exp_month(int card_exp_month) {
		this.card_exp_month = card_exp_month;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public int getCard_cvv() {
		return card_cvv;
	}

	public void setCard_cvv(int card_cvv) {
		this.card_cvv = card_cvv;
	}

}
