package org.geoazul.model.basic.rural;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.geoazul.model.basic.Point;
import org.geoazul.model.basic.Polygon;
import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Geometry;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity 
@Table(name = "app_polygonripointri")
public class PolygonPoint 
{

   @EmbeddedId
   @AttributeOverrides({
         @AttributeOverride(name = "pointriId", column = @Column(name = "pointri_id", nullable = false)),
         @AttributeOverride(name = "polygonriId", column = @Column(name = "polygonri_id", nullable = false)) })
   @NotNull
   private GeometryGeometryId id;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "pointri_id", nullable = false, insertable = false, updatable = false)
   @NotNull
   private Point pointri;
   
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "polygonri_id", nullable = false, insertable = false, updatable = false)
   @NotNull
   private Polygon polygonri;
   
   @Column(name = "pointnext", nullable = true)
   private String pointNext;
   
   @Column(name = "sequencia", nullable = true)
   private Integer sequencia = 0;
   
   @Column(name = "tipo_limite", nullable = true)
   private String tipoLimite;

   @Column(columnDefinition = "Geometry", 
		   nullable = true, name = "geometry")
	@JsonIgnore

	private Geometry geometry;

   
   //org.geoazul.model.basic.rural.PolygonPoint
   //@Column(columnDefinition = "Geometry", nullable = true)
   //private LineString  the_geom;
   
   @Column(name = "Distancia")
   private String distancia;
   
   @Column(name = "Azimuth")
   private String azimuth;
   
   @Column(name = "Angle")  
   private Double angle;
      
   @Column(name = "Leste")  
   private double leste;
   
   @Column(name = "Norte")
   private double norte;
   
   @Column(name = "descritivo", nullable = true)
   private String descritivo;

   @Column(name = "cns", nullable = true)   
   private String cns;

   @Column(name = "matricula", nullable = true)
   private String matricula;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "point_id_to", nullable = true)
   private Point pointTo;
   
   @Column(name = "anchorpointx")  
   private Integer anchorpointx;

   @Column(name = "anchorpointy")  
   private Integer anchorpointy;
   
   @Column(name = "displacementx")  
   private Integer displacementx;
   
   @Column(name = "displacementy")  
   private Integer displacementy;
   
   @Column(name = "rotation")  
   private Integer rotation;

   @Column(name = "marco_label")
   private String marcoLabel;
   
   @ColumnDefault("false")
   @Column(name = "print_conf")
   private Boolean printConf;
   
   @ColumnDefault("false")
   @Column(name = "print_dist")
   private Boolean printDist;
   
   @ColumnDefault("false")
   @Column(name = "print_marco")
   private Boolean printMarco;
        
   public PolygonPoint()
   {
	   this.displacementx = 0;
	   this.displacementy = 0;
	   this.anchorpointx = 0;
	   this.anchorpointy = 0;
	   this.rotation = 0;
	   this.norte = 0;
	   this.leste = 0;
   }

   public PolygonPoint(GeometryGeometryId id, Point pointri, Polygon polygonri, String pointNext, 
       String tipoLimite,  String distancia, String azimuth, 
         double leste, double norte, Point pointTo)
   {
      this.id = id;
      this.pointri = pointri;
      this.polygonri = polygonri;
      this.pointNext = pointNext;
      this.tipoLimite = tipoLimite;
      this.distancia = distancia;
      this.azimuth = azimuth;
      this.leste = leste;
      this.norte = norte;
      this.pointTo = pointTo;
   }

   public GeometryGeometryId getId()
   {
      return this.id;
   }

   public void setId(GeometryGeometryId id)
   {
      this.id = id;
   }

   public Point getPointri()
   {
      return this.pointri;
   }

   public void setPointri(Point pointri)
   {
      this.pointri = pointri;
   }

   public Polygon getPolygonri()
   {
      return this.polygonri;
   }

   public void setPolygonri(Polygon polygonri)
   {
      this.polygonri = polygonri;
   }

   public String getPointNext()
   {
      return this.pointNext;
   }

   public void setPointNext(String pointNext)
   {
      this.pointNext = pointNext;
   }

   public Integer getSequencia()
   {
      return this.sequencia;
   }

   public void setSequencia(Integer sequencia)
   {
      this.sequencia = sequencia;
   }

   public String getTipoLimite()
   {
      return this.tipoLimite;
   }

   public void setTipoLimite(String tipoLimite)
   {
      this.tipoLimite = tipoLimite;
   }

   public String getDistancia()
   {
      return this.distancia;
   }

   public void setDistancia(String distancia)
   {
      this.distancia = distancia;
   }

   public String getAzimuth()
   {
      return this.azimuth;
   }

   public void setAzimuth(String azimuth)
   {
      this.azimuth = azimuth;
   }
   
	public Double getAngle() {
		return angle;
	}

	public void setAngle(Double angle) {
		this.angle = angle;
	}

   public Geometry getGeometry() {
   	return this.geometry;
   }
 
   public void setGeometry(Geometry geometry) {
   	this.geometry = geometry;
   }

   public double getLeste()
   {
      return this.leste;
   }

   public void setLeste(double leste)
   {
      this.leste = leste;
   }

   public double getNorte()
   {
      return this.norte;
   }

   public void setNorte(double norte)
   {
      this.norte = norte;
   }

   public void setDescritivo(String descritivo)
   {
      this.descritivo = descritivo;
   }

   public String getDescritivo()
   {
      return this.descritivo;
   }

   public String getCns()
   {
      return this.cns;
   }

   public void setCns(String cns)
   {
      this.cns = cns;
   }

   public String getMatricula()
   {
      return this.matricula;
   }

   public void setMatricula(String matricula)
   {
      this.matricula = matricula;
   }
   
   public Point getPointTo()
   {
      return this.pointTo;
   }

   public void setPointTo(Point pointTo)
   {
      this.pointTo = pointTo;
   }

   public Integer getAnchorpointx() {
	   return anchorpointx;
   	}

   public void setAnchorpointx(Integer anchorpointx) {
	   this.anchorpointx = anchorpointx;
   }

   public Integer getAnchorpointy() {
	   return anchorpointy;
   }

   public void setAnchorpointy(Integer anchorpointy) {
	   this.anchorpointy = anchorpointy;
   }

	public Integer getDisplacementx() {
		return displacementx;
	}

	public void setDisplacementx(Integer displacementx) {
		this.displacementx = displacementx;
	}

	public Integer getDisplacementy() {
		return displacementy;
	}

	public void setDisplacementy(Integer displacementy) {
		this.displacementy = displacementy;
	}

	public Integer getRotation() {
		return rotation;
	}

	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}
	
	public String getMarcoLabel() {
		return marcoLabel;
	}

	public void setMarcoLabel(String marcoLabel) {
		this.marcoLabel = marcoLabel;
	}
	
	public Boolean getPrintConf() {
		return printConf;
	}

	public void setPrintConf(Boolean printConf) {
		this.printConf = printConf;
	}

	public Boolean getPrintDist() {
		return printDist;
	}

	public void setPrintDist(Boolean printDist) {
		this.printDist = printDist;
	}
	
	public Boolean getPrintMarco() {
		return printMarco;
	}

	public void setPrintMarco(Boolean printMarco) {
		this.printMarco = printMarco;
	}

	
	  @Override
	    public String toString() {
	        return pointri.getId() + ":" + polygonri.getId();
	    }

}
