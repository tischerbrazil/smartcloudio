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
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonBancoBrasilCalendario implements UserType<BancoBrasilCalendario> {

	public static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public int getSqlType() {
		return SqlTypes.JSON;
	}

	@Override
	public Class<BancoBrasilCalendario> returnedClass() {
		return BancoBrasilCalendario.class;
	}

	@Override
	public boolean equals(BancoBrasilCalendario x, BancoBrasilCalendario y) {
		return (x == y) || (x != null && x.equals(y));
	}

	@Override
	public int hashCode(BancoBrasilCalendario x) {
		return x.hashCode();
	}

	@Override
	public BancoBrasilCalendario nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session,
			Object owner) throws SQLException {

		final String cellContent = rs.getString(position);
		if (cellContent == null) {
			return null;
		}
		try {
			return MAPPER.readValue(cellContent.getBytes("UTF-8"), returnedClass());
		} catch (final Exception ex) {
			throw new RuntimeException("Failed to convert String to BancoBrasilCalendario: " + ex.getMessage(), ex);
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement st, BancoBrasilCalendario value, int index,
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
			throw new RuntimeException("Failed to convert BancoBrasilCalendario to String: " + ex.getMessage(), ex);
		}

	}

	@Override
	public BancoBrasilCalendario deepCopy(BancoBrasilCalendario value) {
		try {
			// use serialization to create a deep copy
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(value);
			oos.flush();
			oos.close();
			bos.close();

			ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
			BancoBrasilCalendario obj = (BancoBrasilCalendario) new ObjectInputStream(bais).readObject();
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
	public Serializable disassemble(BancoBrasilCalendario value) {
		return (Serializable) value;
	}

	@Override
	public BancoBrasilCalendario assemble(Serializable cached, Object owner) {
		return (BancoBrasilCalendario) cached;
	}

	@Override
	public BancoBrasilCalendario replace(BancoBrasilCalendario detached, BancoBrasilCalendario managed, Object owner) {
		return (BancoBrasilCalendario) detached;
	}

}
