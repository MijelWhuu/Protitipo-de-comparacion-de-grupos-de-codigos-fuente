package baseGeneral;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.border.Border;

/**
 *
 * @author GLP, MAVG
 */

public class ImageDesktopPane implements Border{    
    
    private BufferedImage imagen;

    public ImageDesktopPane(BufferedImage imagen){
        this.imagen=imagen;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){
        int x1 = x + (width - imagen.getWidth()) / 2;
        int y1 = y + (height - imagen.getHeight()) / 2;
        g.drawImage(imagen,x1,y1,null);
    }

    public Insets getBorderInsets(Component c){
        return new Insets(0,0,0,0);
    }

    public boolean isBorderOpaque(){
        return true;
    }
}