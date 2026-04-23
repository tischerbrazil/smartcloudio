package org.tenant.hibernate.deltaspyke.jpa;

import org.hibernate.HibernateException;
import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.Stoppable;
import static modules.LoadInitParameter.*;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiTenantProvider implements MultiTenantConnectionProvider, Stoppable, ServiceRegistryAwareService {

    private static final long serialVersionUID = 1L;
    
    @Inject
	private HttpServletRequest request;
	
    
    private ConnectionProvider connectionProvider = null;

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }

    @Override
    public void injectServices(ServiceRegistryImplementor serviceRegistry) {
    	
        Map lSettings = serviceRegistry.getService(ConfigurationService.class).getSettings();

        connectionProvider = new C3P0ConnectionProvider(); 
        Class<?> providerClass = connectionProvider.getClass();
        
        if (!Configurable.class.isAssignableFrom(providerClass) ||
                !Stoppable.class.isAssignableFrom(providerClass) ||
                !ServiceRegistryAwareService.class.isAssignableFrom(providerClass)) {
            throw new RuntimeException("The provider '" + providerClass
                    + "' needs to implement the interfaces: Configurable, Stoppable, ServiceRegistryAwareService");
        }

        ((ServiceRegistryAwareService) connectionProvider).injectServices(serviceRegistry);
        ((Configurable) connectionProvider).configure(lSettings);
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return connectionProvider.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        try {
           // connection.createStatement().execute("SET SCHEMA 'geoazul_192_168_100_14'");
        } catch (Exception e) {
            throw new HibernateException("Could not alter JDBC connection to specified schema [public]", e);
        }
        connectionProvider.closeConnection(connection);
    }

    @Override
    public Connection getConnection(Object tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();
        List<Tenant> tenantList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select schema_name from information_schema.schemata");
        while (resultSet.next()) {
            String resultSetString = resultSet.getString(1);
            Tenant tenant = new Tenant(resultSetString);
            tenantList.add(tenant);
        }
        statement.close();
        boolean contains = tenantList.contains(new Tenant(tenantIdentifier));
    

        if (!contains) {
            //criarSchema(tenantIdentifier); //FIXME
        	throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]");
        }else {
    
        
        try {
            boolean execute = connection.createStatement().execute("SET SCHEMA '" + tenantIdentifier + "'");
        } catch (Exception e) {
            throw new HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e);
        }
        }

        return connection;
    }

    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public void stop() {
        ((Stoppable) connectionProvider).stop();
    }
   
   
    public void criarSchema_(String schemaName) {
        Connection connection = null;
        Statement statement = null;
        try {
        	
            String IP_BANCO_DADOS = pg_HOST;
            String PORTA_BANCO_DADOS = pg_PORT;
            String NOME_BANCO_DADOS_CLIENTE = pg_DAT;
            String USUARIO_SISTEMA_BANCO_DADOS = pg_USER;
            String SENHA_SISTEMA_BANCO_DADOS = pg_PASS;
            
            
            
            connection = DriverManager.getConnection("jdbc:postgresql://" + IP_BANCO_DADOS + ":" + PORTA_BANCO_DADOS + "/" + NOME_BANCO_DADOS_CLIENTE, USUARIO_SISTEMA_BANCO_DADOS, SENHA_SISTEMA_BANCO_DADOS);
            statement = connection.createStatement();
            int executeUpdate0 = statement.executeUpdate("CREATE SCHEMA \"" + schemaName + "\" AUTHORIZATION \"" + USUARIO_SISTEMA_BANCO_DADOS + "\";");
            int executeUpdate1 = statement.executeUpdate("GRANT ALL ON SCHEMA \"" + schemaName + "\" TO \"" + USUARIO_SISTEMA_BANCO_DADOS + "\";");
            statement.close();
            connection.close();
        } catch (Exception e) {
            try {
                statement.close();
                connection.close();
            } catch (Exception ignored) {

            }

        }
    }
    


}
