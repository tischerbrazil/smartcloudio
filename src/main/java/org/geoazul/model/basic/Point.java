package org.geoazul.model.basic;

import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POINT GeoEJB
 */

@Entity
@Table(name = "APP_POINT")
public class Point extends AbstractGeometry {
		
	/**
	 *  
	 */
	
	private static final long serialVersionUID = 1L;

	//@Column(columnDefinition = "Geometry", nullable = true, name = "geometrypoint")
	@Column(columnDefinition = "Geometry", nullable = true, name = "geometry")
	@JsonIgnore

	private Geometry geometry;
	
	   @ManyToMany(targetEntity = Polygon.class)
	   @JoinTable(name = "APP_POLYGONRIPOINTRI",
	         joinColumns = @JoinColumn(name = "pointri_id"),
	         inverseJoinColumns = @JoinColumn(name = "polygonri_id")
	         )
		@JsonIgnore
	
	   private Set<Polygon> obra;
	   	   
	   @Column(name = "metodo_levantamento")
	   private String metodoLevantamento;

	   @Column(name = "sigmae")
	   private Double sigmaE = 0.00;
	 
	   @Column(name = "sigman")
	   private Double sigmaN = 0.00;

	   @Column(name = "altura")
	   private Double altura = 0.00;

	   @Column(name = "sigmah")
	   private Double sigmaH = 0.00;
	   
	   @Transient
	   private String geometrywkt;

	    public Geometry getGeometry() {
			return this.geometry;
		}

		public void setGeometry(Geometry geometry) {
			this.geometry = geometry;
		}
			 

	   public Set<Polygon> getPolygon()
	   {
	      return obra;
	   }

	   public void setPolygon(Set<Polygon> obra)
	   {
	      this.obra = obra;
	   }


	
	   public String getMetodoLevantamento()
	   {
	      return this.metodoLevantamento;
	   }

	   public void setMetodoLevantamento(String metodoLevantamento)
	   {
	      this.metodoLevantamento = metodoLevantamento;
	   }

	   public Double getSigmaE()
	   {
	      return this.sigmaE;
	   }

	   public void setSigmaE(Double sigmaE)
	   {
	      this.sigmaE = sigmaE;
	   }

	   public Double getSigmaN()
	   {
	      return this.sigmaN;
	   }

	   public void setSigmaN(Double sigmaN)
	   {
	      this.sigmaN = sigmaN;
	   }

	   public Double getAltura()
	   {
	      return this.altura;
	   }

	   public void setAltura(Double altura)
	   {
	      this.altura = altura;
	   }

	   public Double getSigmaH()
	   {
	      return this.sigmaH;
	   }

	   public void setSigmaH(Double sigmaH)
	   {
	      this.sigmaH = sigmaH;
	   }

		@JsonIgnore
	
	   public String getGeometrywkt() {
			return geometry.toString();
		}

		public void setGeometrywkt(String geometrywkt) {
			this.geometrywkt = geometrywkt;
		}
	

}
