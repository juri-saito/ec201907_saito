package com.example.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.common.Common;
import com.example.domain.Order;
import com.example.form.OrderReceiveForm;
import com.example.service.OrderService;
import com.example.service.ShoppingCartService;

/**
 * 注文関連処理の制御を行うコントローラ
 * @author juri.saito
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private Common common;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private OrderService orderService;

	/**
	 * 注文内容（ショッピングカート内容）を確認する
	 * @param model
	 * @return
	 */
	@RequestMapping("/confirm")
	public String confirmOrder(Model model) {

		//ログイン中のユーザの未注文の注文情報（ショッピングカート）を表示
		Order order = shoppingCartService.showLoginUserCart(common.GetUserId());
		model.addAttribute("order", order);
		
		return "order_confirm";
	}
	
	/**
	 * 注文する（注文情報を更新する）
	 * @return　注文完了画面
	 */
	@RequestMapping("/finished")
	public String order(OrderReceiveForm form) {
		//ユーザIDをフォームにセットし、注文情報を更新する
		form.setUserId(common.GetUserId());
		orderService.order(form);
		
		return "order_finished";
	}
}
