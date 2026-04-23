FROM eclipse-temurin:25-jdk

COPY /target/app-bootable.jar /app-bootable.jar

ENV PORT 8443
ENV POSTGRESQL_USER smart
ENV POSTGRESQL_PASSWORD smartpass 
ENV POSTGRESQL_SERVICE_HOST localhost
ENV POSTGRESQL_SERVICE_PORT 5433
ENV POSTGRESQL_DATABASE hgeo
ENV KEYCLOAK_SERVICE_HOST auto
ENV SERVER_REQUEST_PATH app
ENV SAVE_FILE_PATH /var/geoserver
ENV JSON_SECURITY_PATH /var/geoserver/security
ENV JASPER_REPORT_PATH /var/geoserver/jasper
ENV TSID_NODE 1
ENV TSID_NODE_COUNT 1024
ENV GEOSERVER_DATA_DIR /var/geoserver/datadir
ENV GEOSERVER_URL https://geoserver.smartcloudio.com.br


CMD ["java", "-jar","/app-bootable.jar"]

