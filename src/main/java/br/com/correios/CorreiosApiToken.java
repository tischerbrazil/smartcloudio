package br.com.correios;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * @author Laercio Tischer
 */

@Entity
@Table(name = "erp_correios_api_token")
@NamedQueries({ @NamedQuery(name = "CorreiosApiToken.FIND", query = "SELECT c FROM CorreiosApiToken c where c.id = 1"),

})
public class CorreiosApiToken implements Serializable {

	public CorreiosApiToken() {
	}

	private static final long serialVersionUID = 1L;

	public static final String FIND = "CorreiosApiToken.FIND";

	@Id
	private Integer id;
	private String ambiente;
	
	@Column(name = "user_id")
	private String userId;

	private String ip;
	private String perfil;
	private String cpf_cnpj;

	@Column(name = "EMISSAO")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime emissao;

	@Column(name = "EXPIRA_EM")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime expiraEm;

	private String zoneOffset;
	@Column(length = 1024)
	private String token;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getCpf_cnpj() {
		return cpf_cnpj;
	}

	public void setCpf_cnpj(String cpf_cnpj) {
		this.cpf_cnpj = cpf_cnpj;
	}

	public LocalDateTime getEmissao() {
		return emissao;
	}

	public void setEmissao(LocalDateTime emissao) {
		this.emissao = emissao;
	}

	public LocalDateTime getExpiraEm() {
		return expiraEm;
	}

	public void setExpiraEm(LocalDateTime expiraEm) {
		this.expiraEm = expiraEm;
	}

	public String getZoneOffset() {
		return zoneOffset;
	}

	public void setZoneOffset(String zoneOffset) {
		this.zoneOffset = zoneOffset;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CorreiosApiToken other = (CorreiosApiToken) obj;
		return Objects.equals(id, other.id);
	}

}