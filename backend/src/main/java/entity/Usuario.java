package entity;

import lombok.Data;

@Data
public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasenia;
    private Empleado empleado;

    public Usuario(String nombreUsuario, String contrasenia, Empleado empleado) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.empleado = empleado;
    }

    public Boolean compareEmployee(Empleado empleado){
        if(this.getEmpleado().equals(empleado)){
            return true;        
        } else {
            return false;
        }

    }
}
