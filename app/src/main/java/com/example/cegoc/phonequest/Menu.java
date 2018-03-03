package com.example.cegoc.phonequest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

    public static MediaPlayer click_sound;
    private MediaPlayer menu_sound;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Si existe la barra de titulo la oculta
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        context=getApplicationContext();

        menu_sound=MediaPlayer.create(this, R.raw.menu_sound);
        menu_sound.setLooping(true);
        menu_sound.start();

        click_sound=MediaPlayer.create(this, R.raw.click);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();
        menu_sound.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        menu_sound.start();

        SharedPreferences settings = getSharedPreferences("config", 0);
        Long tiempoAcaba;

        if (settings.getLong("tiempoAcaba", 0)==0) {
            // Guardo el tiempo en el que quiero que la cuenta atras se pare
            final long TIEMPO_CUENTAATRAS=60000; // 1 min en milliseconds
            tiempoAcaba=System.currentTimeMillis()+TIEMPO_CUENTAATRAS;
            settings.edit().putLong("tiempoAcaba", tiempoAcaba).apply();
        } else{
            Long tiempoActual=System.currentTimeMillis();
            tiempoAcaba=settings.getLong("tiempoAcaba", 0);
            // Si el tiempo actual es mayor que el guardado
            if(tiempoAcaba!=0 && tiempoActual>tiempoAcaba){
                // La cuenta atras ha finalizado
                // Pongo el tiempoAcaba a 0
                settings.edit().putLong("tiempoAcaba", 0).apply();
                // Pongo generaLogros a true
                settings.edit().putBoolean("generaLogros", true).apply();
                creaCustomDialog_error("Has recibido una nueva mision!");
            }
        }
    }

    /**
     *  Metodo para cambiar a la actividad de QuestList.class
     *
     * @param v Vista que lo lanza
     */
    public void goToMenu(final View v){
        click_sound.start();
        v.setScaleX(1.1f);
        v.setScaleY(1.1f);
        Handler handler0 = new Handler();
        handler0.postDelayed(new Runnable() {
            public void run() {
                //Se desactiva el clickable
                v.setScaleX(1.0f);
                v.setScaleY(1.0f);

                Intent i= new Intent(Menu.this, QuestList.class);
                startActivity(i);
            }
        }, 80);
    }

    /**
     *  Metodo para cambiar a la actividad de Estadisticas.class
     *
     * @param v Vista que lo lanza
     */
    public void goToEstadisticas(final View v){
        click_sound.start();
        v.setScaleX(1.1f);
        v.setScaleY(1.1f);
        Handler handler0 = new Handler();
        handler0.postDelayed(new Runnable() {
            public void run() {
                //Se desactiva el clickable
                v.setScaleX(1.0f);
                v.setScaleY(1.0f);
                //ToDo Traducir
                creaCustomDialog_error("No disponible por el momento");
            }
        }, 80);
    }

    /**
     *  Metodo para cambiar a la actividad de Creditos.class
     *
     * @param v Vista que lo lanza
     */
    public void goToCreditos(final View v){
        click_sound.start();
        v.setScaleX(1.1f);
        v.setScaleY(1.1f);
        Handler handler0 = new Handler();
        handler0.postDelayed(new Runnable() {
            public void run() {
                //Se desactiva el clickable
                v.setScaleX(1.0f);
                v.setScaleY(1.0f);
                //ToDo Traducir
                creaCustomDialog_error("No disponible por el momento");
            }
        }, 80);
    }

    /**
     * Crea un dialogo personalizado de tipo error
     */
    private void creaCustomDialog_error(String s){
        View aux=View.inflate(this, R.layout.custom_dialog_error, null);

        final Dialog ad=new Dialog(Menu.this);
        ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ad.setContentView(aux);

        TextView texto=aux.findViewById(R.id.custom_dialog_text2);
        texto.setText(s);

        ad.create();
        ad.show();
    }
}