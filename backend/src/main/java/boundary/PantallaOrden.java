package boundary;
import control.GestorOrden;
import entity.OrdenInspeccion;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class PantallaOrden {
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

    private final GestorOrden gestorOrden;

    public PantallaOrden(GestorOrden gestorOrden) {
        this.gestorOrden = gestorOrden;
    }

    //metodo generico para escanear la entrada del usuario
    public String leerEntradaUsuario() {
        Scanner scanner = new Scanner(System.in);
        String entrada = scanner.nextLine();
        return entrada;

    }
    private static void imprimirOndasSismicas() {
        // Simula ondas sísmicas con caracteres
        String[] ondas = {"~", "~", "^^", "~~~", "^^^^", "~~~~~", "^^^", "~~", "~"};
        System.out.print(GREEN);

        for (String onda : ondas) {
            System.out.print(onda);
            esperarAnimado(100);
        }

        System.out.print(" UTN ");

        for (int i = ondas.length - 1; i >= 0; i--) {
            System.out.print(ondas[i]);
            esperarAnimado(100);
        }

        System.out.println(RESET);
    }

    private static void esperarAnimado(int milisegundos) {
        try {
            TimeUnit.MILLISECONDS.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //metodo para mostrar las opciones
    public void mostrarOpciones() {
        // Título de opciones
        System.out.println(YELLOW + BOLD + "⚡ OPCIONES DISPONIBLES:" + RESET);
        System.out.println();

        // Opciones del menú con estilo moderno
        System.out.println("   " + GREEN + BOLD + "┌──────────────────────────────────────────────────┐" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "                                                  " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "  " + WHITE + BOLD + "[1]" + RESET + " 📋 Cerrar orden de Inspección            " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "      " + DIM + "Finalizar proceso de inspección actual" + RESET + "    " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "                                                  " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "├──────────────────────────────────────────────────┤" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "                                                  " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "  " + WHITE + BOLD + "[2]" + RESET + " 🚪 Salir del Sistema                     " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "      " + DIM + "Cerrar aplicación de manera segura" + RESET + "      " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "│" + RESET + "                                                  " + GREEN + BOLD + "│" + RESET);
        System.out.println("   " + GREEN + BOLD + "└──────────────────────────────────────────────────┘" + RESET);

        System.out.println();

        // Prompt de entrada moderno
        System.out.print(BLUE + BOLD + "┌─ " + RESET + YELLOW + "Seleccione una opción" + RESET + " " + BLUE + BOLD + "──────────────────────────────────────" + RESET);
        System.out.println();
        System.out.print(BLUE + BOLD + "└─❯ " + RESET + WHITE + BOLD);

        String selectedOption = leerEntradaUsuario();

        if (selectedOption.equals("1") || selectedOption.equals("2")) {
            informarGestorSelectedOption(selectedOption);
        } else {
            System.out.println("Opcion no valida, intente de nuevo.");
            mostrarOpciones();
        }
    }

    //metodo para informar al gestor la opcion seleccionada
    public void informarGestorSelectedOption(String selectedOption) {
        gestorOrden.RecibirselectedOption(selectedOption);
    }

    public String solicitarObservaciones() {
        // Prompt de entrada moderno
        System.out.print(BLUE + BOLD + "┌─ " + RESET + YELLOW + "Ingrese las observaciones" + RESET + " " + BLUE + BOLD + "──────────────────────────────────────" + RESET);
        System.out.println();
        System.out.print(BLUE + BOLD + "└─❯ " + RESET + WHITE + BOLD);

        String observaciones = leerEntradaUsuario();
        return observaciones;
    }


    public void mainProcess() {
        System.out.println();
        System.out.println(BLUE + BOLD + "╔════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BLUE + BOLD + "║" + RESET + "                                                                    " + BLUE + BOLD + "║" + RESET);
        System.out.println(BLUE + BOLD + "║" + RESET + "     " + RED + BOLD + "🌍 SISTEMA DE RED SÍSMICA - CCRS 🌍" + RESET + "                     " + BLUE + BOLD + "║" + RESET);
        System.out.println(BLUE + BOLD + "║" + RESET + "                                                                    " + BLUE + BOLD + "║" + RESET);
        System.out.println(BLUE + BOLD + "║" + RESET + "          " + CYAN + BOLD + "Universidad Tecnológica Nacional" + RESET + "                 " + BLUE + BOLD + "║" + RESET);
        System.out.println(BLUE + BOLD + "║" + RESET + "              " + CYAN + "Facultad Regional Córdoba" + RESET + "                      " + BLUE + BOLD + "║" + RESET);
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
        mostrarOpciones();

    }

    public void mostrarOrdenInspeccion(String ordenInspeccionString) {

        System.out.println(ordenInspeccionString);
        System.out.println();
    }
}
