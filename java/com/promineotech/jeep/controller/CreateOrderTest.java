package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.controller.support.CreateOrderTestSupport;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") //remember since the ActiveProfiles is set to "test", it will load up the h2 mock database
@Sql (scripts = {"classpath:flyaway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyaway/migrations/V1.0__Jeep_Data.sql"},
     config = @SqlConfig(encoding = "utf-8"))

class CreateOrderTest extends CreateOrderTestSupport {
  
  //this test is not returning a 200, it's returning a 201 creates success 
  @Test
  void testCreateOrderReturnsSuccess201() {
  //given an order as JSON body type 
    String body = createOrderBody();
    String uri = getBaseUriForOrders();
  //you need to make sure your header type is set in JSON type so it knows the body is JSON 
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);
  //when the order is sent,
    ResponseEntity<Order> response = getRestTemplate().exchange(uri, HttpMethod.POST, bodyEntity, Order.class);
  //then a 201 success status is returned. we dont need the Parameterized Test reference because we are 
  //expecting a single class back and not a list of classes 
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  //and then the returned order is correct  
    assertThat(response.getBody()).isNotNull();
   //add json body as a variable. ".getBody()" is a special ResponseEntity method that gets the body 
   Order order = response.getBody(); //you're going back to your json body to get your .isEqualTo information
   assertThat(order.getCustomer().getCustomerId()).isEqualTo("MORISON_LINA");
   assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.WRANGLER);
   assertThat(order.getModel().getTrimLevel()).isEqualTo("Sport Altitude");
   assertThat(order.getModel().getNumDoors()).isEqualTo(4);
   assertThat(order.getColor().getColorId()).isEqualTo("EXT_NACHO");
   assertThat(order.getEngine().getEngineId()).isEqualTo("2_0_TURBO");
   assertThat(order.getTire().getTireId()).isEqualTo("35_TOYO");
   assertThat(order.getOptions()).hasSize(6);
 //  
  }
}

//week 4: video one.  this junit test tests the createorder method. it extends the
//CreateOrderTestSupport class. in this test, we create json which will then be
//thrown at the running application. json will be used as the rest request body type in your
//createOrderBody method housed in the CreateOrderTestSupport class. if you look at the 
//table "orders" in the schema, you can see what columns exist. remember, order_pk is created by mysql.
//in this test, you will copy and paste the json information into the String "body" (from where??? github jeep-sales????) 
//you will pass your json String orderbody as an entitiy object


