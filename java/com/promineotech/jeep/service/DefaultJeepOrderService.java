package com.promineotech.jeep.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.promineotech.jeep.dao.JeepOrderDao;
import com.promineotech.jeep.entity.Color;
import com.promineotech.jeep.entity.Customer;
import com.promineotech.jeep.entity.Engine;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.Option;
import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.OrderRequest;
import com.promineotech.jeep.entity.Tire;

//week 4 video 2
@Service
public class DefaultJeepOrderService implements JeepOrderService {

  @Autowired
  private JeepOrderDao jeepOrderDao;
  
//for this testing method, we need to fetch all of the records in the orderRequest
 //which will include the orderRequest's customer, model, trim, etc.  it involves a LOT of helper methods
//created in this class and the defaultjeeporderdao class  
  @Transactional
  @Override
  public Order createOrder(OrderRequest orderRequest) {
 //week 4-video 3: orElseThrow w/an empty lambda with no parameters
    Customer customer = getCustomer(orderRequest);        
   //week 4: video 2--- Customer customer = jeepOrderDao.fetchCustomer(orderRequest.getCustomer());
//fetchCustomer is in JeepOrderDao interface, createOrder method is in JeepOrderService interface 
 //in order to create an order, you will need to have a customer put that order in along with    
 //jeep, color, engine, and tire.
    Jeep jeep = getJeep(orderRequest);
    Color color = getColor(orderRequest);
    Engine engine = getEngine(orderRequest);
    Tire tire = getTire(orderRequest);   
    List<Option> options = getOption(orderRequest);
 //week 4-video three adds BigDecimal java class that adds together all the different
 //prices for a lump sum price   
    BigDecimal price = jeep.getBasePrice().add(color.getPrice()).
        add(engine.getPrice()).add(tire.getPrice());
 //week 4-video 3: create saveOrder method in JeepOrderDao interface
  //and add option prices  
    for(Option option : options) {
      price = price.add(option.getPrice());
     //since its a big decimal, you have to have price = price...??? 
    }
    return jeepOrderDao.saveOrder(customer, jeep, color, engine, tire, price, options);

}
//week 4-video 3: creating getOption method with another method you will create: fetchOptions
//inside your JeepOrderDao interface  
  private List<Option> getOption(OrderRequest orderRequest) {
    return jeepOrderDao.fetchOptions(orderRequest.getOptions());
    
    }
    
  

  private Tire getTire(OrderRequest orderRequest) {
    return jeepOrderDao.fetchTire(orderRequest.getTire())
        .orElseThrow(() -> new NoSuchElementException("Tire with ID=" +
            orderRequest.getTire() + " was not found"));
  }

  private Engine getEngine(OrderRequest orderRequest) {
    return jeepOrderDao.fetchEngine(orderRequest.getEngine())
        .orElseThrow(() -> new NoSuchElementException("Engine with ID=" +
            orderRequest.getEngine() + " was not found"));
  }

  private Color getColor(OrderRequest orderRequest) {
    return jeepOrderDao.fetchColor(orderRequest.getColor())
        .orElseThrow(() -> new NoSuchElementException("Color with ID=" +
            orderRequest.getColor() + " was not found"));
  }

  private Jeep getJeep(OrderRequest orderRequest) {
 // private Jeep getModel(OrderRequest orderRequest) {
    return jeepOrderDao.fetchModel(orderRequest.getModel(), orderRequest.getTrim(), orderRequest.getDoors())
        .orElseThrow(() -> new NoSuchElementException("Model with ID=" +
            orderRequest.getModel() + ", trim=" + orderRequest.getTrim() 
            + orderRequest.getDoors() + " was not found"));
  }

  private Customer getCustomer(OrderRequest orderRequest) {
    return jeepOrderDao.fetchCustomer(orderRequest.getCustomer())
        .orElseThrow(() -> new NoSuchElementException("Customer with ID=" +
        orderRequest.getCustomer() + " was not found"));
  }
}