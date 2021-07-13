/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espol.model;

import ec.edu.espol.util.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


/**
 *
 * @author camil
 */
public class Vendedor extends Usuario{
    private ArrayList<Vehiculo> vehiculos;

    public Vendedor(int id, String nombres, String apellidos, String correo_elec, String organizacion, String clave) {
        super(id, nombres, apellidos, correo_elec, organizacion, clave);
        this.vehiculos = new ArrayList<>();
    }

    public ArrayList<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(ArrayList<Vehiculo> vehiculos) {   
        this.vehiculos = vehiculos;
    }

    @Override
    public String toString() {
        return "Vendedor{" +"id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + ", correo_elec=" + correo_elec + ", organizacion=" + organizacion + ", clave=" + clave  + '}';
    }
    


    public static void vender(Vehiculo v,String nomfile_vehiculos, String nomfile_ofertas){       //lista de vehiculos del vendedor
        ArrayList<Vehiculo> vehiculos = Vehiculo.readFile(nomfile_vehiculos);
        vehiculos.remove(v);
        Vehiculo.saveFile(nomfile_vehiculos,vehiculos);
        ArrayList<Oferta> ofertas_v = v.getOfertas();
        ArrayList<Oferta> ofertas = Oferta.readFile(nomfile_ofertas);
        for(Oferta o : ofertas_v)
            ofertas.remove(o);
        Oferta.saveFile(nomfile_ofertas,ofertas);

        }

    public static ArrayList<Vendedor> readFile(String nomfile){
        ArrayList<Vendedor> usuarios = new ArrayList<>();
        try(Scanner sc = new Scanner(new File(nomfile))){
            while(sc.hasNextLine())
            {
                // linea = "1|20201010|eduardo|cruz"
                String linea = sc.nextLine();
                String[] tokens = linea.split("\\|");
                Vendedor user = new Vendedor(Integer.parseInt(tokens[0]), tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
                usuarios.add(user);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return usuarios;
        
    }
    
    
    public static Vendedor searchByID(ArrayList<Vendedor> usuarios, int id)
    {
        for(Vendedor user : usuarios)
        {
            if(user.id == id)
                return user;
        }
        return null;
    }    
    
    public static Vendedor searchByCorreoYClave(ArrayList<Vendedor> usuarios, String correo,String clave)
    {
        for(Vendedor user : usuarios)
        {
            if(user.correo_elec.equals(correo) && user.clave.equals(clave) )
                return user;
        }
        return null;
    }
}
