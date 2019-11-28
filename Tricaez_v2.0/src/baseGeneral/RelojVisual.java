package base;

/*
 * Javier Abellán, 10 de octubre de 2004
 * reloj.java
 */


import java.awt.Color;
import javax.swing.JLabel;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import java.text.SimpleDateFormat;

/**
 * Visual para mostrar el reloj.
 * Es un JLabel que recibe un Observable de cambio de fecha.
 */
public class RelojVisual extends javax.swing.JLabel
 {
     /**
      * Se pasa un observable de fecha/hora. El Observable debe pasar un
      * Date a esta visual para que la presente.
      */
     public RelojVisual(Observable modelo)
     {
         // La fecha/hora se pinta en el centro de este JLabel
         this.setHorizontalAlignment((SwingConstants.CENTER));
         
         // Suscripción al cambio de fecha/hora en el modelo recibido.
         modelo.addObserver (new Observer ()
         {
             // Método al que el Observable llamará cuando se cambie
             // la fecha/hora. El arg se espera que sea un Date.
             public void update(java.util.Observable o, Object arg) 
             {
                 final Object fecha = arg;
                 
                 // Se actualiza en pantalla la fecha/hora.
                 SwingUtilities.invokeLater (new Runnable()
                 {
                     public void run()
                     {
                         setText (format.format(fecha));
                     }
                 });
             }
         });
         
         // Se da una dimension al JLabel.
         this.setPreferredSize(new Dimension (500, 50));
         this.setForeground(new java.awt.Color(0,0,0));
         this.setFont(new Font("Tahoma", Font.BOLD, 24));
     }
     
     /**
      * Cambia el formato de presentacion de la fecha/hora en pantalla.
      */
     public void setFormat (SimpleDateFormat unFormato)
     {
         format = unFormato;
     }
     
     /**
      * Clase para mostrar una fecha/hora en formato texto.
      */
    SimpleDateFormat format = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss");
}