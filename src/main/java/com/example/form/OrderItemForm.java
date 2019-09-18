package com.example.form;

import java.util.List;

import com.example.domain.Item;
import com.example.domain.Topping;

/**
 * 注文商品をショッピングカートに追加するときに使用するフォーム
 * @author juri.saito
 *
 */
public class OrderItemForm {

	/** id */
	private String id;
	
	/** 商品ID */
	private String itemId;
	
	/** 注文ID */
	private String orderId;
	
	/** 数量 */
	private String quantity;
	
	/** サイズ */
	private Character size;
	
	/** 商品 */
	private Item item;
	
	/** 注文トッピングリスト */
	private List<Topping> orderToppingList;
}
