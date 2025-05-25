package entity;

public enum Rol {
    CLIENTE,
    ADMIN,
    RESPONSABLE_INSPECCIONES,
    RESPONSABLE_REPARACIONES;

    public Boolean esRespReparacion() {
        return this.equals(RESPONSABLE_REPARACIONES);
    }

    public Boolean esRespInspeccion() {
        return this.equals(RESPONSABLE_INSPECCIONES);
    }
}
