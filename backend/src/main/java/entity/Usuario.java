package entity;

import lombok.Data;

@Data
public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasenia;
    private String email;

    public Usuario(String nombreUsuario, String contrasenia, String mail) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.email = mail;

    }
}
