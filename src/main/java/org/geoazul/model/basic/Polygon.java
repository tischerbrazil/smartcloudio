package org.geoazul.model.basic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.geoazul.model.basic.rural.PolygonPoint;
import org.geoazul.model.ctm.ParcelEntity;
import org.locationtech.jts.geom.Geometry;
import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "APP_POLYGON")
@NamedQueries({
	    @NamedQuery(name = Polygon.SURFACE_ID, query = "SELECT p FROM Polygon p WHERE p.id = :id"),
		@NamedQuery(name = Polygon.SURFACE_ITEMS33, query = "SELECT  polygon FROM Polygon polygon WHERE polygon.layer = :layer"),
		@NamedQuery(name = Polygon.SURFACE_ITEMS_FILTER33, query = "SELECT  polygon FROM Polygon polygon "
				+ "WHERE polygon.father = :father AND " + "polygon.layer = :layer"),
		@NamedQuery(name = "getPolOriginId", query = "SELECT p "
				+ "FROM Polygon p "
				+ "WHERE p.origin.id = :originId"),
		@NamedQuery(name = Polygon.SURFACE_ITEMS_FILTER_SEC33, query = 
				"SELECT  polygon FROM Polygon polygon "
				+ "JOIN AbstractGeometry father  " 
				+ "ON father = polygon.father " 
				+ "WHERE polygon.layer = :layer AND " 
				+ "father.father = :fatherSecond"),
})
public class Polygon extends AbstractGeometry {

	public static final String SURFACE_ID = "getPoygonId";
	public static final String SURFACE_ORIGIN_ID = "getPolOriginId";
	public static final String SURFACE_ITEMS33 = "getSurfacesItems33";
	public static final String SURFACE_ITEMS_FILTER33 = "getSurfacesItemsFilter33";
	public static final String SURFACE_ITEMS_FILTER_SEC33 = "getSurfacesItemsFilterSec33";

	public Polygon() {
	}

	@Column(columnDefinition = "Geometry", nullable = true, name = "geometry")
	@JsonIgnore
	private Geometry geometry;
	
	@Transient
	private String geometrywkt;

	@Transient
	private String maxX;

	@Transient
	private String maxY;

	@Transient
	private String minX;

	@Transient
	private String minY;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH })
	@JoinTable(name = "APP_POLYGONRIPOINTRI", joinColumns = {
			@JoinColumn(name = "polygonri_id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "pointri_id", nullable = false) })
	@JsonIgnore

	private Set<Point> vertices = new HashSet<Point>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "polygonri", cascade = { CascadeType.REMOVE }, orphanRemoval = true)
	@OrderBy("sequencia ASC")
	@JsonIgnore

	private List<PolygonPoint> verticeObrasList = new ArrayList<PolygonPoint>();

	@Column(name = "area")
	private Double area = 0.0;

	@Column(name = "perimetro")
	private Double perimetro = 0.0;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH }, orphanRemoval = true, mappedBy = "surface")
	@OrderBy("parte ASC")
	private List<ParcelEntity> parcels = new ArrayList<ParcelEntity>();

	public Geometry getGeometry() {
		return this.geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	@JsonIgnore
    public String getGeometrywkt() {
		return geometry.toString();
	}

	public void setGeometrywkt(String geometrywkt) {
		this.geometrywkt = geometrywkt;
	}

	public void addVerticeObra(PolygonPoint verticeObra) {
		verticeObrasList.add(verticeObra);
	}

	public void removeVerticeObra(PolygonPoint verticeObra) {
		verticeObrasList.remove(verticeObra);
	}

	public void sequencia() {
		Iterator<PolygonPoint> VOrdem = verticeObrasList.iterator();
		Integer novaSequencia = 1;
		List<PolygonPoint> voNewList = new ArrayList<PolygonPoint>();
		while (VOrdem.hasNext()) {
			PolygonPoint VOOrdem = VOrdem.next();
			VOOrdem.setSequencia(novaSequencia);
			voNewList.add(VOOrdem);
			novaSequencia++;
		}
		this.setVerticeObrasList(voNewList);
	}

	public void exclude() {
		Iterator<PolygonPoint> VOrdem = verticeObrasList.iterator();
		Integer novaSequencia = 1;
		List<PolygonPoint> voNewList = new ArrayList<PolygonPoint>();
		while (VOrdem.hasNext()) {
			PolygonPoint VOOrdem = VOrdem.next();
			voNewList.add(VOOrdem);
			novaSequencia++;
		}
		this.setVerticeObrasList(voNewList);
	}

	// ================================ END FUNCTIONS
	// ------------------ TRANSIENTS

	public String getMaxX() {
		Double maxXDouble = 0.0;
		try {
			maxXDouble = this.getGeometry().getEnvelope().getEnvelopeInternal().getMaxX();
		} catch (Exception e) {
			maxXDouble = 0.0;
		}
		return maxXDouble.toString();
	}

	public void setMaxX(String maxX) {
		this.maxX = maxX;
	}

	public String getMaxY() {
		Double maxYDouble = 0.0;
		try {
			maxYDouble = this.getGeometry().getEnvelope().getEnvelopeInternal().getMaxY();
		} catch (Exception e) {
			maxYDouble = 0.0;
		}
		return maxYDouble.toString();
	}

	public void setMaxY(String maxY) {
		this.maxY = maxY;
	}

	public String getMinX() {
		Double minXDouble = 0.0;
		try {
			minXDouble = this.getGeometry().getEnvelope().getEnvelopeInternal().getMinX();
		} catch (Exception e) {
			minXDouble = 0.0;
		}
		return minXDouble.toString();
	}

	public void setMinX(String minX) {
		this.minX = minX;
	}

	public String getMinY() {
		Double minYDouble = 0.0;
		try {
			minYDouble = this.getGeometry().getEnvelope().getEnvelopeInternal().getMinY();
		} catch (Exception e) {
			minYDouble = 0.0;
		}
		return minYDouble.toString();
	}

	public void setMinY(String minY) {
		this.minY = minY;
	}

	public Set<Point> getVertices() {
		return this.vertices;
	}

	public void setVertices(Set<Point> vertices) {
		this.vertices = vertices;
	}

	public List<PolygonPoint> getVerticeObrasList() {
		return verticeObrasList;
	}

	public void setVerticeObrasList(List<PolygonPoint> verticeObrasList) {
		this.verticeObrasList = verticeObrasList;
	}

	public Double getArea() {
		return this.area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public Double getPerimetro() {
		return this.perimetro;
	}

	public void setPerimetro(Double perimetro) {
		this.perimetro = perimetro;
	}

	public List<ParcelEntity> getParcels() {
		return parcels;
	}

	public void setParcels(List<ParcelEntity> parcels) {
		this.parcels = parcels;
	}

}
