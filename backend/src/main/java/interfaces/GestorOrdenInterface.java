package interfaces;

import entity.*;

import java.time.LocalDateTime;
import java.util.List;

public interface GestorOrdenInterface {
    void RecibirSelectedOption(String selectedOption);
    Empleado buscarEmpleado();
    List<OrdenInspeccion> buscarOrdenesInspeccion();
    List<OrdenInspeccion> ordenarOI(List<OrdenInspeccion> ordenesInspeccionToOrder);
    String stringificarOI(OrdenInspeccion ordenesInspeccionToStringify);
    void pasarToPantallaOIs();
    void tomarNumeroOI(Long selectedOrdenNumero);
    void tomarDatosObservacion(String observacion);
    void  tomarSeleccionDecicionSismografo(String selectedDecicionSismografo);
    void manageSismografoFS();
    public List<String> stringificarMFS();
    void tomarMFSyComentario(TipoMotivo motivoSeleccionado, String comentario);
    void tomarConfirmacioncierreOI(Boolean input);
    void buscarEstadoFS();
    Boolean validarMotivo();
    void buscarEstadoCerradoOI();
    LocalDateTime getFechaHoraActual();
    List<String> obtenerMailsResponsablesReparacion();
    //ELIMINADOS POR APLICACION DE PATRON OBSERVER.
    // String confeccionarMensaje(OrdenInspeccion selectedOrden);
    // void enviarNotificacionMail(String mensaje);
    // void publicarMonitores();
    void FinCU();
    Usuario obtenerUsuarioLogueado();
    void RecibirTipoMotivos(List<TipoMotivo> listaMotivos);
}
