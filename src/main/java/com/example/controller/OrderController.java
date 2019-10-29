package com.example.controller;


import java.util.List;

import javax.validation.groups.Default;

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
		
		return "order_confirm";
	}
	
	/**
	 * 銀行払いで注文処理を行う
	 * @return　注文完了画面
	 */
	@RequestMapping(value = "/finished", params = {"paymentMethod=1"})
	public String orderAndPayByBank(@Validated({Default.class}) OrderReceiveForm form, BindingResult result, Model model) {
//	public String order(@Validated OrderReceiveForm form, BindingResult result, Model model) {
		
		//支払い方法が選択されていない場合登録画面に戻る
  		if(form.getPaymentMethod() == null) {
  			return confirmOrder(model);
  		}
		
  		 //エラーが一つでもあれば登録画面に戻る
  		if(result.hasErrors()) {
  			System.out.println(result.getAllErrors());
  			return confirmOrder(model);
  		}

  		//注文を情報を更新してメールを送る
		this.order(form, model);
		
		return "order_finished";
	}
	/**
	 *  クレジットカード払いで注文処理を行う
	 * @return　注文完了画面
	 */
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
		
		//クレジットカード払いのとき
		Integer paymantMethod = Integer.parseInt(form.getPaymentMethod());
		if (paymantMethod == 2) {
			//決済処理
			SettlementResult response = orderService.Settlement(form);
			//決済結果を確認
			if(response.getError_code().equals("E-01")) {
				result.rejectValue("card_exp_year", "", "カードの有効期限が切れています");
				result.rejectValue("card_exp_month", "", "カードの有効期限が切れています");
			}else if(response.getError_code().equals("E-02")) {
				result.rejectValue("card_cvv", "", "セキュリティコードが誤っています");
			}else if(response.getError_code().equals("E-03")) {
				result.rejectValue("card_exp_year", "", "カードの有効期限は半角数字でご入力ください");
				result.rejectValue("card_exp_month", "", "カードの有効期限は半角数字でご入力ください");
			}
		}	
		
		//注文を情報を更新してメールを送る
		this.order(form, model);
		
		return "order_finished";
	}
	
	/**
	 * 注文を情報を更新してメールを送る
	 * @param form　注文フォーム
	 * @param model リクエストスコープ
	 * @return　注文完了ページ
	 */
	public void order(OrderReceiveForm form, Model model ) {
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
