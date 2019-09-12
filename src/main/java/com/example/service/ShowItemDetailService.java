package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.domain.Topping;
import com.example.repository.ItemRepository;
import com.example.repository.ToppingRepository;

/**
 * 商品詳細関連機能の業務処理を行うサービス.
 * 
 * @author juri.saito
 *
 */
@Service
@Transactional
public class ShowItemDetailService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ToppingRepository toppingRepository;
	
	/**
	 * 商品IDから商品情報を取得.
	 * @param id 商品ID
	 * @return　商品情報
	 */
	public Item findItemById(int id) {
		return itemRepository.findById(id);
	}

	/**
	 * トッピングリストを取得
	 * @return トッピングリスト
	 */
	public List<Topping> findAllToppings(){
		return toppingRepository.findAll();
	}
}
