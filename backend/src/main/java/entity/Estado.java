package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_ESTADO")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private int idEstado;

    @Column(name = "ambito", nullable = false)
    private String ambito;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    public Estado(int idEstado, String ambito, String nombre) {
        this.idEstado = idEstado;
        this.ambito = ambito;
        this.nombre = nombre;
    }

    @Transient
    public Boolean esFinalizado() {
        return esAmbitoOrdendeInspeccion() && "Finalizado".equals(getNombre());
    }

    @Transient
    public Boolean esAmbitoSismografo() {
        return "SISMOGRAFO".equals(getAmbito());
    }

    @Transient
    public Boolean esFueraDeServicio() {
        return esAmbitoSismografo() && "Fuera de Servicio".equals(getNombre());
    }

    @Transient
    public Boolean esAmbitoOrdendeInspeccion() {
        return "ORDEN_INSPECCION".equals(getAmbito());
    }

    @Transient
    public Boolean esCerrada() {
        return esAmbitoOrdendeInspeccion() && "CierreDefinitivo".equals(getNombre());
    }
}
