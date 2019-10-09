package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
	 * ページング用メソッド
	 * @param page　表示させたいページ番号
	 * @return　1ページに表示される商品一覧情報
	 */
	public Page<Item> showListPaging(int page){
		//どの商品から表示させるかというカウント値（1つのページの先頭の商品）
		int startItemCount = (page-1) * 6; //1ページに6個の商品を表示する設定
		
		//表示させたいページの商品リスト
		List<Item> list = itemRepository.findAPageItems(startItemCount);
		//上記で作成した該当ページに表示させる商品一覧をページングできる形に変換して返す
		Page<Item> itemPage = new PageImpl<Item>(list, PageRequest.of(1, 6), itemRepository.findAllItemCount());
		return itemPage;
	}
	
	/**
	 * 商品情報をあいまい検索
	 * @return　検索結果リスト
	 */
	public List<Item> findLikeName(String name){
		return itemRepository.findByLikeName(name);
	}
}
