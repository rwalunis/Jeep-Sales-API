package com.promineotech.jeep.entity;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import lombok.Data;


@Data //@Data creates your getters and setters w/lombok. jackson must have setters in order to 
//populate this object

public class OrderRequest {
 //bean validators are added to fields in week 4 video 2 
  @NotNull
  @Length(max = 30)
  @Pattern(regexp = "[\\w\\s]*")  
  private String customer;
  
  @NotNull
  private JeepModel model;
  
  @NotNull
  @Length(max = 30)
  @Pattern(regexp = "[\\w\\s]*")
  private String trim;
  //this bean validation has to do something with looking at the json body???
  
  
  @Positive
  @Min(2)
  @Max(4)
  private int doors;
  
  @NotNull
  @Length(max = 30)
  @Pattern(regexp = "[\\w\\s]*")
  private String color;
  
  @NotNull
  @Length(max = 30)
  @Pattern(regexp = "[\\w\\s]*")
  private String engine;
  
  @NotNull
  @Length(max = 30)
  @Pattern(regexp = "[\\w\\s]*")
  private String tire;
  
  private List<@Length(max = 30) @Pattern(regexp = "[\\w\\s]*") String> options;
  //private List<@NotNull@Length(max = 30) @Pattern(regexp = "[A-Z0-9_]*") String> options;
  //week4: video 2---11:49
//in a list the parameter validators are placed inside the <>, looks different compared to String/primitive object types
}