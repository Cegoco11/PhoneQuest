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
    private final float tiempoCreaccion;
    private final int ID_LOGRO;
    private final int tipoLogro;
    private int img;
    private String texto;
    private int estado; //0 Mision activada; 1 Mision nueva; 2 Mision completada

    /**
     *  Constructor de la clase
     *
     * @param tipo
     *   1- Conectar cascos
     *   2- Conectar USB
     *   3- Descargar Movil
     *   4- Cargar Movil
     *   5- Activar Bluetooth
     */
    public Logro(int tipo){
        this.tiempoCreaccion= System.currentTimeMillis();
        this.estado=1;
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
                this.texto="Descargar Movil";
                break;
            case 4:
                this.img=R.drawable.icon4;
                this.texto="Cargar Movil";
                break;
            case 5:
                this.img=R.drawable.icon5;
                this.texto="Activar Bluetooth";
                break;
            default:
                this.img=R.drawable.icon9;
                this.texto="No especificado";
        }
    }

    public String getTexto() {
        return texto;
    }

    public int getImg() {
        return img;
    }

    public void setEstado(int s){
        this.estado=s;
    }

    public int getTipo(){
        return tipoLogro;
    }

    public int getEstado(){
        return estado;
    }

    public float getTiempoCreaccion(){
        return tiempoCreaccion;
    }

    public int getID_LOGRO(){
        return ID_LOGRO;
    }
}
