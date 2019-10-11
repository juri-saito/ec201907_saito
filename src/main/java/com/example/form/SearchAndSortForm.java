package com.example.form;

/**
 * 商品一覧画面で、あいまい検索・並び替えのフォーム
 * @author juri.saito
 *
 */
public class SearchAndSortForm {

	/** 表示させたいページ番号 */
	private Integer page;
	
	/** 並び替え */
	private String orderPrice;
	
	/** 検索ワード */
	private String name;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String priceOrder) {
		this.orderPrice = priceOrder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
