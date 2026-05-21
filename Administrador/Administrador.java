package Administrador;

import User.user;

public class Administrador extends user {

    public Administrador(String cedula, String nombre, String apellido,
                         String correo, String contrasena) {
        super(cedula, nombre, apellido, correo, contrasena);
    }

    @Override
    public String getRol() {
        return "Administrador";
    }

    @Override
    public String toString() {
        return "[ADMINISTRADOR] " + super.toString();
    }
}
