package org.geoazul.ecommerce.model.order;

import java.util.Random;
import java.util.Collection;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.ArrayList;

public abstract class Service
{
    protected final Database database;
    public ArrayList<Exception> exceptionsList;
    private static final String ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final Hashtable<String, Boolean> USED_IDS;
    
    protected Service(final PaymentDatabase db, final Exception... exc) {
        this.database = db;
        this.exceptionsList = new ArrayList<Exception>(Arrays.asList(exc));
    }
    
    public abstract String receiveRequest(final Object... p0) throws DatabaseUnavailableException;
    
    protected abstract String updateDb(final Object... p0) throws DatabaseUnavailableException;
    
    protected String generateId() {
        final StringBuilder random = new StringBuilder();
        final Random rand = new Random();
        while (random.length() < 12) {
            final int index = (int)(rand.nextFloat() * "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".length());
            random.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".charAt(index));
        }
        String id = random.toString();
        if (Service.USED_IDS.get(id) != null) {
            while (Service.USED_IDS.get(id)) {
                id = this.generateId();
            }
        }
        return id;
    }
    
    static {
        USED_IDS = new Hashtable<String, Boolean>();
    }
}