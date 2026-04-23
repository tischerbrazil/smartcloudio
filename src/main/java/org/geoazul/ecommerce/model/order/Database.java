package org.geoazul.ecommerce.model.order;

public abstract class Database<T>
{
    public abstract T add(final T p0) throws DatabaseUnavailableException;
    
    public abstract T get(final String p0) throws DatabaseUnavailableException;
}
