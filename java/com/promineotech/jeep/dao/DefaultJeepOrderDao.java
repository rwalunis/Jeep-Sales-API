package com.promineotech.jeep.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import com.promineotech.jeep.entity.Color;
import com.promineotech.jeep.entity.Customer;
import com.promineotech.jeep.entity.Engine;
import com.promineotech.jeep.entity.FuelType;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Option;
import com.promineotech.jeep.entity.OptionType;
import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.OrderRequest;
import com.promineotech.jeep.entity.Tire;
import lombok.extern.slf4j.Slf4j;

//week 4 video 2
@Component
public class DefaultJeepOrderDao implements JeepOrderDao {
  
  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  //week 4-video4: create class at bottom titled, "SqlParams" and create a bunch of little
  //helper methods (generateInsertSql) to be housed inside saveOrder. you will call the updatemethod on the
  //jdbc template which will return the primary key value to us. you may want to use a KEYHOLDER
  //to make sure that your connection pool hasnt switched connections (although unlikely in this
  //service because its all in the same thread)
  @Override
  public Order saveOrder(Customer customer, Jeep jeep, Color color, Engine engine, Tire tire,
      BigDecimal price, List<Option> options) { 
  //generating sql statements and parameter map minus options bc we will create the order object
  //and then we will get the order primary key value that was written to the db, and
  //we will use that primary key to update the *****MANY TO MANY RELATIONSHIP BETWEEN THE 
  //ORDER_OPTIONS TABLE*****  
    SqlParams params = 
        generateInsertSql(customer, jeep, color, engine, tire, price);
   //this "keyHolder" object//from the KeyHolder and GeneratedKeyHolder 
   //imports  will receive the primary key values when we call "update" on the
   //jdbctemplate.  we will need the orderPK in order to save POST data/options thru  
   //creating a "saveOptions" method
   
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(params.sql, params.source, keyHolder);
    Long orderPK = keyHolder.getKey().longValue();
    saveOptions(options, orderPK);
    return Order.builder()
        .orderPK(orderPK)
        .customer(customer)
        .model(jeep)
        .color(color)
        .engine(engine)
        .tire(tire)
        .options(options)
        .price(price)
        .build();
        
  }
//week 4-video 4: method found in saveOrder that saves the options with the orderPK
//option with contain the optionPK. this will be saved in the ORDER_OPTIONS (MANY-TO-MANY) table  
  private void saveOptions(List<Option> options, Long orderPK) {
    for(Option option: options) {
      SqlParams params = generateInsertSql(option, orderPK);
      jdbcTemplate.update(params.sql, params.source);
    }
    
  }
//wee4-video4: method housed within the saveOptions method above. remember SqlParams class created at bottom
  private SqlParams generateInsertSql(Option option, Long orderPK) {
    SqlParams  params = new SqlParams();
    params.sql = "INSERT INTO order_options (option_fk, order_fk) VALUES (:option_fk, :order_fk)";
    params.source.addValue("option_fk", option.getOptionPK());
    params.source.addValue("order_fk", orderPK);
    return params;
  }
//week 4-video 4. Look at orders table and insert the tables' columns from the schema sql file
  //it will include foreign key values and price
  //*****EXCLUDING the primary key since it is generated automatically by SQL with the "AUTO_INCREMENT" feature*****
  //Remember, no orders have yet been created in your data sql file
 private SqlParams generateInsertSql(Customer customer, Jeep jeep, Color color, Engine engine,
      Tire tire, BigDecimal price) {
    String sql = 
        "INSERT INTO orders (customer_fk, color_fk, engine_fk, tire_fk, model_fk, price)"
        + " VALUES (:customer_fk, :color_fk, :engine_fk, :tire_fk, :model_fk, :price)";
  //remember, the VALUES will come from the USER and the ":" prevents user sql injection 
    SqlParams params = new SqlParams();
    params.sql = sql;
 //you want to add the values' foreign key. remember, the values foreign key equals the 
 //same values' primary key, so you can just add ".getCustomerPK" to the map's key value   
    params.source.addValue("customer_fk", customer.getCustomerPK());
    params.source.addValue("color_fk", color.getColorPK());
    params.source.addValue("engine_fk", engine.getEnginePK());
    params.source.addValue("tire_fk", tire.getTirePK());
    params.source.addValue("model_fk", jeep.getModelPK());
    params.source.addValue("price", price); //remember price does not have a PK value
    return params;
  }

