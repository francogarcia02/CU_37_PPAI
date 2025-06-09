package boundary;
import control.GestorOrden;
import interfaces.PantallaOrdenInterface;
import lombok.Data;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Data
public class PantallaOrden implements PantallaOrdenInterface {
    // Códigos ANSI para colores y efectos
    private static final String RESET = "\033[0m";
    private static final String BOLD = "\033[1m";
    private static final String DIM = "\033[2m";
    private static final String CYAN = "\033[96m";
    private static final String PURPLE = "\033[95m";
    private static final String GREEN = "\033[92m";
    private static final String BLUE = "\033[94m";
    private static final String YELLOW = "\033[93m";
    private static final String RED = "\033[91m";
    private static final String WHITE = "\033[97m";

    public String selectedOption;
    public String selectedMFS = "";
    public String inputComentrarioFS;
    private GestorOrden gestorOrden;

    public PantallaOrden(GestorOrden gestorOrden) {
        this.gestorOrden = gestorOrden;
    }

    //metodo generico para escanear la entrada del usuario
    private final Scanner scanner = new Scanner(System.in);
    
    public String leerEntradaUsuario() {
        return scanner.nextLine();
    }
    
    //Solicita al usuario que ingrese un valor numérico entero (int primitivo)
    public int numericInput(String mensaje, String mensajeError) {
        while (true) {
            try {
                // Calcular el ancho restante para la línea
                int anchoRestante = 70 - mensaje.length();
                String linea = BLUE + BOLD + "┌─ " + RESET + YELLOW + mensaje + RESET + " " + 
                             BLUE + BOLD + "─".repeat(Math.max(0, anchoRestante)) + RESET;
                System.out.println(linea);
                System.out.print(BLUE + BOLD + "└─> " + RESET + WHITE + BOLD);
                
                String entrada = scanner.nextLine().trim();
                return Integer.parseInt(entrada);
                
            } catch (NumberFormatException e) {
                System.out.println(RED + "✖ " + mensajeError + RESET);
                System.out.println();
            }
        }
    }
    
    //Solicita al usuario que ingrese un valor numérico largo (Long wrapper)
    public Long numericInputLong(String mensaje, String mensajeError) {
        while (true) {
            try {
                // Calcular el ancho restante para la línea
                int anchoRestante = 70 - mensaje.length();
                String linea = BLUE + BOLD + "┌─ " + RESET + YELLOW + mensaje + RESET + " " + 
                             BLUE + BOLD + "─".repeat(Math.max(0, anchoRestante)) + RESET;
                System.out.println(linea);
                System.out.print(BLUE + BOLD + "└─> " + RESET + WHITE + BOLD);
                
                String entrada = scanner.nextLine().trim();
                if (entrada.isEmpty()) {
                    return null; // Opcional: devolver null si se ingresa una cadena vacía
                }
                return Long.valueOf(entrada);
                
            } catch (NumberFormatException e) {
                System.out.println(RED + "✖ " + mensajeError + RESET);
                System.out.println();
            }
        }
    }

    public void imprimirOndasSismicas(String mensaje) {
        // Simula ondas sísmicas con caracteres
        String[] ondas = {"~", "~", "^^", "~~~", "^^^^", "~~~~~", "^^^", "~~", "~"};
        System.out.print(GREEN);

        for (String onda : ondas) {
            System.out.print(onda);
            esperarAnimado(100);
        }

        System.out.print(mensaje);

        for (int i = ondas.length - 1; i >= 0; i--) {
            System.out.print(ondas[i]);
            esperarAnimado(100);
        }

        System.out.println(RESET);
    }

