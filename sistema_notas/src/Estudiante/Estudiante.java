package Estudiante;

import java.util.Scanner;
import User.user;
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


    public void matricular(ArrayList<materia> materias_disponibles, Scanner sc){
        System.out.println("Materias disponibles" + "\n");

        int n = 0;

        for(materia m : materias_disponibles){
           System.out.println(n + ")");
           System.out.println(m.toString());
           System.out.println("\n\n");
           n++; 
        }

        System.out.println("Seleccione el numero de la materia que desea matricular");
        int opc = sc.nextInt();

        if(opc >= 0 && opc < materias_disponibles.size()){
        materia seleccionada = materias_disponibles.get(opc);
        materias.add(seleccionada);
        seleccionada.agregarEstudiante(this);
        } else {
        System.out.println("Opción inválida");
        } 
    }
}