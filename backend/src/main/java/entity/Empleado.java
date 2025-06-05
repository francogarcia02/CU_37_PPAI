package entity;

import lombok.Data;

@Data
public class Empleado {
    public Long idEmpleado;
    public String nombreEmpleado;
    public String apellidoEmpleado;
    public Rol rolEmpleado;
    public String mail;
    public String telefono;

    public Empleado(
            Long idEmpleado, 
            String nombreEmpleado, 
            String apellidoEmpleado, 
            Rol rolEmpleado, 
            String mail,
            String telefono
        ) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
        this.rolEmpleado = rolEmpleado;
        this.mail = mail;
        this.telefono = telefono;
    }

    public boolean esResponsableReparaciones() {
        return this.rolEmpleado.equals(Rol.RESPONSABLE_REPARACIONES);
    }

    public String  obtenerMail() {
        return this.getMail();
    };
}
