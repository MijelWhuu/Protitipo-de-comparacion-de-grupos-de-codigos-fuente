/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitys;

/**
 *
 * @author RAULDELHOYO
 */
public class BASE_Combos {
   

    private String id;
    private String nombre;
    private String clave;
    
    public BASE_Combos(){
        this.id = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public BASE_Combos(String id,String nombre,String clave){
        this.id=id;
        this.nombre=nombre;
        this.clave=clave;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String toString(){
        return this.nombre;
    }
    
    

}
