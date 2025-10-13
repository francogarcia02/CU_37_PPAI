package entity;

import lombok.Data;

@Data
public class Estado {
    public String ambito;
    public String nombre;

    public Estado(String ambito, String nombre) {
        this.ambito = ambito;
        this.nombre = nombre;
    }


    public Boolean esFinalizado() {
        if ( this.esAmbitoOrdendeInspeccion() && "finalizada".equals(this.getNombre()) ){
            return true;
        } else {
            return false;
        }
    }


    public Boolean esAmbitoSismografo() {
        if (  this.getAmbito().equals("SISMOGRAFO") ) {
            return true;
        } else {
            return false;
        }
    }


    public Boolean esFueraDeServicio() {
        if ( this.esAmbitoSismografo() && "fueraServicio".equals(this.getNombre()) ){
            return true;
        } else {
            return false;
        }
    }


    public Boolean esAmbitoOrdendeInspeccion() {
        if (  this.getAmbito().equals("ORDEN_INSPECCION") ) {
            return true;
        } else {
            return false;
        }
    }


    public Boolean esCerrada() {
        if ( this.esAmbitoOrdendeInspeccion() && "cierreDefinitivo".equals(this.getNombre()) ){
            return true;
        } else {
            return false;
        }
    }
}
