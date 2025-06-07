package interfaces;

import control.GestorOrden;
import entity.TipoMotivo;

import java.util.List;

public interface PantallaOrdenInterface {
    public void mostrarOpciones();
    public void informarGestorSelectedOption();
    public String leerEntradaUsuario();
    public int numericInput(String mensaje, String mensajeError);
    public Long numericInputLong(String mensaje, String mensajeError);


    public void mostrarOI(String ordenInspeccionString);
    public Long tomarNumeroOI();
    public String solicitarObservacion();
    public String tomarObservación();
    public String confirmarActualizacionSituacionSismografo();
    public String tomarConfirmaciónActuSitSismog();
    public void mostrarMFS(List<String> tipoMotivos);
    public String  SolicitarMFS();
    public void tomarMFS();
    public void solicitarComentario();
    public void tomarComentario();
    public Boolean solicitarConfirmacionCierreOI();
    public void tomarConfirmación();

    public void imprimirOndasSismicas(String mensaje);
    public void esperarAnimado(int milisegundos);
}
