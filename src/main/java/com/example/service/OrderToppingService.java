package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.repository.OrderToppingRepository;
import com.example.repository.ToppingRepository;

/**
 * 注文トッピング関連機能の業務処理を行うサービス
 * @author juri.saito
 *
 */
@Service
@Transactional
public class OrderToppingService {

	@Autowired
	private OrderToppingRepository orderToppingRepository;
	
	@Autowired
	private ToppingRepository toppingRepository;

	/**
	 * 注文商品IDから注文トッピングリストを取得して各トッピング情報を付与
	 * @param orderId　注文商品ID
	 * @return 注文トッピングリスト
	 */
	public List<OrderTopping> findByOrderItemId(Integer orderItemId) {
		//注文トッピングリストを取得
				List<OrderTopping> orderToppingList = orderToppingRepository.findByOrderItemId(orderItemId);
				
				//注文トッピング情報1つ1つにトッピング情報を付与
				for (OrderTopping orderTopping : orderToppingList) {
				Topping topping = toppingRepository.findById(orderTopping.getToppingId());
				orderTopping.setTopping(topping); //参照中のorderToppingに既にtopping情報を付与
				}
				return orderToppingList;
	}
}
