package com.example.service;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;

import com.example.common.Common;
import com.example.domain.CreditCardInfo;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.SettlementResult;
import com.example.form.OrderReceiveForm;
import com.example.repository.OrderRepository;

/**
 * 注文関連機能の業務処理を行うサービス
 * @author juri.saito
 *
 */
@Service
@Transactional
public class OrderService {
	
	@Autowired
	private Common common;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private OrderToppingService orderToppingService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * 注文をする（注文情報を更新する）
	 * @param orderItem　注文商品
	 * @param orderTopping　注文トッピング
	 */
	public Order order(OrderReceiveForm form) {
		
			//ユーザIDを取得
			form.setUserId(common.GetUserId());
			Integer userId = form.getUserId();
		
			//該当ユーザの未注文の注文情報（ショッピングカート）を取得
			Order order = orderRepository.findByUserIdAndStatus0(userId);
			
			//取得したOrderオブジェクトに新規作成し、フォームで受け取った注文情報をセット
			
			//当日の日付を注文日にセット
			Date today = new Date();
			order.setOrderDate(today);
			
			//注文日の年だけ取得
			int orderYear = today.getYear() + 1900;
			
			//宛先の名前・メールアドレス・郵便番号・住所・電話番号をセット
			order.setDestinationName(form.getDestinationName());
			order.setDestinationEmail(form.getDestinationEmail());
			order.setDestinationZipcode(form.getDestinationZipcode());
			order.setDestinationAddress(form.getDestinationAddress());
			order.setDestinationTel(form.getDestinationTel());
			
			//配達日と配達時間をTimestamp型に変換してOrderオブジェクトの配達日時にセット
			String deliveryDateTime = form.getDeliveryDate() + " " + form.getDeliveryTime();
	        System.out.println("配達日" + deliveryDateTime);
			Timestamp ts = Timestamp.valueOf(deliveryDateTime);
	        order.setDeliveryTime(ts);
			
	        //支払い方法をセット
	        Integer paymantMethod = Integer.parseInt(form.getPaymentMethod());
			order.setPaymentMethod(paymantMethod);
			
			//支払い方法に応じてstatusを更新してセット
			if(paymantMethod == 1) {
				//銀行振込のとき
				order.setStatus(1); 
			}else if (paymantMethod == 2) {
				//クレジットカード払いのとき
				order.setStatus(2); 
			}
			
			//取得したOrderオブジェクトの注文商品リストを取得
			List<OrderItem> orderItemList = orderItemService.findByOrderId(order.getId());

			
			//注文商品1つ1つに対応する注文トッピングリストを取得して注文商品情報に付与
			for (OrderItem orderItem : orderItemList) {
				List<OrderTopping> orderToppingList = orderToppingService.findByOrderItemId(orderItem.getId());
				orderItem.setOrderToppingList(orderToppingList);
			}
			//注文トッピング情報を付与した注文商品リストを取得したOrderオブジェクトに付与
			order.setOrderItemList(orderItemList);
			
			//税込み合計金額を算出してセット
			order.setTotalPrice(order.getCalcTotalPrice());
			
			//注文番号冒頭の年月日を表す値をセット
			order.setOrdeDaterNum(order.getOrderDate());
			
			//最後の注文番号の年を取得する
			int latestOrderYear = orderRepository.getLatestOrder();
			
			if(orderYear == latestOrderYear) {
				//注文年が変わらないときの注文
				orderRepository.order(order);
			}else {
				//注文年が変わるときの注文
				orderRepository.orderAndResetSeq(order);
			}
			
			String orderNum = orderRepository.getOrderNum(order);
			order.setOrder_number(orderNum);
			
			return order;
	}
	
	/**
	 * クレジットカード決済処理
	 * @param form
	 * @return　決済処理
	 */
	public SettlementResult settlement(OrderReceiveForm form, BindingResult result) {
		//クレジットカード払いのとき
		//クレジットカード情報をドメインに詰める
		CreditCardInfo creditCardInfo = new CreditCardInfo();
		creditCardInfo.setUser_id(form.getUserId());
		creditCardInfo.setOrder_number(form.getLongOrder_number());
		creditCardInfo.setAmount(form.getlongAmount());
		creditCardInfo.setCard_number(form.getLongCard_number());
		creditCardInfo.setCard_exp_year(form.getIntCard_exp_year());
		creditCardInfo.setCard_exp_month(form.getIntCard_exp_month());
		creditCardInfo.setCard_name(form.getCard_name());
		creditCardInfo.setCard_cvv(form.getIntCard_cvv());
		//WebAPI呼び出し
		String url = "http://172.16.0.13:8080/web-api-sample/credit-card/payment";
		//レスポンスを取得
		SettlementResult response = restTemplate.postForObject(url, creditCardInfo, SettlementResult.class);
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
		return response;
	}

	/**
	 * クレジットカード決済キャンセル処理
	 * @param form
	 * @return　決済キャンセル処理
	 */
	public SettlementResult cancel(String order_number) {
		//WebAPI呼び出し
		String url = "http://172.16.0.13:8080/web-api-sample/credit-card/cancel";
		//レスポンスを取得
		SettlementResult response = restTemplate.postForObject(url, order_number, SettlementResult.class);
		
		return response;
	}
}
