package interfaces;

public interface PantallaOrdenInterface {
    public void OpCerrarOrden();
    public void habilitarVentanaCierre();
    public void mostrarOpciones();

    public String leerEntradaUsuario();
    public int numericInput(String mensaje, String mensajeError);
    public Long numericInputLong(String mensaje, String mensajeError);
    public void imprimirOndasSismicas(String mensaje);
    public void esperarAnimado(int milisegundos);

    public void mostrarOI(String ordenInspeccionString);
    public Long tomarNumeroOI();
    public String solicitarObservacion();
    public String tomarObservación();
    public String confirmarActualizacionSituacionSismografo();
    public String tomarConfirmaciónActuSitSismog();
    public int SolicitarMFS();
    public void tomarMFS();
    public void solicitarComentario();
    public void pedirComentario();
    public Boolean solicitarConfirmacionCierreOI();
    public void tomarConfirmación();


}
