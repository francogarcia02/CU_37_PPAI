package entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

//@Data Borrado
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "T_SISMOGRAFO")
public class Sismografo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sismografo")
    private Long idSismografo;

    @Column(name = "fecha_adquisicion")
    private String fechaAdquisicion;

    @Column(name = "numero_serie")
    private Long numeroDeSerie;

    @Column(name = "fabricante")
    private String fabricante;

    @Column(name = "modelo")
    private String modelo;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estadoActual;

    public Sismografo(Long idSismografo, String fechaAdq, Long nroDeSerie, String fabricante, String modelo, Estado estado) {
        this.idSismografo = idSismografo;
        this.fechaAdquisicion = fechaAdq;
        this.numeroDeSerie = nroDeSerie;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.estadoActual = estado;
    }

    // Modificacion del metodo toString() que agrega por defecto Lombok dentro del @Data
    @Override
    public String toString() {
        return "Sismografo[id=" + idSismografo + "]";
    }

    public void enviarSismografoAReparar(Estado estadoFS) {
        this.estadoActual = estadoFS;
    }
}
