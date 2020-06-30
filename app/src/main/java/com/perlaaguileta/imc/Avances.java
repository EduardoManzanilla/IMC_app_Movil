package com.perlaaguileta.imc;

public class Avances{
    //variable de los datos a registrar

    private String imcAvance ;
    private String clasificacionAvance;
    private String recomendacionAvance;
    private String imagen;

    public Avances() {
    }

    public Avances(String imcAvance, String clasificacionAvance, String recomendacionAvance, String imagen) {
        this.imcAvance = imcAvance;
        this.clasificacionAvance = clasificacionAvance;
        this.recomendacionAvance = recomendacionAvance;
        this.imagen = imagen;
    }

    public String getImcAvance() {
        return imcAvance;
    }

    public void setImcAvance(String imcAvance) {
        this.imcAvance = imcAvance;
    }

    public String getClasificacionAvance() {
        return clasificacionAvance;
    }

    public void setClasificacionAvance(String clasificacionAvance) {
        this.clasificacionAvance = clasificacionAvance;
    }

    public String getRecomendacionAvance() {
        return recomendacionAvance;
    }

    public void setRecomendacionAvance(String recomendacionAvance) {
        this.recomendacionAvance = recomendacionAvance;
    }

    @Override
    public String toString() {
        return "Avances{" +
                "imcAvance='" + imcAvance + '\'' +
                ", clasificacionAvance='" + clasificacionAvance + '\'' +
                ", recomendacionAvance='" + recomendacionAvance + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
