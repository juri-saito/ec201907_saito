package com.example.controller;

import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.common.Common;
import com.example.domain.Order;
import com.example.form.ShoppingCartReceiveForm;
import com.example.service.ShoppingCartService;

/**
 * ショッピングカート関連機能の処理の制御を行うコントローラ.
 * @author juri.saito
 *
 */
@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private Common common;
	
	
	//////////////////////////////////////////////////////////
	///ショッピングカートに注文商品と注文トッピングを追加/////
	//////////////////////////////////////////////////////////
	
	/**
	 * ショッピングカートに注文商品と注文トッピングを追加する.
	 * @param form 商品の注文情報
	 * @return 商品一覧画面
	 */
	@RequestMapping("/add")
	public String addItem(ShoppingCartReceiveForm form, Model model) {
		//認証情報を取得
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//比較用にトークンを退避
		String compareToken = (String) session.getAttribute("token"); //tokenというセッションキーに対応する値をセッションから取得してString型に変換して代入
		String compareUserId = String.valueOf(session.getAttribute("userId")); //userIdというセッションキーに対応する値をセッションから取得してString型に変換して代入
		
		if(principal == "anonymousUser" && (compareToken == null || compareToken.equals(""))) {
			//①未ログインでショッピングカートも無い（初めてトークンを作成する）ときの処理
			String token = UUID.randomUUID().toString();
			//セッションにトークンを退避
			session.setAttribute("token", token);
			//仮のユーザを登録
			Random rand = new Random();
			session.setAttribute("userId", rand.nextInt(899999999)+100000000); //100000000~999999998の範囲で乱数を生成（int型の最大値）してユーザIDとしてセッションにセット
			//仮のユーザidをフォームにセットし、ショッピングカートに商品を追加
			form.setUserId(Integer.parseInt(String.valueOf(session.getAttribute("userId")))); 
			shoppingCartService.addItem(form);
		
		}else if(compareToken != null && session.getAttribute("token") != null && principal == "anonymousUser" && compareToken.equals((String)session.getAttribute("token"))){
			//②未ログインでショッピングカートがある（トークンが既に作成されている）ときの処理
			form.setUserId(Integer.parseInt(compareUserId));		
			shoppingCartService.addItem(form);
		
		}else{
			//③ログインしているときの処理
			form.setUserId(common.GetUserId());
			shoppingCartService.addItem(form);
		}
		return "redirect:/cart/show";
	}
	
	
	//////////////////////////////////////////
	///ショッピングカートの中身を表示する/////
	//////////////////////////////////////////
	
	@RequestMapping("/show")
	public String showCart(Model model) {
		//認証情報を取得
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				//比較用にトークンを退避
				String compareToken = (String) session.getAttribute("token"); //tokenというセッションキーに対応する値をセッションから取得してString型に変換して代入
				String compareUserId = String.valueOf(session.getAttribute("userId")); //userIdというセッションキーに対応する値をセッションから取得してString型に変換して代入
				
				if(principal == "anonymousUser" && (compareToken == null || compareToken.equals(""))) {
					//①未ログインでショッピングカートも無い（初めてトークンを作成する）ときに空のショッピングカートを表示
					String token = UUID.randomUUID().toString();
					//セッションにトークンを退避
					session.setAttribute("token", token);
					//仮のユーザIDを作成
					Random rand = new Random();
					session.setAttribute("userId", rand.nextInt(899999999)+100000000); //100000000~999999998の範囲で乱数を生成（int型の最大値）してユーザIDとしてセッションにセット
					//仮のユーザidで空のショッピングカートを作成し表示
					Order order = shoppingCartService.showLoginUserCart(Integer.parseInt(String.valueOf(session.getAttribute("userId"))));
					model.addAttribute("order", order);
				
				}else if(compareToken != null && session.getAttribute("token") != null && principal == "anonymousUser" && compareToken.equals((String)session.getAttribute("token"))){
					//②未ログインでショッピングカートがある（トークンが既に作成されている）ときにショッピングカートを表示
					Order order = shoppingCartService.showLoginUserCart(Integer.parseInt(compareUserId));
					model.addAttribute("order", order);
				
				}else{
					//③ログイン中のユーザのショッピングカートを表示
					Order order = shoppingCartService.showLoginUserCart(common.GetUserId());
					model.addAttribute("order", order);
				}
				return "cart_list";
	}
	
	//////////////////////////////////////////
	///ショッピングカートの中身を削除する/////
	//////////////////////////////////////////
	@RequestMapping("/delete")
	public String deleteOrderItem(Integer orderItemId) {
		shoppingCartService.deleteOrderItem(orderItemId);
		return "redirect:/cart/show";
	}
}
