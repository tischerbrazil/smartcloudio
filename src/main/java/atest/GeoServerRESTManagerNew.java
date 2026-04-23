package atest;



import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.GeoWebCacheREST;
import it.geosolutions.geoserver.rest.http.GeoServerRestAuthenticator;
import it.geosolutions.geoserver.rest.http.UsernamePasswordAuthenticator;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTAbstractManager;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTResourceManager;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTSecurityManager;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStructuredGridCoverageReaderManager;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStyleManager;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTCustomService;

import java.net.URL;


public class GeoServerRESTManagerNew extends GeoServerRESTAbstractManager {

    private final GeoServerRESTPublisherNew publisher;
    private final GeoServerRESTReader reader;

    private final GeoServerRESTStoreManager storeManager;
    private final GeoServerRESTStyleManager styleManager;
    
    private final GeoServerRESTStructuredGridCoverageReaderManager structuredGridCoverageReader;
    
    private final GeoWebCacheREST geoWebCacheRest;
    private final GeoServerRESTResourceManager resourceManager;
    private final GeoServerRESTSecurityManager securityManager;
    
    /**
     * Default constructor.
     *
     * Indicates connection parameters to remote GeoServer instance.
     *
     * @param restURL GeoServer REST API endpoint
     * @param username GeoServer REST API authorized username
     * @param password GeoServer REST API password for the former username
     * @throws java.lang.IllegalArgumentException
     */
    public GeoServerRESTManagerNew(URL restURL, String username, String password)
            throws IllegalArgumentException {
        this(restURL, new UsernamePasswordAuthenticator(username, password));
    }

    /**
     * Default constructor.
     *
     * Indicates connection parameters to remote GeoServer instance.
     *
     * @param restURL GeoServer REST API endpoint
     * @param auth GeoServer REST API authenticator
     * @throws java.lang.IllegalArgumentException
     */
    public GeoServerRESTManagerNew(URL restURL, GeoServerRestAuthenticator auth)
            throws IllegalArgumentException {
        super(restURL, auth);

        // Internal publisher and reader, provide simple access methods.
        publisher = new GeoServerRESTPublisherNew(restURL.toString(), auth);
        reader = new GeoServerRESTReader(restURL, auth);
        structuredGridCoverageReader = new GeoServerRESTStructuredGridCoverageReaderManager(restURL, auth);
        storeManager = new GeoServerRESTStoreManager(restURL, auth);
        styleManager = new GeoServerRESTStyleManager(restURL, auth);
        geoWebCacheRest = new GeoWebCacheREST(restURL, auth);
        resourceManager = new GeoServerRESTResourceManager(restURL, auth);
        securityManager = new GeoServerRESTSecurityManager(restURL, auth);
    }

    /**
     * <p>Getter for the field <code>publisher</code>.</p>
     *
     * @return a {@link it.geosolutions.geoserver.rest.GeoServerRESTPublisher} object.
     */
    public GeoServerRESTPublisherNew getPublisher() {
        return publisher;
    }

    /**
     * <p>Getter for the field <code>reader</code>.</p>
     *
     * @return a {@link it.geosolutions.geoserver.rest.GeoServerRESTReader} object.
     */
    public GeoServerRESTReader getReader() {
        return reader;
    }

    /**
     * <p>Getter for the field <code>storeManager</code>.</p>
     *
     * @return a {@link it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager} object.
     */
    public GeoServerRESTStoreManager getStoreManager() {
        return storeManager;
    }

    /**
     * <p>Getter for the field <code>styleManager</code>.</p>
     *
     * @return a {@link it.geosolutions.geoserver.rest.manager.GeoServerRESTStyleManager} object.
     */
    public GeoServerRESTStyleManager getStyleManager() {
        return styleManager;
    }

    /**
     * <p>Getter for the field <code>structuredGridCoverageReader</code>.</p>
     *
     * @return a {@link it.geosolutions.geoserver.rest.manager.GeoServerRESTStructuredGridCoverageReaderManager} object.
     */
    public GeoServerRESTStructuredGridCoverageReaderManager getStructuredGridCoverageReader() {
        return structuredGridCoverageReader;
    }

    /**
     * <p>Getter for the field <code>geoWebCacheRest</code>.</p>
     *
     * @return a {@link it.geosolutions.geoserver.rest.GeoWebCacheREST} object.
     */
    public GeoWebCacheREST getGeoWebCacheRest() {
        return geoWebCacheRest;
    }
    
    /**
     * <p>Getter for the field <code>resourceManager</code>.</p>
     *
     * @return a {@link it.geosolutions.geoserver.rest.manager.GeoServerRESTResourceManager} object.
     */
    public GeoServerRESTResourceManager getResourceManager() {
        return resourceManager;
    }
    
    /**
     * <p>Getter for the field <code>securityManager</code>.</p>
     *
     * @return a {@link it.geosolutions.geoserver.rest.manager.GeoServerRESTSecurityManager} object.
     */
    public GeoServerRESTSecurityManager getSecurityManager() {
        return securityManager;
    }
    
    /**
     * <p>Getter for the field <code>custom service</code>.</p>
     *
     * @return a {@link it.geosolutions.geoserver.rest.manager.GeoServerRESTCustomService} object.
     */
    public GeoServerRESTCustomService getCustomService(String service) {
        return new GeoServerRESTCustomService(gsBaseUrl, auth, service);
    }

}
