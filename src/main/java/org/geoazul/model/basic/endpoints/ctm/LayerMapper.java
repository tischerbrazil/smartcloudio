package org.geoazul.model.basic.endpoints.ctm;

import java.util.ArrayList;
import java.util.List;

import org.geoazul.model.basic.Polygon;

public class LayerMapper {

    public static LayerSurface toSurface(List<Polygon> entities) {

        if (entities == null) return null;

        LayerSurface layerSurface = new LayerSurface();
        layerSurface.setSurfaces(entities);
        layerSurface.setTotalRecords(entities.size());
       //--------------------------------

        return layerSurface;
    }

   
}
