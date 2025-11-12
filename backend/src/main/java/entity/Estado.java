package entity;

import lombok.Data;

@Data
public class Estado {
    public int idEstado;
    public String ambito;
    public String nombre;

    public Estado(int idEstado, String ambito, String nombre) {
        this.idEstado = idEstado;
        this.ambito = ambito;
        this.nombre = nombre;
    }


    public Boolean esFinalizado() {
        if ( this.esAmbitoOrdendeInspeccion() && "Finalizado".equals(this.getNombre()) ){
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
        if ( this.esAmbitoSismografo() && "Fuera de Servicio".equals(this.getNombre()) ){
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
        if ( this.esAmbitoOrdendeInspeccion() && "CierreDefinitivo".equals(this.getNombre()) ){
            return true;
        } else {
            return false;
        }
    }
}
