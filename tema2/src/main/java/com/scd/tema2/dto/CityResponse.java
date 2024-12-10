package com.scd.tema2.dto;

public class CityResponse {
    private String id;
    private String idTara;
    private String nume;
    private double lat;
    private double lon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTara() {
        return idTara;
    }

    public void setIdTara(String idTara) {
        this.idTara = idTara;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
