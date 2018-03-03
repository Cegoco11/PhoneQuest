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

// Me salen un monton de advertencias a las que no le veo el sentido
@SuppressWarnings("all")
public class Logro implements Serializable{

    public static final int TOTAL_LOGROS=5;
    private static final AtomicInteger count = new AtomicInteger(0);
    private final long tiempoCreaccion;
    private final int ID_LOGRO;
    private final int tipoLogro;
    private int img;
    private int img2;
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
                this.img2=R.drawable.icon1_b;
                //Todo Traduccion
                this.texto="Conectar cascos";
                break;
            case 2:
                this.img=R.drawable.icon2;
                this.img2=R.drawable.icon2_b;
                //Todo Traduccion
                this.texto="Conectar USB";
                break;
            case 3:
                this.img=R.drawable.icon3;
                this.img2=R.drawable.icon3_b;
                //Todo Traduccion
                this.texto="Descargar Movil";
                break;
            case 4:
                this.img=R.drawable.icon4;
                this.img2=R.drawable.icon4_b;
                //Todo Traduccion
                this.texto="Cargar Movil";
                break;
            case 5:
                this.img=R.drawable.icon5;
                this.img2=R.drawable.icon5_b;
                //Todo Traduccion
                this.texto="Activar Bluetooth";
                break;
            default:
                this.img=R.drawable.icon9;
                this.img2=R.drawable.icon9_b;
                //Todo Traduccion
                this.texto="No especificado";
        }
    }

    public String getTexto() {
        return texto;
    }

    public int getImg() {
        return img;
    }

    public int getImg2() {
        return img2;
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