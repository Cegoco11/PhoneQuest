package com.example.cegoc.phonequest;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import java.util.Collections;
import java.util.Comparator;


/**
 *
 */
public class QuestList extends AppCompatActivity {

    private static final UsbChangeListener broadCast_ConectarUsb=
            new UsbChangeListener();
    private static final HeadphonesChangeListener broadCast_ConectarCascos=
            new HeadphonesChangeListener();
    private static final BatteryChangedListener broadCast_DescargarMovil=
            new BatteryChangedListener(false);
    private static final BatteryChangedListener broadCast_CargarMovil=
            new BatteryChangedListener(true);
    private static final BluetoothChangeListener broadCast_ConectarBlue=
            new BluetoothChangeListener();

    private static ArrayList<Logro> logros;
    private MediaPlayer list_sound;
    private LinearLayout contenedor;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_list);
        getSupportActionBar().hide();

        context=getApplicationContext();

        this.contenedor=findViewById(R.id.linear_scroll);

        // Cargo los logros, los ordeno por estado y tiempo, y genero el layout
        cargaLogros();
        sortStateAndTime();
        creaLogrosLayout();

        // Si se ejecuta la aplicacion despues de haberse cerrado se ejecuta esto
        SharedPreferences settings = getSharedPreferences("config", 0);
        if (settings.getBoolean("firstTime", true)) {
            reactivarLogros();
            settings.edit().putBoolean("firstTime", false).apply();
        }

        list_sound=MediaPlayer.create(this, R.raw.quest_list_sound);
        list_sound.setLooping(true);
        list_sound.start();

    }

    @Override
    protected void onPause(){
        super.onPause();
        guardaLogros(logros);
        list_sound.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        list_sound.start();
    }

    /**
     * Guarda la lista en el archivo file_logros
     *
     * @param lista logros a guardar
     */
    public void guardaLogros(ArrayList<Logro> lista){
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
     * Carga los datos del archivo file_logros en la variable logros
     */
    public void cargaLogros(){
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
        final LinearLayout.LayoutParams params_img2=new LinearLayout.LayoutParams
                (toDp(40), toDp(40));
        params_img2.gravity= Gravity.CENTER;
        final LinearLayout.LayoutParams params_text=new LinearLayout.LayoutParams
                (toDp(170), ViewGroup.LayoutParams.WRAP_CONTENT);
        params_text.gravity= Gravity.CENTER;

        LinearLayout aux_linear;
        ImageView aux_img, aux_img2;
        TextView aux_text;

        final Typeface tf=Typeface.createFromAsset(getAssets(), "arabolic.TTF");
        // Se genera la estructura del layout que usa los objetos de tipo logro
        for (int i=0; i<logros.size(); i++){
            aux_linear=new LinearLayout(this);
            aux_linear.setOrientation(LinearLayout.HORIZONTAL);
            aux_linear.setLayoutParams(params_linear);

            aux_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Gracias a la id del logro que puse en el tag puedo acceder a cada boton
                    int id=(int)view.getTag();
                    // Recorro todos los logros
                    for(Logro o : logros){
                        // Si la id del logro es igual a la del tag
                        if(o.getID_LOGRO()==id){
                            // Selecciona el tipo de mision y crea un dialogo
                            Menu.click_sound.start();
                            creaCustomDialog_pregunta(o);
                            break;
                        }
                    }
                }
            });

            aux_img=new ImageView(this);
            aux_img.setLayoutParams(params_img);
            aux_img.setImageResource(logros.get(i).getImg());
            aux_img.setPadding(toDp(5),toDp(5),toDp(5),toDp(5));

            aux_img2=new ImageView(this);
            aux_img2.setLayoutParams(params_img2);
            aux_img2.setImageResource(R.drawable.scarab);
            aux_img2.setPadding(toDp(5),toDp(5),toDp(5),toDp(5));

            aux_text=new TextView(this);
            aux_text.setLayoutParams(params_text);
            aux_text.setText(logros.get(i).getTexto());
            aux_text.setTypeface(tf);
            aux_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            aux_text.setPadding(toDp(5),toDp(5),toDp(5),toDp(5));

            // Si la mision esta activa(0) se ve la imagen, y se quita el clickable
            // Si la mision es nueva (1) la imagen no se ve, y es clickable
            // Si la mision esta completada(2) no es clickable, y el texto sale tachado
            switch (logros.get(i).getEstado()){
                case 0:
                    aux_img2.setVisibility(View.VISIBLE);
                    aux_linear.setClickable(false);
                    break;
                case 1:
                    aux_img2.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    aux_img2.setVisibility(View.INVISIBLE);
                    aux_text.setPaintFlags(aux_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    aux_linear.setClickable(false);
            }

            aux_linear.addView(aux_img);
            aux_linear.addView(aux_text);
            aux_linear.addView(aux_img2);

            // Cojo la id del logro actual y lo asigno de tag al boton
            aux_linear.setTag(logros.get(i).getID_LOGRO());

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
     * Activa el broadcast que detecta si se activa el bluetooth
     *
     * @param estado true para activar, false para detener
     */
    public static void usarConectarBluetooth(boolean estado){
        if(estado){
            context.registerReceiver(broadCast_ConectarBlue, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        } else{
            context.unregisterReceiver(broadCast_ConectarBlue);
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
    public static void generarNotificacion(int id,String titulo, String contenido, int img){
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
     * 1- Conectar cascos
     * 2- Conectar USB
     * 3- Descargar Movil (2%)
     * 4- Cargar Movil (5%)
     * 5- Activar Bluetooth
     */
    private void activarMision(int num){
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
                usarDescargarMovil(true);
                Toast.makeText(context, "Mision 3 activada", Toast.LENGTH_SHORT).show();;
                break;
            case 4:
                usarCargarMovil(true);
                Toast.makeText(context, "Mision 4 activada", Toast.LENGTH_SHORT).show();;
                break;
            case 5:
                Toast.makeText(context, "Mision 5 activada", Toast.LENGTH_SHORT).show();
                usarConectarBluetooth(true);
                break;
            default:
                Toast.makeText(context, "No funcional! WIP", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Comprueba si se puede usar la mision de descargar el movil
     *
     * @return retorno true si se puede, false si no
     */
    public boolean sePuedeDescargar(){
        boolean retorno;
        // Recojo el nivel actual de bateria
        Intent datosBateria=new Intent(Intent.ACTION_BATTERY_CHANGED);
        int level=datosBateria.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        // Si el nivel es mayor de lo que tiene que descargarse
        if(level>BatteryChangedListener.PORCENTAJE_DESCARGA){
            retorno=true;
        } else{
            retorno=false;
        }
        return retorno;
    }

    /**
     * Comprueba si se puede usar la mision de cargar el movil
     *
     * @return retorno true si se puede, false si no
     */
    public boolean sePuedeCargar(){
        boolean retorno;
        // Recojo el nivel actual de bateria
        Intent datosBateria=new Intent(Intent.ACTION_BATTERY_CHANGED);
        int level=datosBateria.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        // Si el nivel no es mayor que 100-PORCENTAJE_CARGA
        if(!(level>(100-BatteryChangedListener.PORCENTAJE_CARGA))){
            retorno=true;
        } else{
            retorno=false;
        }
        return retorno;
    }

    /**
     * Crea un dialogo personalizado al que se le pasa como parametro el texto
     * y el tipo para pasarselo al metodo 'selectorMision(int)'
     *
     * @param o Logro con el que estoy
     */
    private void creaCustomDialog_pregunta(final Logro o){
        LayoutInflater inflater = getLayoutInflater();
        View aux=inflater.inflate(R.layout.custom_dialog_pregunta,null);

        final Dialog ad=new Dialog(QuestList.this);
        ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ad.setContentView(aux);

        TextView texto=aux.findViewById(R.id.custom_dialog_text);
        texto.setText("Estas seguro?");
        ImageView btnOk=aux.findViewById(R.id.custom_dialog_btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Menu.click_sound.start();
                view.setScaleX(1.1f);
                view.setScaleY(1.1f);

                Handler handler0 = new Handler();
                handler0.postDelayed(new Runnable() {
                    public void run() {
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                    }
                }, 90);

                int tipoAux=o.getTipo();
                switch (tipoAux){
                    case 3:
                        if(sePuedeCargar()){
                            clickParaFila(tipoAux, o);
                        } else{
                            ad.dismiss();
                            creaCustomDialog_error("No dispones de los requisitos " +
                                    "para completar esta mision");
                        }
                        break;
                    case 4:
                        if(sePuedeDescargar()){
                            clickParaFila(tipoAux, o);
                        } else{
                            ad.dismiss();
                            creaCustomDialog_error("No dispones de los requisitos " +
                                    "para completar esta mision");
                        }
                        break;
                    // Para todos los demas casos se hace con normalidad
                    default:
                        clickParaFila(tipoAux, o);
                }
            }
        });
        ImageView btnCancel=aux.findViewById(R.id.custom_dialog_btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Menu.click_sound.start();
                view.setScaleX(1.1f);
                view.setScaleY(1.1f);
                Handler handler0 = new Handler();
                handler0.postDelayed(new Runnable() {
                    public void run() {
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);

                    }
                }, 80);

                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    public void run() {
                        ad.dismiss();
                    }
                }, 100);
            }
        });
        ad.create();
        ad.show();
    }

    /**
     * Crea un dialogo personalizado de tipo error
     *
     * @param s texto del dialogo
     */
    private void creaCustomDialog_error(String s){
        LayoutInflater inflater = getLayoutInflater();
        View aux=inflater.inflate(R.layout.custom_dialog_error,null);

        final Dialog ad=new Dialog(QuestList.this);
        ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ad.setContentView(aux);

        TextView texto=aux.findViewById(R.id.custom_dialog_text);
        texto.setText(s);

        ad.create();
        ad.show();
    }

    /**
     * Metodo que ordena por estado y luego por fecha de creaccion
     * de la mision (false -> true && 0 -> n)
     */
    public void sortStateAndTime(){
        Collections.sort(logros, new Comparator<Logro>() {

            public int compare(Logro o1, Logro o2) {

                Integer state1 = o1.getEstado();
                Integer state2 = o2.getEstado();
                int sComp = state1.compareTo(state2);

                if (sComp != 0) {
                    return sComp;
                } else {
                    Float f1 = o1.getTiempoCreaccion();
                    Float f2 = o2.getTiempoCreaccion();
                    return f1.compareTo(f2);
                }
            }});
    }

    /**
     * Metodo que busca un logro, segun la id que se pasa como parametro, y cambia
     * su estado
     *
     * @param aux id del logro que queremos buscar
     * @param estado a que estado quieres cambiar el logro
     */
    public void modificaEstado(int aux, int estado){
        for(Logro o : logros){
            if(o.getID_LOGRO()==aux){
                o.setEstado(estado);
            }
        }
    }

    /**
     * Activa los broadcast de los logros con estado 0
     */
    public void reactivarLogros(){
        for(Logro o : logros){
            if(o.getEstado()==0){
                activarMision(o.getTipo());
            }
        }
    }

    /**
     * Busca un tipo de logro en el array de logros
     *
     * @param tipo tipo de logro que buscamos
     * @return true si encuentra el tipo, false si no
     */
    public boolean existeLogro(int tipo){
        for(Logro o : logros){
            if(o.getTipo()==tipo){
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo auxiliar que elige que mision se activa,
     * desactiva el clickable, modifica el estado del logro, y
     * guarda la modificacion
     *
     * @param tipoAux tipo de mision
     * @param o logro a modificar
     */
    private void clickParaFila(int tipoAux, Logro o){
        activarMision(tipoAux);
        contenedor.findViewWithTag(o.getID_LOGRO()).setClickable(false);
        modificaEstado(o.getID_LOGRO(), 0);
        guardaLogros(logros);
        recreate();
    }
}