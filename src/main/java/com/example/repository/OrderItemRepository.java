package com.example.repository;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;

/**
 * ショッピングカート情報を操作するリポジトリ.
 * @author juri.saito
 *
 */
@Repository
public class OrderItemRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private SimpleJdbcInsert insert;
	
	@PostConstruct
	public void init() {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert((JdbcTemplate) template.getJdbcOperations());
		SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName("order_items");
		insert = withTableName.usingGeneratedKeyColumns("id");
	}
	
	/**
	 * 注文商品のローマッパーを定義
	 */
	private static final RowMapper<OrderItem> ORDER_ITEM_ROW_MAPPER = (rs, i) -> {
		OrderItem orderItem = new OrderItem();
		orderItem.setId(rs.getInt("id"));
		orderItem.setItemId(rs.getInt("item_id"));
		orderItem.setOrderId(rs.getInt("order_id"));
		orderItem.setQuantity(rs.getInt("quantity"));
		orderItem.setSize(rs.getString("size").charAt(0));
		Item item = null;
		orderItem.setItem(item);
		List<OrderTopping> orderToppingList = null;
		orderItem.setOrderToppingList(orderToppingList);

		return orderItem;
	};

	/**
	 * 注文商品情報をDBに挿入する
	 * @param orderItem　注文商品
	 * @return 注文商品
	 */
	public OrderItem insert(OrderItem orderItem) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);
		Number key = insert.executeAndReturnKey(param);
		orderItem.setId(key.intValue());
		return orderItem;
	}

	/**
	 * 注文idから注文商品リストを取得.
	 * @param id 注文ID
	 * @return　注文商品リスト
	 */
	public List<OrderItem> findByOrderId(Integer orderId) {
		String sql = "SELECT id,item_id,order_id,quantity,size FROM order_items WHERE order_id = :orderId;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderId", orderId);
		List<OrderItem> orderItemList = template.query(sql, param, ORDER_ITEM_ROW_MAPPER);
		return orderItemList;
	}
	
	/**
	 * 注文商品をショッピングカートから削除
	 * @param orderItemId　注文商品ID
	 */
	public void deleteById(Integer orderItemId) {
		System.out.println(orderItemId);
		String sql = "DELETE FROM order_items WHERE id=:id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", orderItemId);
		template.update(sql, param);
	}
}
