package entity;

import control.notificacion.DatosNotificacionCierre;
import interfaces.OrdenInspeccionInterface;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data // Borrado
@Getter // Agregado
@Setter // Agregado
@NoArgsConstructor
@Entity
@Table(name = "T_ORDEN_INSPECCION")
public class OrdenInspeccion implements OrdenInspeccionInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Long numeroOrden;

    @ManyToOne
    @JoinColumn(name = "id_estacion", nullable = false)
    private EstacionSismologica estacionSismologica;

    @ManyToOne
    @JoinColumn(name = "id_responsable", nullable = false)
    private Empleado responsableOrdenInspeccion;

    @OneToMany(mappedBy = "ordenInspeccion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<TareaTecnicaRevision> tareasTecnicasRevisiones = new ArrayList<>();

    @Column(name = "observaciones_cierre")
    private String observaciones;


    @OneToMany(mappedBy = "ordenInspeccion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CambioEstado> cambiosEstados = new ArrayList<>();


    public OrdenInspeccion(Long numeroOrden, EstacionSismologica estacionSismologica, List<TareaTecnicaRevision> tareasTecnicasRevisiones, Empleado responsableOrdenInspeccion, String observaciones, List<CambioEstado> cambiosEstados) {
        this.numeroOrden = numeroOrden;
        this.estacionSismologica = estacionSismologica;
        this.tareasTecnicasRevisiones = new ArrayList<>(tareasTecnicasRevisiones);
        this.responsableOrdenInspeccion = responsableOrdenInspeccion;
        this.observaciones = observaciones;
        this.cambiosEstados = new ArrayList<>(cambiosEstados);
    }

    // Getter para cambiosEstados
    public List<CambioEstado> getCambiosEstado() {
        return cambiosEstados;
    }

    // Getter para observaciones
    public String getObservacionesCierre() {
        return observaciones;
    }

    @Override
    public Boolean esTuRI(Empleado empleado) {
        if (empleado == null || this.responsableOrdenInspeccion == null) {
            return false;
        }
        return Objects.equals(this.responsableOrdenInspeccion.getIdEmpleado(), empleado.getIdEmpleado());
    }

    public Boolean estaFinalizada() {
        CambioEstado estadoActual = this.obtenerCambioEstadoActual();
        return estadoActual != null && "Finalizado".equalsIgnoreCase(estadoActual.getEstadoNuevo().getNombre());
    }

    @Override
    public Boolean cerrar(String observacion, List<MotivoFueraServicio> motivosNuevos, Estado estadoCerrada, Empleado responsableEjecucion) {
        CambioEstado cambioActual = this.obtenerCambioEstadoActual();
        if (cambioActual != null && "Finalizado".equalsIgnoreCase(cambioActual.getEstadoNuevo().getNombre())) {
            this.setObservaciones(observacion);
            cambioActual.setFechaHorafin(LocalDateTime.now());

            CambioEstado nuevoCambioCerrado = new CambioEstado();
            nuevoCambioCerrado.setEstadoAnterior(cambioActual.getEstadoNuevo());
            nuevoCambioCerrado.setEstadoNuevo(estadoCerrada);
            nuevoCambioCerrado.setFechaHorainicio(LocalDateTime.now());
            nuevoCambioCerrado.setResponsableCambioEstado(responsableEjecucion);
            nuevoCambioCerrado.setOrdenInspeccion(this);

            if (motivosNuevos != null && !motivosNuevos.isEmpty()) {
                nuevoCambioCerrado.setMotivosCambioEstados(motivosNuevos);
            }

            this.cambiosEstados.add(nuevoCambioCerrado);
            return true;
        }
        return false;
    }


    @Override
    public void realizar() {}

    @Override
    public void confirmarPte() {}

    @Override
    public void finalizar() {}

    public void enviarSismografoAReparar(Estado estadoFs) {
        EstacionSismologica estacionSelected = this.getEstacionSismologica();
        estacionSelected.enviarSismografoAReparar(estadoFs);
    }

    public LocalDateTime obtenerFechaFinalizacion() {
        return cambiosEstados.stream()
                .filter(CambioEstado::esFinalizado)
                .map(CambioEstado::getFechaHorafin)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Transient
    public CambioEstado obtenerCambioEstadoActual() {
        return cambiosEstados.stream()
            .filter(cambio -> cambio.getFechaHorafin() == null)
            .findFirst()
            .orElse(null);
    }

    public Boolean compareNroOrder(Long number){
        return this.getNumeroOrden().equals(number);
    }

    // Modificacion del metodo toString() que agrega por defecto Lombok dentro del @Data
    @Override
    public String toString() {
        return "OrdenInspeccion[id=" + numeroOrden + "]";
    }


    /**
     * @deprecated Este metodo viola el principio de Separación de Intereses (SoC) y SRP
     * La lógica de formateo de la Vista (códigos de color ANSI) no debe
     * estar en la clase Entidad.
     * La nueva Vista implementada (JavaFX) implementa su propia lógica de presentación.
     */
    @Deprecated
    public String toStringForPantalla() {
        CambioEstado estadoActual = obtenerCambioEstadoActual();
        String fechaFin = (estadoActual != null && estadoActual.getFechaHorainicio() != null)
                ? estadoActual.getFechaHorainicio().toString()
                : "No disponible";

        return String.format(
                "\033[95m____________________________________________________________________________%n\033[0m" +
                "\033[92mNúmero de Orden:\033[0m %d%n" +
                "\033[92mEstado:\033[0m %s%n" +
                "\033[92mFecha de Finalización:\033[0m %s%n" +
                "\033[92mEstación Sismológica:\033[0m %s%n" +
                "\033[92mSismógrafo:\033[0m %s%n" +
                "\033[95m____________________________________________________________________________%n\033[0m",
                numeroOrden,
                (estadoActual != null) ? estadoActual.getEstadoNuevo().getNombre() : "N/A",
                fechaFin,
                estacionSismologica.getNombreEstacion(),
                estacionSismologica.getSismografo().getIdSismografo()
        );
    }

    @Transient
    public DatosNotificacionCierre generarDatosNotificacion(String sismografoEstadoActual, List<MotivoFueraServicio> motivos, Empleado responsable) {
        String sismografoId = this.estacionSismologica.getSismografo().getIdSismografo().toString();
        LocalDateTime fechaHora = this.obtenerCambioEstadoActual().getFechaHorainicio();
        Long numeroOrden = this.getNumeroOrden();
        String nombreEstacion = this.estacionSismologica.getNombreEstacion();
        String nombreResponsable = responsable.getNombreEmpleado();

        return new DatosNotificacionCierre(
                sismografoId,
                sismografoEstadoActual,
                fechaHora,
                motivos,
                numeroOrden,
                nombreEstacion,
                nombreResponsable
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdenInspeccion that = (OrdenInspeccion) o;
        return Objects.equals(numeroOrden, that.numeroOrden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroOrden);
    }
}
