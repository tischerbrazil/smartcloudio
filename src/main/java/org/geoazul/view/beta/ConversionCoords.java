package org.geoazul.view.beta;

public class ConversionCoords {

	
	
   /**
   * Conversions degrees to radians X radian to degrees
   * 1 degree = 0.017453292519943295769236907684886 radians
   * 1 radian = 57.295779513082320876798154814105 degrees;
   * 1 arcmin = 0.000290888208665722 radians
   * 1 arcsec = 0.00000484813681109536 radians.
   */
   private static final double PI_DIV_180 = 0.017453292519943295769236907684886;
   public static final double DEGREES_PER_RADIAN = 57.295779513082320876798154814105;
  
   
  
   /**
    * Constructor:
    * a  Semimayor Axis in the ellipsoide (WGS84)
    * b  Semiminor Axis in the ellipsoide (WGS84)
    * Flattening in the ellipsoide (WGS84)
    * ep Second excentricity
    * ep2 Second excentricity ^2
    */
   private ConversionCoords() {
   }

   /**
    * This method makes the coordinates conversion (geodetic --> tridimensional cartesian)
    * @param lon = longitude
    * @param lat = latitude
    * @param helip = elipsoidal height
    * @param elip = elipsode
    * @return double[] ={X, Y, Z}
    */
   static double[] geotri(final double lon, final double lat, final double helip, final Elipsoide elip) {
       final double[] radios = elip.radios(lat);
       final double rn = radios[1];
       final double rlon = lon * PI_DIV_180;
       final double rlat = lat * PI_DIV_180;
       final double xTri = (rn + helip) * Math.cos(rlat) * Math.cos(rlon);
       final double yTri = (rn + helip) * Math.cos(rlat) * Math.sin(rlon);
       final double a2 = Float11.pow(elip.getA(), 2);
       final double b2 = Float11.pow(elip.getB(), 2);
       final double zTri = ((b2 / a2) * rn + helip) * Math.sin(rlat);
       final double[] tridi = {xTri, yTri, zTri};

       return tridi;
   }

   /**
    * This method makes the coordinates conversion (tridimensional cartesian --> geodetic)
    * @param X 
    * @param Y
    * @param Z
    * @param elip = elipsoide
    * @return double[] = {longitude, latitude, elipsoidal height}
    */
    static double[] trigeo(final double X, final double Y, final double Z, final Elipsoide elip) {

       final double p = Math.sqrt(Float11.pow(X, 2) + Float11.pow(Y, 2));
       final double a = elip.getA();
       final double b = elip.getB();
       final double pe2 = elip.getPe2();
       final double se2 = elip.getSe2();
       final double tecta = Float11.atan((Z * a) / (p * b));
       final double numerador = Z + se2 * b * Float11.pow(Math.sin(tecta), 3);
       final double denominador = p - pe2 * a * Float11.pow(Math.cos(tecta), 3);
       final double latgeorad = Float11.atan(numerador / denominador);
       final double latgeo = latgeorad * DEGREES_PER_RADIAN;
       double longeorad = 0;
       if (X > 0) {
           longeorad = Float11.atan(Y / X);
       }
       if (X < 0 & Y > 0) {
           longeorad = Math.PI + Float11.atan(Y / X);
       }
       if (X < 0 & Y < 0) {
           longeorad = -(Math.PI - Float11.atan(Y / X));
       }
       if (X == 0 & Y > 0) {
           longeorad = Math.PI / 2;
       }
       if (X == 0 & Y < 0) {
           longeorad = -(Math.PI / 2);
       }
       if (X == 0 & Y == 0) {
           longeorad = 0;
       }

       final double longeo = longeorad * DEGREES_PER_RADIAN;

       final double[] radios = elip.radios(latgeo);
       final double rn = radios[1];
       final double h = p / (Math.cos(latgeo) - rn);

       final double[] geodes = {longeo, latgeo, h};
       return geodes;
   }


  
}