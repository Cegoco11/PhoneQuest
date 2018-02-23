package com.example.cegoc.phonequest;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que guarda la informacion de un Logro
 * La clase tiene un contador estatico que va aumentando a medida que se van creando
 * instancias de la misma, para asi poder hacer un generador de ID autoincremental
 *
 * @author Caesar
 */

public class Logro implements Serializable{

    private static final AtomicInteger count = new AtomicInteger(0);
    private final int ID_LOGRO;
    private final int tipoLogro;
    private int img;
    private String texto;
    private boolean estado;


    /**
     *  Constructor de la clase
     *
     * @param tipo
     *   1- Conectar cascos
     *   2- Conectar USB
     *   3- DescargarMovil
     *   4- CargarMovil
     */
    public Logro(int tipo){
        this.estado=false;
        this.tipoLogro=tipo;
        this.ID_LOGRO=count.incrementAndGet();
        switch (tipo){
            case 1:
                this.img=R.drawable.icon1;
                this.texto="Conectar cascos";
                break;
            case 2:
                this.img=R.drawable.icon2;
                this.texto="Conectar USB";
                break;
            case 3:
                this.img=R.drawable.icon3;
                this.texto="DescargarMovil";
                break;
            case 4:
                this.img=R.drawable.icon4;
                this.texto="CargarMovil";
                break;
            default:
                this.img=R.drawable.icon9;
                this.texto="No especificado";
        }
    }

    public int getImg() {
        return img;
    }

    public String getTexto() {
        return texto;
    }

    public void setComplete(){
        this.estado=true;
    }

    public int getTipo(){
        return tipoLogro;
    }

    public boolean getEstado(){
        return estado;
    }

    public int getID_LOGRO(){
        return ID_LOGRO;
    }
}
