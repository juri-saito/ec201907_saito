package com.example.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public String showDetail(int id, Model model, HttpServletRequest request, HttpServletResponse response) {
		model.addAttribute("item", showItemDetailService.findItemById(id));
		model.addAttribute("toppingList", showItemDetailService.findAllToppings());
		
		//商品IDをクッキーに格納（jQueryで行うことになったため使用しない）
//		String value = null;
//		Cookie cookie[] = request.getCookies();
//		
//		if(cookie != null){
//		    for(int i = 0; i < cookie.length; i++){
//		    	
//		        if(!cookie[i].getName().equals("recent_view")){
//		        	//クッキーrecent_viewが存在しないとき
//		        	Cookie newCookie = new Cookie("recent_view", String.valueOf(id));
//		        	newCookie.setMaxAge(60*60*2);
//		        	newCookie.setPath("/");
//		        	response.addCookie(newCookie);
//		        }else {
//		        	//クッキーrecent_viewが存在するとき
//		        	value = cookie[i].getValue();
//		        	String array[] = value.split(",");
//		        	List<String> list = Arrays.asList(array);
//		        	List<String> newList = new ArrayList<>(list);
//		        	
//		        	if(newList.size() < 5) {
//		        		newList.add(String.valueOf(id));
////		        		String newArray[] = list.toArray(new String[list.size()]);
//		        		Cookie newCookie = new Cookie("recent_view", newList.toString());
//		        		System.out.println("ああああああああああああ" + newList.toString());
//			        	newCookie.setMaxAge(60*60*2);
//			        	newCookie.setPath("/");
//			        	response.addCookie(newCookie);
//		        	}
//		        }
//		    }
//		}
		return "item_detail";
	}
	
}
