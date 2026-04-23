package org.geoazul.ecommerce.view.shopping;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.geoazul.ecommerce.model.Item;

import java.util.*;


@Named
@RequestScoped
public class RatedItemsBean {

    // ======================================
    // =          Injection Points          =
    // ======================================

    //@Inject
    //private FacesContext facesContext;



    // ======================================
    // =             Attributes             =
    // ======================================

    List<Item> topRatedItems;
    Set<Item> randomItems = new HashSet<>();

    // ======================================
    // =         Lifecycle methods          =
    // ======================================

  

    // ======================================
    // =          Business methods          =
    // ======================================

  


    // ======================================
    // =        Getters and Setters         =
    // ======================================

    public List<Item> getTopRatedItems() {
        return topRatedItems;
    }

    public void setTopRatedItems(List<Item> topRatedItems) {
        this.topRatedItems = topRatedItems;
    }

    public List<Item> getRandomItems() {
        return new ArrayList<>(randomItems);
    }
}