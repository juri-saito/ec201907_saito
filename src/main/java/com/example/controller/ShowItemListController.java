package com.example.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.form.SearchAndSortForm;
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
	private ShowItemListService showItemListService;
	
	@Autowired
	private HttpSession session;
	
	@ModelAttribute
	public SearchAndSortForm setUpForm() {
		return new SearchAndSortForm();
	}

	/**
	 * トップページに商品リストを表示
	 * @param page　出力したいページ番号
	 * @param model　リクエストスコープ
	 * @return　トップページ
	 */
	@RequestMapping("")
	public String showAllItems(Model model, Integer page, HttpServletRequest request)
			throws ServletException, IOException, UnsupportedEncodingException {
		 
		//ページング機能追加
		if(page == null) {
			//ページ番号の指定が無い場合は1ページ目を表示させる
			page = 1;
		}
		
		Page<Item> itemPage = showItemListService.showListPaging(page);
		List<Item> itemList1 = itemPage.getContent();
		List<List<Item>> itemList3 = new ArrayList<>();
		List<Item> itemList2 = new ArrayList<>();
		
		for (int i = 1; i <= itemList1.size(); i++) {
			
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
		model.addAttribute("itemPage", itemPage);
		
		//ページ番号リストをスコープに格納
		int totalPages = (int)Math.ceil(showItemListService.countAllItems() / 6);
		List<Integer> pageNumbers = calcPageNumbers(model, totalPages);
		model.addAttribute("pageNumbers", pageNumbers);
		
		//オートコンプリート用にjavascriptの配列の中身を文字列で作ってスコープへ格納
		this.getItemListForAutocomplete(model);
		
		//最近見た商品リストをスコープへ格納
		this.getRecentView(model, request);
		
		return "item_list.html";
	}
	
	/**
	 * トップページに曖昧検索・並び替えした結果のリストを表示
	 * @param name　曖昧検索ワード
	 * @param model　リクエストスコープ
	 * @return　トップページ
	 */
	@PostMapping("/findItems")
	public String findByNameOrderPrice(SearchAndSortForm form, Model model, HttpServletRequest request) 
			throws ServletException, IOException, UnsupportedEncodingException {
		
		session.setAttribute("form", form);
		
		//ページング機能追加
		if(form.getPage() == null) {
			//ページ番号の指定が無い場合は1ページ目を表示させる
			form.setPage(1);
		}
		
		if(form.getOrderPrice() == null) {
			form.setOrderPrice("");
		}
		
		Page<Item> itemPage = showItemListService.findByNameOrderPrice(form);
		
		
		
		List<Item> itemList1 = itemPage.getContent();
		List<List<Item>> itemList3 = new ArrayList<>();
		List<Item> itemList2 = new ArrayList<>();
		
		for (int i = 1; i <= itemList1.size(); i++) {
			
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
		model.addAttribute("itemPage", itemPage);
		
		//ページ番号リストをスコープに格納
		int totalPages = (int)Math.ceil(showItemListService.countByNameOrderPrice(form)/6);
		List<Integer> pageNumbers = calcPageNumbers(model, totalPages);
		model.addAttribute("pageNumbers", pageNumbers);
		
		//オートコンプリート用にjavascriptの配列の中身を文字列で作ってスコープへ格納
		this.getItemListForAutocomplete(model);
		
		//最近見た商品リストをスコープへ格納
		this.getRecentView(model, request);
		
		return "item_list.html";
	}
	
	/**
	 * トップページに曖昧検索・並び替えした結果のリストを表示
	 * @param name　曖昧検索ワード
	 * @param model　リクエストスコープ
	 * @return　トップページ
	 */
	@GetMapping("/findItems")
	public String findByNameOrderPriceGet(SearchAndSortForm form, Model model, HttpServletRequest request) 
			throws ServletException, IOException, UnsupportedEncodingException {
		
		//ページング機能追加
		if(form.getPage() == null) {
			//ページ番号の指定が無い場合は1ページ目を表示させる
			form.setPage(1);
		}
		
		if(form.getOrderPrice() == null) {
			form.setOrderPrice("");
		}
		
		Page<Item> itemPage = showItemListService.findByNameOrderPrice(form);
		
		List<Item> itemList1 = itemPage.getContent();
		List<List<Item>> itemList3 = new ArrayList<>();
		List<Item> itemList2 = new ArrayList<>();
		
		for (int i = 1; i <= itemList1.size(); i++) {
			
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
		model.addAttribute("itemPage", itemPage);
		
		//ページ番号リストをスコープに格納
		int totalPages = (int)Math.ceil(showItemListService.countByNameOrderPrice(form)/6);
		List<Integer> pageNumbers = calcPageNumbers(model, totalPages);
		model.addAttribute("pageNumbers", pageNumbers);
		
		//オートコンプリート用にjavascriptの配列の中身を文字列で作ってスコープへ格納
		this.getItemListForAutocomplete(model);
		
		//最近見た商品リストをスコープへ格納
		this.getRecentView(model, request);
		
		return "item_list.html";
	}
	
	/**
	 * ページングのリンクに使うページ番号をスコープに格納
	 * @param model　リクエストパラメータ
	 * @param totalPages　総ページ数
	 * @return　ページ番号リスト
	 */
	private List<Integer> calcPageNumbers (Model model, int totalPages){
		
		//空のページ番号リストを作成して初期値nullを入れておく
		List<Integer> pageNumbers = null;
		//ページ番号が複数ある場合、
		if(totalPages > 0) {
			//ページ番号リストに値を詰め込んでいく
			pageNumbers = new ArrayList<Integer>();
			for (int i = 1; i <= totalPages; i++) {
				pageNumbers.add(i);
			}
		}
		return pageNumbers;
	}
	
	/**
	 * オートコンプリート用にjavascriptの配列の中身を文字列で作ってスコープへ格納
	 * @param model
	 */
	public void getItemListForAutocomplete(Model model) {
		List<Item> serchItemList = showItemListService.findAll();
		StringBuilder itemListForAutocomplete = showItemListService.getItemListForAutocomplete(serchItemList);
		model.addAttribute("itemListForAutocomplete", itemListForAutocomplete);
	}

	/**
	 * 最近見た商品リストをスコープへ格納
	 * @param model
	 */
	public void getRecentView(Model model, HttpServletRequest request) throws UnsupportedEncodingException  {
		//最近見た商品リストをスコープへ格納
		String value = null;
		Cookie cookie[] = request.getCookies();
		List<Item> recentViewList = new ArrayList<>();
		if(cookie != null){
		    for(int i = 0; i < cookie.length; i++){
		    	//最近見た商品のcookieか判定
		        if(cookie[i].getName().equals("recent_view")){
		            value = URLDecoder.decode(cookie[i].getValue(), "UTF-8");
		            //cookie値を配列に変換
		            String array[] = value.split(",");
		            for (int j = 0; j < array.length; j++) {
		            	Item item = showItemListService.findById(Integer.parseInt(array[j]));
		            	recentViewList.add(item);
					}
		        }
		    }
		}
		model.addAttribute("recentViewList", recentViewList);
	}
	
	
}
