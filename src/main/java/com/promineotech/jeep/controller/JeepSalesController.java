package com.promineotech.jeep.controller;

import java.util.List;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.promineotech.jeep.Constants;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


  @Validated //turns bean validation on 
  @RequestMapping("/jeeps") 
  @OpenAPIDefinition(info = @Info(title = "Jeep Sales Service"), servers = { 
  @Server(url = "http://localhost:8080", description = "Local server.")})
  
  public interface JeepSalesController { //@formatter off
  
  //week3:video 3public static final int TRIM_MAX_LENGTH = 30; >put this in constants class, "Constants"

  @Operation(
  
  summary = "Returns a list of Jeeps", description =
  "Returns a list of Jeeps given an optional model and/or trim",
  
  responses = {
  
  @ApiResponse( responseCode = "200", description = "A list of Jeeps is returned.", content
  = @Content(mediaType = "application/json", schema = @Schema(implementation = Jeep.class))),
  
  @ApiResponse( responseCode = "400", description = "Request paramaters are invalid.", content
  = @Content(mediaType = "application/json")),
  
  @ApiResponse( responseCode = "404", description = "No Jeeps were found with the input criteria.",
  content = @Content(mediaType = "application/json")),
  
  @ApiResponse( responseCode = "500", description = "An unplanned error occured.", content
  = @Content(mediaType = "application/json"))
  
  },
  
  parameters = {
  
  @Parameter( name = "model", allowEmptyValue = false, required = false, description =
  "The model name (i.e., 'WRANGLER')"),
  
  @Parameter( name = "trim", allowEmptyValue = false, required = false, description =
  "The trim level (i.e., 'Sport')") }
  
  )
  
  //in week 3, video 3 you add a bean validation @Length and @Pattern
  //you will need to turn bean validation on in the class level with @Validated at the top of this interface
  //Validating user input is a super common requirement in most applications. And the Java Bean Validation 
  //framework has become the de facto standard for handling this kind of logic.
  @GetMapping 
  @ResponseStatus(code = HttpStatus.OK) 
  List<Jeep> fetchJeeps(@RequestParam(required = false)JeepModel model, @Length(max = Constants.TRIM_MAX_LENGTH)@Pattern(regexp = "[]\\w\\s]*") @RequestParam(required = false)
  String trim); 
} 
  //@Length(max = 30) refactor>create constant "TRIM_MAX_LENGTH" for 30 creating constants class       
  //at most, your trim can only have 30 characters. you want to place 
  //@Length first bc the reg expressions become ineffecient with long strings tp prevent regular expression denial of service attack
  //with @Pattern, youre limiting your character classes to a word character and spaces (excludes !, $, etc.)    
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
