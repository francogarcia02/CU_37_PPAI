package com.DSI.PPAI.Red.sismica.Entity;

import jakarta.persistence.*;

@Entity
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;
    private String email;

    @Enumerated(EnumType.STRING)
    private Roles rol; // RI, Reparador, Admin...

    @Column(unique = true)
    private String username;
}
