package com.example.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.User;
import com.example.form.InsertUserForm;
import com.example.service.UserService;

/**
 * ユーザ関連機能の処理の制御を行うコントローラ
 * @author juri.saito
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@ModelAttribute
	private InsertUserForm setUpInsertUserForm() {
		return new InsertUserForm();
	}
	
	@Autowired
	private UserService userService;


	/**
	 * ユーザ登録画面を表示
	 * @return ユーザ登録画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert() {
		return "register_user.html";
	}
	
	/**
	 * ユーザ情報を登録する.
	 * @param form　ユーザ情報登録に用いるフォーム
	 * @param result　エラーメッセージを格納するオブジェクト
	 * @return　ログイン画面　エラーがある場合はユーザ情報登録画面へ遷移
	 */
	@RequestMapping("/insert")
	public String insert(@Validated InsertUserForm form, BindingResult result) {
		
		
		//パスワード確認
		if(!form.getPassword().equals(form.getConfirmationPassword())) {
			result.rejectValue("password", "", "パスワードが一致しません");
		}
		
		//メールアドレスの確認
		User existUser = userService.findByEmail(form.getEmail());
		if(existUser != null) {
			result.rejectValue("email", "", "このメールアドレスは既に登録されています");
		}
		
		//エラーが一つでもあれば登録画面に戻る
		if(result.hasErrors()) {
			return toInsert();
		}
		
		
		User user = new User();
		BeanUtils.copyProperties(form, user);
		//ユーザ登録処理
		userService.insert(user);
		
		return "/user/toLogin";
	}
	
	/**
	 * ログインページを表示
	 * @return　ログインページ
	 */
	@RequestMapping("/toLogin")
	public String toLogin(Model model, @RequestParam(required = false) String error) {
		if(error != null) {
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です");
		}
		return "login.html";
	}
}
