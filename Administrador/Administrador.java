package Administrador;
import java.util.ArrayList;
import java.util.Scanner;
import Docente.Docente;
import Estudiante.Estudiante;
import Materia.materia;
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


    public String asignar_docente(int opc, ArrayList<Docente> docentes, materia m){
        if(opc < 0 || opc >= docentes.size())
            return "Opción inválida";

        m.setDocente(docentes.get(opc));
        return "Docente asignado exitosamente";
    }


    public Docente crear_docente(String cedula, String nombre, String apellido, 
                                String correo, String contrasena, String codigo, String especialidad){

        Docente nuevo_docente = new Docente(cedula, nombre, apellido, correo, contrasena, codigo, especialidad);

        return nuevo_docente;
    }

    public Estudiante crear_estudiante(String cedula, String nombre, String apellido,
                                    String correo, String contrasena, String codigo, int semestre){

        Estudiante nuevo_estudiante = new Estudiante(cedula, nombre, apellido, correo, contrasena, codigo, semestre);

        return nuevo_estudiante;
    }


    public materia crear_materia(String codigo, int creditos, String nombre, Docente docente){
        materia nueva_materia = new materia(codigo, creditos, nombre, docente);

        return nueva_materia;
    }
}
