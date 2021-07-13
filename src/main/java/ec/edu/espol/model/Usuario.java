/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espol.model;

import ec.edu.espol.util.Util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author camil
 */
public class Usuario {
    protected int id;
    protected String nombres, apellidos, correo_elec, organizacion, clave;


    public Usuario(int id, String nombres, String apellidos, String correo_elec, String organizacion, String clave) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo_elec = correo_elec;
        this.organizacion = organizacion;
        this.clave = clave;
    }
    
    //getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo_elec() {
        return correo_elec;
    }

    public void setCorreo_elec(String correo_elec) {
        this.correo_elec = correo_elec;
    }

    public String getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(String organizacion) {
        this.organizacion = organizacion;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    //equals
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
        final Usuario other = (Usuario) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.correo_elec, other.correo_elec)) {
            return false;
        }
        return true;
    }
    

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + ", correo_elec=" + correo_elec + ", organizacion=" + organizacion + ", clave=" + clave + '}';
    }
    
    public static void nextUsuario(String nomfile, String nomfile_hash)
    {
        String correo_elec;
        do{/**/
        correo_elec = JOptionPane.showInputDialog(null, "Por favor ingrese su correo electrónico: ", "CompraVende", JOptionPane.QUESTION_MESSAGE);        
         //Realizar la validación       
        } while(Util.correoInFile(correo_elec, nomfile) || !Util.validacionCorreo(correo_elec));// repetir mientras que el correo este en el archivo o el correo NO este bien escrito
        String clave = JOptionPane.showInputDialog(null, "Por favor ingrese su clave: ", "CompraVende", JOptionPane.QUESTION_MESSAGE); 
        String nombres = JOptionPane.showInputDialog(null, "Por favor ingrese sus nombres: ", "CompraVende", JOptionPane.QUESTION_MESSAGE);        
        String apellidos = JOptionPane.showInputDialog(null, "Por favor ingrese sus apellidos: ", "CompraVende", JOptionPane.QUESTION_MESSAGE);        
        String organizacion = JOptionPane.showInputDialog(null, "Por favor ingrese su organización: ", "CompraVende", JOptionPane.QUESTION_MESSAGE);        
               
       
        int id = Util.nextID(nomfile);
        Usuario user = new Usuario(id, nombres, apellidos, correo_elec, organizacion,clave);
        user.saveFile(nomfile);
        
        ArrayList<Usuario> usuarios = Usuario.readFile_usuario(nomfile);
        Util.crearArchivoHash(usuarios, nomfile_hash);
        
    }
    
    public void saveFile(String nomfile){
        try(PrintWriter pw = new PrintWriter(new FileOutputStream(new File(nomfile),true)))
        {
            pw.println(this.id+"|"+this.nombres+"|"+this.apellidos+"|"+this.correo_elec+"|"+this.organizacion+"|"+this.clave);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public static ArrayList<Usuario> readFile_usuario(String nomfile){
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try(Scanner sc = new Scanner(new File(nomfile))){
            while(sc.hasNextLine())
            {
                // linea = "1|20201010|eduardo|cruz"
                String linea = sc.nextLine();
                String[] tokens = linea.split("\\|");
                Usuario user = new Usuario(Integer.parseInt(tokens[0]), tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
                usuarios.add(user);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return usuarios;
        
    }
    
    
    public static Usuario searchByID_usuario(ArrayList<Usuario> usuarios, int id)
    {
        for(Usuario user : usuarios)
        {
            if(user.id == id)
                return user;
        }
        return null;
    }    
}
