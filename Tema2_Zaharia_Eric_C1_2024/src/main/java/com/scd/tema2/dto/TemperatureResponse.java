package com.scd.tema2.dto;

public class TemperatureResponse {

    public TemperatureResponse(String id, Double valoare, String timestamp) {
        this.id = id;
        this.valoare = valoare;
        this.timestamp = timestamp;
    }

    private String id;
    private Double valoare;
    private String timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValoare() {
        return valoare;
    }

    public void setValoare(Double valoare) {
        this.valoare = valoare;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
