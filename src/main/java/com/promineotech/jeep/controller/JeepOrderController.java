package com.promineotech.jeep.controller;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.OrderRequest;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;


  @Validated //turns bean validation on 
  @RequestMapping("/orders") 
  @OpenAPIDefinition(info = @Info(title = "Jeep Order Service"), servers = { 
  @Server(url = "http://localhost:8080", description = "Local server.")})
  
  public interface JeepOrderController { //@formatter off
  
  

  @Operation(
  
  summary = "Create an order for a Jeep", description =
  "Return the created Jeep",
  
  responses = {
  
  @ApiResponse( responseCode = "201", description = "A created Jeep is returned.", content
  = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
  
  @ApiResponse( responseCode = "400", description = "Request paramaters are invalid.", content
  = @Content(mediaType = "application/json")),
  
  @ApiResponse( responseCode = "404", description = "A Jeep component was not found with input criteria.",
  content = @Content(mediaType = "application/json")),
  
  @ApiResponse( responseCode = "500", description = "An unplanned error occured.", content
  = @Content(mediaType = "application/json"))
  
  },
  
  parameters = {
  
  @Parameter( name = "orderRequest", required = true, description =
  "The order as JSON")
  }
  )
  
//we want to validate the incoming data to make sure it's correct. so in addition to
//@RequestBody you will add @Valid that adds bean validation. add your validation annotations
//to your OrderRequest class  
  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED) 
 //parameter is passed in the body 
  Order createOrder(@Valid @RequestBody OrderRequest orderRequest);    
    
  }
  //@formatter: on 
  
 



// "@RequestMapping"any uri that comes in and has "/jeeps" after the port number will get mapped to this
// JeepSalesController class
// "allowEmptyValue" means that if you pass in an empty value it will return it to a null, meaning
// that the value in a
// column is unknown or missing (not empty) aka null
// We need to tell swagger (set of tools that allow you to describe the structure of your APIs so
// that machines can read them) what the content type is
// that each of the ApiReponses will return. the content type we want to return is a mime type
// which may include text/plain or application/xml or application/json or application/jpeg, etc.
// for this microservice, we are returning the media or content type, json
// there will be 4 potential responses: success response (200), bad format response (400)
// not found response(404), and unplanned error response (500)
// the summary is a short description of what your method is doing
// your're going to add documentation (OpenApi 3 documentaion) to describe
// your controller. You'll need to add your Open API dependency in your pom.xml. the url
// is what swagger will use to perform your operations
