package org.geoazul.model.mobile;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import org.hibernate.annotations.NaturalId;
import com.erp.modules.commonClasses.BaseEntity;

@Entity
@Table(name = "MOB_CURRENCY")
@NamedQueries({
	@NamedQuery(name = CurrencyEntity.CURRENCY_ALL, query = "SELECT currency FROM CurrencyEntity currency "),
})
public class CurrencyEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String CURRENCY_ALL = "getCurrencyAll";
	
	private String symbol; // R$
	
	private String name; // Brazilian Real
	
	private String symbol_native; // R$
	
	private String decimal_digits; // 2
	
	@Column(name="rounding", columnDefinition = "smallint")
	private Integer rounding; // 0
	
	private String code; // BRL
	
	private String name_plural; // Brazilian reals
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol_native() {
		return symbol_native;
	}

	public void setSymbol_native(String symbol_native) {
		this.symbol_native = symbol_native;
	}

	public String getDecimal_digits() {
		return decimal_digits;
	}

	public void setDecimal_digits(String decimal_digits) {
		this.decimal_digits = decimal_digits;
	}

	public Integer getRounding() {
		return rounding;
	}

	public void setRounding(Integer rounding) {
		this.rounding = rounding;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName_plural() {
		return name_plural;
	}

	public void setName_plural(String name_plural) {
		this.name_plural = name_plural;
	}

	
}

