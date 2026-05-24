package Materia;

public class actividad {

    private String nombre;
    private double porcentaje; 
 

    public actividad(String nombre, double porcentaje){
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }
 
    // Getters
    public String getNombre(){return nombre;}
    public double getPorcentaje(){return porcentaje;}
 
    // Setters
    public void setNombre(String nombre){this.nombre = nombre;}
    public void setPorcentaje(double porcentaje){this.porcentaje = porcentaje;}
 
    @Override
    public String toString() {
        return "Actividad: " + nombre + " | Porcentaje: " + porcentaje + "%";
    }
}
 

