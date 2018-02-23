package com.example.cegoc.phonequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;


/**
 *
 *
 * @author Caesar
 */
public class BatteryChangedListener extends BroadcastReceiver {

    private int level;
    private boolean control;
    private boolean eleccion;
    private int cantidad;

    public BatteryChangedListener(boolean b, int c){
        this.level=-5;
        this.control=true;
        this.eleccion=b;
        this.cantidad=c-1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setControl(boolean control) {
        this.control = control;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int aux = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);


        Log.i("BATTERYLISTENER","Detect");

        if (level == -5) {
            setLevel(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
        }

        if (control){
            if(eleccion){
                Log.i("CARGAR","level: "+level+"; aux: "+(aux+cantidad));
               if (level < aux-cantidad) {
                   Log.i("BATTERYLISTENER","CARGAR LOGRO COMPLETADO");
                    QuestList.generarNotificacion(2, "Bateria",
                            "Se ha cargado un "+cantidad+"%", R.drawable.ic_cascos);
                    setControl(false);
                    QuestList.usarCargarMovil(false);
                }
            } else{
                if (level > aux+cantidad) {
                    Log.i("BATTERYLISTENER","DESCARGAR LOGRO COMPLETADO");
                    QuestList.generarNotificacion(3, "Bateria",
                            "Se ha descargado un "+cantidad+"%", R.drawable.ic_cascos);
                    QuestList.usarDescargarMovil(false);
                    setControl(false);
                }
            }
        }
    }
}
