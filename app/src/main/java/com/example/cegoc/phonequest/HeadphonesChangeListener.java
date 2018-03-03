package com.example.cegoc.phonequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *
 *
 * @author Caesar
 */

public class HeadphonesChangeListener extends ChangeListener {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getIntExtra("state", 0)==1){
            QuestList.generarNotificacion(0, "Cascos",
                    "Has conectado los cascos");

            QuestList.usarConectarCascos(false, getId_logro());
        }
    }
}
