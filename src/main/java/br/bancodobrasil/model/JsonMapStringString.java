package br.bancodobrasil.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;


import com.fasterxml.jackson.databind.ObjectMapper;
public class JsonMapStringString implements UserType<Map> {

	 public static final ObjectMapper MAPPER = new ObjectMapper();
	 
	 
		@Override
		public int getSqlType() {
			 return SqlTypes.JSON;
		}

		@Override
		public Class<Map> returnedClass() {
			  return Map.class;
		}

		@Override
		public boolean equals(Map x, Map y) {
			return (x == y) || (x != null && x.equals(y));
		}

		@Override
		public int hashCode(Map x) {
			return x.hashCode();
		}

		@Override
		public Map nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session,
				Object owner) throws SQLException {
			
	            final String cellContent = rs.getString(position);
	            if (cellContent == null) {
	                return null;
	            }
	            try {
	                return MAPPER.readValue(cellContent.getBytes("UTF-8"), returnedClass());
	            } catch (final Exception ex) {
	                throw new RuntimeException("Failed to convert String to Map: " + ex.getMessage(), ex);
	            }
		}

		@Override
		public void nullSafeSet(PreparedStatement st, Map value, int index,
				SharedSessionContractImplementor session) throws SQLException {
			if (value == null) {
	            st.setNull(index, Types.OTHER);
	            return;
	        }
	        try {
	            final StringWriter w = new StringWriter();
	            MAPPER.writeValue(w, value);
	            w.flush();
	            st.setObject(index, w.toString(), Types.OTHER);
	        } catch (final Exception ex) {
	            throw new RuntimeException("Failed to convert Map to String: " + ex.getMessage(), ex);
	        }
			
		}

		@Override
		public Map deepCopy(Map value) {
			 try {
		            // use serialization to create a deep copy
		            ByteArrayOutputStream bos = new ByteArrayOutputStream();
		            ObjectOutputStream oos = new ObjectOutputStream(bos);
		            oos.writeObject(value);
		            oos.flush();
		            oos.close();
		            bos.close();
		             
		            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
		            Map obj = (Map)new ObjectInputStream(bais).readObject();
		            bais.close();
		            return obj;
		        } catch (ClassNotFoundException | IOException ex) {
		            throw new HibernateException(ex);
		        }
		}

		@Override
		public boolean isMutable() {
			return false;
		}

		@Override
		public Serializable disassemble(Map value) {
			return (Serializable) value;
		}

		@Override
		public Map assemble(Serializable cached, Object owner) {
			return (Map) cached;
		}

		@Override
		public Map replace(Map detached, Map managed, Object owner) {
			return (Map) detached;
		}
	
	

	

	
   
  
   
	
	
}
