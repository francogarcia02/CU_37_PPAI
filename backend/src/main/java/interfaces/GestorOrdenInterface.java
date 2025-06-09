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
    public void tomarMFSyComentario(TipoMotivo motivoSeleccionado, String comentario);
    public void tomarConfirmacioncierreOI(Boolean input);
    public void buscarEstadoFS();
    public Boolean validarMotivo();
    public void buscarEstadoCerradoOI();
    public LocalDateTime getFechaHoraActual();
    public List<String> obtenerMailsResponsablesReparacion();
    public String confeccionarMensaje(OrdenInspeccion selectedOrden);
    public void enviarNotificacionMail(String mensaje);
    public void publicarMonitores();
    public void FinCU();
    public Usuario obtenerUsuarioLogueado();
    public void RecibirTipoMotivos(List<TipoMotivo> listaMotivos);
}
