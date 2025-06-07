package interfaces;

import entity.*;

import java.time.LocalDateTime;
import java.util.List;

public interface GestorOrdenInterface {
    public void RecibirSelectedOption(String selectedOption);

    public Empleado buscarEmpleado();

    public List<OrdenInspeccion> buscarOrdenesInspeccion();

    public List<OrdenInspeccion> ordenarOI(List<OrdenInspeccion> ordenesInspeccionToOrder);

    public String stringificarOI(OrdenInspeccion ordenesInspeccionToStringify);

    public void pasarToPantallaOIs();

    public void tomarNumeroOI(Long selectedOrdenNumero);
    public void tomarDatosObservacion(String observacion);

    public void  tomarSeleccionDecicionSismografo(String selectedDecicionSismografo);
    public void manageSismografoFS();
    public List<String> stringificarMFS();
    public void tomarMFS();

    public void tomarComentario();

    public void buscarEstadosFS();

    public Boolean validarMotivo();

    // metodo cuestionable, la orden de inspección es quien debe responder su estado.
    // viola  el patrón experto
    public Estado buscarEstadoOrdenInspección(OrdenInspeccion ordenInspeccion);

    public LocalDateTime getFechaHoraActual();

    public List<String> obtenerMailsResponsablesReparacion();

    public void enviarNotificacionMail(String mensaje);

    public void publicarMonitores();


    public String confeccionarMensaje(OrdenInspeccion selectedOrden);

    public void FinCU();

    // nuevos metodos, cargarlos en el diagrama de clases

    public Usuario obtenerUsuarioLogueado();

    public void RecibirTipoMotivos(List<TipoMotivo> listaMotivos);
}
