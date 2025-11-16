package entity;

public enum Apreciacion {
    // Aquí puedes definir los valores fijos de apreciación.
    // Por ejemplo:
    BUENO("Bueno", "Verde"),
    REGULAR("Regular", "Amarillo"),
    MALO("Malo", "Rojo");

    private final String nombre;
    private final String color;

    Apreciacion(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }
}
