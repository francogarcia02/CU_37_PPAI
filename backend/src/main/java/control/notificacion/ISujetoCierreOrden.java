package control.notificacion;

public interface ISujetoCierreOrden {
    void agregarObservador(IObservadorCierreOrden observador);
    void quitarObservador(IObservadorCierreOrden observador);
    void notificar(Object datos, String evento);
}
