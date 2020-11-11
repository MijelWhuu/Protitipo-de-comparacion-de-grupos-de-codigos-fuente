/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herramientas;
import analisis.Palabra;
import analisis.Palabra.TIPO;

/**
 *
 * @author Miguel
 */
public class ManejadorCodigoJava {
    private String codigo;
    private int contador;

    public ManejadorCodigoJava(String codigo) {
        this.codigo = codigo;
        this.contador=0;
    }
    
    public char caracter(){
        if(this.contador<this.codigo.length())
            return codigo.charAt(this.contador++);
        else{
            return 3;//Cuando se llegue a 3 es necesario reiniciar
        }
    }
    
    public Palabra siguiente(){
        char c= caracter();
        while(c=='\n' || c==' ' || c==9)
            c=caracter();
        if ((c > 64 && c < 91) || (c > 96 && c < 123) || c==95) //El caracter es una letra, variables pueden empezar con _
            return formarCadena(c);
        else if ((c > 47 && c < 58) || c==46) //El caracter es un numero
            return  formarNumero(c);
//                Son los demás simbolos que considera como validos dentro del lenguaje y que pueden formar una instrucción
//                admiracion ! 33
//                comillas "" 34---
//                numeral # 35
//                modulo % 37 operacion
//                amperson && 38 --
//                coma simple ' 39 --
//                parentesis ( 40 
//                parentesis )41 Parentesis izq
//                asterisco * 42 
//                mas + 43--
//                coma , 44
//                menos - 45--
//                punto . 46
//                diagonal / 47--
//                ----------48 a 57 numeros-------------
//                dos puntos 58
//                punto y coma 59
//                menor que < 60--
//                igual = 61--
//                mayor que > 62--
//                interrogacion ? 63
//                arroba @ 64
//                corrchete izq [ 91
//                corrchete der ] 93
//                abre llaves { 123
//                barra | 124--
//                cierra llaves } 125
//        else if ( c==35 )//Paquete #
//            return new Palabra("#", TIPO.PAQUETE); esto es para c
        else if (c== 37 || c== 42 || c==44 ||  c== 63)//Modulo, multiplicacion, coma, punto, ?
            return new Palabra(""+c, TIPO.OPERACION);
        else if(c== 40)
            return new Palabra("(", TIPO.PARENTESIS_I);
        else if(c== 41 )
            return new Palabra(")", TIPO.PARENTESIS_D);
        else if (c== 46)//punto
            return new Palabra(".", TIPO.PUNTO);
        else if( c== 58) // : dos puntos
            return new Palabra(":", TIPO.DOS_P);
        else if(c==59 )//punto y coma
            return new Palabra(";", TIPO.FIN);
        else if (c==64)//Anotacion
            return new Palabra(""+c, TIPO.ANOTACION);
        else if(c== 91 || c== 93 )//Corrchetes
            return new Palabra(""+c, TIPO.CORRCHETES);
        else if( c== 123 || c== 125)// cierra y abre {}
           return new Palabra(""+c, TIPO.FIN);
        else if(c==33)//Admiracion !
            return formarNegacion(c);
        else if (c==34)//Es texto "
           return formarTexto(c);
        else if(c== 38)
           return formarAnd(c);
        else if(c== 39)//caracter '
            return formarCaracter(c);
        else if(c== 43)//suma +
            return formarSuma(c);
        else if(c== 45)//resta -
            return formarResta(c);
        else if(c== 47)//comentarios /
            return formarComentariosDivision(c);
        else if(c== 60)//menor desigua
            return formarDesigualdad(c);
        else if(c== 61)// igualdad
            return formarIgualdad(c);
        else if(c== 62)//mayor desigua
            return formarDesigualdad(c);
        else if(c==124)
            return formarOr(c);
        else if(c==3)//Termino el documento
            return new Palabra("3", TIPO.FINAL);
        else
        {
            return new Palabra("~", TIPO.ERROR);//Caracter desconocido
        }
        
        
        //PREGUNTAR SI SE INCUYEN OPERACIONES DE BITS
    }

    private void reiniciar() {
        this.contador=0;
    }

    private Palabra formarCadena(char c) {
        String cadena=""+c;
        while((c>64 && c< 91)||(c>96 && c< 123) || (c>47 && c< 58) || c==95){//A-Z, a-z, 0 -9, _, .{//Ve que sean letras, guin bajo, numeros, 
            c=caracter();
            if((c>64 && c< 91)||(c>96 && c< 123) || (c>47 && c< 58) || c==95){//es letra o numero o guion bajo
                cadena+=""+c;
            }else{
                this.contador--;
                return esGuionesBajos(cadena)?new Palabra("~", TIPO.ERROR):new Palabra(cadena, TIPO.PALABRA);//Si solo son guiones bajos regresa error
            }
        }
        return new Palabra(cadena, TIPO.PALABRA);
    }

