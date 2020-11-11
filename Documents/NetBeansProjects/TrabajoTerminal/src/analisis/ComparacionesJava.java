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
public class ComparacionesJava implements InterfaceComparaciones{
    private ArrayList<Codigo> codigos;
    private ArrayList<Similares> iguales;

    public ComparacionesJava() {
        this.iguales=new ArrayList<>();
    }
    
    @Override
    public void clasificarCodigos(int nivel, ArrayList<Codigo> codigos) {
        this.codigos=(ArrayList<Codigo>)codigos.clone();
        switch(nivel){
            case 0:
                nivel0();
                break;
            default:
                break;
        }
    }

    @Override
    public void nivel0() {
        for(int i=0;i<this.codigos.size()-1;i++){
            boolean bandera=false;
            for(Similares s: this.iguales){//Busca que no este en un grupo anteriormente hecho
                if(s.agreado(this.codigos.get(i))){//Si está agregado deja de buscar
                    bandera =true;
                    break;
                }
            }
            if(bandera)//Si está en un grupo anteriormente creado, salta las comparaciones
                continue;
            Similares grupo= new Similares(0, "Grupo "+this.iguales.size()+1);
            grupo.addElemento(this.codigos.get(i));//Agrega el codigo inicialmente
            for(int j=i+1;j<this.codigos.size();j++){//Si no existe en un grupo anterior, procederá a comparar
                if(this.codigos.get(i).getOriginal().equals(this.codigos.get(j).getOriginal())){//Compara el texto original
                    //Si son iguales, al grupo creado le agrega el codigo j (el anterior ya se agrego al intanciarse
                    grupo.addElemento(this.codigos.get(j)); 
                }
            }
            //Ya recorrio todos los codigos y agregó los iguales
            if(grupo.size()!=1)//Ve si el tamaño no es 1 (solo hay un codigo y por ello no hay similares)
                this.iguales.add(grupo);
        }
    }

    @Override
    public void nivel1() {
        
    }

    @Override
    public void nivel2() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void nivel3() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Similares> getSimilares() {
        return this.iguales;
    }
    
}
