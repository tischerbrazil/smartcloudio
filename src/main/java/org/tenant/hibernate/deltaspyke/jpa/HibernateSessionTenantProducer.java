package org.tenant.hibernate.deltaspyke.jpa;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import static modules.LoadInitParameter.*;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class HibernateSessionTenantProducer {
	
	

    private SessionFactory sessionFactory;
    
    private CurrentTenantIdentifierResolver currentTenantIdentifierResolver;

    public HibernateSessionTenantProducer() {
          
    
        Map<String, String> properties = new HashMap<>();
        properties.put(Environment.JAKARTA_JDBC_URL, "jdbc:postgresql://" + pg_HOST + ":" + pg_PORT + "/" + pg_DAT);
        properties.put(Environment.JAKARTA_JDBC_PASSWORD, pg_PASS);
        properties.put(Environment.JAKARTA_JDBC_USER, pg_USER);
        
        
        properties.put(Environment.HBM2DDL_AUTO, "none");
		
       
        properties.put(Environment.C3P0_MIN_SIZE, "5");
        properties.put(Environment.C3P0_MAX_SIZE, "30");
       
        properties.put(Environment.C3P0_ACQUIRE_INCREMENT, "5");
        properties.put(Environment.C3P0_TIMEOUT, "1800");

        properties.put(Environment.C3P0_MAX_STATEMENTS, "0");
       
        properties.put(Environment.C3P0_IDLE_TEST_PERIOD, "500"); 
        
        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        
       //properties.put("hibernate.search.backend.type", "elasticsearch");
      

         properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, "org.tenant.hibernate.deltaspyke.jpa.MultiTenantProvider");
                  
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("risteos", properties);
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    @Produces
    @ApplicationScoped
    public SessionFactory getSessionFactory() {
         return sessionFactory;
    }

    @Inject
    @TenantInject
    private Tenant tenant;

    @Produces
    @RequestScoped
    public Session getSession() {
        Session session = sessionFactory.withOptions().tenantIdentifier(tenant.getId()).openSession();
        return session;
    }

    public void closeSession(@Disposes Session session) {
        session.close();
    }


}
