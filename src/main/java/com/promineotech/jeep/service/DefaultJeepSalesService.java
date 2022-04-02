package com.promineotech.jeep.service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.promineotech.jeep.dao.JeepSalesDao;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import lombok.extern.slf4j.Slf4j;


//remember slf4j is your log line
//this class implements the fetchJeeps method of JeepSalesService class through the
//use of a bean. a bean is a managed object that has an @Service, @Contoller, @Component, or
//@Repository  in it 
//a bean is an implementing class with one of these annotations  listed above. create a bean with these annotations then
//use @Autowired to perform the dependency injection
@Service
@Slf4j
public class DefaultJeepSalesService implements JeepSalesService {

 //In the spring boot, @Autowired annotation is used for dependency
  //injection. Dependency injection (DI) is the concept in which objects get other required objects from outside. 
  //A Java class has a dependency on another class, if it uses an instance of this class
  //In spring boot application, all loaded beans are eligible for auto wiring to another bean. 
  //The annotation @Autowired in spring boot is used to auto-wire a bean into another
  //bean. a bean is an object that is instantiated, assembled, and managed by Spring Inversion
  //of control container. Its an object in your application that spring manipulates.
  //with spring and a container in action, an object can retrieve its dependencies from an
  //inversion of control container instead of constructing the dependencies itself.
  //in this instance, we are having spring injecting the dependency jeepSalesDao from the jeepSalesDao interface
  @Autowired 
  private JeepSalesDao jeepSalesDao;
  
  
  
 // @Override
//  public List<Jeep> fetchJeeps(JeepModel model, String trim) {
 //   log.info("The fetchJeeps method was called with model={} and trim={}", model, trim);
 //rememeber {} are for place holders   
//    return null;
 // }

  
//@Transactional can be added as a class level annotation which will apply
//"transactions" to every public method. meaning that any failure causes the 
//entire operation to roll back to its previous state and to re-throw the original exception
//It allows us to set propagation, isolation, timeout, read-only, and rollback conditions
//for our transaction. Spring creates a proxy, or manipulates the class byte-code, to manage 
 //the creation, commit, and rollback of the transaction.
 //If we don’t provide any value for readOnly in @Transactional, then the default value 
 //will be false. If we use @Transactional(readOnly = true) to a method which is performing
 //create or update operation, then we will not have newly created or updated record into the
 //database but we will have the response data.hmm not sure why you need it in this case since 
 //the fetchJeeps method is only reading/getting information 
  @Transactional(readOnly = true)
  @Override
  public List<Jeep> fetchJeeps(JeepModel model, String trim) {
    log.info("The fetchJeeps method was called with model={} and trim={}", model, trim);
    //rememeber {} are for place holders   
    //   return jeepSalesDao.fetchJeeps(model, trim);
    List<Jeep> jeeps = jeepSalesDao.fetchJeeps(model,  trim);
  //  use a breakpoint on Collections.sort(jeeps) to see what the
  //return value is from the dao, then run jeep test in debug mode(???) your error should arise from returning an empty list of jeep  
  //so you'll need to throw an exception listed below:      
    if(jeeps.isEmpty()) {
      String msg = String.format(
          "No jeeps found with model=%s and trim=%s" , model, trim);
      throw new NoSuchElementException(msg);
    }
    
    Collections.sort(jeeps);
  //collections will sort the list of jeeps   
    return jeeps;
  }

}
//we need to create a bean class and then we need to let spring know that it needs to be managed
//the @Service lets spring know that we want spring to manage this default jeep sales service
//and it is a candidate for injection in jeepsalesserie interface
//remember the @autowired annotation in the controller class that creates an interface object bean that houses
//the fetchjeepmethod(?)
