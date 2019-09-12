package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.service.ShowItemDetailService;

/**
 * 商品詳細関連機能の処理の制御を行うコントローラ
 * @author juri.saito
 *
 */
@Controller
@RequestMapping("/item")
public class ShowItemDetailController {
	
	@Autowired
	private ShowItemDetailService showItemDetailService;
	
	/**
	 * 商品詳細画面を表示する.
	 * @return　商品詳細画面
	 */
	@RequestMapping("/showDetail")
	public String showDetail(int id, Model model) {
		System.out.println(id);
		model.addAttribute("item", showItemDetailService.findItemById(id));
		model.addAttribute("toppingList", showItemDetailService.findAllToppings());
		return "item_detail";
	}
	
}