    public void esperarAnimado(int milisegundos) {
        try {
            TimeUnit.MILLISECONDS.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //metodo para mostrar las opciones
    public void mostrarOpciones() {
        System.out.println();
        System.out.println(BLUE + BOLD + "╔════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BLUE + BOLD + "║" + RESET + "                                                                    " + BLUE + BOLD + "║" + RESET);
        System.out.println(BLUE + BOLD + "║" + RESET + "     " + RED + BOLD + "   🌍 SISTEMA DE RED SÍSMICA - CCRS 🌍" + RESET + "                     " + BLUE + BOLD + "    ║" + RESET);
        System.out.println(BLUE + BOLD + "║" + RESET + "                                                                    " + BLUE + BOLD + "║" + RESET);
        System.out.println(BLUE + BOLD + "║" + RESET + "          " + CYAN + BOLD + "Universidad Tecnológica Nacional" + RESET + "                 " + BLUE + BOLD + "         ║" + RESET);
        System.out.println(BLUE + BOLD + "║" + RESET + "              " + CYAN + "Facultad Regional Córdoba" + RESET + "                      " + BLUE + BOLD + "       ║" + RESET);
        System.out.println(BLUE + BOLD + "║" + RESET + "                                                                    " + BLUE + BOLD + "║" + RESET);
        System.out.println(BLUE + BOLD + "╚════════════════════════════════════════════════════════════════════╝" + RESET);

        // Banner principal de bienvenida
        System.out.println();
        System.out.println(RED + BOLD + "═════════════════════════════════════════════════════════════════════" + RESET);
        System.out.println();
        System.out.println(WHITE + BOLD + "    🚨 BIENVENIDO AL SISTEMA DE RED SÍSMICA DE LA CCRS" + RESET);
        System.out.println();
        System.out.println(CYAN + "       Software desarrollado en la UTN Facultad Regional Córdoba" + RESET);
        System.out.println(DIM + "         Sistema de monitoreo sísmico en tiempo real" + RESET);
        System.out.println(DIM + "           Detección y análisis de actividad sísmica" + RESET);
        System.out.println();
        System.out.println(RED + BOLD + "═════════════════════════════════════════════════════════════════════" + RESET);

        // Título de opciones
        System.out.println(YELLOW + BOLD + "⚡ OPCIONES DISPONIBLES:" + RESET);
        System.out.println();

        // Opciones del menú con estilo moderno
        System.out.println("   " + GREEN + BOLD + "┌──────────────────────────────────────────────────┐" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "                                                  " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "  " + WHITE + BOLD + "[1]" + RESET + " 📋 Cerrar orden de Inspección            " + GREEN + BOLD + "   │" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "      " + DIM + "Finalizar proceso de inspección actual" + RESET + "    " + GREEN + BOLD + "  │" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "                                                  " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "├──────────────────────────────────────────────────┤" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "                                                  " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "  " + WHITE + BOLD + "[2]" + RESET + " 🚪 Salir del Sistema                     " + GREEN + BOLD + "   │" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "      " + DIM + "Cerrar aplicación de manera segura" + RESET + "      " + GREEN + BOLD + "    │" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "                                                  " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "└──────────────────────────────────────────────────┘" + RESET);

        System.out.println();

        // Prompt de entrada moderno
        System.out.print(BLUE + BOLD + "┌─ " + RESET + YELLOW + "Seleccione una opción" + RESET + " " + BLUE + BOLD + "──────────────────────────────────────" + RESET);
        System.out.println();
        System.out.print(BLUE + BOLD + "└─> " + RESET + WHITE + BOLD);

        String tempSelectedOption = leerEntradaUsuario();

        if (tempSelectedOption.equals("1") || tempSelectedOption.equals("2")) {
            setSelectedOption(tempSelectedOption);
        } else {
            System.out.println("Opcion no valida, intente de nuevo.");
            while (!tempSelectedOption.equals("1") && !tempSelectedOption.equals("2")) {
                tempSelectedOption = leerEntradaUsuario();
            }
            setSelectedOption(tempSelectedOption);
        }

        this.informarGestorSelectedOption();
    }

    //metodo para informar al gestor la opcion seleccionada
    public void informarGestorSelectedOption() {
        gestorOrden.RecibirSelectedOption(selectedOption);
    }

    public String solicitarObservaciones() {
        // Prompt de entrada moderno
        System.out.print(BLUE + BOLD + "┌─ " + RESET + YELLOW + "Ingrese las observaciones" + RESET + " " + BLUE + BOLD + "──────────────────────────────────────" + RESET);
        System.out.println();
        System.out.print(BLUE + BOLD + "└─> " + RESET + WHITE + BOLD);

        String observaciones = leerEntradaUsuario();
        return observaciones;
    }

    public String solicitarMotivoComentario(){
        String comentario = "";
        while(comentario.equals("")){
            System.out.print(BLUE + BOLD + "┌─ " + RESET + YELLOW + "Ingrese un comentario sobre el motivo" + RESET + " " + BLUE + BOLD + "──────────────────────────────────────" + RESET);
            System.out.println();
            System.out.print(BLUE + BOLD + "└─> " + RESET + WHITE + BOLD);
            comentario = leerEntradaUsuario();
        }
        return comentario;
    }

    public void comunicarFeedbackGestor (String mensaje) {
        System.out.println();
        System.out.println(YELLOW + BOLD + "~GestorOrdenes~ " + RESET + BLUE + mensaje + RESET);
        System.out.println();
    }

    public void comunicarFeedbackGestorLeve (String mensaje) {
        System.out.println();
        System.out.println( RESET + BLUE + mensaje + RESET);
        System.out.println();
    }


    public void mostrarOI(String ordenInspeccionString) {

        System.out.println(ordenInspeccionString);
        System.out.println();
    }

    @Override
    public Long tomarNumeroOI() {
        Long selectedOrdenNumero = this.numericInputLong("ingrese el numero de la Orden de Inspeccion a cerrar ", "Solo puede ingresar números");

        gestorOrden.tomarNumeroOI(selectedOrdenNumero);

        return selectedOrdenNumero;
    }

    @Override
    public String solicitarObservacion() {
        this.comunicarFeedbackGestorLeve("Por favor ingrese las observaciones de la orden de inspeccion");
        String observaciones = null;

        observaciones = this.tomarObservación();
        while (observaciones == "") {
            this.comunicarFeedbackGestor("Es obligatorio ingresar observaciones para cerrar la orden de inspeccion");
            observaciones = this.tomarObservación();
        }
        this.comunicarFeedbackGestor("He recibido las observaciones: " + observaciones);

        return observaciones;
    }

    @Override
    public String tomarObservación() {
        return this.leerEntradaUsuario();
    }

    public void mostrarTipoMotivosFueraServicio(String motivoTipoFueraServicio) {
        System.out.println(motivoTipoFueraServicio);
        System.out.println();
    }


    public String SolicitarMFS() {
        return String.valueOf(numericInput("Seleccione un motivo (0 para salir):",
                "Por favor ingrese un número válido o 0 para salir"));
    }

    @Override
    public void tomarMFS(String SelectedMFS) {
       setSelectedMFS(SelectedMFS);
    }


    @Override
    public void tomarComentario(String inputComentrarioFS) {
        setInputComentrarioFS(inputComentrarioFS);
    }

    @Override
    public Boolean solicitarConfirmacionCierreOI(){
        int respuesta = tomarConfirmaciónCierreOI();
        return respuesta == 1;
    }

    @Override
    public int tomarConfirmaciónCierreOI() {
        return numericInput("¿Desea confirmar el cierre de la OI? (1:Si, 0:No)",
                "Por favor ingrese 1 para Sí o 0 para No");
    }

    @Override
    public String tomarConfirmaciónActuSitSismog() {
        return String.valueOf(numericInput("¿Desea cambiar la situación del sismógrafo de la estación? (1: Si, 0: No)",
                "Por favor ingrese 1 para Sí o 0 para No"));
    }

    @Override
    public void mostrarMFS(List<String> tipoMotivos) {
        tipoMotivos.stream().forEach(System.out::println);
    }

    public String confirmarActualizacionSituacionSismografo(){
        String input = tomarConfirmaciónActuSitSismog();

        while (!input.equals("1") && !input.equals("0")) {
            input = tomarConfirmaciónActuSitSismog();
        }

        return input;
    }

    public void mostrarResultadoCierre(Boolean result){
        if(result){
            System.out.println("Orden de inspeccion cerrada con exito!");
        } else {
            System.out.println("Orden de inspeccion no se a cerrado correctamente");
        }
    }




}
