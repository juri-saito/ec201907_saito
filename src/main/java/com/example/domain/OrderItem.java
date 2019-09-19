package com.example.domain;

import java.util.List;

/**
 * 注文商品情報を表すドメインクラス
 * @author juri.saito
 *
 */
public class OrderItem {
	
	/** id */
	private Integer id;
	
	/** 商品ID */
	private Integer itemId;
	
	/** 注文ID */
	private Integer orderId;
	
	/** 数量 */
	private Integer quantity;
	
	/** サイズ */
	private Character size;
	
	/** 商品 */
	private Item item;
	
	/** 注文トッピングリスト */
	private List<OrderTopping> orderToppingList;
	
	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", itemId=" + itemId + ", orderId=" + orderId + ", quantity=" + quantity
				+ ", size=" + size + ", item=" + item + ", orderToppingList=" + orderToppingList + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Character getSize() {
		return size;
	}

	public void setSize(Character size) {
		this.size = size;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<OrderTopping> getOrderToppingList() {
		return orderToppingList;
	}

	public void setOrderToppingList(List<OrderTopping> orderToppingList) {
		this.orderToppingList = orderToppingList;
	}
	
	/**
	 * 小計金額を算出
	 * @return　小計金額
	 */
	public int getSubTotal() {
		int subTotal = 0;
		int curryPrice = 0;
		int toppingSubTotal = 0;
		
		//サイズに応じた商品価格とトッピング小計を取得
		if(size == 'M') {
			curryPrice = item.getPriceM();
			for (OrderTopping orderTopping : orderToppingList) {
				toppingSubTotal = toppingSubTotal + orderTopping.getTopping().getPriceM();
			}
		}else if (size == 'L'){
			curryPrice = item.getPriceL();
			for (OrderTopping orderTopping : orderToppingList) {
				toppingSubTotal = toppingSubTotal + orderTopping.getTopping().getPriceL();
			}
		}
		
		//小計を算出
		subTotal = curryPrice + toppingSubTotal;
		
		return subTotal;
	}
	
}
