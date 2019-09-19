package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.OrderTopping;
import com.example.domain.Topping;

/**
 * 注文トッピング情報に関するDBを操作するリポジトリ
 * @author juri.saito
 *
 */
@Repository
public class OrderToppingRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * 注文トッピングのローマッパーを定義
	 */
	private static final RowMapper<OrderTopping> ORDER_TOPPING_ROW_MAPPER = (rs, i) -> {
		OrderTopping orderTopping = new OrderTopping();
		orderTopping.setId(rs.getInt("id"));
		orderTopping.setOrderItemId(rs.getInt("order_item_id"));
		orderTopping.setId(rs.getInt("order_item_id"));
		Topping topping =null;
		orderTopping.setTopping(topping);
		return orderTopping;
	};

	/**
	 * 注文トッピング情報を注文トッピングテーブルに挿入する.
	 * @param orderTopping 注文トッピング
	 */
	public void insert(OrderTopping orderTopping) {
		String sql = "INSERT INTO order_toppings (topping_id, order_item_id) VALUES (:toppingId, :orderItemId);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderTopping);
		template.update(sql, param);
	}
	
	/**
	 * 注文商品IDから注文トッピングリストを取得
	 * @param 注文商品ID
	 * @return 注文トッピングリスト
	 */
	public List<OrderTopping> findByOrderItemId(Integer orderItemId) {
		String sql = "SELECT id,topping_id,order_item_id FROM order_toppings WHERE order_item_id=:orderItemId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderItemId", orderItemId);
		List<OrderTopping> orderToppingList = template.query(sql, param, ORDER_TOPPING_ROW_MAPPER);
		return orderToppingList;
	}
}
