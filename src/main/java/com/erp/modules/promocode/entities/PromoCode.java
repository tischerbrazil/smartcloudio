package com.erp.modules.promocode.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;

@Entity
@Table(name = "erp_promo_code")
@NamedQueries({ @NamedQuery(name = "PromoCode.findByCode", query = "SELECT p FROM PromoCode p WHERE p.code = :code"),
})

public class PromoCode extends LocalGeneratedIdEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROMO_CODE = "PromoCode.findByCode";


	@Column(name = "EXPIRED_DATE")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate expiredDate;

	@Column(name = "PERCENT_OFF")
	private Integer percent_off;

	@Column(name = "TRYAL_DAYS")
	private Integer trialdays;

    @ColumnDefault("true")
	@Column(name = "USED")
	private Boolean used;

	@Column(name = "CODE")
	private String code = createRandomCode(8);

	public PromoCode() {
		this.code = createRandomCode(8);
		this.setUsed(false);
		this.setExpiredDate(LocalDate.now());
	}

	public String createRandomCode(int codeLength) {
		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new SecureRandom();
		for (int i = 0; i < codeLength; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		return output;
	}

	@Override
	public String toString() {
		return "--- PromoCode[ id=" + super.getId() + " ] ---";
	}

	public LocalDate getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(LocalDate expiredDate) {
		this.expiredDate = expiredDate;
	}

	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getPercent_off() {
		return percent_off;
	}

	public void setPercent_off(Integer percent_off) {
		this.percent_off = percent_off;
	}

	public Integer getTrialdays() {
		return trialdays;
	}

	public void setTrialdays(Integer trialdays) {
		this.trialdays = trialdays;
	}

}