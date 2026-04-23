package atest;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.kml.v22.KMLConfiguration;
import org.geotools.xsd.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import java.io.InputStream;


public class KMLImporter {
    public static void main(String[] args) throws Exception {
        File kmlFile = new File("/home/tischer/Documentos/teste2.kml");
       // KMLConfiguration configuration = new KMLConfiguration();
        InputStream inputStream = new FileInputStream(kmlFile);

        // Create a parser with the KML configuration
        Parser parser = new Parser(new KMLConfiguration());

        // Parse the KML document (this often returns the top-level Document/Folder as a SimpleFeature)
        SimpleFeature kmlFeature = (SimpleFeature) parser.parse(inputStream);
        
        // Extract the list of nested features (Placemarks, Folders, etc.)
        // KML features are typically found under an attribute named "Feature"

        List<SimpleFeature> features = (List<SimpleFeature>) kmlFeature.getAttribute("Feature");

        if (features != null) {

            for (SimpleFeature feature : features) {
                // Access the default geometry, which might be a Point, LineString, Polygon, etc.
                Object defaultGeometry = feature.getDefaultGeometry();
                
                if (defaultGeometry instanceof Polygon) {
                	Polygon polygon = (Polygon) defaultGeometry;
                }
                // You can add more checks for other geometry types if needed
            }
        }
        
        
        
       
    }
}
