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
import javax.swing.JOptionPane;

/**
 *
 * @author camil
 */
public class Comprador extends Usuario{
    private ArrayList<Oferta> ofertas;

    public Comprador(int id, String nombres, String apellidos, String correo_elec, String organizacion, String clave) {
        super(id, nombres, apellidos, correo_elec, organizacion, clave);
        this.ofertas = new ArrayList<>();
    }

    public ArrayList<Oferta> getOfertas() {
        return ofertas;
    }

    public void setOfertas(ArrayList<Oferta> ofertas) {
        this.ofertas = ofertas;
    }

    @Override
    public String toString() {
        return "Comprador{" + "id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + ", correo_elec=" + correo_elec + ", organizacion=" + organizacion + ", clave=" + clave + '}';
    }
        
    
    public void comprar(Vehiculo v, String nomfile, double precio){ //ofertas.txt
        int id_oferta = Util.nextID(nomfile);
        Oferta new_oferta = new Oferta(id_oferta, this.id, v.getId(), precio);
        new_oferta.saveFile(nomfile);
    }
    
    public static ArrayList<Comprador> readFile(String nomfile){
        ArrayList<Comprador> usuarios = new ArrayList<>();
        try(Scanner sc = new Scanner(new File(nomfile))){
            while(sc.hasNextLine())
            {
                // linea = "1|20201010|eduardo|cruz"
                String linea = sc.nextLine();
                String[] tokens = linea.split("\\|");
                Comprador user = new Comprador(Integer.parseInt(tokens[0]), tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
                usuarios.add(user);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return usuarios;
        
    }
    
    
    public static Comprador searchByID(ArrayList<Comprador> usuarios, int id)
    {
        for(Comprador user : usuarios)
        {
            if(user.id == id)
                return user;
        }
        return null;
    }    
    
    public static Comprador searchByCorreoYClave(ArrayList<Comprador> usuarios, String correo,String clave)
    {
        for(Comprador user : usuarios)
        {
            if(user.correo_elec.equals(correo) && user.clave.equals(clave) )
                return user;
        }
        return null;
    }
}
