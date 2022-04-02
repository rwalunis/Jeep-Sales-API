package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doThrow;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.Constants;
import com.promineotech.jeep.controller.support.FetchJeepTestSupport;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.service.JeepSalesService;
// import io.swagger.v3.oas.models.PathItem.HttpMethod;
import org.springframework.http.HttpMethod;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql (scripts = {"classpath:flyaway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyaway/migrations/V1.0__Jeep_Data.sql"},
     config = @SqlConfig(encoding = "utf-8"))
//you write the @Sql annotation with a script parameter which takes an array of script and executes them in order
//before each test. our scripts are located in source.test.resources >fly away >migrations V1schema 
//we want to add the classpath to V1 Jeep schema
//and V1 Jeep data. we also want to set our configuration to utf-8
//the @ActiveProfiles annotation means Spring boot will look for application-test.yaml with the debug level
//logging configuration 

class FetchJeepTest extends FetchJeepTestSupport {
  
 // @Autowired
 // private JdbcTemplate jdbcTemplate;
  
//@Test
//void testDb() {
//  int numrows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "customers");
//  System.out.println("num" + numrows);
//to check your test is working correctly with h2 db, you can countRowsInTable and wait for expected answer (15)  
//}
 
  
  
//week 3:video 4: create a managed bean (mockbean) so everytime we call method
  //on the mockedbean it will throw an exception, and it needs to be set up
  //in the bean registry(???????) create inner class "TestsThatDoNotPolluteTheApplicationContext" and teststhatdonotpollute...
  //which extends fetchjeeptestsupport class. @Nested + @SpringBootTest creates a new app
  //context for that test. you will copy and paste this classes other tests (minus the stream one) into the 1st nested test
 
