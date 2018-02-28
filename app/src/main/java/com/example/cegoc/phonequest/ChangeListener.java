package com.example.cegoc.phonequest;

import android.content.BroadcastReceiver;

/**
 * Created by cegoc on 28/02/2018.
 */

public abstract class ChangeListener extends BroadcastReceiver {
    protected  int id_logro;

    public int getId_logro() {
        return id_logro;
    }

    public void setId_logro(int id_logro) {
        this.id_logro = id_logro;
    }
}
