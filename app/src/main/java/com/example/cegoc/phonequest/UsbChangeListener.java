package com.example.cegoc.phonequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 *
 *
 * @author Jesús y Caesar
 */
public class UsbChangeListener extends ChangeListener {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equalsIgnoreCase("android.hardware.usb.action.USB_STATE")) {
            if (intent.getExtras().getBoolean("connected")) {
                QuestList.generarNotificacion(1, "Cable USB",
                        "Has conectado el usb", R.drawable.ic_cascos);
                QuestList.usarConectarUsb(false, getId_logro());
            }
        }
    }
}
