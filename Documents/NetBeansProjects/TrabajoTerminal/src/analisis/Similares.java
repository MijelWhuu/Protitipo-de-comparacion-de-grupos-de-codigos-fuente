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
public class Similares {
    private int nivel;
    private String nombre;
    private ArrayList<Codigo> codigos;

    public Similares(int nivel, String nombre, ArrayList<Codigo> codigos) {
        this.nivel = nivel;
        this.nombre = nombre;
        this.codigos = codigos;
    }

    public Similares(int nivel, String nombre) {
        this.nivel = nivel;
        this.nombre = nombre;
        this.codigos = new ArrayList<>();
    }
    
    public int getNivel() {
        return nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Codigo> getCodigos() {
        return codigos;
    }
    
    public void addElemento(Codigo codigo){
        this.codigos.add(codigo);
    }
    
    public boolean agreado(Codigo codigo){
        for(Codigo c : this.codigos){
            if(c.getNombre().equals(codigo.getNombre()))
                return true;
        }
        return false;//No está
    }
    
    public int size(){
        return this.codigos.size();
    }
    
    public static ArrayList<Similares> getNivel(int nivel, ArrayList<Similares> similares){
        ArrayList<Similares> nivelEspecificado = new ArrayList<>();
        for(Similares s: similares){
            if(nivel==s.nivel)
                nivelEspecificado.add(s);
        }
        return nivelEspecificado;
    }
    
    @Override
    public String toString(){
        return this.nombre;
    }
    
}
