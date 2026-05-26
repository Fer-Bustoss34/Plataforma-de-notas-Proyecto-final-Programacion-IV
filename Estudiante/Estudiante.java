package Estudiante;

import java.util.Scanner;
import User.user;
import Materia.materia;

import java.util.ArrayList;
 
public class Estudiante extends user {
 
    private int semestre;
    private ArrayList<materia> materias;
 

    public Estudiante(String cedula, String nombre, String apellido,
                      String correo, String contrasena, String codigo, int semestre){

        super(cedula, nombre, apellido, correo, contrasena);
        this.semestre = semestre;
        this.materias = new ArrayList<>();
    }

    public Estudiante(String cedula, String nombre, String apellido,
                      String correo, String contrasena,
                      String codigoEstudiante) {
        this(cedula, nombre, apellido, correo, contrasena, codigoEstudiante, 1);
    }
 
    // Getters
    public int getSemestre(){return semestre;}
    public ArrayList<materia> getMaterias(){return materias;}
 
    // Setters
    public void setSemestre(int semestre) {
        if (semestre >= 1) {
            this.semestre = semestre;
        }
    }
 

    @Override
    public String getRol() {
        return "Estudiante";
    }
 
    @Override
    public String toString() {
        return "[ESTUDIANTE] " + super.toString() +
               " | Semestre: " + semestre;
    }


    public String matricular(int opc, ArrayList<materia> materias_disponibles){
        if(opc < 0 || opc >= materias_disponibles.size())
            return "Opción inválida";

        materia seleccionada = materias_disponibles.get(opc);

        if(materias.contains(seleccionada))
            return "Ya estás matriculado en esta materia";

        materias.add(seleccionada);
        seleccionada.agregarEstudiante(this);
        return "Matrícula exitosa en " + seleccionada.getNombre();
    }
}