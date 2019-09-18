package com.example.form;

import java.util.List;

/**
 * ショッピングカートの情報を受け取るフォーム.
 * @author juri.saito
 *
 */
public class ShoppingCartReceiveForm {

	/** 商品Id */
	private String itemId;
	
	/** ピザサイズ */
	private String size;
	
	/** ピザ数量 */
	private String quantity;
	
	/** トッピングリスト */
	private List<Integer> toppingList;
	
	/** ユーザID */
	private Integer userId;

	@Override
	public String toString() {
		return "ShoppingCartReceiveForm [itemId=" + itemId + ", size=" + size + ", quantity=" + quantity
				+ ", toppingList=" + toppingList + ", userId=" + userId + "]";
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public List<Integer> getToppingList() {
		return toppingList;
	}

	public void setToppingList(List<Integer> toppingList) {
		this.toppingList = toppingList;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
