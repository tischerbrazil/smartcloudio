package org.geoazul.view.location;

import static java.util.Optional.ofNullable;
import static modules.LoadInitParameter.save_FILE_PATH;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Optional;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.geoazul.model.Contador;
import org.geoazul.model.security.RealmEntity;
import org.keycloak.example.oauth.UserData;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.model.InsightsResponse;

public class LocationService implements Serializable {

	private static final long serialVersionUID = 1L;

	private DatabaseReader dbReader;

	@Inject
	private EntityManager entityManager;

	@Inject
	private UserData userData;

	public Optional<RealmEntity> findById(String name) {
		TypedQuery<RealmEntity> query = entityManager.createNamedQuery(RealmEntity.REALM_NAME_GET, RealmEntity.class);
		query.setParameter("name", name);
		return getOptionalSingleResult(query);
	}

	@SuppressWarnings("hiding")
	public static <RealmEntity> Optional<RealmEntity> getOptionalSingleResult(TypedQuery<RealmEntity> typedQuery) {
		return ofNullable(getSingleResultOrNull(typedQuery));
	}

	public static <T> T getSingleResultOrNull(TypedQuery<T> typedQuery) {
		try {
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	public Contador locationPersist(String ip, String sessionId, Long gcmid) throws IOException {

		Geometry geometry = null;

		Optional<RealmEntity> realm = findById(userData.getRealmEntity());

		int accountId = 1297595;
		String licenseKey = "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";
		InsightsResponse response = null;
		String subdivision = null;
		String subdivisionISO = null;
		InetAddress ipAddress = InetAddress.getByName(ip);
		
		try {
			WebServiceClient client = new WebServiceClient.Builder(accountId, licenseKey).build();

			response = client.insights(ipAddress);

	

			if (response.subdivisions().size() > 0) {
				subdivision = response.subdivisions().get(0).name();
				subdivisionISO = response.subdivisions().get(0).name();
			}

			String pointString = "POINT(" + response.location().longitude().toString() + " "
					+ response.location().latitude().toString() + ")";
			WKTReader wktReader = new WKTReader();
			geometry = wktReader.read(pointString);
			geometry.setSRID(4326);

		} catch (Exception e) {
		}
		Contador contador1 = new Contador(realm.get(), sessionId, ip, gcmid, response.country().name(),
				response.country().isoCode(), subdivision, subdivisionISO, response.city().name(),
				response.postal().code(), geometry);

		locationPersist1(contador1);
		return contador1;
	}

	@Transactional
	public void locationPersist1(Contador contador1) {
		try {
			entityManager.persist(contador1);
			entityManager.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DatabaseReader getDbReader() {
		return dbReader;
	}

	public void setDbReader(DatabaseReader dbReader) {
		this.dbReader = dbReader;
	}

}