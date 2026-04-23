package org.geoazul.view.beta;

import org.locationtech.jts.geom.Coordinate;


public class ProcessAreaCartesian2 {

	
public Coordinate PTL(Coordinate origem, Coordinate destino, double semiEixoMaior, double semiEixoMenor, double f, double ht){
    	
       		//	double a = 6378137.00000; // Semi-eixo maior do elipsóide de referência  
    		//	double b = 6356752.31414; // Semi-eixo menor do elipsóide de referência

    			double fs = (semiEixoMaior - semiEixoMenor) / semiEixoMaior;
    			
    			   
    			
    	
				
		double senLonOrigin = Math.sin(origem.y/180.0*Math.PI);
		double cosLonOrigin = Math.cos(origem.y/180.0*Math.PI);


		double tanLongGeoOrigin = Math.tan(origem.y/180.0*Math.PI); // EXTRAIR O COSENO DA LONGITUDE  Ï•
		
		double senLonP;
		double cosLonP; 

				
		
		
	    	
    		
    		 senLonP = Math.sin(destino.y/180.0*Math.PI);
    		 cosLonP = Math.cos(destino.y/180.0*Math.PI); 

    	     
    	     

    			// e2 --> Raio de curvatura da seÃ§Ã£o meridiana do elipsÃ³ide de referÃªncia em P 0 (origem do sistema);
    			// Dependency a,b
    			double e2 = (Math.pow(semiEixoMaior, 2) -  Math.pow(semiEixoMenor, 2)) / Math.pow(semiEixoMaior, 2) ;
    			//double e2 = 0.00669454;  // Primeira excentricidade do elipsÃ³ide de referÃªncia 0.006694541915624534
    		

    			// Start the convert cartesian function 

    			double deltaLatGeoLinha  = (destino.x - origem.x) * 3600;  // Î”Î»" 
    			double deltaLonGeoLinha  = (destino.y - origem.y) * 3600;  // Î”Ï•" 
    							
    				
    			double deltaLatGeoLinhaUm = 	deltaLatGeoLinha * (1 - 3.9173 * Math.pow(10, -12) * Math.pow(deltaLatGeoLinha, 2)); // Î”Î»1
    			
    			double deltaLonGeoLinhaUm = deltaLonGeoLinha * (1 - 3.9173 * Math.pow(10, -12) * Math.pow(deltaLonGeoLinha, 2)); // Î”Ï•1 
    			
    			
    			
    			// VAMOS ALEM
    			double arc1 = 0.0000048481; // seno 1"
		
    			
    			// Mo --> Raio de curvatura da seÃ§Ã£o meridiana do elipsÃ³ide de referÃªncia em P 0 (origem do sistema);
    			// Dependency a, e2, sinlonGeoO(Ï•) 
    	 		double senoLongQuad = Math.pow( senLonOrigin , 2) ;   
    			double Mo1 = semiEixoMaior * (1 -e2 ) ;
    			double Mo2 = (1 - e2 * senoLongQuad);
    			double Mo4 = Math.pow( Mo2 , 3);   
    			double Mo5 = Math.sqrt(Mo4);   
    			double Mo = Mo1 / Mo5;

    							
    			// No --> Raio de curvatura da seÃ§Ã£o normal ao plano meridiano do elipsÃ³ide de referÃªncia em Po
    			// Dependency a, e2, senoLongQuad(Ï•)
    			double No1 = (1 - e2 * senoLongQuad);
    			double No3 = Math.sqrt(No1);   
    			double No = semiEixoMaior / No3;
    			
    			// Ro --> Raio de curvatura da seÃ§Ã£o normal ao plano meridiano do elipsÃ³ide de referÃªncia em Po
    			// Dependency Mo, No, No2
    			double Ro1 = Mo * No;
    			double Ro =  Math.sqrt(Ro1);  
    	      	
    			// Np --> Raio de curvatura da seÃ§Ã£o normal ao plano meridiano do elipsÃ³ide de referÃªncia em Po
    			// Dependency a, e2, sinlonGeo(Ï•)
    			
    			double senoLongQuadP = Math.pow( senLonP , 2) ;  
    			double Np1 = (1 - e2 * senoLongQuadP);
    			double Np3 = Math.sqrt(Np1);   
    			double Np = semiEixoMaior / Np3;
    			
    			// c --> Fator de elevaÃ§Ã£o, adimensional. Fator (c) 
    			// Dependency Ro, ht
    			
    			double c = (Ro + ht) / Ro;  
    			
    			
    			// B --> 
    			// Dependency arc1, Mo
    			double B = 1 / (Mo * arc1);
    			
    			// C -->  
    			// Dependency tanLongGeoOrigin, Mo, No, arc1
    			double C = (-1) * tanLongGeoOrigin / (2 * Mo * No * arc1 );
    		
    			// D -->
    			// Dependency e2,sinlonGeoO,coslonGeoO,arc1
    			double D = (-1) * ( 3 * e2 *  senLonOrigin * cosLonOrigin * arc1) / (2 * (1 - e2 * Math.pow(senLonOrigin, 2)));
    		
    			// E -->
    			// Dependency tanLongGeoOrigin, Mo, No, arc1
    			double E1 = ( 1 + (3 * tanLongGeoOrigin )) ;
    			double E2 = 6 * Math.pow(No, 2);
    			double E = E1 / E2;
    			
    			
    		
    			// xp -->  Ponto X em P'
    			// Dependency deltaLatGeoLinhaUm, coslonGeo, No, arc1, c
    			double xp = (-1) * deltaLatGeoLinhaUm * cosLonP * Np * arc1 * c ;

    			
    			// yp -->  Ponto Y em P'
    			// Dependency deltaLatGeoLinha, coslonGeo, No, arc1, c
    			double yp1 = 1 / B;
    			double yp2 = deltaLonGeoLinhaUm + C * Math.pow(xp, 2) + D * Math.pow(deltaLonGeoLinhaUm, 2);
    			double yp3 = E * (deltaLonGeoLinhaUm) * Math.pow(xp, 2) + E * C * Math.pow(xp, 4);
    			double yp = yp1 * ( yp2 + yp3) * c;
    			
    			Coordinate coordenadaLocal = new Coordinate(xp,yp);

	   
		 return coordenadaLocal;
		
		

		
	    

    }
	
	
}
