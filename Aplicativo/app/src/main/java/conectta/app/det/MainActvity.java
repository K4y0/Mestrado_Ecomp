package conectta.app.det;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MainActvity extends Activity implements SensorEventListener, Runnable {

    private SensorManager sensorManager;
    private SensorManager sensorManager2;
    private Sensor sensor;
    private Sensor sensorAcelerometro;
    private TextView tvGiroX;
    private TextView tvGiroY;
    private TextView tvGiroZ;
    private TextView tvAcelX;
    private TextView tvAcelY;
    private TextView tvAcelZ;

    private String ntp;
    private String fog;
    private String port;
    private String route;
    private Button btStop;
    private TextView tvTimestamp;
    private long now = 0;
    private boolean fechar = true;
    public  InfoSensores infoSensores = new InfoSensores();
    public List<double[]> lista = new ArrayList<>();
    public  Handler handler1 = new Handler();

    public int aux = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        fechar = false;
        //permissão para fazer requisições de rede na thread principal(isso é errado, mas vai servir por enquanto).
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.i("AsyncTask", "Thread: " +
                Thread.currentThread().getName());

        tvGiroX = findViewById(R.id.tvGiroX);
        tvGiroY = findViewById(R.id.tvGiroY);
        tvGiroZ = findViewById(R.id.tvGiroZ);
        tvAcelX = findViewById(R.id.tvAcelX);
        tvAcelY = findViewById(R.id.tvAcelY);
        tvAcelZ = findViewById(R.id.tvAcelZ);
        btStop = findViewById(R.id.btStop);
        tvTimestamp = findViewById(R.id.tvTimestamp);

        //Recebe as informações digitadas na tela anterior
        Intent it = getIntent();
        ntp = it.getStringExtra("param");
        fog = it.getStringExtra("param1");
        port = it.getStringExtra("param2");
        route = it.getStringExtra("param3");

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechar = true;
                Intent it = new Intent(MainActvity.this, ConfigServer.class);
                finish();
                startActivity(it);

            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        sensorManager2 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAcelerometro = sensorManager2.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        final Handler handler = new Handler();
        handler.postDelayed(this, 45);
    }

    @Override
    public void run() {

        if (now == 0) {
            consultaNTP();
        } else {
            convertTimestamp(now);
        }

        String eixoX = tvAcelX.getText().toString();
        String eixoY = tvAcelY.getText().toString();
        String eixoZ = tvAcelZ.getText().toString();
        String giroX = tvGiroX.getText().toString();
        String giroY = tvGiroY.getText().toString();
        String giroZ = tvGiroZ.getText().toString();

        int tamanho = lista.size();
        double[] arrayDouble = new double[]{Double.parseDouble(eixoX), Double.parseDouble(eixoY), Double.parseDouble(eixoZ),
                Double.parseDouble(giroX), Double.parseDouble(giroY), Double.parseDouble(giroZ)};
        lista.add(arrayDouble);

//        Log.i("Item Lista "+ tamanho, Arrays.toString(lista.get(tamanho)));
        infoSensores.setLista(lista);


        if (lista.size() == 22) {
            infoSensores.setTimestamp(now);
            aux = aux + 1;

            //teste enviando JSON pronto
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(infoSensores, infoSensores.getClass());
//            Log.i("APRESENTACAO", json);


//            Log.i("ANTES DE ENVIAR", "Itens: " + infoSensores.getLista().size());
            FazRequisicao fazRequisicao = new FazRequisicao(MainActvity.this);
//            fazRequisicao.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, now, infoSensores, fog, port, route, aux);

            fazRequisicao.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fog, port, route, aux, json);

            lista.clear();

    }



        if(fechar == true) {
            handler1.removeCallbacks(this);
        }else{
            handler1.postDelayed(this, 45);
        }

    }

    public void consultaNTP(){
        final SNTPClient client = new SNTPClient();

        new Thread()
        {
            public void run() {
                if (client.requestTime(ntp, 2000))  {
                    now = client.getNtpTime() + SystemClock.elapsedRealtime() - client.getNtpTimeReference();
                    Log.i("consultaNTP", "Value 1: >"+now+"<");
                    //infoSensores.setTimestamp(now);
                }
                else{
                    Log.i("consultaNTP","ERROR: Request unsuccessful");
                }
            }
        }.start();
    }


    public void convertTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.MILLISECOND, 45);
        long timeFinal = calendar.getTimeInMillis();
        now = timeFinal;
        tvTimestamp.setText(String.valueOf(now));
//        Log.i("CONVERT", "Data com soma: " + timeFinal);

    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            getGyroscope(event);
        }

    }

    private void getAccelerometer(SensorEvent event) {

        String eixoX = String.valueOf(event.values[0]);
        String eixoY = String.valueOf(event.values[1]);
        String eixoZ = String.valueOf(event.values[2]);


        tvAcelX.setText(eixoX);
        tvAcelY.setText(eixoY);
        tvAcelZ.setText(eixoZ);


    }

    private void getGyroscope(SensorEvent event) {


        String giroX = String.valueOf(event.values[0]);
        String giroY = String.valueOf(event.values[1]);
        String giroZ = String.valueOf(event.values[2]);


        tvGiroX.setText(giroX);
        tvGiroY.setText(giroY);
        tvGiroZ.setText(giroZ);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager2.registerListener(this, sensorAcelerometro, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {

        super.onPause();
        sensorManager.unregisterListener(this);
        sensorManager2.unregisterListener(this);
    }


}
