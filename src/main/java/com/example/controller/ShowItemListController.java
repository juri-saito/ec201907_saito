package com.example.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.service.ShowItemListService;

/**
 * 商品関連機能の処理の制御を行うコントローラ
 * @author juri.saito
 *
 */
@Controller
@RequestMapping("/top")
public class ShowItemListController {
	
	@Autowired
	private ShowItemListService itemService;

	/**
	 * トップページに全商品リストを表示
	 * @param model　リクエストスコープ
	 * @return　トップページ
	 */
	@RequestMapping("")
	public String showAllItems(Model model) {
		List<Item> itemList1 = itemService.findAll();
		
		List<List<Item>> itemList3 = new ArrayList<>();
		List<Item> itemList2 = new ArrayList<>();
		
		for (int i = 1; i < itemList1.size(); i++) {
			
			//itemList1から１個ずつ取り出したitemをitemList2に格納
			itemList2.add(itemList1.get(i-1));
			
			//3の倍数のとき、itemList3にitemList2を格納。itemList2を空にする。
			if(i % 3 == 0) {
				itemList3.add(itemList2);
				itemList2 = new ArrayList<>();
			}
		}
		//itemList1のitem数が3の倍数個でなくても最後にitemList3にitemList2を格納
		itemList3.add(itemList2);
		
		model.addAttribute("itemList3", itemList3);
		return "item_list.html";
	}

	/**
	 * トップページに曖昧検索結果を表示
	 * @param name　曖昧検索ワード
	 * @param model　リクエストスコープ
	 * @return　トップページ
	 */
	@RequestMapping("/findItems")
	public String findLikeName(String code,Model model) {
		List<Item> itemList1 = itemService.findLikeName(code);
		
		List<List<Item>> itemList3 = new ArrayList<>();
		List<Item> itemList2 = new ArrayList<>();
		
		for (int i = 1; i < itemList1.size(); i++) {
			
			//itemList1から１個ずつ取り出したitemをitemList2に格納
			itemList2.add(itemList1.get(i-1));
			
			//3の倍数のとき、itemList3にitemList2を格納。itemList2を空にする。
			if(i % 3 == 0) {
				itemList3.add(itemList2);
				itemList2 = new ArrayList<>();
			}
		}
		//itemList1のitem数が3の倍数個でなくても最後にitemList3にitemList2を格納
		itemList3.add(itemList2);
		
		model.addAttribute("itemList3", itemList3);
		return "item_list.html";
	}
}
