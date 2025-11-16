package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_EMPLEADO")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Long idEmpleado;

    @Column(name = "nombre_empleado", nullable = false)
    private String nombreEmpleado;

    @Column(name = "apellido_empleado", nullable = false)
    private String apellidoEmpleado;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_empleado", nullable = false)
    private Rol rolEmpleado;

    @Column(name = "mail")
    private String mail;

    @Column(name = "telefono")
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

    public String obtenerMail() {
        return this.getMail();
    }
}
