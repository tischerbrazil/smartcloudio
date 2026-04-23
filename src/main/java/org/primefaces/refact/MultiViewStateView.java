package org.primefaces.refact;


import org.primefaces.PrimeFaces;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("dtMultiViewStateView")
@ViewScoped
public class MultiViewStateView implements Serializable {

    private List<Customer> customers;

    private List<Customer> filteredCustomers;

    private Customer selectedCustomer;

    @Inject
    private CustomerService service;

    @PostConstruct
    public void init() {
       customers = service.getCustomers(50);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Customer> getFilteredCustomers() {
        return filteredCustomers;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public CustomerStatus[] getCustomerStatus() {
        return CustomerStatus.values();
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public void setFilteredCustomers(List<Customer> filteredCustomers) {
        this.filteredCustomers = filteredCustomers;
    }

    public void setService(CustomerService service) {
        this.service = service;
    }

    public void clearMultiViewState() {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        PrimeFaces.current().multiViewState().clearAll(viewId, true, this::showMessage);
    }

    private void showMessage(String clientId) {
        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, clientId + " multiview state has been cleared out", null));
    }
}
