package atest;


import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;



import java.io.File;
import java.io.IOException;

public class ShapefileFieldReader {

    public static void main(String[] args) {
        // 1. Specify the path to your .shp file
        File file = new File("/home/jboss/data_dir/s822152117635518587-wmsgeoserver/RO_Municipios_2024.shp");

        try {
            // 2. Obtain a FileDataStore to handle the shapefile
            FileDataStore store = FileDataStoreFinder.getDataStore(file);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            
            // 3. Get the schema (structure) to access field names
            SimpleFeatureType schema = featureSource.getSchema();
           

            for ( AttributeDescriptor    att : schema.getAttributeDescriptors()) {
            }
            
            
            // 4. Access the features (records) in the shapefile
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures();
            
          
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}