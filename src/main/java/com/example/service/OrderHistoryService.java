package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.form.OrderReceiveForm;
import com.example.repository.OrderRepository;

/**
 * 注文履歴関連機能の業務処理を行うサービス
 * @author juri.saito
 *
 */
@Service
@Transactional
public class OrderHistoryService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private OrderToppingService orderToppingService;
	
	/**
	 * 注文履歴を表示する
	 * ユーザID→注文ID取得→注文商品ID取得→注文トッピングID取得➡注文トッピングリストを注文商品にセット→注文商品リストを注文（order）にセット➡最終的な注文(order)情報をreturn
	 * 
	 * @param userId ユーザID
	 * @return 注文情報
	 */
	public List<Order> showOrderHistory(Integer userId) {
		//空のorderオブジェクトリストを作成
		List<Order> orderList = new ArrayList<>();
		
		if(orderRepository.findByUserIdAndStatus1(userId) != null) {
			//①対象ユーザIDに注文済みの注文情報（注文履歴）があった場合の処理
			//対象ユーザの注文履歴を取得
			orderList = orderRepository.findByUserIdAndStatus1(userId);

			for (Order order : orderList) {
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
			}
		}else {
			//②対象ユーザIDにショッピングカートが無かった場合の処理
			Order order = new Order();
			order.setUserId(userId);
			order.setStatus(0);
			order.setTotalPrice(0);
			orderRepository.insert(order);
			
			List<OrderItem> orderItemList = new ArrayList<>();
			order.setOrderItemList(orderItemList);
		}
		return orderList;
	}
	
}
