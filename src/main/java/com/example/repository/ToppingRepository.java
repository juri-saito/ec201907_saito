package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Topping;

/**
 * トッピング情報の操作に用いるリポジトリ
 * @author juri.saito
 *
 */
@Repository
public class ToppingRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Topping> TOPPING_ROW_MAPPER = (rs, i) -> {
		Topping topping = new Topping();
		topping.setId(rs.getInt("id"));
		topping.setName(rs.getString("name"));
		topping.setPriceM(rs.getInt("price_m"));
		topping.setPriceL(rs.getInt("price_l"));
		return topping;
	};
	
	/**
	 * 全トッピング情報を取得
	 * @return トッピング情報一覧リスト
	 */
	public List<Topping> findAll(){
		String sql = "SELECT id,name,price_m,price_l FROM toppings ORDER BY name;";
		List<Topping> toppingList = template.query(sql, TOPPING_ROW_MAPPER);
		return toppingList;
	}
	
	/**
	 * idからトッピング情報を取得
	 * @param トッピングID
	 * @return トッピング情報
	 */
	public Topping findById(Integer id){
		String sql = "SELECT id,name,price_m,price_l FROM toppings WHERE id=:id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		List<Topping> toppingList = template.query(sql, param, TOPPING_ROW_MAPPER);
		
		Topping topping = new Topping();
		
		if(toppingList != null && toppingList.size() != 0) {
			topping = toppingList.get(0);
		}
		
		return topping;
	}
}
