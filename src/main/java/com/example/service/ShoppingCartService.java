package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.form.ShoppingCartReceiveForm;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;

/**
 * ショッピングカート関連機能の業務処理を行うサービス
 * @author juri.saito
 *
 */
@Service
@Transactional
public class ShoppingCartService {
	
	@Autowired
	private OrderToppingRepository orderToppingRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;
	
	/**
	 * ショッピングカートに注文商品と注文トッピングを追加する.
	 * @param orderItem　注文商品
	 * @param orderTopping　注文トッピング
	 */
	public void addItem(ShoppingCartReceiveForm form) {
		
		//ユーザIDを取得
		Integer userId = form.getUserId();
		
		//①取得したユーザIDに未注文の注文情報（ショッピングカート）があった場合の処理
		//該当ユーザのショッピングカートを構成するOrderItemテーブルとOrderToppingテーブルにフォームから受け取った情報を挿入していく
		if(orderRepository.findByUserIdAndStatus0(userId) != null) {
			//該当ユーザのショッピングカートを取得
			Order order = orderRepository.findByUserIdAndStatus0(userId);
			
			//OrderItemオブジェクトを新規作成し、フォームで受け取った注文商品情報をセット
			OrderItem orderItem = new OrderItem();
			orderItem.setItemId(Integer.parseInt(form.getItemId()));
			orderItem.setOrderId(order.getId()); //order_itemsテーブルのorder_idとordersテーブルのidが一致
			orderItem.setQuantity(Integer.parseInt(form.getQuantity()));
			orderItem.setSize(form.getSize().charAt(0));
			
			//注文商品情報をDBに挿入、自動採番されたidを含むorderItemを受け取る
			orderItem = orderItemRepository.insert(orderItem);
			
			//フォームから受け取ったトッピングリストが空でない場合
			//リストからトッピング情報をひとつずつ取り出して、注文トッピング情報としてDBに挿入
			List<Integer> toppingList = form.getToppingList();
			if(toppingList != null) {
				for(Integer toppingId : toppingList) { 
					OrderTopping orderTopping = new OrderTopping();
					orderTopping.setOrderItemId(orderItem.getId());
					orderTopping.setToppingId(toppingId);
					
					//注文トッピング情報をDBに挿入
					orderToppingRepository.insert(orderTopping);;
				}
			}
		}else { //②取得したユーザIDに未注文の注文情報（ショッピングカート）が無かった場合の処理
			//ショッピングカートを作成
			Order order = new Order();
			order.setUserId(userId);
			order.setStatus(0);
			order.setTotalPrice(0);
			order = orderRepository.insert(order);
			System.out.println("ShoppingCartService内　オーダーidは" + order.getId());
			
			//OrderItemオブジェクトを新規作成し、フォームで受け取った注文商品情報をDBに挿入、自動採番されたidを含むorderItemを受け取る
			OrderItem orderItem = new OrderItem();
			orderItem.setItemId(Integer.parseInt(form.getItemId()));
			orderItem.setOrderId(order.getId());
			orderItem.setQuantity(Integer.parseInt(form.getQuantity()));
			orderItem.setSize(form.getSize().charAt(0));
			orderItem = orderItemRepository.insert(orderItem);
			
			//フォームから受け取ったトッピングリストが空でない場合
			//リストからトッピング情報をひとつずつ取り出して、注文トッピング情報としてDBに挿入
			List<Integer> toppingList = form.getToppingList();
			if(toppingList != null) {
				for (Integer toppingId : toppingList) {
					OrderTopping orderTopping = new OrderTopping();
					orderTopping.setOrderItemId(orderItem.getId());
					orderTopping.setToppingId(toppingId);
					orderToppingRepository.insert(orderTopping);
				}
			}
		}
	}
}
