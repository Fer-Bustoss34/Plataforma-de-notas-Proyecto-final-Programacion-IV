package Materia;

import Estudiante.Estudiante;

public class calificacion {

    private Estudiante estudiante; 
    private actividad actividad;  
    private double valor;     
 
    public calificacion(Estudiante estudiante, actividad actividad, double valor) {
        this.estudiante = estudiante;
        this.actividad = actividad;
        this.valor = valor;
    }
 
    // Getters
    public Estudiante getEstudiante(){return estudiante;}
    public actividad getActividad(){return actividad;}
    public double getValor(){return valor;}
 
    // Setters
    public void setEstudiante(Estudiante estudiante){ this.estudiante = estudiante;}
    public void setActividad(actividad actividad){ this.actividad = actividad;}
    public void setValor(double valor){ this.valor = valor;}
 
    @Override
    public String toString() {
        return "Estudiante: " + estudiante.getNombre() + " " + estudiante.getApellido() +
               " | Actividad: "  + actividad.getNombre() +
               " | Porcentaje: " + actividad.getPorcentaje() + "%" +
               " | Nota: "       + valor;
    }
}
