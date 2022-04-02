package com.promineotech.jeep.service;

import java.util.List;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;

public interface JeepSalesService {

  /**
   * 
   * @param model
   * @param trim
   * @return
   */
    
  List<Jeep> fetchJeeps(JeepModel model, String trim);
//you'll need to create an implementing class "DefaultJeepSalesService" that implements the fetchJeeps method through the
//use of a bean. a bean is a managed object that has an @Service, @Contoller, @Component, or
//@Repository  in it 
//a bean is an implementing class with one of these annotations  
  //i mean, you could just implement the service class(defaultjeepsaleservice) inside the bean w/o interface instead the interface containing
  //the service class but interfaces may be a better way bc if you're working with more than one person
  //they can add different components to the interface level so you both can continue working
}
