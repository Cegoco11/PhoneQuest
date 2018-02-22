package com.example.cegoc.phonequest;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class QuestList extends AppCompatActivity {

    private static final UsbChangeListener broadCast_ConectarUsb=
            new UsbChangeListener();;
    private static final HeadphonesChangeListener broadCast_ConectarCascos=
            new HeadphonesChangeListener();;
    private static final BatteryChangedListener broadCast_DescargarMovil=
            new BatteryChangedListener(true, 2);;
    private static final BatteryChangedListener broadCast_CargarMovil=
            new BatteryChangedListener(false, 1);;

    private MediaPlayer list_sound;
    private ArrayList<Logro> logros;
    private LinearLayout contenedor;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_list);
        getSupportActionBar().hide();

        context=getApplicationContext();

        this.contenedor=findViewById(R.id.linear_scroll);

        cargaLogros();
        creaLogrosLayout();

        list_sound=MediaPlayer.create(this, R.raw.quest_list_sound);
        list_sound.setLooping(true);
        list_sound.start();
    }

    public void onPause(){
        super.onPause();
        guardaLogros(logros);
        list_sound.pause();
    }

    public void onResume(){
        super.onResume();
        list_sound.start();
    }

    /**
     * Guarda la lista en el archivo file_logros
     *
     * @param lista logros a guardar
     */
    private void guardaLogros(ArrayList<Logro> lista){
        FileOutputStream fos;
        ObjectOutputStream out = null;
        try {
            fos = openFileOutput("file_logros", Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.writeObject(lista);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga los datos del archivo file_logros
     */
    private void cargaLogros(){
        try {
            FileInputStream fis = openFileInput("file_logros");
            ObjectInputStream in = new ObjectInputStream(fis);
            this.logros = (ArrayList<Logro>) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Rellena el contenido del LinearLayout del pergamino con los logros que existen
     *
     */
    private void creaLogrosLayout(){

        final LinearLayout.LayoutParams params_linear=new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout.LayoutParams params_img=new LinearLayout.LayoutParams
                (toDp(80), toDp(80));
        final LinearLayout.LayoutParams params_text=new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_text.gravity= Gravity.CENTER;

        LinearLayout aux_linear;
        ImageView aux_img;
        TextView aux_text;

        for (int i=0; i<logros.size(); i++){
            aux_linear=new LinearLayout(this);
            aux_linear.setOrientation(LinearLayout.HORIZONTAL);
            aux_linear.setLayoutParams(params_linear);

            aux_img=new ImageView(this);
            aux_img.setLayoutParams(params_img);
            aux_img.setImageResource(logros.get(i).getImg());
            aux_img.setPadding(toDp(5),toDp(5),toDp(5),toDp(5));

            aux_text=new TextView(this);
            aux_text.setLayoutParams(params_text);
            aux_text.setText(logros.get(i).getTexto());
            aux_text.setPadding(toDp(5),toDp(5),toDp(5),toDp(5));

            aux_linear.addView(aux_img);
            aux_linear.addView(aux_text);
            aux_linear.setTag(i+1);

            aux_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tagView=(int)view.getTag();
                    selectorMision(tagView);
                }
            });

            contenedor.addView(aux_linear);
        }
    }

    /**
     * Este metodo pasa de pixeles a dp
     *
     * @param num numero de pixeles
     * @return num transformado a dp
     */
    private int toDp(int num){
        float factor = contenedor.getResources().getDisplayMetrics().density;
        return (int) factor*num;
    }

    /**
     * Activa el broadcast de descargar el movil un 2%
     *
     * @param estado true para activar, false para detener
     */
    public static void usarDescargarMovil(boolean estado){
        if(estado){
            context.registerReceiver(broadCast_DescargarMovil, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        } else{
            context.unregisterReceiver(broadCast_DescargarMovil);
        }
    }

    /**
     * Activa el broadcast de cargar el movil un 2%
     *
     * @param estado true para activar, false para detener
     */
    public static void usarCargarMovil(boolean estado){
        if(estado){
            context.registerReceiver(broadCast_CargarMovil, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        } else{
            context.unregisterReceiver(broadCast_CargarMovil);
        }
    }

    /**
     * Activa el broadcast que detecta si se conectan unos cascos
     *
     * @param estado true para activar, false para detener
     */
    public static void usarConectarCascos(boolean estado){
        if(estado){
            context.registerReceiver(broadCast_ConectarCascos, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
        } else{
            context.unregisterReceiver(broadCast_ConectarCascos);
        }
    }

    /**
     * Activa el broadcast que detecta si se conecta el usb
     *
     * @param estado true para activar, false para detener
     */
    public static void usarConectarUsb(boolean estado){
        if(estado){
            context.registerReceiver(broadCast_ConectarUsb, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        } else{
            context.unregisterReceiver(broadCast_ConectarUsb);
        }
    }

    /**
     * Genera una notificacion
     *
     * @param id id de la notificacion
     * @param titulo titulo de la notificacion
     * @param contenido texto que va a tener la notificacion
     * @param img imagen que queremos que se vea (R.drawable.nombre_de_la_imagen)
     */
    public static void GenerarNotificacion(int id,String titulo, String contenido,int img){
        // Paso 1: creo la notificacion
        NotificationCompat.Builder prueba=new NotificationCompat.Builder(context);
        prueba.setSmallIcon(img);
        prueba.setContentTitle(titulo);
        prueba.setContentText(contenido);
        prueba.setGroup("logros_app");

        // Paso 2: crear y enviar
        prueba.setAutoCancel(true);
        prueba.setPriority(Notification.PRIORITY_MAX);
        NotificationManager notificador= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificador.notify(id, prueba.build());
    }

    /**
     * Elige que broadcast se va a activar dependiendo del parametro
     *
     * @param num
     */
    public void selectorMision(int num){
        switch (num){
            case 1:
                Toast.makeText(context, "Mision 1 activada", Toast.LENGTH_SHORT).show();
                usarConectarCascos(true);
                break;
            case 2:
                Toast.makeText(context, "Mision 2 activada", Toast.LENGTH_SHORT).show();
                usarConectarUsb(true);
                break;
            case 3:
                Toast.makeText(context, "Mision 3 activada", Toast.LENGTH_SHORT).show();
                usarDescargarMovil(true);
                break;
            case 4:
                Toast.makeText(context, "Mision 4 activada", Toast.LENGTH_SHORT).show();
                usarCargarMovil(true);
                break;
            default:
                Toast.makeText(context, "No funcional! WIP", Toast.LENGTH_SHORT).show();
        }
    }
}
