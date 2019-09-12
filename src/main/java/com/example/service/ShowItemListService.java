package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.repository.ItemRepository;

/**
 * 商品関連機能の業務処理を行うサービス
 * @author juri.saito
 *
 */
@Service
@Transactional
public class ShowItemListService {
	
	@Autowired
	private ItemRepository itemRepository;

	/**
	 * 商品情報を全件検索
	 * @return　全商品リスト
	 */
	public List<Item> findAll(){
		return itemRepository.findAll();
	}
	
	/**
	 * 商品情報を全件検索
	 * @return　全商品リスト
	 */
	public List<Item> findLikeName(String name){
		return itemRepository.findByLikeName(name);
	}
}
