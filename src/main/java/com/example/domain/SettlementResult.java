package com.example.domain;

/**
 * 決済処理の結果を表すドメインクラス
 * @author juri.saito
 *
 */
public class SettlementResult {

	/** 決済状況 */
	private String status;
	
	/** メッセージ */
	private String message;
	
	/** エラーコード */
	private  String error_code;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	
	
}
