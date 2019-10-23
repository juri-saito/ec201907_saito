package com.example.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;

import com.example.common.Common;
import com.example.domain.Order;
import com.example.form.OrderReceiveForm;
import com.example.service.OrderHistoryService;
import com.example.service.OrderService;
import com.example.service.SendMailService;
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
	
	@Autowired
	private OrderHistoryService orderHistoryService; 
	
	@Autowired
	private SendMailService sendMailService;
	
	@ModelAttribute
	private OrderReceiveForm setUpOrderReceiveForm() {
		return new OrderReceiveForm();
	}

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
	public String order(@Validated OrderReceiveForm form, BindingResult result, Model model) {
		
		//エラーが一つでもあれば登録画面に戻る
		if(result.hasErrors()) {
			return confirmOrder(model);
		}
		
		//ユーザIDをフォームにセットし、注文情報を更新する
		form.setUserId(common.GetUserId());
		Order order = orderService.order(form);
		
		//注文完了メール送信
		Context context = new Context();
		context.setVariable("name", order.getDestinationName());
		context.setVariable("order", order);
		context.setVariable("bootstrap", "../../static/css/bootstrap.css");
		context.setVariable("imgCurry", "/img_curry/");
		String email = order.getDestinationEmail();
		sendMailService.sendMail(context, email);
		
		return "order_finished";
	}
	
	/**
	 * 注文履歴ページを表示する
	 * @return　注文履歴ページ
	 */
	@RequestMapping("/history")
	public String showOrderHistory(Model model) {
		//ログイン中のユーザの未注文の注文情報（ショッピングカート）を表示
		List<Order> orderList = orderHistoryService.showOrderHistory(common.GetUserId());
		model.addAttribute("orderList", orderList);
		
		return "order_history";
	}
}
