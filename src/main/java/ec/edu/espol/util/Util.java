/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espol.util;

import ec.edu.espol.model.Usuario;
import ec.edu.espol.model.Vehiculo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author camil
 */
public class Util {
    private Util(){}
    
    public static int nextID(String nomfile)
    {
        int id = 0;
        try(Scanner sc = new Scanner(new File(nomfile)))
        {
           while(sc.hasNextLine())
           {
               String linea = sc.nextLine();
               String[] tokens = linea.split("\\|");
               id = Integer.parseInt(tokens[0]);
           }
        }
        catch(Exception e)
        {
        }
        return id+1;
    }
   
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    { 
        // Static getInstance method is called with hashing SHA 
        MessageDigest md = MessageDigest.getInstance("SHA-256"); 
  
        // digest() method called 
        // to calculate message digest of an input 
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8)); 
    }
    
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation 
        BigInteger number = new BigInteger(1, hash); 
  
        // Convert message digest into hex value 
        StringBuilder hexString = new StringBuilder(number.toString(16)); 
  
        // Pad with leading zeros
        while (hexString.length() < 32) 
        { 
            hexString.insert(0, '0'); 
        } 
  
        return hexString.toString(); 
    }
  
    public static void enviarConGMail(String destinatario,String remitente, String asunto, String cuerpo, String clave) {
    // Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el remitente también.
    
    Properties props = System.getProperties();
    props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
    props.put("mail.smtp.user", remitente); // Direccion del Remitente del mensaje
    props.put("mail.smtp.clave", clave);    //La clave de la cuenta
    props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
    props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
    props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

    Session session = Session.getDefaultInstance(props);
    MimeMessage message = new MimeMessage(session);

    try {
        message.setFrom(new InternetAddress(remitente));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));   //Se obtiene del address del destinatario y se podrían añadir varios de la misma manera
        message.setSubject(asunto);
        message.setText(cuerpo);
        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", remitente, clave);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
    catch (MessagingException me) {
        me.printStackTrace();   //Si se produce un error
    }
}
  
    public static void crearArchivoHash(ArrayList<Usuario> usuarios, String nomfile){
        
        try(PrintWriter pw = new PrintWriter(new FileOutputStream(new File(nomfile))))
        {
            for (Usuario usu: usuarios){
                pw.println(toHexString(getSHA(usu.getClave())));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        } 
        
    }
    
    
    public static Boolean validacionCorreo(String correo){
        
        if ((correo.contains("@") && correo.contains("."))){
            String[] correoArroba= correo.split("\\@");
            if(correoArroba.length == 2){
                return true;
            }
        }
        return false;
    } //true si el correo esta bien escrito
    
    public static Boolean correoInFile(String correo, String archivo){
        try(Scanner sc = new Scanner(new File(archivo)))
        {
           while(sc.hasNextLine()){
               String linea = sc.nextLine();
               String[] tokens = linea.split("\\|");
               if(tokens[3].equals(correo))
                   return true;
              /* if (claveHash == linea){
               return true;
           }*/
           }   
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    } // true si el correo esta en el archivo
   
    public static Boolean validacionClave(String clave, String archivoHash){
        try{
        String claveHash = toHexString(getSHA(clave));
        try(Scanner sc = new Scanner(new File(archivoHash)))
        {
           while(sc.hasNextLine()){
               String linea = sc.nextLine();
               if(linea.equals(claveHash))
                   return true;
              /* if (claveHash == linea){
               return true;
           }*/
           }   
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
        catch (NoSuchAlgorithmException e) { 
            System.out.println("Exception thrown for incorrect algorithm: " + e); 
        } 
        return false;
    } //true si la clave esta en el archivoHash
    
    //true si la clave existe en el archivo hash y el correo existe en el archivo
    
    public static Boolean validacionClaveCorreo(String correo, String clave, String archivoHash, String archivo){//pedir lista usuarios
        ArrayList<Usuario> usuarios = Usuario.readFile_usuario(archivo);
        int ID = searchBycorreoID(correo,usuarios);
        if (Usuario.searchByID_usuario(usuarios, ID)==null){
            return false;
        }
        String claveReal = Usuario.searchByID_usuario(usuarios, ID).getClave();
        return claveReal.equals(clave) && validacionClave(clave,archivoHash) && correoInFile(correo, archivo);//camibar validacion por correo por correoInFile
        
    }
    
    public static Boolean validacionPlaca(String placa, ArrayList<Vehiculo> vehiculos){
        
        for(Vehiculo vehiculo: vehiculos){
            if(vehiculo.getPlaca().equals(placa))
                return true;
        }
        return false;
    } // true si la placa esta en la lista de vehiculos
    
    public static int searchBycorreoID(String correo,ArrayList<Usuario> usuarios){
        for(Usuario user : usuarios)
        {
            if(user.getCorreo_elec().equals(correo))
                return user.getId();
        }
        return -1;
    }
    
    public static boolean isNumeric(String cadena){
	try {
		double d = Double.parseDouble(cadena);
		if (d>= 0)
                    return true;
                else
                    return false;
	} catch (NumberFormatException nfe){
		return false;
	}
}

    public static boolean isInt(String cadena){
	try {
		int i = Integer.parseInt(cadena);
                if (i>= 0)
                    return true;
                else
                    return false;
	} catch (NumberFormatException nfe){
		return false;
	}
}
}

