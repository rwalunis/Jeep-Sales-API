package com.promineotech.jeep.controller.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import lombok.Getter;

public class BaseTest {
  @LocalServerPort
  private int serverPort;
  
  @Autowired
  @Getter
  private TestRestTemplate restTemplate;

  
  /**
   * 
   * @return
   */
  protected String getBaseUriForJeeps() {
    return String.format("http://localhost:%d/jeeps", serverPort);

  }
 //each test will have their own special uri 
  /**
   * 
   * @return
   */
  protected String getBaseUriForOrders() {
    return String.format("http://localhost:%d/orders", serverPort);

  }
  
}



// this is the uri/uniform resource identifier that is going to be sent to the application running
// under the spring boot
// test framework "%d" is the placeholder for the port
// tells spring boot to inject a copy of a test dress template that its created for us
// lombok adds a getter for getBaseUri
// "@LocalServerPort" will create a random port number for all your tests to utilize
// When spring boot starts up the test it creates what's called an "application context"
// The application context contains all the environment configurations/stuff needed in order
// to actually run the test. This is where dependency injection occurs. Dependency injection
// Dependency Injection (DI) is a design pattern that removes the dependency from the programming
// code so that it can
// be easy to manage and test the application.
// Dependency Injection makes our programming code loosely coupled.

