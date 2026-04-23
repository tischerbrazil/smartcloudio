package modules;

import java.io.Serializable;
import java.util.List;

import jakarta.faces.model.SelectItem;


public class FormField implements Serializable {

    private static final long serialVersionUID = 20120521L;

    private Object value;
    private boolean required;
    private List<SelectItem> selectItems;

    public FormField(Object value) {
        this.value = value;
    }

    public FormField(boolean required) {
        this.required = required;
    }

    public FormField(Object value, boolean required) {
        this.value = value;
        this.required = required;
    }

    public FormField(Object value, boolean required, List<SelectItem> selectItems) {
        this.value = value;
        this.required = required;
        this.selectItems = selectItems;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }
}