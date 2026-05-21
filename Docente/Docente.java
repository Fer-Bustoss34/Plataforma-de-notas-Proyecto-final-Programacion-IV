package Docente;

import java.util.ArrayList;

import Materia.materia;
import User.user;

public class Docente extends user {


    private String codigoDocente;
    private String especialidad;
    private ArrayList<materia> materias;


    public Docente(String cedula, String nombre, String apellido,
                   String correo, String contrasena,
                   String codigoDocente, String especialidad){

        super(cedula, nombre, apellido, correo, contrasena);
        this.codigoDocente = codigoDocente;
        this.especialidad = especialidad;
        this.materias = new ArrayList<>();
    }

    public Docente(String cedula, String nombre, String apellido,
                   String correo, String contrasena,
                   String codigoDocente) {
        this(cedula, nombre, apellido, correo, contrasena, codigoDocente, "");
    }

    // Getters
    public String getCodigoDocente(){return codigoDocente;}
    public String getEspecialidad(){return especialidad;}
    public ArrayList<materia> getMaterias() { return materias; }

    // Setters
    public void setCodigoDocente(String codigoDocente) { this.codigoDocente = codigoDocente; }
    public void setEspecialidad(String especialidad)   { this.especialidad  = especialidad; }

    @Override
    public String getRol() {
        return "Docente";
    }

    @Override
    public String toString() {
        return "[DOCENTE] " + super.toString() +
               " | Especialidad: " + especialidad;
    }
}
