package com.promineotech.jeep.controller.support;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;


/**
 * @author Rachael
 *
 */
public class FetchJeepTestSupport extends BaseTest{
  protected List<Jeep> buildExpected() {
    List<Jeep> list = new LinkedList<>();
  //in the buildexpected method, what happend if you change the order of numdoors (4 then 2) ? you will 
 //get an error message because it doesnt match our expected value. to fix this, we can create 
 //a sorting capability on  the jeep class   
    //@formatter: off
 //     Jeep j1 = Jeep.builder().build(); 
    list.add(Jeep.builder()
        .modelId(JeepModel.WRANGLER)
        .trimLevel("Sport")
        .numDoors(2)
        .wheelSize(17)
        .basePrice(new BigDecimal("28475.00"))
         .build());
    
    list.add(Jeep.builder()
        .modelId(JeepModel.WRANGLER)
        .trimLevel("Sport")
        .numDoors(4)
        .wheelSize(17)
        .basePrice(new BigDecimal("31975.00"))
         .build());    
   //@formatter: on   
    
//you're sorting the actual and service and expected in build expected which will
//all call the compareTo method created in the Jeep class. you dont want to rely
//on the default order of mysql so using the sort/collections and compareto is important
//because you dont know what the default order will be.      
    Collections.sort(list);
      return list;
  } 
  /**
   *  
   * @param error
   * @param status
   */    
   
protected void assertErrorMessageValid(Map<String, Object> error, HttpStatus status) {
   assertThat(error).
    containsKey("message").
    containsEntry("status code", status.value()).
    containsEntry("uri", "/jeeps").
    containsKey("timestamp").
    containsEntry("reason", status.getReasonPhrase());
 }    
     
  
}
//INSERT INTO models (model_id, trim_level, num_doors, wheel_size, base_price) VALUES('WRANGLER', 'Sport', 2, 17, 28475.00);
//INSERT INTO models (model_id, trim_level, num_doors, wheel_size, base_price) VALUES('WRANGLER', 'Sport', 4, 17, 31975.00);
//go into your V1.0_Jeep_Data text editor and copy the "INSERT INTO" Wrangler and sport values (2 lines) 
//Starting off with your testing hierarcy and your support class, this class
//will start off empty where you will then add to it. This class will extend the 
//base class, "BaseTest"  
  


