SmarCloudIO EE GeoWeb App (1.0.0-BETA)

Jakarta EE 11 on Faces 4.1 web application deploy on Wildfly 39 with Java 25.

SmartCloudIO persist data on Postgres and integrating GeoServer involves connecting it with geospatial data sources (PostGIS, Shapefiles), web services (WMS/WFS/WCS) to any Gis Editor with serve interactive maps.

SmartCloudIO is compose to:

1 - Rich Gis Editor on Jakarta Faces use Primefaces a Primefaces extension livrary on rich user interface.
2 - Content Management System based on resuse components, blocks, widgets and conteiners to create a complete web-site.
3 - User management system with Role-based access control (RBAC) restricts system access based on user roles.

1. Introduction

These web application run on the WildFly application server. 

Make sure you read this entire document before you attempt to work this.

2. System Requirements

The applications these projects produce are designed to be run on WildFly Application Server 39 or later such as WAR file or a Bootable App Jar file.

All you need to build these projects is Java SE 17.0 or later, and Maven 3.6.0 or later. (We have use Eclipse Temurin 25 jdk).

3. Define WILDFLY_HOME and another system requiements variables.

export POSTGRESQL_USER='postgres_user_here'
export POSTGRESQL_PASSWORD='postgres_password_here'
export POSTGRESQL_SERVICE_HOST='localhost'
export POSTGRESQL_SERVICE_PORT='5432'
export POSTGRESQL_DATABASE='database_name'
export SAVE_FILE_PATH='/var/geoserver'
export JSON_SECURITY_PATH='/var/geoserver/security'
export JASPER_REPORT_PATH='/var/geoserver/jasper'	
export GEOSERVER_DATA_DIR='/var/geoserver/datadir'
export GEOSERVER_URL='http://127.0.0.1:8081'

4. Suggested Approach to the Quickstarts

If you are a beginner or new to Wildfly you can start with quickstarts compile wildfly create wildfly bootalle app

This expanded capabilities and functionality. If a prerequisite quickstart is listed, make sure you configure systems vars, change standalone.xml file create datasource, enable Jakarta Authentication once again needs to be activated before the application can be deployed (it's a recurring theme for WilfFly it seems). See also https://stackoverflow.com/a/70240973/472792

For creating datasource in wildfly you need add lines to standalone.xml in the dataources section like this:

<datasources>
	<datasource jndi-name="java:jboss/datasources/PostgreSQLDS" pool-name="PostgreSQLDS" enabled="true" use-java-context="true" use-ccm="true">
		<connection-url>jdbc:postgresql://localhost:5432/DATABASENAME_HERE</connection-url>
                    <driver>postgresql</driver>
                    <pool>
                        <flush-strategy>IdleConnections</flush-strategy>
                    </pool>
                    <validation>
                        <check-valid-connection-sql>SELECT 1</check-valid-connection-sql>
                        <background-validation>true</background-validation>
                        <background-validation-millis>60000</background-validation-millis>
                    </validation>
        </datasource>
	<drivers>
		<driver name="postgresql" module="org.postgresql">
        	        <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
        	</driver>
	</drivers>
</datasources>
           
And you must add a JDBC Jar driver on Wilfly Module.

Install  geoserver 3 on another port (recomended 8081) and Postgresql 17 and create the database and install the Postgis Extension.

5 . Build and Deploy SmartCloudIO
 
    Open a terminal and navigate to the root directory of this quickstart.

    Type the following command to build the quickstart.

    $ mvn clean package
    
    Start wildgly on /wildflydir/bin/standalone.sh
    
    or start the Bootble Jar with 
    
    $ java -jar target/app-bootable.jar

Once WildFly starts, please go to http://localhost:8080