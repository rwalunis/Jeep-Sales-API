package com.promineotech.jeep.service;

import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.OrderRequest;

//created in week 4 video 2.
public interface JeepOrderService {

  Order createOrder(OrderRequest orderRequest);

}
