package com.example.cegoc.phonequest;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *
 *
 * @author David y Caesar
 */

public class BluetoothChangeListener extends ChangeListener{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BLUE","Receive state: "+intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0));
        if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)==BluetoothAdapter.STATE_ON){
            QuestList.generarNotificacion(4, "Bluetooth",
                    "Has activado el bluetooth", R.drawable.ic_cascos);

            QuestList.usarConectarBluetooth(false, getId_logro());
        }
    }
}
