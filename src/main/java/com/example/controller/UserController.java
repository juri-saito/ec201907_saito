package com.example.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

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
import com.example.form.UpdateUserForm;
import com.example.service.UserService;

/**
 * ユーザ関連機能の処理の制御を行うコントローラ
 * @author juri.saito
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private HttpSession session;

	@ModelAttribute
	public UpdateUserForm setUpUpdateUserForm() {
		return new UpdateUserForm();
	}
	
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
		
		System.out.println("ユーザ登録処理");
		
		return "login.html";
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
	
	/**
	 * ユーザ情報画面を出力.
	 * 
	 * @return
	 */
	@RequestMapping("/toUpdate")
	public String toUpdate(Principal principal, Model model) {
		User currentUser = userService.inputCurrentUser(principal);
		model.addAttribute("currentUser", currentUser);
		return "user_info.html";
	}
	
	/**
	 * 変更内容画面を出力.
	 * 
	 * @return
	 */
	@RequestMapping("/confirmChanges")
	public String confirmChanges (@Validated UpdateUserForm form, BindingResult result, Principal principal, Model model) {
		
		//もしエラーが一つでもあれば入力画面に戻る
		if(result.hasErrors()) {
			System.out.println("ユーザ情報変更時にエラーあり" + result);
			return toUpdate(principal, model);
		}

		User user = new User();

		User currentUser = userService.inputCurrentUser(principal);
		form.setPassword(currentUser.getPassword());
		form.setId(currentUser.getId());

		BeanUtils.copyProperties(form, user);
		System.out.println("変更内容出力" + user);
		session.setAttribute("user", user);
		return "user_update_confirm.html";
	}

	/**
	 * ユーザ情報変更.
	 * 
	 * @param form　ユーザ情報変更用フォーム 
	 * @param result　エラーメッセージを格納
	 * @return　ログイン画面へリダイレクト
	 */
	@RequestMapping("/update")
	public String update(Model model, String rawPassword) {
		
		if(userService.matches((User)session.getAttribute("user"), rawPassword)) {
			//変更処理
			userService.update((User)session.getAttribute("user"));
			return "user_update_finished.html";
		}else {
			model.addAttribute("passwordError", "正しいパスワードをご入力ください");
			return "user_update_confirm.html";
		}
	
	}
}