    private Palabra formarNumero(char c) {
        String numero=""+c;
        char aux=c;
        boolean punto;
        if(punto=(c==46)){//Empieza con punto el numero
            aux=caracter();
            if((aux > 47 && aux < 58)){
                numero+=""+aux;
                aux=caracter();
                while((aux > 47 && aux < 58)){
                    numero+=""+aux;
                    aux=caracter();
                }
                this.contador--;
            }else{
                this.contador--;
                return new Palabra(".", TIPO.PUNTO);
            }
        }else{//No empieza con punto
            while((aux > 47 && aux < 58) || c==46){//Ve que sea punto o numero
                aux=caracter();
                if((c > 47 && c < 58)){//Ve si es numero
                    numero+=""+aux;
                }else if(c==46){//Es un punto 
                    if(punto)//Ya había un punto antes y por eso es un error
                        return new Palabra("~", TIPO.ERROR);
                    else{//No había punto y por eso lo agraga
                        punto = true;
                        numero+=""+aux;
                    }
                }else{
                    this.contador--;
                    return new Palabra(numero, TIPO.NUMERO);
                }
            }
        }
        
        return new Palabra(numero, TIPO.NUMERO);
    }

    private Palabra formarTexto(char c) {//Texto "...."
        String cadena=""+c;
        do{
            c=caracter();
            cadena+=""+c;
        }while(c!=34 || c!=3);//Hasta que llegue a otras comillas, el final o salto de linea
        if(c==34)//Si es un texto tipo "..."
        return new Palabra(cadena, TIPO.COMENTARIO);
        else return new Palabra("~", TIPO.ERROR);//no cierra comillas, y al pasar eso deshecha todos el resto del código
    }

    private Palabra formarAnd(char c) {//And 
        if(caracter()==38){
            return new Palabra("&&", TIPO.OPERACION);
        }else{
            this.contador--;
            return new Palabra("~", TIPO.ERROR);
        }
    }

    private Palabra formarCaracter(char c) {//'..'
        String cadena=""+c;
        do{
            c=caracter();
            cadena+=""+c;
        }while(c!=39 || c!=3);//Hasta que llegue a otra comilla, el final
        if(c==39)//Si es un texto tipo ´'...'
        return new Palabra(cadena, TIPO.TEXTO);
        else return new Palabra("~", TIPO.ERROR);//no cierra comillas, y al pasar eso deshecha todos el resto del código
    }

    private Palabra formarSuma(char c) {//43 +
        String cadena= ""+c;
        c=caracter();
        if(c==43){//Es ++
            cadena="++";
        }else{//es +
            this.contador--;
        }
        return new Palabra(cadena, TIPO.OPERACION);
    }

    private Palabra formarResta(char c) {//45 --
        String cadena= ""+c;
        c=caracter();
        if(c==45){//Es --
            cadena="--";
        }else{//es -
            this.contador--;
        }
        return new Palabra(cadena, TIPO.OPERACION);
    }

    private Palabra formarComentariosDivision(char c) {//47 / o division
        String comentario=""+c;
        c=caracter();
        comentario+=""+c;
        switch (c) {
        //Descripción //
            case 47:
                do{
                    c=caracter();
                    if(c!='\n' || c!=3 )
                        comentario+=""+c;
                    if(c=='\n')
                        break;
                }while(c!=3);
                return new Palabra(comentario, TIPO.COMENTARIO);
        //Planeacion revision /*...*/
            case 42:
                do{
                    c=caracter();
                    if( c!=3  )
                        comentario+=""+c;
                    if(c==47)
                        break;
                }while(c!=3);
                return new Palabra(comentario, TIPO.COMENTARIO);
            case 61://igual a la division con
                return new Palabra("/=", TIPO.OPERACION);
            default:
                //Es division
                this.contador--;
                return new Palabra("/", TIPO.OPERACION);
        }
    }

    private Palabra formarDesigualdad(char c) {// < >
        String des=""+c;
        c=caracter();
        if(c==61){//<= o >=
            des+=""+c;
        }else{//< o >
            this.contador--;
        }
        return new Palabra(des, TIPO.OPERACION);
    }

    private Palabra formarIgualdad(char c) {// =
        String des=""+c;
        c=caracter();
        if(c==61){//== 
            des+=""+c;
        }else{//=
            this.contador--;
        }
        return new Palabra(des, TIPO.OPERACION);
    }

    private Palabra formarOr(char c) {// ||
        if(caracter()==124){
            return new Palabra("||", TIPO.OPERACION);
        }else{//error
            this.contador--;
            return new Palabra("~", TIPO.ERROR);
        }
    }
    
    private Palabra formarNegacion(char c) {//!
        String negacion=""+c;
        c=caracter();
        if(c==61){//Ve si es desigualdad !=
            negacion+=""+c;
        }else{
            this.contador--;//Solo es negacion !
        }
        return new Palabra(negacion, TIPO.OPERACION);
    }
    
    private boolean esGuionesBajos(String cadena){
        for(char c: cadena.toCharArray()){
            if(c!= 95)
                return false;
        }
        return true;
    }
}
