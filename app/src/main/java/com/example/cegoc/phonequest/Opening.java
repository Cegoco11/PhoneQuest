package com.example.cegoc.phonequest;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 */
public class Opening extends AppCompatActivity {

    private MediaPlayer op_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        getSupportActionBar().hide();

        creaFichero();

        op_sound=MediaPlayer.create(this, R.raw.opening_sound);
        op_sound.setLooping(true);
        op_sound.start();
    }

    /**
     *  Metodo para cambiar de actividad
     *
     * @param v
     */
    public void goToQuestList(View v){
        final RelativeLayout aux=findViewById(R.id.PrincipalOpening);
        aux.setClickable(false);
        aux.setBackground(getDrawable(R.drawable.bg2));

        Handler handler0 = new Handler();
        handler0.postDelayed(new Runnable() {
            public void run() {
                aux.setBackground(getDrawable(R.drawable.bg));
            }
        }, 200);

        ImageView aux2=findViewById(R.id.gif_tap);
        aux2.setVisibility(View.GONE);

        op_sound.stop();
        MediaPlayer.create(Opening.this, R.raw.tap_sound).start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent i= new Intent(Opening.this, QuestList.class);
                startActivity(i);
                finish();
            }
        }, 2100);
    }

    public void onPause(){
        super.onPause();
        op_sound.pause();
    }

    public void onResume(){
        super.onResume();
        op_sound.start();
    }

    /**
     * Crea el fichero con los logros
     */
    private void creaFichero(){
        File file = getFileStreamPath("file_logros");

        if (!file.exists()) {

            ArrayList<Logro> listaLogros= creaLogros();

            FileOutputStream fos;
            ObjectOutputStream out = null;
            try {
                fos = openFileOutput("file_logros", Context.MODE_PRIVATE);
                out = new ObjectOutputStream(fos);
                out.writeObject(listaLogros);
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            // El archivo existe y no se hace nada
        }
    }

    /**
     * Metodo auxiliar para crear todos los logros con los que se empieza
     *
     * @return
     */
    private ArrayList<Logro> creaLogros(){
        ArrayList<Logro> lista= new ArrayList<Logro>();
        lista.add(new Logro(1));
        lista.add(new Logro(2));
        lista.add(new Logro(3));
        lista.add(new Logro(2));
        lista.add(new Logro(4));
        return lista;
    }
}
