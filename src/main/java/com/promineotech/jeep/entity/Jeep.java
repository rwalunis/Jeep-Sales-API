package com.promineotech.jeep.entity;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Comparator;

@Data //also creates getters/setters and no args constructor, toString, equalsandhashcode in your outline to right
@Builder //this turns noargsconstructor into an allargsconstructor
@NoArgsConstructor //but you may want to create a noargsconstructor with an empty jeep object which breaks the builder so you have to add the allargs constructor ndown below
@AllArgsConstructor
  

//adding implements Comparable to add sorting to sql statement with returning list of jeep
  public class Jeep implements Comparable<Jeep>{
    private Long modelPK;
    private JeepModel modelId;
    private String trimLevel;
    private int numDoors;
    private int wheelSize;
    private BigDecimal basePrice;
     
//we will want to modify the jeep class itself where we will tell Jackson to not
//populate the primary key value coming back through overriding the the getModelPK getter lombok 
//initially had created for us. "@JsonIgnore" means when jackson is serializing this object into json,
//it will leave out the model primary key (27:13--week 3 video 1)      
    @JsonIgnore
    public Long getModelPK() {
      return modelPK;
    }

    @Override
   public int compareTo(Jeep that) {
   return Comparator.comparing(Jeep::getModelId).
      thenComparing(Jeep::getTrimLevel)
      .thenComparing(Jeep::getNumDoors)
      .compare(this,  that);
    }
    
}
 //method referencing operators that are comparing with one to another (can also be done w/lambda expressions)   
//return a comparison method from the comparator class



//@Getter //import getter lombok to automatically get getters for jeep parameters
//@Setter
//@EqualsAndHashCode  //import lombok statment so you can compare two different jeep objects
//@ToString      //import lombok statmenent creates a ToString method that prints out you jeep object info 
//INSERT INTO models (model_id, trim_level, num_doors, wheel_size, base_price) VALUES('WRANGLER', 'Sport', 2, 17, 28475.00);
//INSERT INTO models (model_id, trim_level, num_doors, wheel_size, base_price) VALUES('WRANGLER', 'Sport', 4, 17, 31975.00);
//go into your V1.0_Jeep_Data text editor and copy the "INSERT INTO" Wrangler and sport values (2 lines) 