  //week 4-video 3 (1655)  
  @Override
  public List<Option> fetchOptions(List<String> optionIds) {
    if(optionIds.isEmpty()) {
      return new LinkedList<>(); //if there is no optionIds list, return an empty list
    }
    Map<String, Object> params = new HashMap<>(); 
    //@formatter: off
    String sql = "SELECT * FROM options WHERE option_id IN("; //"IN(" ends on line 75
//The SQL IN condition (sometimes called the IN operator) allows you to easily test if 
//an expression matches any value in a list of values. It is used to help reduce the need
//for multiple OR conditions in a SELECT, INSERT, UPDATE, or DELETE statement.    
    //formatter:on   
    for (int index = 0; index < optionIds.size(); index++) {     
///If your list of optionIds exists and is NOT empty, then...      
//Starting at 0, String "key" equals "option_0".
//This string "key" (option_0) will then be placed inside of the Map<String, Object> titled "params"
//where it's matching key value will be optionIds.get(0), which would
//retrieve/grab the first index value of the List<String> optionIds. This String "key" would then be
//added to the String "sql" containing your query statement, so it would read
//"SELECT * FROM options WHERE option_id IN(:option_0, "
//This for loop will keep adding option key values to the sql statement (first 0, then 1, then 2, and so on...)
//until it reaches the last element of the optionsId list(from index < optionIds.size() parameter).
//After the loop is over, that sql query statement created
//will then subtract the last two characters of that string (", ")and end the sql statement with a ")"  
//Remember, the SUBSTRING() function extracts some characters from a string
      String key = "option_" + index; 
      sql += ":" + key + ", ";
      params.put(key, optionIds.get(index));
      
    }
      sql = sql.substring(0, sql.length() - 2);
      sql += ")";
      
      return jdbcTemplate.query(sql, params, new RowMapper<Option>() {
        @Override
        public Option mapRow(ResultSet rs, int rowNum) throws SQLException {
          return Option.builder()
              .category(OptionType.valueOf(rs.getString("category")))
              .manufacturer(rs.getString("manufacturer"))
              .name(rs.getString("name"))
              .optionId(rs.getString("option_id"))
              .optionPK(rs.getLong("option_pk"))
              .price(rs.getBigDecimal("price"))
              .build();
        }});  
  }
 
//if the query returns null,the optional will be empty. if the query returns a value, the optional 
//will contain that value
  @Override
  public Optional <Customer> fetchCustomer(String customerId) {
    String sql = "SELECT * FROM customers WHERE customer_id = :customer_id";
    Map<String, Object> params = new HashMap<>();
    params.put("customer_id",  customerId);
//return a resultsetextractor (not a row mapper) when you're returning a single item    
    return Optional.ofNullable(jdbcTemplate.query(sql, params, new CustomerResultSetExtractor()));
  }
  
  
  @Override
  public Optional <Engine> fetchEngine(String engineId) {
    String sql = "SELECT * FROM engines WHERE engine_id = :engine_id";
    Map <String, Object> params = new HashMap<>();
    params.put("engine_id", engineId);
    return Optional.ofNullable(jdbcTemplate.query(sql, params, new EngineResultSetExtractor()));
    
  }

  
  
  @Override
  public Optional <Color> fetchColor(String colorId) {
    //@formatter:off
    String sql = "SELECT * FROM colors WHERE color_id = :color_id";
    //@formatter: on
    Map <String, Object> params = new HashMap<>();
    params.put("color_id", colorId);
    return Optional.ofNullable(jdbcTemplate.query(sql,  params, new ColorResultSetExtractor()));
   
  }
  
  
  @Override
  public Optional <Tire> fetchTire (String tireId) {
    String sql = "SELECT * FROM tires WHERE tire_id = :tire_id";
    Map <String, Object> params = new HashMap<>();
    params.put("tire_id", tireId);
    return Optional.ofNullable(jdbcTemplate.query(sql, params, new TireResultSetExtractor()));
    //week 4: video 2--return jdbcTemplate.query(sql, params, new TireResultSetExtractor());
    //week 4 video 3---tire CAN be null. so we want to use Optional and ofNullable to account 
    //for that possibility instead of getting an error(?)
  }
  
