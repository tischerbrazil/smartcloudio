package atest;

import static java.time.Instant.now;
import java.io.Serializable;
import java.time.Instant;
import org.terracotta.statistics.archive.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;

@MappedSuperclass
public abstract class TimestampedEntity extends GeneratedIdEntity implements Timestamped, Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private Instant created;

	@Column(name="last_modified" ,nullable = false)
	private Instant lastModified;

	@Transient
	private Boolean skipAdjustLastModified = true;
	
	

	public Boolean getSkipAdjustLastModified() {
		return skipAdjustLastModified;
	}

	public void setSkipAdjustLastModified(Boolean skipAdjustLastModified) {
		this.skipAdjustLastModified = skipAdjustLastModified;
	}

	@PrePersist
	public void onPrePersist() {
		Instant timestamp = now();
		setCreated(timestamp);
		setLastModified(timestamp);
	}

	@PreUpdate
	public void onPreUpdate() {
		if (!skipAdjustLastModified) {
			Instant timestamp = now();
			setLastModified(timestamp);
		}
	}

	public void setCreated(Instant created) {
		this.created = created;
	}

	public Instant getCreated() {
		return created;
	}

	public void setLastModified(Instant lastModified) {
		this.lastModified = lastModified;
	}

	public Instant getLastModified() {
		return lastModified;
	}

}