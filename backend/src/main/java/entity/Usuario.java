package entity;

import lombok.Data;

@Data
public class Usuario {
    public int idUsuario;
    public String nombreUsuario;
    public String contrasenia;
    public Empleado empleado;

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
