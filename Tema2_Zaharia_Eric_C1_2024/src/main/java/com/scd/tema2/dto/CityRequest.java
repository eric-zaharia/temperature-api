package com.scd.tema2.dto;

public class CityRequest {
    private Long id;
    private String nume;
    private Long idTara;
    private Double lat;
    private Double lon;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Long getIdTara() {
        return idTara;
    }

    public void setIdTara(Long idTara) {
        this.idTara = idTara;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
