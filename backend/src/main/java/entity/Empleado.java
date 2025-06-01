package entity;

import lombok.Data;

@Data
public class Empleado {
    private Long idEmpleado;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private Rol rolEmpleado;
    private String mail;
    private String telefono;

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
