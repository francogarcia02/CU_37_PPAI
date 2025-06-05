package entity;

import interfaces.EstadoInterface;
import lombok.Data;

@Data
public class Estado implements EstadoInterface {
    public String ambito;
    public String nombre;

    public Estado(String ambito, String nombre) {
        this.ambito = ambito;
        this.nombre = nombre;
    }

    @Override
    public Boolean esFinalizado() {
        if ( this.esAmbitoOrdendeInspeccion() && "finalizada".equals(this.getNombre()) ){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean esAmbitoSismografo() {
        if (  this.getAmbito().equals("SISMOGRAFO") ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean esFueraDeServicio() {
        if ( this.esAmbitoSismografo() && "fueraServicio".equals(this.getNombre()) ){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean esAmbitoOrdendeInspeccion() {
        if (  this.getAmbito().equals("ORDEN_INSPECCION") ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean esCerrada() {
        if ( this.esAmbitoOrdendeInspeccion() && "cierreDefinitivo".equals(this.getNombre()) ){
            return true;
        } else {
            return false;
        }
    }
}
