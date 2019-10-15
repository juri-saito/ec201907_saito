package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;

/**
 * 商品情報の操作に用いるリポジトリ
 * @author juri.saito
 *
 */
@Repository
public class ItemRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * 商品情報のローマッパーを定義
	 */
	private static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setDescription((rs.getString("description")));
		item.setPriceM(rs.getInt("price_m"));
		item.setPriceL(rs.getInt("price_l"));
		item.setImagePath(rs.getString("image_path"));
		item.setDeleted(rs.getBoolean("deleted"));
		return item;
	};

	
	/**
	 * 全商品数を取得
	 * @return　全商品数
	 */
	public Integer countAllItems() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) FROM items;");
		SqlParameterSource param = null;
		Integer count = template.queryForObject(sql.toString(), param, Integer.class); //クラス名のオブジェクトを生成して結果を詰めて返す
		return count;
	}
	
	/**
	 * 商品情報を6件取得
	 * @param startItemCount　どの商品から表示させるかというカウント値（1つのページの先頭の商品番号）
	 * @return　6件分の商品リスト
	 */
	public List<Item> findAPageItems(int startItemCount){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted ");
		sql.append("FROM items ORDER BY id ");
		sql.append("LIMIT 6 OFFSET :startItemCount;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("startItemCount", startItemCount);
		List<Item> itemList = template.query(sql.toString(), param, ITEM_ROW_MAPPER);
		return itemList;
	}
	
	
	/**
	 * 商品情報を曖昧検索・並び替えして6件の商品を取得
	 * @return　商品情報を曖昧検索・並び替えした結果の商品数
	 */
	public  List<Item>  findByNameOrderPrice(int startItemCount, String name, String orderPrice)  {
		StringBuilder sql = new StringBuilder();
		StringBuilder preSql = buildSql(name, orderPrice);
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted ");
		sql.append(preSql);
		preSql.append("ORDER BY ");
		
		if(orderPrice.equals("1")) { //価格の高い順
			preSql.append("price_m DESC, ");
		}else if (orderPrice.equals("2")) { //価格の低い順
			preSql.append("price_m ASC, ");
		}
		
		preSql.append("id ");
		sql.append("LIMIT 6 OFFSET :startItemCount;");
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+ name + "%").addValue("startItemCount", startItemCount);
		List<Item> itemList = template.query(sql.toString(), param, ITEM_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 商品情報を曖昧検索・並び替えした結果すべての商品数を取得
	 * @return　商品情報を曖昧検索・並び替えした結果の商品数
	 */
	public int countByNameOrderPrice(String name, String orderPrice)  {
		
		
		StringBuilder sql = new StringBuilder();
		SqlParameterSource param = null;
		
		StringBuilder preSql = buildSql(name, orderPrice);
		sql.append("SELECT count(*) ");
		sql.append("FROM items ");
		
		if(name != null) {
			sql.append("WHERE name LIKE :name ");
			param = new MapSqlParameterSource().addValue("name", "%"+ name + "%");
		}
		
		Integer count = template.queryForObject(sql.toString(), param, Integer.class); //クラス名のオブジェクトを生成して結果を詰めて返す
		return count;
	}
	
	/**
	 * 商品情報を曖昧検索・並び替えするSQL文を作成する
	 * @param startItemCount
	 * @param name
	 * @param orderPrice
	 * @return　商品情報を曖昧検索・並び替えするSQL文
	 */
	private StringBuilder buildSql(String name, String orderPrice) {
		StringBuilder preSql = new StringBuilder();
		preSql.append("FROM items ");
		
		if(name != null) {
			preSql.append("WHERE name LIKE :name ");
		}
		
		return preSql;
	}
	
	
	/**
	 * idから商品情報を取得.
	 * @param id 商品ID
	 * @return　商品情報
	 */
	public Item findById(int id) {
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items WHERE id = :id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);
		return item;
	}
}
