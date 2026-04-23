
package modules;

import java.io.Serializable;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoadInitParameter implements Serializable {

	    private static final long serialVersionUID = 3094821516983232692L;
	    
		public final static Integer TSID_NODE = Integer.valueOf(System.getenv("TSID_NODE"));  
	    public final static Integer TSID_NODE_COUNT = Integer.valueOf(System.getenv("TSID_NODE_COUNT"));  

	    public final static String GEOSERVER_DATA_DIR = System.getenv("GEOSERVER_DATA_DIR"); 
	    public final static String GEOSERVER_URL =  System.getenv("GEOSERVER_URL"); 
				
		public final static String pg_HOST = System.getenv("POSTGRESQL_SERVICE_HOST"); 
		public final static String pg_PORT = System.getenv("POSTGRESQL_SERVICE_PORT");
		public final static String pg_DAT = System.getenv("POSTGRESQL_DATABASE");
		public final static String pg_USER = System.getenv("POSTGRESQL_USER");
		public final static String pg_PASS = System.getenv("POSTGRESQL_PASSWORD");

		public final static String save_FILE_PATH = System.getenv("SAVE_FILE_PATH");
		public final static String security_FILE_PATH = System.getenv("JSON_SECURITY_PATH");
		public final static String jasper_FILE_PATH = System.getenv("JASPER_REPORT_PATH");

}
