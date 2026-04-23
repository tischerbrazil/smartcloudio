package org.geoazul.view.utils;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;

public class GeometryBuffer {

	
	 public Geometry runBuffer(final Geometry geom,final double bufferSize,final String endCapStyle, int quadrantSegments){
		   BufferOp bufOp=null;
		   if (endCapStyle.equalsIgnoreCase("SQUARE")) {
			   BufferParameters squareParam = new BufferParameters(BufferParameters.DEFAULT_QUADRANT_SEGMENTS,BufferParameters.CAP_SQUARE);
			   squareParam.setQuadrantSegments(quadrantSegments);
			   bufOp=new BufferOp(geom,squareParam);
		   } else   if (endCapStyle.equalsIgnoreCase("FLAT")) {
			  BufferParameters flatParam = new BufferParameters(BufferParameters.CAP_FLAT, BufferParameters.JOIN_MITRE);
			  flatParam.setQuadrantSegments(0);
			  flatParam.setMitreLimit(0);
			  flatParam.setJoinStyle(0);
		     bufOp=new BufferOp(geom,flatParam);
		     
		   }else  if (endCapStyle.equalsIgnoreCase("SIDE")) {
			   BufferParameters sideParam = new BufferParameters();
			   sideParam.setSingleSided(true);
			   		  
			   bufOp=new BufferOp(geom,sideParam);
		   } else if (endCapStyle.equalsIgnoreCase("ROUND")) {
		      BufferParameters roundParam = new BufferParameters(BufferParameters.DEFAULT_QUADRANT_SEGMENTS,BufferParameters.CAP_ROUND);
			  roundParam.setQuadrantSegments(quadrantSegments);
		     bufOp=new BufferOp(geom,roundParam);
		   }
		   
		   
		   return bufOp.getResultGeometry(bufferSize);
		}
	
	
}