  @Nested
  @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
  @ActiveProfiles("test")
  @Sql (scripts = {"classpath:flyaway/migrations/V1.0__Jeep_Schema.sql",
      "classpath:flyaway/migrations/V1.0__Jeep_Data.sql"},
       config = @SqlConfig(encoding = "utf-8"))
  class testsThatDoNotPolluteTheApplicationContext extends FetchJeepTestSupport {
    @Test 
    void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() { 
  //Given a valid model, trim, and uri. remember uri is a uniform resource identifier or string of characters that uniquely identify a resource by name, location, or both on the internet 
  JeepModel model = JeepModel.WRANGLER; 
  String trim = "Sport"; 
  String uri = String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim); 
  //When a connection is made to the uri 
   ResponseEntity<List<Jeep>> response = getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}); 
  //Then a list of jeeps is returned, or your status code (200)id returned/a success
  assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); 
  //AND: the actual list returned is the same as the expected list 
   List <Jeep> actual = response.getBody();
   List <Jeep> expected = buildExpected();
   assertThat(actual).isEqualTo(expected);
   
    }
    @Test 
    void testThatAnErrorMessageIsReturnedWhenAnUnknownTrimIsSupplied() { 
  //Given a valid model, invalid trim, and uri. remember uri is a uniform resource identifier or string of characters that uniquely identify a resource by name, location, or both on the internet 
  JeepModel model = JeepModel.WRANGLER; 
  String trim = "Invalid Value"; 
  String uri = String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim); 
  //When a connection is made to the uri  
   ResponseEntity<Map<String, Object>> response = getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}); 
  //Then a 404 not found status error code is provided
  assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND); 
  //AND an error message is returned 
   Map<String, Object> error = response.getBody();
   assertErrorMessageValid(error, HttpStatus.NOT_FOUND);
    }
    @ParameterizedTest
    @MethodSource("com.promineotech.jeep.controller.FetchJeepTest#parametersForInvalidInput")
    void testThatAnErrorMessageIsReturnedWhenAnInvalidValueIsSupplied(String model, String trim, String reason) { 
  //Given a valid model, invalid trim, and uri. remember uri is a uniform resource identifier or string of characters that uniquely identify a resource by name, location, or both on the internet 

  String uri = String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim); 
  //When a connection is made to the uri  
   ResponseEntity<Map<String, Object>> response = getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}); 
  //Then a 404 not found status error code is provided
  assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); 
  //AND an error message is returned 
   Map<String, Object> error = response.getBody();
   assertErrorMessageValid(error, HttpStatus.BAD_REQUEST);
    }
 
  }
  
  static Stream<Arguments> parametersForInvalidInput () {
    // @formatter: off
    return Stream.of(
        arguments(
            "WRANGLER",
            "@#$#",
            "Trim contains non-alpha-numeric characters"),
        arguments("WRANGLER",
            "C".repeat(Constants.TRIM_MAX_LENGTH + 1), 
            "Trim length is too long"),
        arguments("INVALID",  //add IllegalArguementException in GlobalErrorHandler class-week3/video3
            "Sport", 
            "Model is not enum value")
        
        //@formatter: on
        );
  //takes an array and turns in into a stream  
  }

  //week 3: video 4: you create your mockbean in this class. this bean will
  //be programmed to throw an exception when a method is called on it.
  //mockbean jeepSalvesService is created and is placed in the bean registry
  @Nested
  @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
  @ActiveProfiles("test")
  @Sql (scripts = {"classpath:flyaway/migrations/V1.0__Jeep_Schema.sql",
      "classpath:flyaway/migrations/V1.0__Jeep_Data.sql"},
       config = @SqlConfig(encoding = "utf-8"))
  class testsThatPolluteTheApplicationContext extends FetchJeepTestSupport {
   @MockBean
   private JeepSalesService jeepSalesService;
   /**
    * 
    */
     
 //this test below tests that a 404 error message is given when an invalid trim is applied  
   @Test 
   void testThatAnUnplannedErrorResultsInA500Status() { 
 //Given a valid model, invalid trim, and uri. remember uri is a uniform resource identifier or string of characters that uniquely identify a resource by name, location, or both on the internet 
 JeepModel model = JeepModel.WRANGLER; 
 String trim = "Invalid"; 
 String uri = String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim); 

 //week 3: video 4: programming mock object below w/doThrow
 //in BasicJeepSalesController class, the jeepSalesService mockobject is injected
 //which throws an exception when fetchJeep method is called
 doThrow(new RuntimeException("Ouch!")).when(jeepSalesService).
 fetchJeeps(model, trim);
 
 
 //When a connection is made to the uri  
  ResponseEntity<Map<String, Object>> response = getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}); 
 //Then a 500-internal service error is returned
 assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR); 
 //AND an error message is returned 
  Map<String, Object> error = response.getBody();
  assertErrorMessageValid(error, HttpStatus.INTERNAL_SERVER_ERROR);
   }
  }
  
  
 
  /**
   * 
   */
  

  @Test 
  void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() { 
//Given a valid model, trim, and uri. remember uri is a uniform resource identifier or string of characters that uniquely identify a resource by name, location, or both on the internet 
JeepModel model = JeepModel.WRANGLER; 
String trim = "Sport"; 
String uri = String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim); 
//When a connection is made to the uri 
//ResponseEntity<Jeep> response = getRestTemplate().getForEntity(uri, Jeep.class); //create Jeep class in entity package 
 ResponseEntity<List<Jeep>> response = getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}); 
//A parameterized type reference lets us add in a parameterized type List <Jeep> . if you leave the diamond operator blank, it will infer that it needs a List <Jeep>  
//Then a list of jeeps is returned, or your status code (200)id returned/a success
assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); 
//AND: the actual list returned is the same as the expected list 
 List <Jeep> actual = response.getBody();
 List <Jeep> expected = buildExpected();
 

/// actual.forEach(jeep -> jeep.setModelPK(null));
//the actual statement will expect a lambda expression of jeep. this expression will loop thru
//the actual return values for each element in the list. the lambda expression pulls
 //out the jeep variable of type jeep. for each jeep, we are setting the primary key to zero
 //a better approach though is to modify the jeep class itself where we will tell Jackson to not
 //populate the primary key value coming back 
 //since we don't know what it is yet (week 3- video 1)
 //System.out.println(expected);
 assertThat(actual).isEqualTo(expected);
 //assertThat(response.getBody()).isEqualTo(expected);
 //remove the primary key values from the values that come back
  }

 
  
  
  /**
   * 
   */
    
//this test below tests that a 404 error message is given when an invalid trim is applied  
  @Test 
  void testThatAnErrorMessageIsReturnedWhenAnUnknownTrimIsSupplied() { 
//Given a valid model, invalid trim, and uri. remember uri is a uniform resource identifier or string of characters that uniquely identify a resource by name, location, or both on the internet 
JeepModel model = JeepModel.WRANGLER; 
String trim = "Invalid Value"; 
String uri = String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim); 
//When a connection is made to the uri  
 ResponseEntity<Map<String, Object>> response = getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}); 
