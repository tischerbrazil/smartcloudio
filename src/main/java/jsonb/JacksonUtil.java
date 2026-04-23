package jsonb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

//import org.geoazul.model.basic.properties.FieldAttribute;

/**
 * @author Vlad Mihalcea
 */
public class JacksonUtil {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  
	public static <T> T fromString(String string, Class<T> clazz) {


    if (clazz.getTypeName().equals("java.lang.String")){
    	 return (T) string;
    }else if (clazz.getTypeName().equals("java.util.List")){
    

    //	List<FieldAttribute> jsonToPersonList = null;
    //	try {
    //		
    	//	TypeReference<List<FieldAttribute>> mapType = new TypeReference<List<FieldAttribute>>() {};
    		
    //	 jsonToPersonList = OBJECT_MAPPER.readValue(string, mapType);
        
    		
		
					 
	//	} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
    
    //	 return (T) jsonToPersonList;
    	return null;
    	 
    }else{
    	
    	try{
    		return  OBJECT_MAPPER.readValue(string, clazz);
        } catch (IOException e) {
           throw new IllegalArgumentException("The given string value: " + string + " cannot be transformed to Json object");
        }
    }
        
    }

    public static String toString(Object value) {

        try {
        	 if (value.getClass().getTypeName().equals("java.lang.String")){
        		 return value.toString();
        	 }else{
        		 return OBJECT_MAPPER.writeValueAsString(value);
        	 }
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("The given Json object value: " + value + " cannot be transformed to a String");
        }
    }

    public static JsonNode toJsonNode(String value) {

        try {
            return OBJECT_MAPPER.readTree(value);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

   
	public static <T> T clone(T value) {
        return fromString(toString(value), (Class<T>) value.getClass());
    }
}