  @Override
  public Optional<Jeep> fetchModel (JeepModel model, String trim, int doors) {
    //@formatter: off
    String sql = "SELECT * FROM models WHERE model_id = :model_id AND trim_level = :trim_level AND num_doors = :num_doors";
    Map <String, Object> params = new HashMap<>();
    params.put("model_id", model.toString());
    params.put("trim_level", trim);
    params.put("num_doors", doors);
    return Optional.ofNullable(jdbcTemplate.query(sql, params, new ModelResultSetExtractor()));
  }
  
  
  
  //create a new inner class, CustomerResultSetExtractor so you can "re-use this inner class"
  //this class houses the method, "extractData" which takes in a resultset.
  //when the resultset comes to us it is in a pre-initialized state so we need to utilize
  //"rs.next();" in order to get the data results in the next row of our table (if one exists)
  class CustomerResultSetExtractor implements ResultSetExtractor<Customer> {
    @Override
    public Customer extractData(ResultSet rs) throws SQLException {
      rs.next();
  //remember to add your @Builder annotations into your Customer class, so you can build your objects for the method    
      //@formatter: off
      return Customer.builder()
          .customerId(rs.getString("customer_id"))
          .customerPK(rs.getLong("customer_pk"))
          .firstName(rs.getString("first_name"))
          .lastName(rs.getString("last_name"))
          .phone(rs.getString("phone"))
          .build();
      //@formatter: on
    }
  }
    class TireResultSetExtractor implements ResultSetExtractor<Tire> {
      @Override
      public Tire extractData(ResultSet rs) throws SQLException {
        rs.next();
     //@formatter: off   
        return Tire.builder()
            .manufacturer(rs.getString("manufacturer"))
            .price(rs.getBigDecimal("price"))
            .tireId(rs.getString("tire_id"))
            .tirePK(rs.getLong("tire_pk"))
            .tireSize(rs.getString("tire_size"))
            .warrantyMiles(rs.getInt("warranty_miles"))
            .build();
        //@formatter: on
      }
    }
    
    class ColorResultSetExtractor implements ResultSetExtractor<Color> {
      @Override
      public Color extractData(ResultSet rs) throws SQLException {
        rs.next();
        return Color.builder()
            .colorPK(rs.getLong("color_pk"))
            .colorId(rs.getString("color_id"))
            .color(rs.getString("color"))
            .price(rs.getBigDecimal("price"))
            .isExterior(rs.getBoolean("is_exterior"))
            .build();        
      }     
    }

    class EngineResultSetExtractor implements ResultSetExtractor<Engine> {
      @Override
      public Engine extractData(ResultSet rs) throws SQLException {
        rs.next();
        return Engine.builder()
            .enginePK(rs.getLong("engine_pk"))
            .engineId(rs.getString("engine_id"))
            .sizeInLiters(rs.getFloat("size_in_liters"))
            .name(rs.getString("name"))
            .fuelType(FuelType.valueOf(rs.getString("fuel_type")))
            .mpgCity(rs.getFloat("mpg_city"))
            .mpgHwy(rs.getFloat("mpg_hwy"))
            .hasStartStop(rs.getBoolean("has_start_stop"))
            .description(rs.getString("description"))
            .price(rs.getBigDecimal("price"))
            .build();
      }
    }
    
    class ModelResultSetExtractor implements ResultSetExtractor<Jeep> {
      @Override
      public Jeep extractData(ResultSet rs) throws SQLException {
        rs.next();

        // @formatter:off
        return Jeep.builder()
            .basePrice(rs.getBigDecimal("base_price"))
            .modelId(JeepModel.valueOf(rs.getString("model_id")))
            .modelPK(rs.getLong("model_pk"))
            .numDoors(rs.getInt("num_doors"))
            .trimLevel(rs.getString("trim_level"))
            .wheelSize(rs.getInt("wheel_size"))
            .build();
        // @formatter:on
      }
    }

 //week 4-video 4: this class is needed for the saveOrder method at the top
    class SqlParams {
      String sql;
      MapSqlParameterSource source = new MapSqlParameterSource();
    }

 
}
