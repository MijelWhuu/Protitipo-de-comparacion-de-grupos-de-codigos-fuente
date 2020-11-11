/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisis;

/**
 *
 * @author Miguel
 */
public class Palabra {
    public enum TIPO{NUMERO, PALABRA, FINAL, OPERACION, FIN, ERROR, PUNTO, COMENTARIO, TEXTO, PARENTESIS_I,PARENTESIS_D, DOS_P, ANOTACION, CORRCHETES}
    public enum reservadas{FOR,DO,DO_WHILE,PAQUETE,VARIABLE}
    private String contenido;
    private TIPO tipo;

    public Palabra(String contenido, TIPO tipo) {
        this.contenido = contenido;
        this.tipo = tipo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public TIPO getTipo() {
        return tipo;
    }

    public void setTipo(TIPO tipo) {
        this.tipo = tipo;
    }
    
}
