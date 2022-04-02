package com.promineotech.jeep.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.service.JeepSalesService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j

public class BasicJeepSalesController implements JeepSalesController {

 //@Autowired tells Spring we want an object (bean?) injected here into jeepSalesService
  @Autowired
  private JeepSalesService jeepSalesService;
  
  @Override
  public List<Jeep> fetchJeeps(JeepModel model, String trim) {
    log.debug("model = {}, trim = {}", model, trim);
    //return null; (causes test to go red in week 2-video 2)
    return jeepSalesService.fetchJeeps(model, trim);
  }

  
}

// @Override
// public List<Jeep> fetchJeeps(String model, String trim) {
//   log.info("model = {}, trim = {}", model, trim);
//log.debug("model = {}, trim = {}", model, trim);  

 //  return null;
// }


//using lombok annotation to create your "logging". uses a logging bridge, slf4j(?)
//slf4j creates a "log" instance variable of type logger object (you can see it in your Outline to right)
//which lets you create a "log" where you write "log." and select your log level(there are a lot of "log" levels to choose from)
//an api typically has their loggers log on the "info" logging level. this means that anything below the info level (ex:debugger trace)
//will NOT get logged
//remember anything written inside of a "{}" means it is a replaceable parameter. what's written after the curly brackets means those are your
//input parameters

//remember anything written inside of a "{}" means it is a replaceable parameter. what's written after the curly brackets means those are your
//input parameters. so whatever we pass in as the model and trim parameter will be "logged" or submitted. also remember that your
//"fetchJeeps" method is written in your interface "JeepSalesController" where it "gets" /reads a List of Jeeps given a model and trim     
//Prior to week 2/video two you have been logging in at the info level, but how do you do logging
//on the debugging level? INFO is used to log the information your program is working as expected. DEBUG is used to find the reason in case your program
//is not working as expected or an exception has occurred. it's in the interest of the developer.When you are logging at the info level, you will see
//a "log" line. logging at the debug level will not have the "log" line after info is logged into the web api. at default, logger runs on info level.
//you can add application configuration to change that in the "Application.yaml" class
//this class will start/change all loggers that start with "com.promineotech" at the debug level  
//copy this class into your "test/srs/resources" package. rename this class in the resources package to "Application-test.yaml"  
//create an annotation "@ActiveProfiles("test")" in your "FetchJeepTest" class. 
//you must use "@RestController" annotation inside of your implementing class (this one) and not
//the actual interface "JeepSalesController". @RestController is an annotation type that implements
//two other annotations, "@Controller" (used by Spring that lets Spring know that this class
//is indeed a Contoller class and should be paid attention to (handled specially and mapped. Spring life cycles???)
//and "@ResponseBody" maps everything to JSON payload type into a Java object(?) and reverse-wise,
//anything you receive from the API service will be converted into a JSON payload type(?)