package com.example.cegoc.phonequest;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

    public static MediaPlayer click_sound;
    private MediaPlayer menu_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        menu_sound=MediaPlayer.create(this, R.raw.menu_sound);
        menu_sound.setLooping(true);
        menu_sound.start();

        click_sound=MediaPlayer.create(this, R.raw.click);
    }

    public void onPause(){
        super.onPause();
        menu_sound.pause();
    }

    public void onResume(){
        super.onResume();
        menu_sound.start();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        recreate();
    }

    /**
     *  Metodo para cambiar a la actividad de QuestList.class
     *
     * @param v
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
     * @param v
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

                creaCustomDialog_error("No disponible por el momento");
            }
        }, 80);
    }

    /**
     *  Metodo para cambiar a la actividad de Creditos.class
     *
     * @param v
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

                creaCustomDialog_error("No disponible por el momento");
            }
        }, 80);
    }

    /**
     * Crea un dialogo personalizado de tipo error
     *
     * @param s texto del dialogo
     */
    private void creaCustomDialog_error(String s){
        LayoutInflater inflater = getLayoutInflater();
        View aux=inflater.inflate(R.layout.custom_dialog_error,null);

        final Dialog ad=new Dialog(Menu.this);
        ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ad.setContentView(aux);

        TextView texto=aux.findViewById(R.id.custom_dialog_text);
        texto.setText(s);

        ad.create();
        ad.show();
    }
}
