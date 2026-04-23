package org.geoazul.view.beta;



public class Elipsoide {
	private static final Elipsoide WGS84 = new Elipsoide(6378137.0,1/298.257222101);
	
   
	/**
	 * Semimayor Axis
	 */
	private double a;
	/**
	 * Flattening
	 */
	private double f;
	/**
	 * Semiminor Axis
	 */
	private double b;
	/**
	 * First Excentricity
	 */
	private double pe;
	/**
	 * First Excentricity ^2
	 */
	private double pe2;
	/**
	 * Second Excentricity
	 */
	private double se;
	/**
	 * Second Excentricity ^2
	 */
	private double se2;
	/**
	 * Radio de curvatura polar
	 */
	private double c;
	
	
	/**
	 * Constructor
	 * @param _a  Semimayor Axis elipsoide
	 * @param _f  Flattening elipsoide
	 */
	public Elipsoide(double _a, double _f){
		this.a = _a;
		this.f = _f;

		b = a*(1-f);
		pe = Math.sqrt(((Float11.pow(a,2))-(Float11.pow(b,2)))/((Float11.pow(a,2))));
		se = Math.sqrt(((Float11.pow(a,2))-(Float11.pow(b,2)))/((Float11.pow(b,2))));		
		pe2 = Float11.pow(pe,2);
		se2 = Float11.pow(se,2);
		c = (Float11.pow(a,2))/b;
	}

	
	/**
	 * 
	 * @param lat = latitude
	 * @return double[] = {rm, rn, rg}
	 */
	public double[] radios (double lat){
		
		double rlat = lat*Math.PI/180;
		double denomin = (1-pe2*(Float11.pow(Math.sin(rlat),2)));
		/* radio meridiano*/
		double rm = (a*(1-pe2))/(Float11.pow(denomin, 1.5));
		/* radio primer vertical*/
		double rn = a/(Math.sqrt(denomin));
		/* radio gaussiano */
		double rg = Math.sqrt(rm*rn);
		
		double[] lRadios = {rm, rn, rg}; 
		return lRadios;
		
	}
	
	/**
	 * Get Semimayor Axis
	 */
	public double getA() {
		return a;
	}

	/**
	 * Get Semiminor Axis
	 */
	public double getB() {
		return b;
	}

	/**
	 * Get Flattening
	 */
	public double getF() {
		return f;
	}

	/**
	 * Get First Excentricity
	 */
	public double getPe() {
		return pe;
	}

	/**
	 * Get First Excentricity ^2
	 */
	public double getPe2() {
		return pe2;
	}

	/**
	 * Get Second Excentricity
	 */
	public double getSe() {
		return se;
	}

	/**
	 * Get Second Excentricity ^2
	 */
	public double getSe2() {
		return se2;
	}
	/**
	 * Get Radio de curvatura polar
	 */
	public double getC() {
		return c;
	}


	public static Elipsoide getWGS84() {
		return WGS84;
	}
}