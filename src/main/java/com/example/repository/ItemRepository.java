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
	 * 商品情報を全件検索.
	 * @return　全商品リスト
	 */
	public List<Item> findAll() {
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items ORDER BY id;";
		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
	}
	
	public Integer findAllItemCount() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) FROM items;");
		SqlParameterSource param = null;
		Integer count = template.queryForObject(sql.toString(), param, Integer.class); //クラス名のオブジェクトを生成して結果を詰めて返す
		return count;
	}
	
	/**
	 * 商品情報を6件検索
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
	 * 商品情報を価格の昇順に6件検索
	 * @param startItemCount　どの商品から表示させるかというカウント値（1つのページの先頭の商品番号）
	 * @return　6件分の商品リスト
	 */
	public List<Item> orderPrice(int startItemCount){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted ");
		sql.append("FROM items ORDER BY price_m, id ");
		sql.append("LIMIT 6 OFFSET :startItemCount;");
		SqlParameterSource param = new MapSqlParameterSource().addValue("startItemCount", startItemCount);
		List<Item> itemList = template.query(sql.toString(), param, ITEM_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 商品情報を曖昧検索.
	 * @param name 検索ワード
	 * @return 検索結果
	 */
	public List<Item> findByLikeName(String name){
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items WHERE name LIKE :name ORDER BY id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+ name + "%");
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
		return itemList;
	}
	
	/**
	 * 商品情報を曖昧検索.
	 * @param name 検索ワード
	 * @return 検索結果
	 */
	public List<Item> findByNameOrderPrice(int startItemCount, String name, String orderPrice){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id,name,description,price_m,price_l,image_path,deleted ");
		sql.append("FROM items ");
		
		if(name != null) {
			sql.append("WHERE name LIKE :name ");
		}
		
		sql.append("ORDER BY ");
		
		if(orderPrice.equals("1")) { //価格の高い順
			sql.append("price_m DESC, ");
		}else if (orderPrice.equals("2")) { //価格の低い順
			sql.append("price_m ASC, ");
		}
		
		sql.append("id;");
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+ name + "%");
		List<Item> itemList = template.query(sql.toString(), param, ITEM_ROW_MAPPER);
		return itemList;
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
