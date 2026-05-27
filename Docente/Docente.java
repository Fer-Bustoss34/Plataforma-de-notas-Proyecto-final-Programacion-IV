package Docente;

import java.util.ArrayList;
import java.util.Scanner;

import Estudiante.Estudiante;
import Materia.actividad;
import Materia.calificacion;
import Materia.materia;
import User.user;

public class Docente extends user {


    private String especialidad;
    private ArrayList<materia> materias;


    public Docente(String cedula, String nombre, String apellido,
                   String correo, String contrasena, String especialidad){

        super(cedula, nombre, apellido, correo, contrasena);
        this.especialidad = especialidad;
        this.materias = new ArrayList<>();
    }

    public Docente(String cedula, String nombre, String apellido,
                   String correo, String contrasena) {
        this(cedula, nombre, apellido, correo, contrasena, "");
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

    public String calificar_estudiante(materia m, Estudiante e, actividad a, double valor){
    
        if(valor < 0.0 || valor > 5.0)
            return "Nota inválida, debe estar entre 0.0 y 5.0";

        calificacion nueva = new calificacion(e, a, valor);
        m.getCalificaciones().add(nueva);

        return "Calificación registrada exitosamente";
    }

    public String crear_actividad(String titulo, double porciento, materia m){

        if(!m.getDocente().getCedula().equals(getCedula())){
            return "No puedes modificar una materia que no es tuya";}

        if(porciento < 0 || porciento > 100)
            return "Porcentaje inválido";

        double totalActual = 0;
        for(actividad a : m.getActividades())
            totalActual += a.getPorcentaje();

        if(totalActual + porciento > 100)
            return "Porcentaje disponible: " + (100 - totalActual) + "%";

        m.getActividades().add(new actividad(titulo, porciento));
        return "Actividad creada exitosamente";
    }
}
