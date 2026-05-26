package Materia;

import java.util.Scanner;
import java.util.ArrayList;
import Materia.actividad;
import Materia.calificacion; 
import Docente.Docente;
import Estudiante.Estudiante;

public class materia{
    private String codigo_materia;
    private int creditos;
    private String nombre;
    private Docente docente;

    private ArrayList<actividad> actividades;
    private ArrayList<calificacion> notas;
    private ArrayList<Estudiante> estudiantes;


    public materia(String codigo_materia, int creditos, String nombre, Docente docente) {
        this.codigo_materia  = codigo_materia;
        this.creditos = creditos;
        this.nombre = nombre;
        this.docente = docente;
        this.actividades = new ArrayList<>();
        this.notas = new ArrayList<>();
        this.estudiantes = new ArrayList<>();
    }

    // Getters
    public String getCodigoMateria(){return codigo_materia;}
    public int getCreditos(){return creditos;}
    public String getNombre(){return nombre;}
    public Docente getDocente(){return docente;}
    public ArrayList<actividad> getActividades(){return actividades;}
    public ArrayList<calificacion> getCalificaciones(){return notas;}
    public ArrayList<Estudiante> getEstudiantes(){ return estudiantes; }
 
    // Setters
    public void setCodigoMateria(String codigoMateria){this.codigo_materia = codigoMateria;}
    public void setCreditos(int creditos){this.creditos = creditos;}
    public void setNombre(String nombre){this.nombre = nombre;}
    public void setDocente(Docente docente){this.docente = docente;}
 
    @Override
    public String toString() {
        return "____________________________________________________"+
               "Código: " + codigo_materia + "\n" +
               " | Materia: " + nombre + "\n" +
               " | Creditos: " + creditos + "\n" +
               " | Docente: " + docente.getNombre() + " " + docente.getApellido() + "\n" +
               "____________________________________________________";
    }

    public void agregarEstudiante(Estudiante estudiante){
    estudiantes.add(estudiante);
    }

    public void crear_actividad(Scanner sc){
        System.out.println("Ingrese el titulo de la actividad: ");
        String titulo = sc.nextLine();
        System.out.println("Ingrese el porcentaje que va a tener en la materia (entre 0 y 100): ");
        double porciento = sc.nextDouble();

        if(porciento < 0 || porciento > 100){
            System.out.println("Porcentaje inválido");
            return;
        }

        double totalActual = 0;
        for(actividad a : actividades){
            totalActual += a.getPorcentaje();
        }

        if(totalActual + porciento > 100){
            System.out.println("No puedes agregar esta actividad, el total superaría el 100%");
            System.out.println("Porcentaje disponible: " + (100 - totalActual) + "%");
            return;
        }

        actividad nueva_actividad = new actividad(titulo, porciento);
        actividades.add(nueva_actividad);
        System.out.println("Actividad creada exitosamente");
    }

}
