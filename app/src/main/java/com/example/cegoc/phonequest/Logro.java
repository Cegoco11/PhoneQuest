package com.example.cegoc.phonequest;

import java.io.Serializable;

/**
 * Created by cegoc on 20/02/2018.
 */

public class Logro implements Serializable{

    private int img;
    private String texto;
    private boolean estado;

    public Logro(int img, String texto){
        this.img=img;
        this.texto=texto;
        this.estado=false;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setComplete(){
        this.estado=true;
    }
}
