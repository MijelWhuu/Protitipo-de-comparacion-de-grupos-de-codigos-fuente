/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisis;

import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public interface InterfaceComparaciones {
    public void clasificarCodigos(int nivel, ArrayList<Codigo> codigos);
    public void nivel0();
    public void nivel1();
    public void nivel2();
    public void nivel3();
    public ArrayList<Similares> getSimilares();
}
