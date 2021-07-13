/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espol.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author camil
 */
public class Oferta {
    private int id;
    private Comprador comprador;
    private int id_comprador;
    private Vehiculo vehiculo;
    private int id_vehiculo;
    private double precio;

    public Oferta(int id, int id_comprador, int id_vehiculo, double precio) {
        this.id = id;
        this.id_comprador = id_comprador;
        this.id_vehiculo = id_vehiculo;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    public int getId_comprador() {
        return id_comprador;
    }

    public void setId_comprador(int id_comprador) {
        this.id_comprador = id_comprador;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Oferta other = (Oferta) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.id_comprador != other.id_comprador) {
            return false;
        }
        if (this.id_vehiculo != other.id_vehiculo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Oferta{" + "id=" + id + ", id_comprador=" + id_comprador + ", id_vehiculo=" + id_vehiculo + ", precio=" + precio + '}';
    }
 
    
    public void saveFile(String nomfile){
        try(PrintWriter pw = new PrintWriter(new FileOutputStream(new File(nomfile),true)))
        {
            pw.println(this.id+"|"+this.id_comprador+"|"+this.id_vehiculo+"|"+this.precio);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static ArrayList<Oferta> readFile(String nomfile){
        ArrayList<Oferta> ofertas = new ArrayList<>();
        try(Scanner sc = new Scanner(new File(nomfile))){
            while(sc.hasNextLine())
            {
                String linea = sc.nextLine();
                String[] tokens = linea.split("\\|");
                Oferta r = new Oferta(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]),Double.parseDouble(tokens[3]));
                ofertas.add(r);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return ofertas;
    }
    
    public static void link(ArrayList<Comprador> compradores, ArrayList<Vehiculo> vehiculos, ArrayList<Oferta> ofertas){
        for(Oferta o: ofertas){
            Comprador comp = Comprador.searchByID(compradores, o.getId_comprador());
            Vehiculo veh = Vehiculo.searchByID(vehiculos, o.getId_vehiculo());
            comp.getOfertas().add(o);
            veh.getOfertas().add(o);
            o.setComprador(comp);
            o.setVehiculo(veh);
        }
    }
    
    public static Oferta searchByID(ArrayList<Oferta> ofertas, int id)
    {
        for(Oferta o : ofertas)
        {
            if(o.id == id)
                return o;
        }
        return null;
    }
    
    public static void saveFile(String nomfile,ArrayList<Oferta> ofertas){
        try(PrintWriter pw = new PrintWriter(new FileOutputStream(new File(nomfile))))
        {
            for (Oferta o: ofertas)
                pw.println(o.id+"|"+o.id_comprador+"|"+o.id_vehiculo+"|"+o.precio);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
