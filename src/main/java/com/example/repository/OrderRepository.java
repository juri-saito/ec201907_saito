package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.domain.User;

/**
 * 注文情報に関するDBを操作するリポジトリ.
 * @author juri.saito
 *
 */
@Repository
public class OrderRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private SimpleJdbcInsert insert;
	
	@PostConstruct
	public void init() {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert((JdbcTemplate) template.getJdbcOperations());
		SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName("orders");
		insert = withTableName.usingGeneratedKeyColumns("id");
	}
	
	//注文のリザルトセットエクストラクターを定義
	private static final ResultSetExtractor<List<Order>> ORDER_EXTRACTOR = (rs) -> {
		List<Order> orderList = new ArrayList<>();
		List<OrderItem> orderItemList = null;
		List<OrderTopping> orderToppingList = null;
		
		//注文ID退避用の変数(デフォルトの初期値0が意図せずorderIdNowと一致しないように-1を初期値として定義)
		int orderIdBefore = -1;
		//注文商品ID退避用の変数
		int orderItemIdBefore = -1;
		
		while (rs.next()) {
			//現在の注文番号
			int orderIdNow = rs.getInt("t1_id");
			//現在の注文商品番号
			int orderItemIdNow = rs.getInt("t2_id");
			
			//新しい注文番号の場合
			if(orderIdBefore != orderIdNow) {
				//新規作成したorderオブジェクトにorderテーブルから取得した値をセット
				Order order = new Order();
				order.setId(rs.getInt("t1_id"));
				order.setUserId(rs.getInt("t1_user_id"));
				order.setStatus(rs.getInt("t1_status"));
				order.setTotalPrice(rs.getInt("t1_total_price"));
				order.setOrderDate(rs.getDate("t1_order_date"));
				order.setDestinationName(rs.getString("t1_destination_name"));
				order.setDestinationEmail(rs.getString("t1_destination_email"));
				order.setDestinationZipcode(rs.getString("t1_destination_zipcode"));
				order.setDestinationAddress(rs.getString("t1_destination_address"));
				order.setDestinationTel(rs.getString("t1_destination_tel"));
				order.setDeliveryTime(rs.getTimestamp("t1_delively_time"));
				order.setPaymentMethod(rs.getInt("t1_payment_method"));
				User user = null;
				order.setUser(user);
				//空の注文商品リストを作成して、orderオブジェクトにセット
				orderItemList = new ArrayList<>();
				order.setOrderItemList(orderItemList);
				
				//現在の注文IDを退避用注文IDにセット
				orderIdBefore = orderIdNow;
				
				//値をセットし終わったorderオブジェクトを注文リストに追加
				orderList.add(order);
			}
		
			//注文商品IDが違う場合
			if(orderItemIdBefore != orderItemIdNow) {
				//新規作成したorderItemオブジェクトにorder_itemsテーブルから取得した値をセット
				OrderItem orderItem = new OrderItem();
				orderItem.setId(rs.getInt("t2_id"));
				orderItem.setItemId(rs.getInt("t2_item_id"));
				orderItem.setOrderId(rs.getInt("t2_order_id"));
				orderItem.setQuantity(rs.getInt("t2_quantity"));
				//sizeがnullの場合とそうでない場合に分ける理由?
				String size = rs.getString("t2_size");
				if(size == null) {
					orderItem.setSize(null);
				}else {
					orderItem.setSize(size.charAt(0));
				}
				Item item = null;
				orderItem.setItem(item);
				//空の注文トッピングリストを作成して、orderItemオブジェクトにセット
				orderToppingList = new ArrayList<>();
				orderItem.setOrderToppingList(orderToppingList);

				//現在の注文商品IDを退避用注文商品IDにセット
				orderItemIdBefore = orderItemIdNow;
				//値をセットし終わったorderItemオブジェクトを注文リストに追加
				orderItemList.add(orderItem);
 			}
			
			//新規作成したorderToppingオブジェクトにorder_toppingsテーブルから取得した値をセット
			OrderTopping orderTopping = new OrderTopping();
			orderTopping.setId(rs.getInt("t3_id"));
			orderTopping.setToppingId(rs.getInt("t3_topping_id"));
			orderTopping.setOrderItemId(rs.getInt("t3_order_item_id"));
			//注文トッピングIDがnullの場合、新しくToppingオブジェクトを作成し、
			//注文トッピングにセットする
			if(orderTopping.getId() != null) {
				Topping topping = new Topping();
				orderTopping.setTopping(topping);
			}
			//値をセットし終わったorderToppingオブジェクトを注文トッピングリストに追加
			orderToppingList.add(orderTopping);
		}
		return orderList;
 	};
	
	
	
	/**
	 * 特定のユーザーの未注文の注文情報（ショッピングカート）を返す.
	 * @param userId ユーザID
	 * @return　未注文の注文情報（ショッピングカート）
	 */
	public Order findByUserIdAndStatus0 (Integer userId) {
		//ordersテーブルとorder_itemテーブルとorder_toppingテーブルを全行結合し、
		//特定のユーザIDの未注文の注文情報（ショッピングカート）を呼び出す
		String sql = "SELECT"
				+ " t1.id AS t1_id"
				+ " ,t1.user_id AS t1_user_id"
				+ " ,t1.status AS t1_status"
				+ " ,t1.total_price AS t1_total_price"
				+ " ,t1.order_date AS t1_order_date"
				+ " ,t1.destination_name AS t1_destination_name"
				+ " ,t1.destination_email AS t1_destination_email"
				+ " ,t1.destination_zipcode AS t1_destination_zipcode"
				+ " ,t1.destination_address AS t1_destination_address"
				+ " ,t1.destination_tel AS t1_destination_tel"
				+ " ,t1.delivery_time AS t1_delively_time"
				+ " ,t1.payment_method AS t1_payment_method"
				+ " ,t2.id AS t2_id"
				+ " ,t2.item_id AS t2_item_id"
				+ " ,t2.order_id AS t2_order_id"
				+ " ,t2.quantity AS t2_quantity"
				+ " ,t2.size AS t2_size"
				+ " ,t3.id AS t3_id"
				+ " ,t3.topping_id AS t3_topping_id"
				+ " ,t3.order_item_id AS t3_order_item_id"
				+ " FROM "
				+ " ( orders AS t1"
				+ " FULL OUTER JOIN order_items AS t2"
				+ " ON t1.id=t2.order_id"
				+ " )"
				+ " FULL OUTER JOIN order_toppings AS t3"
				+ " ON t2.id=t3.order_item_id"
				+ " WHERE t1.user_id=:userId AND t1.status=0";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<Order> orderList = template.query(sql, param, ORDER_EXTRACTOR);
		//該当ユーザのショッピングカートが空の場合nullを返す
		if(orderList.size() == 0) {
			return null;
		}
		return orderList.get(0);
	}
	
	/**
	 * ショッピングカートに追加する
	 * @param order　注文情報
	 * @return　注文情報（主キー付き）
	 */
	public Order insert(Order order) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		Number key = insert.executeAndReturnKey(param);
		order.setId(key.intValue());
		return order;
	}
	
	/**
	 * 注文をする（注文情報を更新する）
	 * 注文年が変わらないとき
	 * @param order　注文情報
	 */
	public void order(Order order) {
		StringBuilder sql = new StringBuilder();
		//注文情報の更新
		sql = this.upDateOrder(order);
		
		//注文番号を取得してセット
		sql.append("UPDATE orders SET ");
		sql.append("order_num = ");
		sql.append("( select :orderDateNum || to_char(nextval('annual_order'), 'FM000000')) ");
		sql.append("where id = :id");
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		template.update(sql.toString(), param);
	}
	
	/**
	 * 注文をする（注文情報を更新する）
	 * 注文年が変わるとき
	 * @param order　注文情報
	 */
	public void orderAndResetSeq(Order order) {
		StringBuilder sql = new StringBuilder();
		//注文情報の更新
		sql = this.upDateOrder(order);
		
		//シーケンスの削除
		sql.append("drop sequence annual_order;");
		//シーケンスの作成
		sql.append("CREATE SEQUENCE annual_order ");
		sql.append("INCREMENT BY 1 ");
		sql.append("MAXVALUE 999999 ");
		sql.append("START WITH 1 ");
		sql.append("OWNED BY orders.id ");
		sql.append("NO CYCLE;");
		//注文番号を取得してセット
		sql.append("UPDATE orders SET ");
		sql.append("order_num = ");
		sql.append("( select :orderDateNum || to_char(nextval('annual_order'), 'FM000000')) ");
		sql.append("where id = :id");
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		template.update(sql.toString(), param);
	}

	/**
	 * 最後の注文番号の年を取得する
	 * @return 最後の注文番号の年
	 */
	public int getLatestOrder() {
		StringBuilder sql = new StringBuilder();
		sql.append("select substring((select Max(order_num) from orders),1,4);");
		SqlParameterSource param = null;
		int latestDateOrder = 0;
		try {
			latestDateOrder = template.queryForObject(sql.toString(), param, Integer.class);
		}catch (NullPointerException e) {
		}
		return latestDateOrder;
	}
	
	/**
	 * 注文番号を取得する
	 * @return 注文番号
	 */
	public String getOrderNum(Order order) {
		StringBuilder sql = new StringBuilder();
		sql.append("select order_num from orders where id = :id");
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
			String orderNum = template.queryForObject(sql.toString(), param, String.class);
		return orderNum;
	}
	
	/**
	 * 注文情報の更新をするSQLを作成
	 * @param order 注文情報
	 * @return 注文情報の更新をするSQLをあ
	 */
	public StringBuilder upDateOrder(Order order) {
		StringBuilder sql = new StringBuilder();
		//注文情報の更新
		sql.append("UPDATE orders SET ");
		sql.append("status = :status, ");
		sql.append("total_price = :totalPrice, ");
		sql.append("order_date = :orderDate, ");
		sql.append("destination_name = :destinationName, ");
		sql.append("destination_email = :destinationEmail, ");
		sql.append("destination_zipcode = :destinationZipcode, ");
		sql.append("destination_address = :destinationAddress, ");
		sql.append("destination_tel = :destinationTel, ");
		sql.append("delivery_time = :deliveryTime, ");
		sql.append("payment_method = :paymentMethod ");
		sql.append("WHERE id=:id;");
		return sql;
	}
	
	/**
	 * 特定のユーザーの注文済みの注文情報（注文履歴）を返す.
	 * @param userId ユーザID
	 * @return　注文済みの注文情報（注文履歴）
	 */
	public List<Order> findByUserIdAndStatus1 (Integer userId) {
		//ordersテーブルとorder_itemテーブルとorder_toppingテーブルを全行結合し、
		//特定のユーザIDの未注文の注文情報（ショッピングカート）を呼び出す
		String sql = "SELECT"
				+ " t1.id AS t1_id"
				+ " ,t1.user_id AS t1_user_id"
				+ " ,t1.status AS t1_status"
				+ " ,t1.total_price AS t1_total_price"
				+ " ,t1.order_date AS t1_order_date"
				+ " ,t1.destination_name AS t1_destination_name"
				+ " ,t1.destination_email AS t1_destination_email"
				+ " ,t1.destination_zipcode AS t1_destination_zipcode"
				+ " ,t1.destination_address AS t1_destination_address"
				+ " ,t1.destination_tel AS t1_destination_tel"
				+ " ,t1.delivery_time AS t1_delively_time"
				+ " ,t1.payment_method AS t1_payment_method"
				+ " ,t2.id AS t2_id"
				+ " ,t2.item_id AS t2_item_id"
				+ " ,t2.order_id AS t2_order_id"
				+ " ,t2.quantity AS t2_quantity"
				+ " ,t2.size AS t2_size"
				+ " ,t3.id AS t3_id"
				+ " ,t3.topping_id AS t3_topping_id"
				+ " ,t3.order_item_id AS t3_order_item_id"
				+ " FROM "
				+ " ( orders AS t1"
				+ " FULL OUTER JOIN order_items AS t2"
				+ " ON t1.id=t2.order_id"
				+ " )"
				+ " FULL OUTER JOIN order_toppings AS t3"
				+ " ON t2.id=t3.order_item_id"
				+ " WHERE t1.user_id=:userId AND t1.status=1";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<Order> orderList = template.query(sql, param, ORDER_EXTRACTOR);
		//該当ユーザのショッピングカートが空の場合nullを返す
		if(orderList.size() == 0) {
			return null;
		}
		return orderList;
	}
	
}
