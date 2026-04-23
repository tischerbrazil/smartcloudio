package org.geoazul.view.website;

import java.util.Objects;

public class Place {

    private String name;
    private String code;
    private String status;

    public Place(String name, String code, String status) {
        this.name = name;
        this.code = code;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(name, place.name) &&
                Objects.equals(code, place.code) &&
                Objects.equals(status, place.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code, status);
    }

    @Override
    public String toString() {
        return name;
    }
}