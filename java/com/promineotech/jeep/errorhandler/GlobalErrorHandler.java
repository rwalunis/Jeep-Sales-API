package com.promineotech.jeep.errorhandler;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import lombok.extern.slf4j.Slf4j;

//week 3: video 2: @RestControllerAdvice and @ExceptionHandler tells spring its a global 
//error handler, then add method where spring
//will map an exception if an exception arises(?) you will override the 200 status code 
//that was
//returned from the controller by adding override @ResponseStatus.
//spring will intercept the no
//such element exception and will call the "handleNoSuchElementException" method, then will
//pass in the exception (nosuchelement)
//remember you created a @transactional annotation in defaultjeepsalesservice 
//with a readonly status to test what would happen if an empty list of jeeps was provided
//by throwing a new nosuchelement exception in the fetchJeeps method of same class(defaultjeepsalesservice)
//we also created a handler method in the GlobalErrorHandler class called "handleNoSuchElementExcpetion" when a 
//500 status code was provided(which is what spring automatically gives with an error
//unless configured otherwise?), to return a 404 status with the nosuchelementexception
//with a custom message that prints out uri,timestamp,reason, statuscode with an 
//assertthat on error return value

  @RestControllerAdvice
  @Slf4j
  public class GlobalErrorHandler {
    private enum LogStatus {
      STACK_TRACE, MESSAGE_ONLY
    }
    
//week 3-video 3
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus (code = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e, WebRequest webRequest) {
      return createExceptionMessage(e, HttpStatus.BAD_REQUEST, webRequest, LogStatus.MESSAGE_ONLY);
    }
    
 //week 3 video 3: adding another globalhandler method for test 400 error/BAD_REQUEST. w/o this, spring will return the automated 500 error(?)
    /**
     * 
     * @param e
     * @param webRequest
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleConstraintViolationException(
        ConstraintViolationException e, WebRequest webRequest) {
      return createExceptionMessage(e, HttpStatus.BAD_REQUEST, 
          webRequest, LogStatus.MESSAGE_ONLY);
    }
    
    
    /**
     * 
     * @param e
     * @param webRequest
     * @return
     */
    
  @ExceptionHandler(NoSuchElementException.class) 
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public Map<String, Object> handleNoSuchElementException(NoSuchElementException e,
      WebRequest webRequest ) {
    return createExceptionMessage(e, HttpStatus.NOT_FOUND, 
        webRequest, LogStatus.MESSAGE_ONLY );    
  }
 //week 3 video 3: this will handle any generic exception. if you get a 
 //generic, unplanned error you will want to log the entire stack trace,
 //in the other previous exceptionhandlers, you don't want to neccessarily log the stack trace,
  //you just want to log the message. to do this, create enum at top of class (logStatus)
  @ExceptionHandler(Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, Object> handleException(Exception e, WebRequest webRequest) {
    return createExceptionMessage(e, HttpStatus.INTERNAL_SERVER_ERROR, 
        webRequest, LogStatus.STACK_TRACE);
  }
  
  

  /**
   * 
   * @param e
   * @param status
   * @param webRequest
   * @return
   */
  
  
  private Map<String, Object> createExceptionMessage(
      Exception e, HttpStatus status, WebRequest webRequest, LogStatus logStatus) {
    Map<String, Object> error = new HashMap <> ();
    String timeStamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
//what does this if statement mean???    
    if (webRequest instanceof ServletWebRequest) {
      error.put("uri", 
          ((ServletWebRequest)webRequest).getRequest().getRequestURI());
    }    
    error.put("message", e.toString());
 //difference between .toString and .getMessage is that .toString in an
 //exception typically will provide the exception class in addition to the message  
    error.put("status code", status.value());  
    error.put("timestamp", timeStamp);
    error.put("reason", status.getReasonPhrase());
    
    if(logStatus == LogStatus.MESSAGE_ONLY) {
      log.error("Exception: {}", e.toString());
    }
    else {
      log.error("Exception: ", e);
    }
    return error;
  }

  
}
