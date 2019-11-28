package interfaz.principal;

import javax.swing.UIManager;
import de.javasoft.plaf.synthetica.SyntheticaWhiteVisionLookAndFeel;
import javax.swing.JOptionPane;

/**
 *
 * @author mavg
 */

public class Main {

    
    protected static MDI mdi;
    protected static InicioSesion inicioSesion;
    
    public static void main(String[] args){
        try{
            UIManager.setLookAndFeel(new SyntheticaWhiteVisionLookAndFeel());

            inicioSesion = new InicioSesion();
            inicioSesion.getLayeredPane().remove(inicioSesion.getLayeredPane().getComponent(1)); //Oculta el titulo de la ventana y los botones min, max, cerrar
            inicioSesion.setLocationRelativeTo(null);
            inicioSesion.show();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage(), "Error en Look And Feel", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
}
