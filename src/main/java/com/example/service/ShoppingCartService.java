package com.example.service;

import java.util.ArrayList;
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
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private OrderToppingService orderToppingService;
	
	//////////////////////////////////////////////////////////
	///ショッピングカートに注文商品と注文トッピングを追加/////
	//////////////////////////////////////////////////////////
	
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
	
	//////////////////////////////////////////
	///ショッピングカートの中身を表示する/////
	//////////////////////////////////////////
	
	/**
	 * ショッピングカートの中身を表示する
	 * ユーザID→注文ID取得→注文商品ID取得→注文トッピングID取得➡注文トッピングリストを注文商品にセット→注文商品リストを注文（order）にセット➡最終的な注文(order)情報をreturn
	 * 
	 * @param userId ユーザID
	 * @return 注文情報
	 */
	public Order showLoginUserCart(Integer userId) {
		//空のorderオブジェクトを作成
		Order order = new Order();
		
		if(orderRepository.findByUserIdAndStatus0(userId) != null) {
			//①対象ユーザIDに未注文の注文情報（ショッピングカート）があった場合の処理
			//対象ユーザのショッピングカートを取得
			order = orderRepository.findByUserIdAndStatus0(userId);
			order.setUserId(userId);

			//ショッピングカート内の注文商品リストを取得
			List<OrderItem> orderItemList = orderItemService.findByOrderId(order.getId());

			//注文商品1つ1つに対応する注文トッピングリストを取得して注文商品情報に付与
			for (OrderItem orderItem : orderItemList) {
				List<OrderTopping> orderToppingList = orderToppingService.findByOrderItemId(orderItem.getId());
				orderItem.setOrderToppingList(orderToppingList);
			}
			//注文トッピング情報を付与した注文商品リストをショッピングカート情報に付与
			order.setOrderItemList(orderItemList);
		}else {
			//②対象ユーザIDにショッピングカートが無かった場合の処理
			order.setUserId(userId);
			order.setStatus(0);
			order.setTotalPrice(0);
			orderRepository.insert(order);
			
			List<OrderItem> orderItemList = new ArrayList<>();
			order.setOrderItemList(orderItemList);
		}
		return order;
	}
	
	//////////////////////////////////////////
	///ショッピングカートの中身を削除する/////
	//////////////////////////////////////////
	
	/**
	 * ショッピングカートの中身を削除する
	 * @param orderItemId　注文商品ID
	 */
	public void deleteOrderItem(Integer orderItemId) {
		orderItemRepository.deleteById(orderItemId);
		orderToppingRepository.deleteById(orderItemId);
	}
}
