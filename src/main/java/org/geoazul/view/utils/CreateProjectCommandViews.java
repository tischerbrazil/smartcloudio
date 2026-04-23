package org.geoazul.view.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import static modules.LoadInitParameter.*;

public class CreateProjectCommandViews { 
  
 
	   
	  
	   public String runProcessViews(String  glebaId, EntityManager entityManager) {


		
		String glebaIdF = glebaId.replace("-", ""); 
	
		String txx = "CREATE TABLE IF NOT EXISTS process.obratemp" + glebaIdF + " (" +
					" id character varying(50) NOT NULL ," +
					" geometry geometry(Geometry,3857)," +
					" codigo character varying(50)," +
					" cpf character varying(50)," +
					" detentor character varying(50)," +
					" nome character varying(255)," +
					" start integer," +
					" valoroffset integer," +
					" gleba_id character varying(50)," +
					" lote character varying(50)," +
					" tipo_id integer," +
					" capstyle character varying(255)," +
					" cns character varying(20)," +
					" matricula character varying(20)," +
					" parte smallint," +
					" imovel_id integer," +
					" natureza_id integer," +
					" situacao smallint," +
					" transmat smallint," +
					" CONSTRAINT obratemp" + glebaIdF + "_pkey PRIMARY KEY (id) " +
					" )" +
					" WITH ( " +
					" OIDS=FALSE" +
					");";
		   
		Query queryNativa1 = entityManager.createNativeQuery(txx);

		queryNativa1.executeUpdate(); 
						
					   
		String txx3 = "CREATE OR REPLACE VIEW process.vp4326_view_" + glebaIdF + " AS  (" +
				   " SELECT DISTINCT vobra.id," +
				   "   vobra.obra_id," +
				   "   vobra.sequencia," +
				   "   vobra.tipolimite," +
				   "   vobra.descritivo," +
				   "   vobra.the_geom" +
				   "  FROM verticesirgas2000 v2000," +
				   "   vertice vert," +
				   "   verticeobra vobra," +
				   "   obra obr," +
				   "   imovel imo" +
				   " WHERE v2000.vertice_id = vert.id AND vert.id = vobra.vertice_id "
				   + "AND vobra.obra_id = obr.id AND obr.imovel_id = imo.id AND public.st_astext(vobra.the_geom) IS NOT NULL "
				   + "AND imo.gleba_id = '" + glebaId + "' ORDER BY vobra.sequencia )";
		   Query queryNativa3 = entityManager.createNativeQuery(txx3);
		   queryNativa3.executeUpdate(); 
		
		   String txx4 = "CREATE OR REPLACE VIEW process.limite4326_view_" + glebaIdF + " AS  (" +
					    "SELECT DISTINCT limite.id," +
					    "limite.nome," +
 						"limite.processo_id," +
 						"limite.the_geom," +
 						"limite.data," +
 						"limite.user_id ," +
 						"limite.limitepai_id " +
					   " FROM limite limite, processo processo" +
					   "  WHERE limite.processo_id = processo.id AND processo.gleba_id = '" + glebaId + "' AND limite.the_geom IS NOT NULL " +
					   "  ORDER BY limite.id)";
			 Query queryNativa4 = entityManager.createNativeQuery(txx4);
			 queryNativa4.executeUpdate(); 
				
				
			 String txx5 = "CREATE OR REPLACE VIEW process.obra4326_view_" + glebaIdF + " AS  (" +
						    "SELECT DISTINCT obra.id," +
						    "obra.imovel_id," +
					        "obra.origin_id," +
						    "imovel.detentor," +
	 						"imovel.gleba_id," +
	 						"imovel.natureza_id," +
	 						"obra.lote," +
	 						"obra.nome," +
	 						"obra.state," +
	 						"obra.the_geom" +
						   " FROM obra obra," +
						   "imovel imovel" +
						   " WHERE obra.imovel_id = imovel.id AND obra.the_geom IS NOT NULL AND imovel.gleba_id = '" + glebaId + "' AND obra.state > 0" +
						   " ORDER BY imovel.natureza_id DESC)";
				Query queryNativa5 = entityManager.createNativeQuery(txx5);
				queryNativa5.executeUpdate(); 

				String txx6 = "CREATE OR REPLACE VIEW process.vertice4326_view_" + glebaIdF + " AS  (" +
							    "SELECT DISTINCT vert.id," +
							    "vert.marco," +
							    "processo.gleba_id," +
		 						"processo.tipo_id," +
		 						"v2000.metodolevantamento AS metodo," +
		 						"v2000.the_geom" +
		 						" FROM verticesirgas2000 v2000," +
		 						" vertice vert," +
							   "processo processo" + 
							   " WHERE v2000.vertice_id = vert.id AND vert.processo_id = processo.id AND processo.gleba_id = '" + glebaId  +
							   "' ORDER BY vert.id)";
				Query queryNativa6 = entityManager.createNativeQuery(txx6);
				queryNativa6.executeUpdate(); 
				
				String txx7 = "CREATE OR REPLACE VIEW process.limiteoffset4326_view_"  + glebaIdF + " AS (" +   
						 "SELECT DISTINCT limite.id," +
						 "limite.nome," +
						 "limite.processo_id," +
						 "limite.the_geom," +
						 "limite.data," +
						 "limite.user_id," +
						 "limite.tipolimite," +
						 "limite.limitepai_id" +
						 " FROM limite limite," + 
						 "processo processo" + 
						 " WHERE limite.processo_id = processo.id AND processo.gleba_id = '" + glebaId  +
						 "' AND public.st_astext(limite.the_geom) IS NOT NULL AND public.st_geometrytype(limite.the_geom) = 'Polygon'" +
						 " ORDER BY limite.id)";
	
						   
				Query queryNativa7 = entityManager.createNativeQuery(txx7); 
				queryNativa7.executeUpdate(); 

				
				
				
				
				
				
				
				
				
				
				
				
			
		  return "success";
	   }
}
