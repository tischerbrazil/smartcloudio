package br.bancodobrasil.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Laercio Tischer
 */

@Entity
@Table(name = "gateway_bancobrasil_api_token")
@NamedQueries({ @NamedQuery(name = "BancoBrasilApiToken.FIND", query = "SELECT s FROM BancoBrasilApiToken s where s.id = 1"),

})
public class BancoBrasilApiToken implements Serializable{

	public BancoBrasilApiToken() {
	}

	private static final long serialVersionUID = 1L;

	public static final String FIND = "BancoBrasilApiToken.FIND";

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "TOKEN")
	private String token; // ":
									// "eyJhbHciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6Im1rX2c1RHJLQ0RzRGt1RjFpTFBuRFdvbUEiLCJNZXJjaGFudFRva2VuIjoibWtfZzVEcktDRHNEa3VGMWlMUG5EV29tQSIsIm1lcmNoYW50Um9sZSI6IntcInBhcmVudE1lcmNoYW50SWRcIjpcImNlODY5Yjc3LWEyYjItNGI5NS04ODYzLTJmNzQ3MzNhMzkyOVwiLFwibWVyY2hhbnRJZFwiOlwiMjhlYjkwODMtZWMyMC00YjBlLTg1ZDYtMjJjZjljMzVhODk4XCIsXCJmYW50YXN5TmFtZVwiOlwiQkVOQ0hNQVJLIFJQQ1wiLFwicm9sZXNcIjpbMl0sXCJtZXJjaGFudFRva2VuXCI6XCJta19nNURyS0NEc0RrdUYxaUxQbkRXb21BXCJ9IiwiUmV0dXJuVXJsIjoiIiwibmJmIjoxNjI0MDE5NTIwLCJleHAiOjE2MjQwMjEzMjAsImlhdCI6MTYyNDAxOTUyMCwiaXNzIjoiQWRpdHVtIiwiYXVkIjoiQWRpdHVtIn0.LGPlDY8qKOvh4UiE2Dq_c3xUJ6gMXjvt_ggC3gQN9tWXXfIhXeDJCqfAuK7omn8OpfnRD3h5Hz2NoAnQOMD4Su-wZB-5SkffhX_DkxeGnyzqnx2wvyinMsbPO55uphW2ed1sLtB1PTTxNTmVSsbzr2W214qTLOJnbf3TTU6oO4NBs5unCte7wDB4uhosPj58uiRqXiiCnGEZeRR9n3qxRLRVJOgoCKhTi5-ewnE0ITppoWWOSY3TuHfvr4B2zfBCMYF8XDxV-W-4xtssR2AvHeHyY345e8N-THa01STPSKl5iK5PyuzuOrrzMpkbIAsqVXVdexl0fMeuoTuwBJl0hg",

	@Column(name = "REFRESH_TOKEN")
	private String refreshToken; // ": "740efb81fcaa4875a54ce724edaf3125",

	@Column(name = "SUCCESS")
	private Boolean success; // ": true,

	@Type(JsonJsonNode.class)
	@Column(name = "ERRORS")
	@ColumnDefault("'{}'") 
	private JsonNode errors; // ": [],

	@Column(name = "TRACEKEY")
	private String traceKey; // ": "375c5e3e-7369-48ea-b036-d9ac65e98182"
		
	@Column(name = "SERVER_NAME")
	private String serverName;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public JsonNode getErrors() {
		return errors;
	}

	public void setErrors(JsonNode errors) {
		this.errors = errors;
	}

	public String getTraceKey() {
		return traceKey;
	}

	public void setTraceKey(String traceKey) {
		this.traceKey = traceKey;
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
		BancoBrasilApiToken other = (BancoBrasilApiToken) obj;
		return Objects.equals(id, other.id);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}




}
