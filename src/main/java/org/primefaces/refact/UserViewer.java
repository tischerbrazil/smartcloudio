package org.primefaces.refact;

import java.io.Serializable;

import org.omnifaces.cdi.ViewScoped;

//import com.flowlogix.jeedao.primefaces.JPALazyDataModel;

import jakarta.inject.Named;
//import lombok.Getter;

@Named
@ViewScoped
public class UserViewer implements Serializable {
	
	
    //private @Getter final JPALazyDataModel<Person2, Long> lazyModel =
     //       JPALazyDataModel.create(builder -> builder
      //              .entityClass(Person2.class)
       //             // the line below is optional, default is case-sensitive (true)
        //            .caseSensitiveFilter(false)
         //           .build());
}
