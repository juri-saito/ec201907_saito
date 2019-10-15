package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.form.SearchAndSortForm;
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
	 * 全商品リストを取得
	 * @return 全商品リスト
	 */
	public List<Item> findAll(){
		List<Item> allItemList = itemRepository.findAllItems();
		return allItemList;
	}
	
	
	/**
	 *  商品情報を全件検索
	 * @param page　表示させたいページ番号
	 * @return　1ページに表示される商品一覧情報
	 */
	public Page<Item> showListPaging(int page){
		//どの商品から表示させるかというカウント値（1つのページの先頭の商品）
		int startItemCount = (page-1) * 6; //1ページに6個の商品を表示する設定
		
		//表示させたいページの商品リスト
		List<Item> list = itemRepository.findAPageItems(startItemCount);
		//上記で作成した該当ページに表示させる商品一覧をページングできる形に変換して返す
		Page<Item> itemPage = new PageImpl<Item>(list, PageRequest.of(1, 6), itemRepository.countAllItems());
		return itemPage;
	}
	
	
	/**
	 * 商品情報を曖昧検索・並び替えして6件の商品を取得
	 * @return　商品情報を曖昧検索・並び替えした結果の商品数
	 */
	public Page<Item> findByNameOrderPrice(SearchAndSortForm form){
		//どの商品から表示させるかというカウント値（1つのページの先頭の商品）
		int startItemCount = (form.getPage()-1) * 6; //1ページに6個の商品を表示する設定
				
		//表示させたいページの商品リスト
		List<Item> list = itemRepository.findByNameOrderPrice(startItemCount, form.getName(), form.getOrderPrice());
		//上記で作成した該当ページに表示させる商品一覧をページングできる形に変換して返す
		Page<Item> itemPage = new PageImpl<Item>(list, PageRequest.of(1, 6), itemRepository.countAllItems());
		return itemPage;
	}
	
	/**
	 * すべての商品数を取得
	 * @return　すべての商品数
	 */
	public int  countAllItems() {
		int count = itemRepository.countAllItems();
		return count;
	}
	
	/**
	 * 商品情報を曖昧検索・並び替えした結果すべての商品数を取得
	 * @return　商品情報を曖昧検索・並び替えした結果の商品数
	 */
	public int  countByNameOrderPrice(SearchAndSortForm form) {
		int count = itemRepository.countByNameOrderPrice(form.getName(), form.getOrderPrice());
		return count;
	}
	
	public StringBuilder getItemListForAutocomplete(List<Item> itemList) {
		StringBuilder itemListForAutocomplete = new StringBuilder();
		for (int i = 0; i < itemList.size(); i++) {
			if(i != 0) {
				itemListForAutocomplete.append(",");
			}
			Item item = itemRepository.findAllItems().get(i);
			itemListForAutocomplete.append("\"");
			itemListForAutocomplete.append(item.getName());
			itemListForAutocomplete.append("\"");
		}
		return itemListForAutocomplete;
	}
	
}
