package com.promineotech.jeep.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import lombok.extern.slf4j.Slf4j;

//remember @slf4j Causes lombok to generate a logger field.
//remember a log file is a file that records either events/data 
//that occur in an operating system or other software runs, 
//or messages between different users of a communication software.
//Logging is the act of keeping a log.
//remember w/@Component, a bean is created where the class is a component class (or service) where
//Spring will: Scan our application for classes 
//annotated with @Component(or service, or the other). Instantiate them and inject any specified 
//dependencies into them. Inject them wherever needed with @autowired at runtime

@Service
//@Component
@Slf4j

public class DefaultJeepSalesDao implements JeepSalesDao {
  
 //inject a jdbctemplate at runtime
  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;


  @Override
  public List<Jeep> fetchJeeps(JeepModel model, String trim) {
    log.debug("DAO: model={}, trim={}", model, trim);
 // @formatter:off    
    String sql = "SELECT * FROM models "
        + "WHERE model_id = :model_id "
        + "AND trim_level = :trim_level";
 //formatter:on   
 //remember the jdbc template helps prevent sql injection with use of colons   
 //create a map where you can pass in your jdbc template
   Map <String, Object> params = new HashMap<>();
   params.put("model_id",  model.toString()); //must put enum toString
   params.put("trim_level", trim);  
 //you can use RowMapper or Row Dataset Extractor. you use RowMapper when you are returning
 //a list. its able to infer that you're returning a list of jeeps from the return stmnt ao you don't have to add "Jeep" to it
 //RowMapper maps rows of a ResultSet (query data results) on a per-row basis.  
   return jdbcTemplate.query(sql,  params, new RowMapper<>() { //i believe new RowMapper<Jeep>() works, too
//spring will add unimplemented mapRow method below
   @Override
   public Jeep mapRow(ResultSet rs, int rowNum) throws SQLException {
 //you'll want to return a Jeep object for every result in the result set or current row. rs is the ResultSet to map
 //rowNum is the number of the current row. it throws SQLException if encountered getting column values    
   //@formatter: off   
      return Jeep.builder()
      //pass in column label as getString parameter    
          .basePrice(new BigDecimal(rs.getString("base_price")))
          .modelId(JeepModel.valueOf(rs.getString("model_id")))
          .modelPK(rs.getLong("model_pk"))
          .numDoors(rs.getInt("num_doors"))
          .trimLevel(rs.getString("trim_level"))
          .wheelSize(rs.getInt("wheel_size"))
          .build();
   //@formatter: on   
    }} );
           
  }

}
