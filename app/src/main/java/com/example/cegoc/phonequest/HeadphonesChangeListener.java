package com.example.cegoc.phonequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *
 *
 * @author Caesar
 */

public class HeadphonesChangeListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getIntExtra("state", 0)==1){
            QuestList.generarNotificacion(0, "Cascos",
                    "Has conectado los cascos", R.drawable.ic_cascos);

            QuestList.usarConectarCascos(false);
        }
    }
}
