package analisis;

import herramientas.ManejadorCodigoJava;

public class Codigo {
    private String nombre;
    private String original;
    private String nivel0;
    private String nivel1;
    private String nivel2;
    private String nivel3;
    private String nivel4;
    private boolean analizado;
    private ManejadorCodigoJava manager;
    public Codigo(String nombre, String texto) {
	this.nombre=nombre;
	this.original=texto;
        this.analizado=false;
        this.manager= new ManejadorCodigoJava(this.original);
    }
    public String getNombre() {
	return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getOriginal() {
	return original;
    }
    public void setOriginal(String nivel0) {
        this.original = nivel0;
    }
    
    public void change(){
        this.analizado=true;
    }

    /**
     * @return the nivel1
     */
    public String getNivel1() {
        return nivel1;
    }

    /**
     * @param nivel1 the nivel1 to set
     */
    public void setNivel1(String nivel1) {
        this.nivel1 = nivel1;
    }

    /**
     * @return the nivel2
     */
    public String getNivel2() {
        return nivel2;
    }

    /**
     * @param nivel2 the nivel2 to set
     */
    public void setNivel2(String nivel2) {
        this.nivel2 = nivel2;
    }

    /**
     * @return the nivel3
     */
    public String getNivel3() {
        return nivel3;
    }

    /**
     * @param nivel3 the nivel3 to set
     */
    public void setNivel3(String nivel3) {
        this.nivel3 = nivel3;
    }

    /**
     * @return the nivel4
     */
    public String getNivel4() {
        return nivel4;
    }

    /**
     * @param nivel4 the nivel4 to set
     */
    public void setNivel4(String nivel4) {
        this.nivel4 = nivel4;
    }

    public String getNivel0() {
        return nivel0;
    }

    public void setNivel0(String nivel0) {
        this.nivel0 = nivel0;
    }

    public boolean isAnalizado() {
        return analizado;
    }

    public void setAnalizado(boolean analizado) {
        this.analizado = analizado;
    }
    
    @Override
    public String toString(){
        return this.analizado? "    "+this.nombre.split("\\.")[0]:this.nombre.split("\\.")[0];
    }
}
