package com.promineotech.jeep.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import com.promineotech.jeep.entity.Color;
import com.promineotech.jeep.entity.Customer;
import com.promineotech.jeep.entity.Engine;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Option;
import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.Tire;

public interface JeepOrderDao {

  //week 4 vido 3: you add Optional so instead of potentially returning a null value
  //it will return an optional 
  
  List<Option> fetchOptions(List<String> optionIds);//this method needs a list of optionIds
  
  Optional <Customer> fetchCustomer(String customerId);

  Optional <Jeep> fetchModel(JeepModel model, String trim, int doors);

  Optional<Tire> fetchTire(String tireId);

  Optional<Color> fetchColor(String colorId);

  Optional<Engine> fetchEngine(String engineId);

  Order saveOrder(Customer customer, Jeep jeep, Color color, Engine engine, Tire tire,
      BigDecimal price, List<Option> options);
}
