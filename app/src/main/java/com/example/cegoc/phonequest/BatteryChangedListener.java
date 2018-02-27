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

    public static final int PORCENTAJE_DESCARGA=2;
    public static final int PORCENTAJE_CARGA=4;

    private int level;
    private boolean control;
    private boolean eleccion;
    private int id_logro;

    public BatteryChangedListener(boolean b){
        this.level=-5;
        this.control=true;
        this.eleccion=b;
    }

    public int getId_logro() {
        return id_logro;
    }

    public void setId_logro(int id_logro) {
        this.id_logro = id_logro;
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
                Log.i("CARGAR","level: "+level+"; aux: "+(aux+(PORCENTAJE_CARGA-1)));
               if (level < aux-(PORCENTAJE_CARGA-1)) {
                   Log.i("BATTERYLISTENER","CARGAR LOGRO COMPLETADO");
                    QuestList.generarNotificacion(2, "Bateria",
                            "Se ha cargado un "+(PORCENTAJE_CARGA-1)+"%", R.drawable.ic_cascos);
                    setControl(false);
                    QuestList.usarCargarMovil(false, getId_logro());
                }
            } else{
                if (level > aux+(PORCENTAJE_DESCARGA-1)) {
                    Log.i("BATTERYLISTENER","DESCARGAR LOGRO COMPLETADO");
                    QuestList.generarNotificacion(3, "Bateria",
                            "Se ha descargado un "+(PORCENTAJE_DESCARGA-1)+"%", R.drawable.ic_cascos);
                    QuestList.usarDescargarMovil(false, getId_logro());
                    setControl(false);
                }
            }
        }
    }
}
