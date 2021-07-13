/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espol.util;
import ec.edu.espol.model.Comprador;
import ec.edu.espol.model.Oferta;
import javax.swing.JOptionPane;
import ec.edu.espol.model.Usuario;
import ec.edu.espol.model.Vehiculo;
import ec.edu.espol.model.Vendedor;
import java.util.ArrayList;

/**
 *
 * @author Daniel Bejarano
 */
public class Menu {
    private Menu(){}

    public static void SubMenuVendedor() {
        int opcionSubmenu;
        do{
            
            opcionSubmenu = Integer.parseInt(JOptionPane.showInputDialog(null, "Vendedor"
                    + "\n1. Registrar un nuevo vendedor."
                    + "\n2. Ingresar un nuevo vehículo."
                    + "\n3. Aceptar oferta."
                    + "\n4. Regresar."
                    + "\nUna vez escrita la opción, pulse la tecla Enter", "CompraVende", JOptionPane.QUESTION_MESSAGE));
            switch (opcionSubmenu){
                case 1:
                    Menu.registrarVendedor();
                    break;
                case 2:
                    Menu.ingresarNuevoVehiculo();
                    break;
                case 3:
                    Menu.aceptarOferta();
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null, "Regresando . . .","CompraVende", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "No se ha elegido una opción válida","CompraVende", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }while(opcionSubmenu != 4);        
    }
    
    public static void SubMenuComprador(){
        int opcionSubmenu;
        do{
           opcionSubmenu = Integer.parseInt(JOptionPane.showInputDialog(null, "Comprador"
                            + "\n1. Registrar un nuevo comprador."
                            + "\n2. Ofertar por un vehículo."
                            + "\n3. Regresar ."
                            + "\nUna vez escrita la opción, pulse la tecla Enter", "CompraVende", JOptionPane.QUESTION_MESSAGE));                    
                    switch (opcionSubmenu){
                        case 1:
                            Menu.registrarNuevoComprador();
                            break;
                        case 2:
                            Menu.ofertarPorUnVehiculo();
                            break;

                        case 3:
                            JOptionPane.showMessageDialog(null, "Regresando....","CompraVende", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "No se ha elegido una opción válida","CompraVende", JOptionPane.INFORMATION_MESSAGE);
                            break;
                    }
                }while(opcionSubmenu != 3);        
    }    

public static void registrarVendedor(){
    JOptionPane.showMessageDialog(null, "Aquí se registra un nuevo vendedor.","CompraVende", JOptionPane.INFORMATION_MESSAGE);
    Usuario.nextUsuario("vendedores.txt","claveHashVendedores.txt");
}

public static void ingresarNuevoVehiculo(){
    JOptionPane.showMessageDialog(null, "Aquí se ingresa un nuevo vehículo.","CompraVende", JOptionPane.INFORMATION_MESSAGE);
    String clave, correo_elec;
    ArrayList<Vendedor> vendedores = Vendedor.readFile("vendedores.txt");
    do{/**/
    correo_elec = JOptionPane.showInputDialog(null, "Por favor ingrese su correo electrónico: ", "CompraVende", JOptionPane.QUESTION_MESSAGE);                            
    clave = JOptionPane.showInputDialog(null, "Por favor ingrese su clave: ", "CompraVende", JOptionPane.QUESTION_MESSAGE);
    }while(!Util.validacionClaveCorreo(correo_elec, clave,"claveHashVendedores.txt","vendedores.txt")); // repetir mientras que  NO el correo y la clave existan en el archivo
    Vendedor vendedor = Vendedor.searchByCorreoYClave(vendedores, correo_elec, clave);
    Vehiculo.nextVehiculo("vehiculos.txt", vendedor.getId());
    Vehiculo.link(vendedores, Vehiculo.readFile("vehiculos.txt"));
}

public static void aceptarOferta(){
    JOptionPane.showMessageDialog(null, "Aquí se acepta la oferta.","CompraVende", JOptionPane.INFORMATION_MESSAGE);
    ArrayList<Vendedor> vendedores = Vendedor.readFile("vendedores.txt");
    String correo_elec,clave;
    do{/**/
        correo_elec = JOptionPane.showInputDialog(null, "Por favor ingrese su correo electrónico: ", "CompraVende", JOptionPane.QUESTION_MESSAGE);                            
        clave = JOptionPane.showInputDialog(null, "Por favor ingrese su clave: ", "CompraVende", JOptionPane.QUESTION_MESSAGE);
    }while(!Util.validacionClaveCorreo(correo_elec, clave,"claveHashVendedores.txt","vendedores.txt"));// repetir mientras que  NO el correo y la clave existan en el archivo
    Vendedor vendedor = Vendedor.searchByCorreoYClave(vendedores, correo_elec, clave);
    
    /*Validar placa*/
    String placa;
    ArrayList<Vehiculo> vehiculos = Vehiculo.readFile("vehiculos.txt");        
    do{       
           placa= JOptionPane.showInputDialog(null, "Por favor ingrese el numero de placa: ", "CompraVende", JOptionPane.QUESTION_MESSAGE);
    }while(!Util.validacionPlaca(placa, vehiculos)); //repetir mientras que la placa no este en el archivo
    
    Vehiculo vehiculo =  Vehiculo.searchByPlaca(vehiculos, placa);
    
                            
    for (Oferta offer: vehiculo.getOfertas()){                            
        int resp=JOptionPane.showConfirmDialog(null, offer);
                            
    if (JOptionPane.OK_OPTION == resp){
        Vendedor.vender(vehiculo, "vehiculos.txt", "ofertas.txt");
        /*Funcion enviarCorreo*/
        Util.enviarConGMail(offer.getComprador().getCorreo_elec(),vendedor.getCorreo_elec(), "Aceptacion Oferta de vehículo", "Buenas " + offer.getComprador().getNombres()+ " " + offer.getComprador().getApellidos() + " mediante la presente le informamos que su oferta por el vehículo con placa: " + offer.getVehiculo().getPlaca() + " ha sido aceptada. A continuación se presenta la oferta aceptada: \n" + offer,vendedor.getClave() );
        
    }
    else{
                                            
    }                            
     }
}

public static void registrarNuevoComprador(){
    JOptionPane.showMessageDialog(null, "Aquí se registra un nuevo compador.","CompraVende", JOptionPane.INFORMATION_MESSAGE);
    Usuario.nextUsuario("compradores.txt","claveHashCompradores.txt");  
}

public static void ofertarPorUnVehiculo(){
    JOptionPane.showMessageDialog(null, "Aquí se oferta un nuevo vehículo.","CompraVende", JOptionPane.INFORMATION_MESSAGE);
    String clave, correo_elec;
    do{/**/
    correo_elec = JOptionPane.showInputDialog(null, "Por favor ingrese su correo electrónico: ", "CompraVende", JOptionPane.QUESTION_MESSAGE);                            
    clave = JOptionPane.showInputDialog(null, "Por favor ingrese su clave: ", "CompraVende", JOptionPane.QUESTION_MESSAGE);
    }while(!Util.validacionClaveCorreo(correo_elec, clave,"claveHashCompradores.txt","compradores.txt"));/// repetir mientras que  NO el correo y la clave existan en el archivo
    
    Comprador compradorOferta = Comprador.searchByCorreoYClave(Comprador.readFile("compradores.txt"), correo_elec, clave);    
    ArrayList<Vehiculo> vehiculo = Vehiculo.readFile("vehiculos.txt");
        int opcion = 0;
    String tipo = null;
    String[] botones = {"Carro", "  Camioneta", "Moto", "Nada"};
       opcion = JOptionPane.showOptionDialog(null,
                            "Seleccione una opcion:\n",
                            "CompraVenta",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null,
                            botones, botones[0]);
                
           switch(opcion){
               case 0:
                       tipo = "carro";                   
                   break;
               case 1:
                       tipo = "camioneta";
                   break;
               case 2:
                       tipo = "moto";
                   break;
               case 3:
                       tipo = "nada";
                       break;
                }    
    
    ArrayList<Vehiculo> vehiculoFiltradoTipo = Vehiculo.searchByTipo(vehiculo, tipo);       
           
    String recorridoMin;
        do{
            recorridoMin =  JOptionPane.showInputDialog(null,"Por favor ingrese el precio mínimo: ", "CompraVende", JOptionPane.INFORMATION_MESSAGE);
        }while(!Util.isNumeric(recorridoMin));
        double recorridoMinD = Double.parseDouble(recorridoMin);    
        
    String recorridoMax;
        do{
            recorridoMax =  JOptionPane.showInputDialog(null,"Por favor ingrese el precio máximo: ", "CompraVende", JOptionPane.INFORMATION_MESSAGE);
        }while(!Util.isNumeric(recorridoMax));
        double recorridoMaxD = Double.parseDouble(recorridoMax);
        
    ArrayList<Vehiculo> vehiculoFiltradoRecorrido = Vehiculo.searchByRecorrido(vehiculo, recorridoMaxD, recorridoMinD);
           
    String yearMin;
        do{
            yearMin =  JOptionPane.showInputDialog(null,"Por favor ingrese el año mínimo: ", "CompraVende", JOptionPane.INFORMATION_MESSAGE);
        }while(!Util.isInt(yearMin));
        int yearMinD = Integer.parseInt(yearMin);    
        
    String yearMax;
        do{
            yearMax =  JOptionPane.showInputDialog(null,"Por favor ingrese el precio máximo: ", "CompraVende", JOptionPane.INFORMATION_MESSAGE);
        }while(!Util.isNumeric(yearMax));
        int yearMaxD = Integer.parseInt(yearMax);    
        
    ArrayList<Vehiculo> vehiculoFiltradoYear = Vehiculo.searchByPrecio(vehiculo, yearMaxD, yearMinD);           
    
    
    String precioMin;
        do{
            precioMin =  JOptionPane.showInputDialog(null,"Por favor ingrese el precio mínimo: ", "CompraVende", JOptionPane.INFORMATION_MESSAGE);
        }while(!Util.isNumeric(precioMin));
        double precioMinD = Double.parseDouble(precioMin);    
        
    String precioMax;
        do{
            precioMax =  JOptionPane.showInputDialog(null,"Por favor ingrese el precio máximo: ", "CompraVende", JOptionPane.INFORMATION_MESSAGE);
        }while(!Util.isNumeric(precioMax));
        double precioMaxD = Double.parseDouble(precioMax);
    
    ArrayList<Vehiculo> vehiculoFiltradoPrecio = Vehiculo.searchByPrecio(vehiculo, precioMaxD, precioMinD);
    
    
    ArrayList<Vehiculo> vehiculosInterseccion1 = Vehiculo.interseccionList(vehiculoFiltradoRecorrido, vehiculoFiltradoYear);
    ArrayList<Vehiculo> vehiculosInterseccion2 = Vehiculo.interseccionList(vehiculoFiltradoTipo, vehiculoFiltradoPrecio);
    ArrayList<Vehiculo> vehiculosFiltradosTotal = Vehiculo.interseccionList(vehiculosInterseccion1, vehiculosInterseccion2);
    

    
    

    int i = 0;
    int ventana = 0, ventana2 = 0, ventana3 = 0;
    String precio_s = null;
    do{
            
       if ( i==0 ){
           String[] botonesInicio = {"Siguiente", "Comprar", "Volver al Menú"};
           ventana = JOptionPane.showOptionDialog(null,
                          "Seleccione una opcion:\n"
                           + vehiculosFiltradosTotal.get(i),
                           "CompraVenta",
                           JOptionPane.DEFAULT_OPTION,
                           JOptionPane.QUESTION_MESSAGE, null,
                           botonesInicio, botonesInicio[0]);
                
           switch(ventana){
               case 0:
                  i+=1;
                  break;
               case 1:
                  JOptionPane.showMessageDialog(null, "Usted ha adquirido la oferta, felicidades.","CompraVende", JOptionPane.INFORMATION_MESSAGE);
                  
                  do{
                  precio_s =  JOptionPane.showInputDialog(null,"Por favor ingrese el precio de su oferta usando el '.' como separador decimal: ", "CompraVende", JOptionPane.INFORMATION_MESSAGE);
                  }while(!Util.isNumeric(precio_s));
                  double precioOferta = Double.parseDouble(precio_s);
                  compradorOferta.comprar(vehiculosFiltradosTotal.get(i), "ofertas.txt",precioOferta);
                  Oferta oferta = new Oferta(Util.nextID("ofertas.txt"),compradorOferta.getId(),vehiculosFiltradosTotal.get(i).getId(),precioOferta);
                  break;
               case 2:
                  JOptionPane.showMessageDialog(null, "Regresando. . .","CompraVende", JOptionPane.INFORMATION_MESSAGE);                        
           }
                
       }else if(i==(vehiculosFiltradosTotal.size()-1)){   
            String[] botonesInicio1 = {"Anterior", "Comprar", "Volver al Menú"};
            ventana2 = JOptionPane.showOptionDialog(null,
                            "Seleccione una opcion:\n"
                            + vehiculosFiltradosTotal.get(i),
                            "CompraVenta",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null,
                            botonesInicio1, botonesInicio1[0]);
                
           switch(ventana2){
               case 0:
                  i-=1;
                  break;
               case 1:
                  JOptionPane.showMessageDialog(null, "Usted ha adquirido la oferta, felicidades.","CompraVende", JOptionPane.INFORMATION_MESSAGE);
                  do{
                  precio_s =  JOptionPane.showInputDialog(null,"Por favor ingrese el precio de su oferta usando el '.' como separador decimal: ", "CompraVende", JOptionPane.INFORMATION_MESSAGE);
                  }while(!Util.isNumeric(precio_s));
                  double precioOferta = Double.parseDouble(precio_s);
                  Oferta oferta = new Oferta(Util.nextID("ofertas.txt"),compradorOferta.getId(),vehiculosFiltradosTotal.get(i).getId(),precioOferta);
                  break;
               case 2:
                  JOptionPane.showMessageDialog(null, "Regresando. . .","CompraVende", JOptionPane.INFORMATION_MESSAGE);
                  break;
                }                
                
       }else{
           String[] botonesInicio2 = {"Anterior", "Siguiente", "Comprar", "Volver al Menú"};
           ventana3 = JOptionPane.showOptionDialog(null,
                           "Seleccione una opcion: \n"
                           + vehiculosFiltradosTotal.get(i),
                           "CompraVenta",
                           JOptionPane.DEFAULT_OPTION,
                           JOptionPane.QUESTION_MESSAGE, null,
                           botonesInicio2, botonesInicio2[0]);
           switch(ventana3){
               case 0:
                  i -= 1;
                  break;
               case 1:
                  i += 1;
                  break;
               case 2:
                  JOptionPane.showMessageDialog(null, "Usted ha adquirido la oferta, felicidades.", "CompraVende", JOptionPane.INFORMATION_MESSAGE);
                  do{
                  precio_s =  JOptionPane.showInputDialog(null,"Por favor ingrese el precio de su oferta usando el '.' como separador decimal: ", "CompraVende", JOptionPane.INFORMATION_MESSAGE);
                  }while(!Util.isNumeric(precio_s));
                  double precioOferta = Double.parseDouble(precio_s);
                  Oferta oferta = new Oferta(Util.nextID("ofertas.txt"),compradorOferta.getId(),vehiculosFiltradosTotal.get(i).getId(),precioOferta);
                  break;
               case 3:
                  JOptionPane.showMessageDialog(null, "Regresando. . .", "CompraVende", JOptionPane.INFORMATION_MESSAGE);                        
                }
        }
        }while((ventana != 1) && (ventana != 2) && (ventana2 != 1) && (ventana2 != 2) && (ventana3 !=2) && (ventana3 != 3));
                   
    Oferta.link(Comprador.readFile("compradores.txt"),Vehiculo.readFile("vehiculos.txt"), Oferta.readFile("ofertas.txt"));
    
    
}
}
