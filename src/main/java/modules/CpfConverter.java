package modules;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;


public class CpfConverter implements Converter {
     @Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {

    	 String cpf = value;
    	 if (cpf != null && cpf.length() == 11)
             cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);

          return cpf;
     }

     @Override
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {

          String cpf= value.toString();
          if (cpf != null && cpf.length() == 11)
               cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);

          return cpf;
     }
}