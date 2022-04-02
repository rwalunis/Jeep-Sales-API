package com.promineotech.jeep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//running your app as a "java" app v. "spring" app has differences in color
//@SpringBootApplication(scanBasePackages = {"com.promineotech"})
//import com.promineotech.ComponentScanMarker;
import com.promineotech.ComponentScanMarker;

@SpringBootApplication (scanBasePackageClasses = {ComponentScanMarker.class})

public class JeepSales {

  public static void main(String[] args) {
    SpringApplication.run(JeepSales.class, args);
 
  }

}



// "@SpringBootApplication" This is all you need to write in order to run your Spring Boot Application
// underneath the main method "SpringApplication.run(JeepSales.class, args);" it turns on "@SpringBootConfiguration" and 
//"EnableAuto-Configuration" which is an auto-configuration
//that runs the auto-configuration beans which are classes that will perform their functionality.
//it also contains a "@ComponentScan" (diagram) annotation that causes spring to load/discover every single class
//belonging to the package containing that "@SpringBootApplication" annotation and its subpackage classes. if we want the @ComponentScan
//in the @SpringBootapllication to scan in other places, we can add base packages like "@ComponentScan()" or
//"@SpringBootApplication(scanBasePackages = {"com.promineotech"}) (which takes an array of string) and will now scan one above this level which would
//contain all the classes prior to week 2 since they all have packages with "com.promineotech". now if you mispelled your
//scanBasePackage parameter, your test would fail. one way to fix this from happening is to create a 
//component scan marker class or interface. Sooooo, create a component scan marker interface titled "ComponentScanMarker" and change your @SpringBootApplication
//to "@SpringBootApplication(scanBasePackageClasses = {ComponentScanMarker.class}) " which takes an array of class
 