//Then a 404 not found status error code is provided
assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND); 
//AND an error message is returned 
 Map<String, Object> error = response.getBody();
 assertErrorMessageValid(error, HttpStatus.NOT_FOUND);
  }
  
//week three-video three test 400-BAD_REQUEST: you create a method which pass in the 
  //http status as a parameter (method assertErrorMessageValid) in FetchJeepTestSupport class  
  //you need to add bean validation by adding it to your pom file, springbootstartervalidation
  //you will add your bean validation in JeepSalesController. you'll also have to add to your GlobalErrorHandler class
  //so you don't get the automated 500 error
  @ParameterizedTest
  @MethodSource("com.promineotech.jeep.controller.FetchJeepTest#parametersForInvalidInput")
  void testThatAnErrorMessageIsReturnedWhenAnInvalidValueIsSupplied(String model, String trim, String reason) { 
//Given a valid model, invalid trim, and uri. remember uri is a uniform resource identifier or string of characters that uniquely identify a resource by name, location, or both on the internet 

String uri = String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim); 
//When a connection is made to the uri  
 ResponseEntity<Map<String, Object>> response = getRestTemplate().exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}); 
//Then a 404 not found status error code is provided
assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); 
//AND an error message is returned 
 Map<String, Object> error = response.getBody();
 assertErrorMessageValid(error, HttpStatus.BAD_REQUEST);
  }

 
}


//week 2-last video: how do you log into your db?  remember in this test class
//you created your "@ActiveProfiles" annotation and set it to "("test")" so you
//could work with logging and override the default logging in your application.yaml file
//(remember default is info logging, not debug which is what you wanted)
//you'll copy your logging information from application.yaml and paste it into
//application-test.yaml and connect it to h2 database. you can leave out the password and username
//bc the h2 database does not require it. you will change the url to "jdbc:h2:mem:jeep" where jeep = db name
//this will override the url in application.yaml and point to our h2 db




/*
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, config = @SqlConfig(encoding = "utf-8"))

class FetchJeepTest {

  @Autowired
  private TestRestTemplate restTemplate;
  @LocalServerPort
  private int serverPort;

  @Test
  void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
    // Given a valid model, trim, and uri.
    JeepModel model = JeepModel.WRANGLER;
    String trim = "Sport";
    String uri =
        String.format("http://localhost:%d/jeeps?model=%s&trim=%s", serverPort, model, trim);
    // When a connection is made to the uri
    ResponseEntity<List<Jeep>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
        new ParameterizedTypeReference<List<Jeep>>() {});
    // Then a list of jeeps is returned, or your status code (200)id returned/a success
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

  }
}

*/




// you will create your test at the top level, then create a support class underneath, and
// then a base test class that's used by all test classes underneath the base test class
// to create your support class, you will have your FetchJeepTest class
// extend "FetchJeepTestSupport" class. You will have three classes in your testing hierarchy,
// "FetchJeepTest" (which will contain the tests)
// "FetchJeepTestSupport"(which will be empty for a while)
// and "BaseTest" (which will add some "support" for us) which all extend upon one another.
// Remember, extension means a class inherits all of the classes/methods/functionality/doings
// of the class it inherits from.
// Rememeber you will need to modify your FetchJeepTest class to make it run as a
// spring boot test w/"@SpringBootTest" so spring boot is in charge of testing.
// you'll want to run your spring test in a web environment so "webEnvironment"
// will be used as a parameter. you dont want all your classes to run on top of each
// other so each test class will need a unique random port through "WebEnvironment.RANDOM_PORT"
// So, when you throw a http request, you will need the host name and port number in order
// to get a random port.
// For the test, the host name is ALWAYS "local host". You will put your annotation for finding the
// random port number in your "BaseTest" class bc we want it to be available for ALL our tests.
// System.out.println(uri);//this will print out
// "http://localhost:64157/jeeps?model=WRANGLER&trim=Sport"
// create jeepmodel enum in seperate entity package where you will import your enum class jeepmodel
// into this class
// remember you need to make an assertion to test your value
// System.out.println(getBaseUri());
// you should see "http://localhost:58704/jeeps" in your console where 58704 is a random port number
// generated that will change every time ran massage method: ge


