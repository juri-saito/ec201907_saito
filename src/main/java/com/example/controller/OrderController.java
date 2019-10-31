package com.example.controller;


import java.util.List;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;

import com.example.common.Common;
import com.example.domain.Order;
import com.example.domain.SettlementResult;
import com.example.form.OrderReceiveForm;
import com.example.form.OrderReceiveForm.PayByCreditCard;
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
		model.addAttribute("userId", common.GetUserId());
		
		return "order_confirm";
	}
	
	/**
	 * 銀行払いで注文処理を行う
	 * @return　注文完了画面
	 */
	@Transactional
	@RequestMapping(value = "/finished", params = {"paymentMethod=1"})
	public String orderAndPayByBank(@Validated({Default.class}) OrderReceiveForm form, BindingResult result, Model model) {
		
		//支払い方法が選択されていない場合登録画面に戻る
  		if(form.getPaymentMethod() == null) {
  			return confirmOrder(model);
  		}
		
  		 //エラーが一つでもあれば登録画面に戻る
  		if(result.hasErrors()) {
  			System.out.println(result.getAllErrors());
  			return confirmOrder(model);
  		}

  		//注文情報を更新して、注文番号が付与された注文情報を取得する
  		Order order = orderService.order(form);
  		
  		//注文完了メールを送る
		try {
			sendMailService.sendMail(order);
		} catch (Exception e) {
			result.rejectValue("destinationEmail", "", "ご注文確認メールが送信できませんでした");
			throw e;
		}
		
		return "order_finished";
	}
	
	/**
	 *  クレジットカード払いで注文処理を行う
	 * @return　注文完了画面
	 */
	@Transactional
	@RequestMapping(value = "/finished", params = {"paymentMethod=2"})
	public String orderAndPayByCredit(@Validated({PayByCreditCard.class, Default.class}) OrderReceiveForm form, BindingResult result, Model model) {
		
		//支払い方法が選択されていない場合登録画面に戻る
		if(form.getPaymentMethod() == null) {
			return confirmOrder(model);
		}
		
		//エラーが一つでもあれば登録画面に戻る
		if(result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return confirmOrder(model);
		}
		
		//注文情報を更新して、注文番号が付与された注文情報を取得する
		Order order = orderService.order(form);
		
		//クレジットカード決済処理
		form.setOrder_number(order.getOrder_number());
		orderService.settlement(form, result);
		
		//エラーが一つでもあれば登録画面に戻る
		if(result.hasErrors()) {
			return confirmOrder(model);
		}
		
		//注文完了メールを送る
		try {
			sendMailService.sendMail(order);
		} catch (Exception e) {
			result.rejectValue("destinationEmail", "", "ご注文確認メールが送信できませんでした");
			//決済処理のキャンセル
			orderService.cancel(order.getOrder_number());
			throw e;
		}
		
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
