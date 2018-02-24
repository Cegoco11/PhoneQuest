package com.example.cegoc.phonequest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
    public void goToMenu(View v){
        click_sound.start();
        v.setScaleX(1.1f);
        v.setScaleY(1.1f);
        Intent i= new Intent(Menu.this, QuestList.class);
        startActivity(i);
    }

    /**
     *  Metodo para cambiar a la actividad de Estadisticas.class
     *
     * @param v
     */
    public void goToEstadisticas(View v){
        click_sound.start();
        v.setScaleX(1.1f);
        v.setScaleY(1.1f);
//        Intent i= new Intent(Menu.this, QuestList.class);
//        startActivity(i);
    }

    /**
     *  Metodo para cambiar a la actividad de Creditos.class
     *
     * @param v
     */
    public void goToCreditos(View v){
        click_sound.start();
        v.setScaleX(1.1f);
        v.setScaleY(1.1f);
//        Intent i= new Intent(Menu.this, QuestList.class);
//        startActivity(i);
    }
}
