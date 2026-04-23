package org.omnifaces.optimusfaces.test.model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity<I extends Comparable<I> & Serializable> implements Comparable<BaseEntity<I>>, Identifiable<I>, Serializable {


	/**
	 * Hashes by default the ID.
	 */
	@Override
    public int hashCode() {
        return (getId() != null)
        	? Objects.hash(getId())
        	: super.hashCode();
    }

	/**
	 * Compares by default by entity class (proxies taken into account) and ID.
	 */
	@Override
    public boolean equals(Object other) {
        return (getId() != null && getClass().isInstance(other) && other.getClass().isInstance(this))
            ? getId().equals(((BaseEntity<?>) other).getId())
            : (other == this);
    }

	/**
	 * Orders by default with "nulls last".
	 */
	@Override
	public int compareTo(BaseEntity<I> other) {
		return (other == null)
			? -1
			: (getId() == null)
				? (other.getId() == null ? 0 : 1)
				: getId().compareTo(other.getId());
	}

	/**
	 * The default format is <code>ClassName[{id}]</code> where <code>{id}</code> defaults to <code>@hashcode</code> when null.
	 */
	@Override
	public String toString() {
		return String.format("%s[%s]", getClass().getSimpleName(), (getId() != null) ? getId() : ("@" + hashCode()));
	}

}