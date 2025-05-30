package entity;

import lombok.Data;

@Data
public class Empleado {
    private Long idEmpleado;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private Rol rolEmpleado;
    private Usuario usuario;

    public Empleado(Long idEmpleado, String nombreEmpleado, String apellidoEmpleado, Rol rolEmpleado, Usuario usuario) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
        this.rolEmpleado = rolEmpleado;
        this.usuario = usuario;
    }

    public Boolean compareEmployee(Usuario user){
        if(this.getUsuario().equals(user)){
            return true;        
        } else {
            return false;
        }

    }

    public boolean esResponsableReparaciones() {
        return this.rolEmpleado.equals(Rol.RESPONSABLE_REPARACIONES);
    }

    public String  obtenerMail() {
        return this.getUsuario().getEmail();
    };
}
