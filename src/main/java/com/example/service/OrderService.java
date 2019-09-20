package com.example.service;


import java.sql.Timestamp;
import java.util.Date;
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
 * 注文関連機能の業務処理を行うサービス
 * @author juri.saito
 *
 */
@Service
@Transactional
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private OrderToppingService orderToppingService;
	
	/**
	 * 注文をする（注文情報を更新する）
	 * @param orderItem　注文商品
	 * @param orderTopping　注文トッピング
	 */
	public void order(OrderReceiveForm form) {
		
			//ユーザIDを取得
			Integer userId = form.getUserId();
		
			//該当ユーザの未注文の注文情報（ショッピングカート）を取得
			Order order = orderRepository.findByUserIdAndStatus0(userId);
			
			//取得したOrderオブジェクトに新規作成し、フォームで受け取った注文情報をセット
			
			//当日の日付を注文日にセット
			Date today = new Date();
			order.setOrderDate(today);
			
			order.setDestinationName(form.getDestinationName());
			order.setDestinationEmail(form.getDestinationEmail());
			order.setDestinationEmail(form.getDestinationEmail());
			order.setDestinationZipcode(form.getDestinationZipcode());
			order.setDestinationAddress(form.getDestinationAddress());
			order.setDestinationTel(form.getDestinationTel());
			
			//配達日と配達時間をTimestamp型に変換してOrderオブジェクトの配達日時にセット
			String deliveryDateTime = form.getDeliveryDate() + " " + form.getDeliveryTime();
	        Timestamp ts = Timestamp.valueOf(deliveryDateTime);
	        order.setDeliveryTime(ts);
			
			order.setPaymentMethod(form.getPaymentMethod());
			
			//statusを更新してセット
			if(form.getPaymentMethod() == 1) {
				order.setStatus(1); 
			}else if (form.getPaymentMethod() == 2) {
				order.setStatus(2); 
			}
			
			//取得したOrderオブジェクトの注文商品リストを取得
			List<OrderItem> orderItemList = orderItemService.findByOrderId(order.getId());

			
			//注文商品1つ1つに対応する注文トッピングリストを取得して注文商品情報に付与
			for (OrderItem orderItem : orderItemList) {
				List<OrderTopping> orderToppingList = orderToppingService.findByOrderItemId(orderItem.getId());
				orderItem.setOrderToppingList(orderToppingList);
			}
			//注文トッピング情報を付与した注文商品リストを取得したOrderオブジェクトに付与
			order.setOrderItemList(orderItemList);
			
			//税込み合計金額を算出してセット
			order.setTotalPrice(order.getCalcTotalPrice());
			
			//注文をする（注文情報を更新する）
			orderRepository.order(order);
	}
	
}
