package com.scd.tema2.dto;

public class TemperatureRequest {
    private Long idOras;
    private Double valoare;

    public Long getIdOras() {
        return idOras;
    }

    public void setIdOras(Long idOras) {
        this.idOras = idOras;
    }

    public Double getValoare() {
        return valoare;
    }

    public void setValoare(Double valoare) {
        this.valoare = valoare;
    }
}
