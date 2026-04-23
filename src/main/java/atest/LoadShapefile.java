package atest;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Collections;

import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

public class LoadShapefile {

	    public static void main(String[] args) throws IOException {
	        // Specify the path to your .shp file
	        File file = new File("/home/tischer/projetos/qgis/CREDISUL/CAR/APPS/APPS_1.shp");

	        // Use DataStoreFinder to find the appropriate factory and create a DataStore
	        Map<String, Object> map = Collections.singletonMap("url", file.toURI().toURL());
	        DataStore dataStore = DataStoreFinder.getDataStore(map);

	        if (dataStore == null) {
	            System.exit(1);
	        }

	        // Get the name of the first feature type (usually just one in a shapefile)
	        String typeName = dataStore.getTypeNames()[0];

	        // Get the feature source
	        FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore.getFeatureSource(typeName);
	        
	        // Get all features from the shapefile
	        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures(Filter.INCLUDE);

	        
	        // Iterate through the features
	        try (FeatureIterator<SimpleFeature> features = collection.features()) {
	           // while (features.hasNext()) {
	            //    SimpleFeature feature = features.next();
	                // You can access attributes by name or index
	         //   }
	        } finally {
	            // It's good practice to close the DataStore when you're done
	            dataStore.dispose();
	        }
	    }
}

