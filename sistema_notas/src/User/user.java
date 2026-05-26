package User;

public abstract class user {

    private String cedula;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;

    public user(String cedula, String nombre, String apellido, String correo, String contrasena) {
        this.cedula    = cedula;
        this.nombre    = nombre;
        this.apellido  = apellido;
        this.correo    = correo;
        this.contrasena = contrasena;
    }

    // Getters
    public String getCedula(){return cedula;}
    public String getNombre(){return nombre;}
    public String getApellido(){return apellido;}
    public String getCorreo(){return correo;}
    public String getContrasena(){return contrasena;}

    // Setters
    public void setCedula(String cedula){this.cedula = cedula;}
    public void setNombre(String nombre){ this.nombre = nombre;}
    public void setApellido(String apellido){this.apellido = apellido; }
    public void setCorreo(String correo){this.correo = correo;}
    public void setContrasena(String contrasena){ this.contrasena = contrasena;}
    public abstract String getRol();

    @Override
    public String toString() {
        return "Cedula: " + cedula +
               " | Nombre: " + nombre + " " + apellido +
               " | Correo: " + correo +
               " | Rol: " + getRol();
    }
}
