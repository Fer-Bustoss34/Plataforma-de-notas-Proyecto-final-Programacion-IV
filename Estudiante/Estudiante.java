package Estudiante;

import java.util.Scanner;
import User.user;
import Materia.calificacion;
import Materia.materia;

import java.util.ArrayList;
 
public class Estudiante extends user {
 
    private int semestre;
    private ArrayList<materia> materias;
 

    public Estudiante(String cedula, String nombre, String apellido,
                      String correo, String contrasena, int semestre){

        super(cedula, nombre, apellido, correo, contrasena);
        this.semestre = semestre;
        this.materias = new ArrayList<>();
    }

    public Estudiante(String cedula, String nombre, String apellido,
                      String correo, String contrasena,
                      String codigoEstudiante) {
        this(cedula, nombre, apellido, correo, contrasena, 1);
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


    public String matricular(materia seleccionada){

        if(materias.contains(seleccionada))
            return "Ya estás matriculado en esta materia";

        materias.add(seleccionada);
        seleccionada.agregarEstudiante(this);
        return "Matrícula exitosa en " + seleccionada.getNombre();
    }


    public double promedio_materia(materia m){
        ArrayList<calificacion> notas_materia = new ArrayList<>();

        for(calificacion n : m.getCalificaciones()){
            if(n.getEstudiante().getCedula().equals(getCedula())){
                notas_materia.add(n);
            }
        } 

        if(notas_materia.size() == 0)
        return 0;

        double promedio = 0;
        
        for(calificacion n : notas_materia){
            promedio += n.getValor();
        }

        return promedio / notas_materia.size();
    }
}