package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.domain.OrderItem;
import com.example.repository.ItemRepository;
import com.example.repository.OrderItemRepository;

/**
 * 注文商品関連機能の業務処理を行うサービス
 * @author juri.saito
 *
 */
@Service
@Transactional
public class OrderItemService {
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private ItemRepository itemRepository;

	/**
	 * 注文IDから注文商品リストを取得して各商品情報を付与
	 * @param orderId　注文ID
	 * @return 注文商品リスト
	 */
	public List<OrderItem> findByOrderId(Integer orderId) {
		//注文商品リストを取得
		List<OrderItem> orderItemList = orderItemRepository.findByOrderId(orderId);
		//注文商品情報1つ1つに商品情報を付与
		for (OrderItem orderItem : orderItemList) {
			Item item = itemRepository.findById(orderItem.getItemId());
			orderItem.setItem(item); //参照中のorderItemに既にitem情報を付与
		}
		return orderItemList;
	}
}
