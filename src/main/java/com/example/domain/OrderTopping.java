package com.example.domain;

/**
 * 注文トッピングを表すドメインクラス
 * @author juri.saito
 *
 */
public class OrderTopping {
	
	/** ID */
	private Integer id;
	
	/** トッピングID */
	private Integer toppingId;
	
	/** 注文商品ID */
	private Integer orderItemId;
	
	/** トッピング */
	private Topping topping;

	@Override
	public String toString() {
		return "OrderTopping [id=" + id + ", toppingId=" + toppingId + ", orderItemId=" + orderItemId + ", topping=" + topping
				+ "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getToppingId() {
		return toppingId;
	}

	public void setToppingId(Integer toppingId) {
		this.toppingId = toppingId;
	}

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderId) {
		this.orderItemId = orderId;
	}

	public Topping getTopping() {
		return topping;
	}

	public void setTopping(Topping topping) {
		this.topping = topping;
	}
}
