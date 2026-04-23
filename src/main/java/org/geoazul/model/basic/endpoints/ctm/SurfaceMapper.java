package org.geoazul.model.basic.endpoints.ctm;

import org.geoazul.model.basic.Polygon;

public class SurfaceMapper {

    public static Surface toSurface(Polygon entity) {

        if (entity == null) return null;

        Surface surface = new Surface();
        surface.setName(entity.getNome());
       //--------------------------------

        return surface;
    }

    public static Polygon toPolygon(Surface surface) {

        if (surface == null) return null;

        Polygon entity = new Polygon();
        entity.setNome(surface.getName());
        //--------------------------------
        return entity;
    }
}
